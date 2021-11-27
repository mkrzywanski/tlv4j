package io.mkrzywanski;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TlvTagRegistryBuilder {

    private final List<TagBuilder<TlvTagRegistryBuilder>> highLevelTagBuilders = new ArrayList<>();

    public static TlvTagRegistryBuilder newInstance() {
        return new TlvTagRegistryBuilder();
    }

    public TagBuilder<TlvTagRegistryBuilder> beginTag(TagId aaa) {
        TagBuilder<TlvTagRegistryBuilder> tagBuilder = new TagBuilder<>(this).tagId(aaa);
        highLevelTagBuilders.add(tagBuilder);
        return tagBuilder;
    }

    public TlvTagRegistryBuilder endTag() {
        return this;
    }

    public TlvTagRegistry build() {
        Map<TagId, Set<TagId>> collect = highLevelTagBuilders.stream()
                .flatMap(b -> b.asTags().entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new TlvTagRegistry(collect);
    }
}
