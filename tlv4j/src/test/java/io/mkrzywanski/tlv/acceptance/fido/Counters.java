package io.mkrzywanski.tlv.acceptance.fido;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

class Counters {
    private final long signatureCounter;
    private final long registrationCounter;

    Counters(final long signatureCounter, final long registrationCounter) {
        this.signatureCounter = signatureCounter;
        this.registrationCounter = registrationCounter;
    }

    static Counters fromBytes(final byte[] bytes) {
        final ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        final long signatureCounter = Integer.toUnsignedLong(buffer.getInt());
        final long registrationCounter = Integer.toUnsignedLong(buffer.getInt());
        return new Counters(signatureCounter, registrationCounter);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Counters counters = (Counters) o;
        return signatureCounter == counters.signatureCounter
                && registrationCounter == counters.registrationCounter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(signatureCounter, registrationCounter);
    }
}
