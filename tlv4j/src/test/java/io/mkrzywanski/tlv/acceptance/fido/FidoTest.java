package io.mkrzywanski.tlv.acceptance.fido;

import io.mkrzywanski.tlv.ParsedTag;
import io.mkrzywanski.tlv.ParsedTagAssert;
import io.mkrzywanski.tlv.ParsedTags;
import io.mkrzywanski.tlv.TagId;
import io.mkrzywanski.tlv.TlvParser;
import io.mkrzywanski.tlv.TlvTagRegistry;
import io.mkrzywanski.tlv.TlvTagRegistryBuilder;
import org.junit.jupiter.api.Test;

import java.nio.ByteOrder;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class FidoTest {

    private static final AssertionInfo EXPECTED_ASSERTION_INFO = new AssertionInfo(256, (short) 1, SignatureAlgAndEncoding.ALG_SIGN_SECP256R1_ECDSA_SHA256_RAW, PublicKeyAlgAndEncoding.ALG_KEY_ECC_X962_RAW);

    @Test
    @SuppressWarnings("checkstyle:abbreviationaswordinname")
    void shouldParseFidoTlvEncodedData() {
        final String uafAssertion = """
                   AT7uAgM-sQALLgkAQUJDRCNBQkNEDi4HAAABAQEAAAEKLiAA9t
                   BzZC64ecgVQBGSQb5QtEIPC8-Vav4HsHLZDflLaugJLiAAZMCPn92yHv1Ip-iCiBb6i4ADq6
                   ZOv569KFQCvYSJfNgNLggAAQAAAAEAAAAMLkEABJsvEtUsVKh7tmYHhJ2FBm3kHU-OCdWiUY
                   VijgYa81MfkjQ1z6UiHbKP9_nRzIN9anprHqDGcR6q7O20q_yctZAHPjUCBi5AACv8L7YlRM
                   x10gPnszGO6rLFqZFmmRkhtV0TIWuWqYxd1jO0wxam7i5qdEa19u4sfpHFZ9RGI_WHxINkH8
                   FfvAwFLu0BMIIB6TCCAY8CAQEwCQYHKoZIzj0EATB7MQswCQYDVQQGEwJVUzELMAkGA1UECA
                   wCQ0ExCzAJBgNVBAcMAlBBMRAwDgYDVQQKDAdOTkwsSW5jMQ0wCwYDVQQLDAREQU4xMRMwEQ
                   YDVQQDDApOTkwsSW5jIENBMRwwGgYJKoZIhvcNAQkBFg1ubmxAZ21haWwuY29tMB4XDTE0MD
                   gyODIxMzU0MFoXDTE3MDUyNDIxMzU0MFowgYYxCzAJBgNVBAYTAlVTMQswCQYDVQQIDAJDQT
                   EWMBQGA1UEBwwNU2FuIEZyYW5jaXNjbzEQMA4GA1UECgwHTk5MLEluYzENMAsGA1UECwwERE
                   FOMTETMBEGA1UEAwwKTk5MLEluYyBDQTEcMBoGCSqGSIb3DQEJARYNbm5sQGdtYWlsLmNvbT
                   BZMBMGByqGSM49AgEGCCqGSM49AwEHA0IABCGBt3CIjnDowzSiF68C2aErYXnDUsWXOYxqIP
                   im0OWg9FFdUYCa6AgKjn1R99Ek2d803sGKROivnavmdVH-SnEwCQYHKoZIzj0EAQNJADBGAi
                   EAzAQujXnSS9AIAh6lGz6ydypLVTsTnBzqGJ4ypIqy_qUCIQCFsuOEGcRV-o4GHPBph_VMrG
                   3NpYh2GKPjsAim_cSNmQ
                """;

        final byte[] bytes = Base64.getUrlDecoder()
                .decode(uafAssertion.replace("\n", "").replace(" ", ""));

        final var TAG_UAFV1_REG_ASSERTION = TagId.fromShort((short) 0x3E01, ByteOrder.LITTLE_ENDIAN);
        final var TAG_UAFV1_KRD = TagId.fromShort((short) 0x3E03, ByteOrder.LITTLE_ENDIAN);
        final var TAG_AAID = TagId.fromShort((short) 0x2E0B, ByteOrder.LITTLE_ENDIAN);
        final var TAG_ASSERTION_INFO = TagId.fromShort((short) 0x2E0E, ByteOrder.LITTLE_ENDIAN);
        final var TAG_FINAL_CHALLENGE_HASH = TagId.fromShort((short) 0x2E0A, ByteOrder.LITTLE_ENDIAN);
        final var TAG_KEYID = TagId.fromShort((short) 0x2E09, ByteOrder.LITTLE_ENDIAN);
        final var TAG_COUNTERS = TagId.fromShort((short) 0x2E0D, ByteOrder.LITTLE_ENDIAN);
        final var TAG_PUB_KEY = TagId.fromShort((short) 0x2E0C, ByteOrder.LITTLE_ENDIAN);
        final var TAG_ATTESTATION_BASIC_FULL = TagId.fromShort((short) 0x3E07, ByteOrder.LITTLE_ENDIAN);
        final var TAG_SIGNATURE = TagId.fromShort((short) 0x2E06, ByteOrder.LITTLE_ENDIAN);
        final var TAG_ATTESTATION_CERT = TagId.fromShort((short) 0x2E05, ByteOrder.LITTLE_ENDIAN);
        final var TAG_ATTESTATION_BASIC_SURROGATE = TagId.fromShort((short) 0x3E08, ByteOrder.LITTLE_ENDIAN);


        final TlvTagRegistry registry = TlvTagRegistryBuilder.newInstance()
                .beginTag(TAG_UAFV1_REG_ASSERTION)
                    .beginTag(TAG_UAFV1_KRD)
                        .addTag(TAG_AAID)
                        .addTag(TAG_ASSERTION_INFO)
                        .addTag(TAG_FINAL_CHALLENGE_HASH)
                        .addTag(TAG_KEYID)
                        .addTag(TAG_COUNTERS)
                        .addTag(TAG_PUB_KEY)
                    .endTag()
                    .beginTag(TAG_ATTESTATION_BASIC_FULL)
                        .addTag(TAG_SIGNATURE)
                    .endTag()
                    .beginTag(TAG_ATTESTATION_BASIC_SURROGATE)
                        .addTag(TAG_SIGNATURE)
                    .endTag()
                .endTag()
                .build();

        final TlvParser tlvParser = TlvParser.Builder.newInstance()
                .tlvTagsRegistry(registry)
                .withByteOrder(ByteOrder.LITTLE_ENDIAN)
                .build();

        final ParsedTags tags = tlvParser.parse(bytes);

        final String aaid = tags.get(TAG_AAID).convert(String::new);
        final AssertionInfo assertionInfo = tags.get(TAG_ASSERTION_INFO).convert(AssertionInfo::fromBytes);
        final Counters counters = tags.get(TAG_COUNTERS).convert(Counters::fromBytes);

        assertThat(aaid).isEqualTo("ABCD#ABCD");
        assertThat(assertionInfo).isEqualTo(EXPECTED_ASSERTION_INFO);
        ParsedTagAssert.assertThat(tags.get(TAG_FINAL_CHALLENGE_HASH)).hasNonEmptyRawValue();
        ParsedTagAssert.assertThat(tags.get(TAG_KEYID)).hasNonEmptyRawValue();
        assertThat(counters).isEqualTo(new Counters(1, 1));
        ParsedTagAssert.assertThat(tags.get(TAG_PUB_KEY)).hasNonEmptyRawValue();
        final ParsedTag parsedTag = tags.get(TAG_SIGNATURE);
        System.out.println("done");
    }
}
