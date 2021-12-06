package io.mkrzywanski.tlv;

public final class Unsigned {

    private Unsigned() {
    }

    public static short toShort(final byte b) {
        return  (short) (b & 0xFF);
    }
}
