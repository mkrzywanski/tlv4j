package io.mkrzywanski.tlv;

final class ByteArrayUtils {

    private ByteArrayUtils() {
    }

    static byte[] reverse(final byte[] arr) {
        final byte[] rev = new byte[arr.length];
        for (int i = 0, j = arr.length - 1; j >= 0; i++, j--) {
            rev[j] = arr[i];
        }
        return rev;
    }
}
