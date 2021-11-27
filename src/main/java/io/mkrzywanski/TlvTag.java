package io.mkrzywanski;

public class TlvTag {
    private final TagId tagId;
    private final TagValue tagValue;

    TlvTag(TagId tagId, TagValue tagValue) {
        this.tagId = tagId;
        this.tagValue = tagValue;
    }

    TagId getTagId() {
        return tagId;
    }

    TagValue getTagValue() {
        return tagValue;
    }
}
