package io.mkrzywanski.tlv;

import java.util.Map;
import java.util.Optional;

public class ParsedTags {

    private final Map<TagId, ParsedTag> tags;

    ParsedTags(final Map<TagId, ParsedTag> tagIdParsedTagMap) {
        tags = tagIdParsedTagMap;
    }

    public Optional<ParsedTag> getAsOptional(final TagId tagId) {
        return Optional.ofNullable(tags.get(tagId));
    }

    public ParsedTag get(final TagId tagId) {
        return tags.get(tagId);
    }
}
