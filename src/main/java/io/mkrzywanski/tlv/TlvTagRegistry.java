package io.mkrzywanski.tlv;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TlvTagRegistry {

    private final Map<TagId, Set<TagId>> associations;

    TlvTagRegistry(final Map<TagId, Set<TagId>> associations) {
        this.associations = associations;
    }

    public static TlvTagRegistry empty() {
        return new TlvTagRegistry(new HashMap<>());
    }

    public Set<TagId> getChildTags(final TagId tagId) {
        return associations.getOrDefault(tagId, Set.of());
    }

    public Set<TagId> allTags() {
        return new HashSet<>(associations.keySet());
    }

}




