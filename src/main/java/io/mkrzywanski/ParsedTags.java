package io.mkrzywanski;

import java.util.Map;
import java.util.Optional;

public class ParsedTags {

    private final Map<TagId, ParsedTag> tags;

    ParsedTags(Map<TagId, ParsedTag> tagIdParsedTagMap) {
        tags = tagIdParsedTagMap;
    }

    public Optional<ParsedTag> getOpt(TagId tagId) {
        return Optional.ofNullable(tags.get(tagId));
    }

    public ParsedTag get(TagId tagId) {
        return tags.get(tagId);
    }
}
