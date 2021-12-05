package io.mkrzywanski.tlv;

import java.nio.ByteBuffer;

class ByteBufferSlicer {

    private final int idFieldSize;
    private final int lengthFieldSize;
    private final ByteBuffer byteBuffer;

    ByteBufferSlicer(final int idFieldSize, final int lengthFieldSize, final ByteBuffer byteBuffer) {
        this.idFieldSize = idFieldSize;
        this.lengthFieldSize = lengthFieldSize;
        this.byteBuffer = byteBuffer;
    }

    byte[] nextTagId() {
        final byte[] bytes = new byte[idFieldSize];
        byteBuffer.get(bytes);
        return bytes;
    }

    byte[] nextLength() {
        final byte[] bytes = new byte[lengthFieldSize];
        byteBuffer.get(bytes);
        return bytes;
    }

    byte[] readData(final int length) {
        final byte[] bytes = new byte[length];
        byteBuffer.get(bytes);
        return bytes;
    }
}
