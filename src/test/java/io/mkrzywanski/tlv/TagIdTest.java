package io.mkrzywanski.tlv;

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
