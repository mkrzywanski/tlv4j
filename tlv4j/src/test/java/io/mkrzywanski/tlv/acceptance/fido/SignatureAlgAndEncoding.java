package io.mkrzywanski.tlv.acceptance.fido;

import java.util.Arrays;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

enum SignatureAlgAndEncoding {

    ALG_SIGN_SECP256R1_ECDSA_SHA256_RAW(0x0001),
    ALG_SIGN_SECP256R1_ECDSA_SHA256_DER(0x0002),
    ALG_SIGN_RSASSA_PSS_SHA256_RAW(0x0003),
    ALG_SIGN_RSASSA_PSS_SHA256_DER(0x0004),
    ALG_SIGN_SECP256K1_ECDSA_SHA256_RAW(0x0005),
    ALG_SIGN_SECP256K1_ECDSA_SHA256_DER(0x0006),
    ALG_SIGN_SM2_SM3_RAW(0x0007),
    ALG_SIGN_RSA_EMSA_PKCS1_SHA256_RAW(0x0008),
    ALG_SIGN_RSA_EMSA_PKCS1_SHA256_DER(0x0009);

    private static final Map<Integer, SignatureAlgAndEncoding> VALUES;
    private final int value;

    static {
        VALUES = Arrays.stream(SignatureAlgAndEncoding.values())
                .collect(toMap(alg -> alg.value, identity()));
    }

    SignatureAlgAndEncoding(final int value) {
        this.value = value;
    }

    static SignatureAlgAndEncoding parse(final int i) {
        return VALUES.get(i);
    }
}
