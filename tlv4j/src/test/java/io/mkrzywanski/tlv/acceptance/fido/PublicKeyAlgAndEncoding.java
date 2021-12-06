package io.mkrzywanski.tlv.acceptance.fido;

import java.util.Arrays;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

enum PublicKeyAlgAndEncoding {

    ALG_KEY_ECC_X962_RAW(0x100),
    ALG_KEY_ECC_X962_DER(0x0101),
    ALG_KEY_RSA_2048_RAW(0x0102),
    ALG_KEY_RSA_2048_DER(0x0103);

    private static final Map<Integer, PublicKeyAlgAndEncoding> VALUES;
    private final int value;

    static {
        VALUES = Arrays.stream(PublicKeyAlgAndEncoding.values())
                .collect(toMap(alg -> alg.value, identity()));
    }

    PublicKeyAlgAndEncoding(final int value) {
        this.value = value;
    }

    static PublicKeyAlgAndEncoding get(final int i) {
        return VALUES.get(i);
    }
}
