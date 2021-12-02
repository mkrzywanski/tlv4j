package io.mkrzywanski.tlv;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class TagId {

    private final byte[] id;

    private TagId(final byte[] id) {
        this.id = id;
    }

    public static TagId of(final byte[] bytes) {
        return new TagId(Arrays.copyOf(bytes, bytes.length));
    }

    public static TagId of(final int value) {
        final byte[] array = ByteBuffer.allocate(Integer.BYTES)
                .putInt(value)
                .array();
        return new TagId(array);
    }

    public static TagId fromShort(final short value) {
        return fromShort(value, ByteOrder.BIG_ENDIAN);
    }

    public static TagId fromShort(final short value, final ByteOrder byteOrder) {
        final byte[] array = ByteBuffer.allocate(Short.BYTES)
                .order(byteOrder)
                .putShort(value)
                .array();
        return new TagId(array);
    }

    byte[] getId() {
        return id;
    }

    boolean hasLength(final int length) {
        return this.id.length == length;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TagId tagId)) {
            return false;
        }
        return Arrays.equals(getId(), tagId.getId());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getId());
    }
}
