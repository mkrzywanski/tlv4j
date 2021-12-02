package io.mkrzywanski.tlv;

import org.junit.jupiter.api.Test;

import java.nio.ByteOrder;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

public class FidoTest {

    @Test
    void shouldParseFidoTlvEncodedData() {
        String uafAssertion = """
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

        byte[] decode = Base64.getUrlDecoder()
                .decode(uafAssertion.replace("\n", "").replace(" ", ""));

        var TAG_UAFV1_REG_ASSERTION = TagId.fromShort((short) 0x3E01, ByteOrder.LITTLE_ENDIAN);
        var TAG_UAFV1_KRD = TagId.fromShort((short) 0x3E03, ByteOrder.LITTLE_ENDIAN);
        var TAG_AAID = TagId.fromShort((short) 0x2E0B, ByteOrder.LITTLE_ENDIAN);
        var TAG_ASSERTION_INFO = TagId.fromShort((short) 0x2E0E, ByteOrder.LITTLE_ENDIAN);
        var TAG_FINAL_CHALLENGE_HASH = TagId.fromShort((short) 0x2E0A, ByteOrder.LITTLE_ENDIAN);
        var TAG_KEYID = TagId.fromShort((short) 0x2E09, ByteOrder.LITTLE_ENDIAN);
        var TAG_COUNTERS = TagId.fromShort((short) 0x2E0D, ByteOrder.LITTLE_ENDIAN);
        var TAG_PUB_KEY = TagId.fromShort((short) 0x2E0C, ByteOrder.LITTLE_ENDIAN);
        var TAG_ATTESTATION_BASIC_FULL = TagId.fromShort((short) 0x3E07, ByteOrder.LITTLE_ENDIAN);
        var TAG_SIGNATURE = TagId.fromShort((short) 0x2E06, ByteOrder.LITTLE_ENDIAN);
        var TAG_ATTESTATION_CERT = TagId.fromShort((short) 0x2E05, ByteOrder.LITTLE_ENDIAN);
        var TAG_ATTESTATION_BASIC_SURROGATE = TagId.fromShort((short) 0x3E07, ByteOrder.LITTLE_ENDIAN);


        TlvTagRegistry tlvTagRegistryBuilder = TlvTagRegistryBuilder.newInstance()
                .beginTag(TAG_UAFV1_REG_ASSERTION)
                .beginTag(TAG_UAFV1_KRD)
                .beginTag(TAG_AAID).endTag()
                .beginTag(TAG_ASSERTION_INFO).endTag()
                .beginTag(TAG_FINAL_CHALLENGE_HASH).endTag()
                .beginTag(TAG_KEYID).endTag().endTag()
                .beginTag(TAG_COUNTERS).endTag()
                .beginTag(TAG_PUB_KEY).endTag()
                .endTag()
                .endTag()
                .build();
//                    .beginTag(TAG_ATTESTATION_BASIC_FULL)

        TlvParser tlvParser = TlvParser.Builder.newInstance()
                .tlvTagsRegistry(tlvTagRegistryBuilder)
                .withByteOrder(ByteOrder.LITTLE_ENDIAN)
                .build();

        ParsedTags parse = tlvParser.parse(decode);

        String convert = parse.get(TAG_AAID).convert(String::new);

        assertThat(convert).isEqualTo("ABCD#ABCD");

    }
}
