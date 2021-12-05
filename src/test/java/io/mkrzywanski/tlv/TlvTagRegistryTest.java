package io.mkrzywanski.tlv;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TlvTagRegistryTest {

    @Test
    void shouldBuildTagRegistry() {
        final TagId tagId = TagId.of(new byte[]{1});
        final TlvTagRegistry registry = TlvTagRegistryBuilder.newInstance()
                .beginTag(tagId)
                    .beginTag(TagId.of(new byte[]{2})).endTag()
                    .beginTag(TagId.of((new byte[]{3}))).endTag()
                .endTag()
                .build();

        assertThat(registry.getChildTags(tagId)).hasSize(2);

    }
}
