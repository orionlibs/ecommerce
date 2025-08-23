package de.hybris.platform.cache.relation;

import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.RelationsCache;
import de.hybris.platform.util.Config;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultRelationsCache implements RelationsCache
{
    private static final int META_INFORMATION_TYPE_CODE = 55;
    private static final DefaultRelationsCache INSTANCE = instantiate();
    private final Function<TypeId, Stream<RelationAttributes>> relationAttributesProvider;


    DefaultRelationsCache(Function<TypeId, Stream<RelationAttributes>> relationAttributesProvider)
    {
        this.relationAttributesProvider = Objects.<Function<TypeId, Stream<RelationAttributes>>>requireNonNull(relationAttributesProvider);
    }


    private static DefaultRelationsCache instantiate()
    {
        TypeSystemRelationAttributesProvider typeSystemRelationAttributesProvider = new TypeSystemRelationAttributesProvider();
        TypePKsExtractor typePKsExtractor = new TypePKsExtractor(Set.of(Integer.valueOf(55)));
        Objects.requireNonNull(typeSystemRelationAttributesProvider);
        Objects.requireNonNull(typePKsExtractor);
        CachingRelationAttributesProvider cachingRelationAttributesProvider = new CachingRelationAttributesProvider(typeSystemRelationAttributesProvider::get, typePKsExtractor::extractTypePKs);
        Objects.requireNonNull(cachingRelationAttributesProvider);
        ConfigurableRelationAttributesProvider configurableRelationAttributesProvider = new ConfigurableRelationAttributesProvider(cachingRelationAttributesProvider::getRelationAttributes, Config::getBoolean);
        Objects.requireNonNull(configurableRelationAttributesProvider);
        return new DefaultRelationsCache(configurableRelationAttributesProvider::getRelationAttributes);
    }


    public static RelationsCache instance()
    {
        return INSTANCE;
    }


    public InvalidationListener createInvalidationListener()
    {
        return (InvalidationListener)new RelationInvalidationListener(this::getAllCacheableAttributes);
    }


    public RelationAttributes getSingleCacheableAttributes(TypeId typeId)
    {
        Collection<RelationAttributes> all = getAllCacheableAttributes(typeId);
        if(all.isEmpty())
        {
            return RelationAttributes.empty();
        }
        if(all.size() == 1)
        {
            RelationAttributes result = all.iterator().next();
            if(result != null)
            {
                return result;
            }
        }
        throw new IllegalStateException("Expected single result for `" + typeId + "` but `" + all + "` has been returned.");
    }


    public Collection<RelationAttributes> getAllCacheableAttributes(TypeId typeId)
    {
        return (Collection<RelationAttributes>)((Stream)this.relationAttributesProvider.apply(typeId))
                        .collect(Collectors.toUnmodifiableList());
    }
}
