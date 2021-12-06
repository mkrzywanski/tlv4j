package io.mkrzywanski.tlv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class TagBuilder<T> {

    private final T previous;
    private final List<TagBuilder<TagBuilder<T>>> next;
    private TagId tagId;

    TagBuilder(final T previous) {
        this.previous = previous;
        this.next = new ArrayList<>();
    }

    TagBuilder() {
        this(null);
    }

    public TagBuilder<T> tagId(final TagId tagId) {
        this.tagId = tagId;
        return this;
    }

    public TagBuilder<TagBuilder<T>> beginTag(final TagId tagId) {
        final TagBuilder<TagBuilder<T>> nextTagBuilder = new TagBuilder<>(this).tagId(tagId);
        this.next.add(nextTagBuilder);
        return nextTagBuilder;
    }

    public TagBuilder<T> addTag(final TagId tagId) {
        final TagBuilder<TagBuilder<T>> tagBuilderTagBuilder = beginTag(tagId);
        return tagBuilderTagBuilder.previous;
    }

    public T endTag() {
        return this.previous;
    }

    Map<TagId, Set<TagId>> asTags() {

        final var childTagsAssociations = next.stream().map(TagBuilder::asTags)
                .flatMap(tagIdSetMap -> tagIdSetMap.entrySet().stream())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (tagIds, tagIds2) -> tagIds));

        final Set<TagId> thisTagAssociations = next.stream().map(tagBuilderTagBuilder -> tagBuilderTagBuilder.tagId).collect(toSet());
        final var thisTagAssocs = Map.of(this.tagId, thisTagAssociations);

        return Stream.of(childTagsAssociations, thisTagAssocs)
                .flatMap(tagIdSetMap -> tagIdSetMap.entrySet().stream())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
