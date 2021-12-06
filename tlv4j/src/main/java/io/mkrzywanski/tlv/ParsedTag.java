package io.mkrzywanski.tlv;

import java.util.Collection;
import java.util.function.Function;

public record ParsedTag(byte[] rawValue,
                        Collection<ParsedTag> children,
                        TagId tagId) {

    public <T> T convert(final Function<byte[], T> converter) {
        return converter.apply(rawValue);
    }

}
