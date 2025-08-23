package de.hybris.bootstrap.ddl.dbtypesystem;

import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class HashGenerationStrategy
{
    private static final String DEPLOYMENTS = "ydeployments";
    private static final String ATOMIC_TYPE = "atomictypes";
    private static final String ITEM_TYPE = "composedtypes";
    private static final String COLLECTION_TYPE = "collectiontypes";
    private static final String NUMBER_SERIES = "numberseries";
    private static final String ENUMERATION_VALUE = "enumerationvalues";
    private static final String MAP_TYPE = "maptypes";
    private static final String PROPS = "props";
    private static final String TYPE_SYSTEM_PROPS = "typesystemprops";
    private static final String ATTRIBUTE_DESCRIPTOR = "attributedescriptors";
    private static final Map<String, HashGenerationStrategy> strategies = new HashMap<>();

    static
    {
        register("ydeployments", (HashGenerationStrategy)new StrategyForDeployments());
        register("atomictypes", (HashGenerationStrategy)new StrategyForAtomicTypes());
        register("composedtypes", (HashGenerationStrategy)new StrategyForItemTypes());
        register("collectiontypes", (HashGenerationStrategy)new StrategyForCollectionTypes());
        register("numberseries", (HashGenerationStrategy)new StrategyForNumberSeries());
        register("enumerationvalues", (HashGenerationStrategy)new StrategyForEnumerationValues());
        register("maptypes", (HashGenerationStrategy)new StrategyForMapTypes());
        register("props", (HashGenerationStrategy)new StrategyForProps());
        register("typesystemprops", (HashGenerationStrategy)new StrategyForProps());
        register("attributedescriptors", (HashGenerationStrategy)new StrategyForAttributeDescriptors());
    }

    public static HashGenerationStrategy getFor(String tableName)
    {
        Objects.requireNonNull(tableName);
        HashGenerationStrategy result = strategies.get(tableName);
        if(result == null)
        {
            return tryToFindMatchingStrategyFor(tableName);
        }
        return result;
    }


    private static HashGenerationStrategy tryToFindMatchingStrategyFor(String tableName)
    {
        for(String strategyName : Sets.newHashSet(strategies.keySet()))
        {
            if(tableName.startsWith(strategyName))
            {
                HashGenerationStrategy strategy = strategies.get(strategyName);
                strategies.put(tableName, strategy);
                return strategy;
            }
        }
        throw new IllegalArgumentException("Can't find strategy for " + tableName);
    }


    private static void register(String tableName, HashGenerationStrategy strategy)
    {
        Objects.requireNonNull(tableName);
        Objects.requireNonNull(strategy);
        strategies.put(tableName, strategy);
    }


    public static HashGenerationStrategy getForAtomicType()
    {
        return getFor("atomictypes");
    }


    public static HashGenerationStrategy getForAttributeDescriptor()
    {
        return getFor("attributedescriptors");
    }


    public static HashGenerationStrategy getForItemType()
    {
        return getFor("composedtypes");
    }


    public static HashGenerationStrategy getForNumberseries()
    {
        return getFor("numberseries");
    }


    public static HashGenerationStrategy getForMapType()
    {
        return getFor("maptypes");
    }


    public static HashGenerationStrategy getForEnumerationValue()
    {
        return getFor("enumerationvalues");
    }


    public static HashGenerationStrategy getForDeployment()
    {
        return getFor("ydeployments");
    }


    public static HashGenerationStrategy getForCollectionType()
    {
        return getFor("collectiontypes");
    }


    public static HashGenerationStrategy getForProps()
    {
        return getFor("props");
    }


    public abstract String getHashFor(Row paramRow);
}
