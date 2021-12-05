package io.mkrzywanski.tlv;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TlvParser {

    private final int idFieldSize;
    private final int lengthFieldSize;
    private final TlvTagRegistry tlvTagRegistry;
    private final ByteOrder byteOrder;

    private TlvParser(final int idFieldSize,
                      final int lengthFieldSize,
                      final TlvTagRegistry tlvTagRegistry,
                      final ByteOrder byteOrder) {
        this.idFieldSize = idFieldSize;
        this.lengthFieldSize = lengthFieldSize;
        this.tlvTagRegistry = tlvTagRegistry;
//        validate(tlvTagRegistry);
        this.byteOrder = byteOrder;
    }

    private void validate(final TlvTagRegistry tlvTagRegistry) {
        final boolean b = tlvTagRegistry.allTags()
                .stream()
                .allMatch(tagId -> tagId.hasLength(this.idFieldSize));
        if (!b) {
            throw new IllegalArgumentException("All tags ids have to have " + idFieldSize + "size");
        }
    }

    public ParsedTags parse(final byte[] bytes) {
        final var parsedTagsMap = parseInternal(bytes);
        return new ParsedTags(parsedTagsMap);

    }

    private BigInteger extractLength(final byte[] arr) {
        if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
            return new BigInteger(1, ByteArrayUtils.reverse(arr));
        } else {
            return new BigInteger(1, arr);
        }
    }

    private Map<TagId, ParsedTag> parseInternal(final byte[] value) {
        final ByteBuffer buffer = ByteBuffer.wrap(value)
                .order(byteOrder);
        final ByteBufferSlicer byteBufferSlicer = new ByteBufferSlicer(idFieldSize, lengthFieldSize, buffer);

        final var resultTags = new HashMap<TagId, ParsedTag>();

        while (buffer.hasRemaining()) {

            final byte[] rawTagId = byteBufferSlicer.nextTagId();
            final byte[] rawLength = byteBufferSlicer.nextLength();

            final int tagValueLength = extractLength(rawLength).intValue();
            final byte[] tagData = byteBufferSlicer.readData(tagValueLength);

            final TagId tagId = TagId.of(rawTagId);
            final Set<TagId> childTags = tlvTagRegistry.getChildTags(tagId);

            final Map<TagId, ParsedTag> tagIdParsedTagMap = new HashMap<>();
            if (!childTags.isEmpty()) {
                tagIdParsedTagMap.putAll(parseInternal(tagData));
            }

            final ParsedTag parsedTag = new ParsedTag(tagData, new ArrayList<>(tagIdParsedTagMap.values()), tagId);
            resultTags.put(tagId, parsedTag);
            resultTags.putAll(tagIdParsedTagMap);
        }
        return resultTags;
    }

    public static class Builder {
        private int idFieldSize = 2;
        private int lengthFieldSize = 2;
        private TlvTagRegistry tlvTagRegistry = TlvTagRegistry.empty();
        private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder withIdFieldLength(final int lengthFieldSize) {
            this.idFieldSize = lengthFieldSize;
            return this;
        }

        public Builder withLengthFieldSize(final int lengthFieldSize) {
            this.lengthFieldSize = lengthFieldSize;
            return this;
        }

        public Builder tlvTagsRegistry(final TlvTagRegistry tlvTagRegistry) {
            this.tlvTagRegistry = tlvTagRegistry;
            return this;
        }

        public Builder withByteOrder(final ByteOrder byteOrder) {
            this.byteOrder = byteOrder;
            return this;
        }

        public TlvParser build() {
            return new TlvParser(idFieldSize, lengthFieldSize, tlvTagRegistry, byteOrder);
        }
    }

}
