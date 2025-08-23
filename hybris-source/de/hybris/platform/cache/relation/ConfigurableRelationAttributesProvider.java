package de.hybris.platform.cache.relation;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Stream;

class ConfigurableRelationAttributesProvider
{
    static final String GLOBAL_ENABLE_FLAG = "relation.cache.enabled";
    private final Function<TypeId, Stream<RelationAttributes>> attributesProvider;
    private final BiPredicate<String, Boolean> config;


    ConfigurableRelationAttributesProvider(Function<TypeId, Stream<RelationAttributes>> attributesProvider, BiPredicate<String, Boolean> config)
    {
        this.attributesProvider = Objects.<Function<TypeId, Stream<RelationAttributes>>>requireNonNull(attributesProvider);
        this.config = Objects.<BiPredicate<String, Boolean>>requireNonNull(config);
    }


    static String typeEnableFlag(String typeName)
    {
        return "relation.cache." + typeName + ".enabled";
    }


    public Stream<RelationAttributes> getRelationAttributes(TypeId typeId)
    {
        Objects.requireNonNull(typeId);
        if(!isEnabledGlobally() || isCachingDisabled(typeId))
        {
            return Stream.empty();
        }
        return ((Stream<RelationAttributes>)this.attributesProvider.apply(typeId))
                        .filter(this::isConfiguredToBeCached);
    }


    private boolean isCachingDisabled(TypeId typeId)
    {
        return
                        !((Boolean)typeId.getTypeName().map(this::isConfiguredToBeCached).orElse(Boolean.TRUE)).booleanValue();
    }


    private boolean isEnabledGlobally()
    {
        return this.config.test("relation.cache.enabled", Boolean.valueOf(true));
    }


    private boolean isConfiguredToBeCached(RelationAttributes attributes)
    {
        return isConfiguredToBeCached(attributes.getOwningTypeCode());
    }


    private boolean isConfiguredToBeCached(String typeName)
    {
        return this.config.test(typeEnableFlag(typeName), Boolean.valueOf(false));
    }
}
