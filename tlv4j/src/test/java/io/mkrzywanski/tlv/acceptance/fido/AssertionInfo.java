package io.mkrzywanski.tlv.acceptance.fido;

import io.mkrzywanski.tlv.Unsigned;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

class AssertionInfo {

    private final int authenticatorVersion;
    private final short authenticationMode;
    private final SignatureAlgAndEncoding alg;
    private final PublicKeyAlgAndEncoding keyAlgAndEncoding;

    AssertionInfo(final int authenticatorVersion,
                  final short authenticationMode,
                  final SignatureAlgAndEncoding alg,
                  final PublicKeyAlgAndEncoding keyAlgAndEncoding) {
        this.authenticatorVersion = authenticatorVersion;
        this.authenticationMode = authenticationMode;
        this.alg = alg;
        this.keyAlgAndEncoding = keyAlgAndEncoding;
    }

    public static AssertionInfo fromBytes(final byte[] bytes) {
        final ByteBuffer buffer = ByteBuffer.wrap(bytes)
                .order(ByteOrder.LITTLE_ENDIAN);
        final int authenticatorVersion = Short.toUnsignedInt(buffer.getShort());
        final short authenticationMode = Unsigned.toShort(buffer.get());
        final int alg = Short.toUnsignedInt(buffer.getShort());
        final int pub = Short.toUnsignedInt(buffer.getShort());
        return new AssertionInfo(
                authenticatorVersion,
                authenticationMode,
                SignatureAlgAndEncoding.parse(alg),
                PublicKeyAlgAndEncoding.get(pub)
        );
    }

    int getAuthenticatorVersion() {
        return authenticatorVersion;
    }

    short getAuthenticationMode() {
        return authenticationMode;
    }

    SignatureAlgAndEncoding getAlg() {
        return alg;
    }

    PublicKeyAlgAndEncoding getKeyAlgAndEncoding() {
        return keyAlgAndEncoding;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AssertionInfo that = (AssertionInfo) o;
        return getAuthenticatorVersion() == that.getAuthenticatorVersion()
                && getAuthenticationMode() == that.getAuthenticationMode()
                && getAlg() == that.getAlg()
                && getKeyAlgAndEncoding() == that.getKeyAlgAndEncoding();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuthenticatorVersion(), getAuthenticationMode(), getAlg(), getKeyAlgAndEncoding());
    }
}
