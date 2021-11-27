package io.mkrzywanski;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class TagId {

    private final byte[] id;

    public static TagId of(byte[] bytes) {
        return new TagId(Arrays.copyOf(bytes, bytes.length));
    }

    public static TagId of(int value) {
        byte[] array = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array();
        return new TagId(array);
    }

    public static TagId fromShort(short value) {
        byte[] array = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(value).array();
        return new TagId(array);
    }

    private TagId(byte[] id) {
        this.id = id;
    }

    byte[] getId() {
        return id;
    }

    boolean hasLength(int length) {
        return this.id.length == length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagId)) return false;
        TagId tagId = (TagId) o;
        return Arrays.equals(getId(), tagId.getId());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getId());
    }
}
