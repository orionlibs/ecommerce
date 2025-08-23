package de.hybris.platform.cache.relation;

import de.hybris.platform.core.PK;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Stream;

class CachingRelationAttributesProvider
{
    private final ConcurrentHashMap<PK, RelationAttributes> typePkToAttributes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<TypeId, Collection<PK>> typeIdToTypePKs = new ConcurrentHashMap<>();
    private final Function<PK, RelationAttributes> attributesProvider;
    private final Function<TypeId, Collection<PK>> typePKsExtractor;


    public CachingRelationAttributesProvider(Function<PK, RelationAttributes> attributesProvider, Function<TypeId, Collection<PK>> typePKsExtractor)
    {
        this.attributesProvider = Objects.<Function<PK, RelationAttributes>>requireNonNull(attributesProvider);
        this.typePKsExtractor = Objects.<Function<TypeId, Collection<PK>>>requireNonNull(typePKsExtractor);
    }


    public Stream<RelationAttributes> getRelationAttributes(TypeId typeId)
    {
        return getTypePKs(typeId).map(this::getRelationAttributes);
    }


    private Stream<PK> getTypePKs(TypeId typeId)
    {
        return typeId.getTypePk().map(Stream::of).orElseGet(() -> extractTypePKs(typeId));
    }


    private Stream<PK> extractTypePKs(TypeId typeId)
    {
        return ((Collection<PK>)this.typeIdToTypePKs.computeIfAbsent(typeId, this.typePKsExtractor)).stream();
    }


    private RelationAttributes getRelationAttributes(PK typePk)
    {
        return this.typePkToAttributes.computeIfAbsent(typePk, this.attributesProvider);
    }


    public void clearCache()
    {
        this.typePkToAttributes.clear();
        this.typeIdToTypePKs.clear();
    }
}
