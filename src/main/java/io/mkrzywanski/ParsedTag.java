package io.mkrzywanski;

import java.util.List;
import java.util.function.Function;

public record ParsedTag(byte[] raw, List<ParsedTag> children,
                         TagId tagId) {

    <T> T convert(Function<byte[], T> converter) {
        return converter.apply(raw);
    }

}
