package io.mkrzywanski;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TlvTagRegistry {

    private final Map<TagId, Set<TagId>> associations;

    TlvTagRegistry(Map<TagId, Set<TagId>> associations) {
        this.associations = associations;
    }

    Set<TagId> getAssocs(TagId tagId) {
        return associations.getOrDefault(tagId, Set.of());
    }

    Set<TagId> allTags() {
        return new HashSet<>(associations.keySet());
    }

}

class TagBuilder<T> {

    private final T previous;
    private final List<TagBuilder<TagBuilder<T>>> next;
    private TagId tagId;

    TagBuilder(T previous) {
        this.previous = previous;
        this.next = new ArrayList<>();
    }

    TagBuilder() {
        this(null);
    }

    TagBuilder<T> tagId(TagId s) {
        this.tagId = s;
        return this;
    }

    TagBuilder<TagBuilder<T>> beginTag(TagId tagId) {
        TagBuilder<TagBuilder<T>> nextTagBuilder = new TagBuilder<>(this).tagId(tagId);
        this.next.add(nextTagBuilder);
        return nextTagBuilder;
    }

    T addTag(TagId tagId) {
        TagBuilder<TagBuilder<T>> tagBuilder = new TagBuilder<>(this).tagId(tagId);
        this.next.add(tagBuilder);
        return this.previous;
    }

    T endTag() {
        return this.previous;
    }

    Map<TagId, Set<TagId>> asTags() {

        Map<TagId, Set<TagId>> childTagsAssociations = next.stream().map(TagBuilder::asTags)
                .flatMap(tagIdSetMap -> tagIdSetMap.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Set<TagId> thisTagAssociations = next.stream().map(tagBuilderTagBuilder -> tagBuilderTagBuilder.tagId).collect(Collectors.toSet());
        Map<TagId, Set<TagId>> thisTagAssocs = Map.of(this.tagId, thisTagAssociations);

        return Stream.of(childTagsAssociations, thisTagAssocs)
                .flatMap(tagIdSetMap -> tagIdSetMap.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}


