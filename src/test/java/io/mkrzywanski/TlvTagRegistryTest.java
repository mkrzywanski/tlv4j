package io.mkrzywanski;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TlvTagRegistryTest {

    @Test
    void name() {
        TagId id1 = TagId.of(new byte[]{1});
        TlvTagRegistry build = TlvTagRegistryBuilder.newInstance()
                .beginTag(id1)
                    .beginTag(TagId.of(new byte[]{2})).endTag()
                    .beginTag(TagId.of((new byte[]{3}))).endTag()
                .endTag()
                .build();

        assertThat(build.getAssocs(id1)).hasSize(2);

    }
}