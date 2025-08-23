package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.data.AbstractAsConfiguration;
import de.hybris.platform.adaptivesearch.data.AsConfigurationHolder;
import de.hybris.platform.adaptivesearch.util.MergeFunction;
import de.hybris.platform.adaptivesearch.util.MergeMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections.MapUtils;

public class DefaultMergeMap<K, T extends AbstractAsConfiguration, R extends AbstractAsConfiguration> extends HashMap<K, AsConfigurationHolder<T, R>> implements MergeMap<K, AsConfigurationHolder<T, R>>
{
    protected static final RankComparator RANK_COMPARATOR = new RankComparator();
    private int highestRank;
    private int lowestRank;
    private transient Comparator<AsConfigurationHolder<T, R>> comparator;


    public DefaultMergeMap()
    {
        this.highestRank = 0;
        this.lowestRank = 0;
    }


    public DefaultMergeMap(Comparator<AsConfigurationHolder<T, R>> comparator)
    {
        this.highestRank = 0;
        this.lowestRank = 0;
        this.comparator = comparator;
    }


    public void clear()
    {
        this.highestRank = 0;
        this.lowestRank = 0;
        super.clear();
    }


    public AsConfigurationHolder<T, R> put(K key, AsConfigurationHolder<T, R> value)
    {
        return mergeAfter(key, value);
    }


    public int getHighestRank()
    {
        return this.highestRank;
    }


    public int getLowestRank()
    {
        return this.lowestRank;
    }


    public AsConfigurationHolder<T, R> mergeBefore(K key, AsConfigurationHolder<T, R> value)
    {
        this.highestRank--;
        value.setRank(this.highestRank);
        return super.put(key, value);
    }


    public void mergeBefore(MergeMap<K, AsConfigurationHolder<T, R>> source, MergeFunction<K, AsConfigurationHolder<T, R>> mergeFunction)
    {
        if(MapUtils.isEmpty((Map)source))
        {
            return;
        }
        int range = source.getLowestRank() - source.getHighestRank();
        int baseRank = this.highestRank - 1 - range - source.getHighestRank();
        this.highestRank = this.highestRank - 1 - range;
        for(Map.Entry<K, AsConfigurationHolder<T, R>> entry : (Iterable<Map.Entry<K, AsConfigurationHolder<T, R>>>)source.entrySet())
        {
            AsConfigurationHolder<T, R> configurationHolder = (AsConfigurationHolder<T, R>)compute(entry.getKey(), (key, value) -> (AsConfigurationHolder)mergeFunction.apply(key, value, entry.getValue()));
            if(configurationHolder != null)
            {
                configurationHolder.setRank(baseRank + ((AsConfigurationHolder)entry.getValue()).getRank());
            }
        }
    }


    public AsConfigurationHolder<T, R> mergeAfter(K key, AsConfigurationHolder<T, R> value)
    {
        this.lowestRank++;
        value.setRank(this.lowestRank);
        return super.put(key, value);
    }


    public void mergeAfter(MergeMap<K, AsConfigurationHolder<T, R>> source, MergeFunction<K, AsConfigurationHolder<T, R>> mergeFunction)
    {
        if(MapUtils.isEmpty((Map)source))
        {
            return;
        }
        int range = source.getLowestRank() - source.getHighestRank();
        int baseRank = this.lowestRank + 1 - source.getHighestRank();
        this.lowestRank = this.lowestRank + 1 + range;
        for(Map.Entry<K, AsConfigurationHolder<T, R>> entry : (Iterable<Map.Entry<K, AsConfigurationHolder<T, R>>>)source.entrySet())
        {
            AsConfigurationHolder<T, R> configurationHolder = (AsConfigurationHolder<T, R>)compute(entry.getKey(), (key, value) -> (AsConfigurationHolder)mergeFunction.apply(key, value, entry.getValue()));
            if(configurationHolder != null)
            {
                configurationHolder.setRank(baseRank + ((AsConfigurationHolder)entry.getValue()).getRank());
            }
        }
    }


    public List<AsConfigurationHolder<T, R>> orderedValues()
    {
        if(this.comparator == null)
        {
            return (List<AsConfigurationHolder<T, R>>)values().stream().sorted((Comparator)RANK_COMPARATOR).collect(Collectors.toList());
        }
        return (List<AsConfigurationHolder<T, R>>)values().stream().sorted(this.comparator.thenComparing((Comparator<? super AsConfigurationHolder<T, R>>)RANK_COMPARATOR)).collect(Collectors.toList());
    }


    public boolean equals(Object o)
    {
        return super.equals(o);
    }


    public int hashCode()
    {
        return super.hashCode();
    }
}
