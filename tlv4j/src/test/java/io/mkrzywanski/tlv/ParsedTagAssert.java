package io.mkrzywanski.tlv;

import org.assertj.core.api.AbstractAssert;

public class ParsedTagAssert extends AbstractAssert<ParsedTagAssert, ParsedTag> {

    private ParsedTagAssert(final ParsedTag parsedTag, final Class<?> selfType) {
        super(parsedTag, selfType);
    }

    public static ParsedTagAssert assertThat(final ParsedTag parsedTag) {
        return new ParsedTagAssert(parsedTag, ParsedTagAssert.class);
    }

    public ParsedTagAssert hasRawLength(final int length) {
        if (actual.rawValue().length != length) {
            failWithMessage("error");
        }
        return this;
    }

    public ParsedTagAssert hasNonEmptyRawValue() {
        final byte[] raw = actual.rawValue();
        if (raw == null || raw.length == 0) {
            failWithMessage("Empty raw value");
        }
        return this;
    }
}
