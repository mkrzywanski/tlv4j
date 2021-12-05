package io.mkrzywanski.tlv;

import java.util.Collection;
import java.util.function.Function;

public record ParsedTag(byte[] raw,
                        Collection<ParsedTag> children,
                        TagId tagId) {

    public <T> T convert(final Function<byte[], T> converter) {
        return converter.apply(raw);
    }

}
