package io.mkrzywanski;

import java.nio.ByteBuffer;

public class ByteBufferSlicer {
    private final int idFieldSize;
    private final int lengthFieldSize;
    private final ByteBuffer byteBuffer;

    public ByteBufferSlicer(int idFieldSize, int lengthFieldSize, ByteBuffer byteBuffer) {
        this.idFieldSize = idFieldSize;
        this.lengthFieldSize = lengthFieldSize;
        this.byteBuffer = byteBuffer;
    }

    byte[] nextTagId() {
        byte[] bytes = new byte[idFieldSize];
        byteBuffer.get(bytes);
        return bytes;
    }

    byte[] nextLength() {
        byte[] bytes = new byte[lengthFieldSize];
        byteBuffer.get(bytes);
        return bytes;
    }

    byte[] readData(int howMuch) {
        byte[] bytes = new byte[howMuch];
        byteBuffer.get(bytes);
        return bytes;
    }
}
