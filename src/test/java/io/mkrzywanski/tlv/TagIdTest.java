package io.mkrzywanski.tlv;

import io.mkrzywanski.tlv.TagId;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class TagIdTest {
    @Test
    void equalsHashcodeTest() {
        EqualsVerifier.simple()
                .forClass(TagId.class)
                .verify();
    }
}