package io.mkrzywanski;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class TlvParser {

    private final int idFieldSize;
    private final int lengthFieldSize;
    private final TlvTagRegistry tlvTagRegistry;

    private TlvParser(int idFieldSize, int lengthFieldSize, TlvTagRegistry tlvTagRegistry) {
        this.idFieldSize = idFieldSize;
        this.lengthFieldSize = lengthFieldSize;
        this.tlvTagRegistry = tlvTagRegistry;
//        validate(tlvTagRegistry);
    }

    private void validate(TlvTagRegistry tlvTagRegistry) {
        boolean b = tlvTagRegistry.allTags()
                .stream()
                .allMatch(tagId -> tagId.hasLength(this.idFieldSize));
        if(!b) {
            throw new IllegalArgumentException("All tags ids have to have " + idFieldSize + "size");
        }
    }

    public ParsedTags parse(byte[] bytes) {
        Map<TagId, ParsedTag> tagIdParsedTagMap = parseInternal(bytes);
        return new ParsedTags(tagIdParsedTagMap);

    }

    private static BigInteger toBigInt(byte[] arr) {
        byte[] rev = reverse(arr);
        return new BigInteger(1, rev);
    }

    private static byte[] reverse(byte[] arr) {
        byte[] rev = new byte[arr.length];
        for (int i = 0, j = arr.length - 1; j >= 0; i++, j--)
            rev[j] = arr[i];
        return rev;
    }

    private byte[] targetDataArrayOf(byte[] length) {
//        BigInteger bigInteger = new BigInteger(1, length);
        BigInteger bigInteger = toBigInt(length);
        int len = bigInteger.intValue();

//        int a = len & 0xFFFFFFFF;

        byte[] data = new byte[len];
        return data;
    }

    private Map<TagId, ParsedTag> parseInternal(byte[] value) {
        ByteBuffer wrap = ByteBuffer.wrap(value).order(ByteOrder.LITTLE_ENDIAN);

        Queue<byte[]> tagsToParse = new LinkedList<>();
        Map<TagId, ParsedTag> result = new HashMap<>();

        while (wrap.hasRemaining()) {

            byte[] rawTagId = new byte[idFieldSize];
            byte[] length = new byte[lengthFieldSize];
            wrap.get(rawTagId).get(length);

//            byte[] reverse = reverse(rawTagId);
            byte[] data = targetDataArrayOf(length);

            wrap.get(data);

            TagId tagId = TagId.of(rawTagId);
            Set<TagId> assocs = tlvTagRegistry.getAssocs(tagId);

            Map<TagId, ParsedTag> tagIdParsedTagMap = Collections.emptyMap();
            if (!assocs.isEmpty()) {
                tagIdParsedTagMap = parseInternal(data);
            }

            ParsedTag parsedTag = new ParsedTag(data, new ArrayList<>(tagIdParsedTagMap.values()), tagId);
            result.put(tagId, parsedTag);
            result.putAll(tagIdParsedTagMap);
        }
        return result;
    }

    public static class Builder {
        private int idFieldSize = 2;
        private int lengthFieldSize = 2;
        private TlvTagRegistry tlvTagRegistry;


        public static Builder newInstance() {
            return new Builder();
        }

        public Builder withIdFieldLength(int lengthFieldSize) {
            this.idFieldSize = lengthFieldSize;
            return this;
        }

        public Builder withLengthFieldSize(int lengthFieldSize) {
            this.lengthFieldSize = lengthFieldSize;
            return this;
        }

        public Builder tlvTagsRegistry(TlvTagRegistry tlvTagRegistry) {
            this.tlvTagRegistry = tlvTagRegistry;
            return this;
        }

        public TlvParser build() {
            return new TlvParser(idFieldSize, lengthFieldSize, tlvTagRegistry);
        }
    }

}
