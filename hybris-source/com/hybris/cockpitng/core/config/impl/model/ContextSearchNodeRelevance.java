/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * Default relevance for search nodes.
 * <P>
 * This implementation stores different relevance zones and a level of relevance for each of them. Zones are sorted in
 * order from most important to least. While comparing this type of relevance a zone with lower priority is checked only
 * if relevance levels in higher priority zone are equal.
 */
public class ContextSearchNodeRelevance implements NodeRelevance
{
    private final Map<String, Integer> levels;


    /**
     * @param zones
     *           relevance zones in order from most important to least important
     */
    public ContextSearchNodeRelevance(final String... zones)
    {
        this(Arrays.asList(zones));
    }


    /**
     * @param zones
     *           relevance zones ordered from most important to least important
     */
    public ContextSearchNodeRelevance(final Collection<String> zones)
    {
        this.levels = new LinkedHashMap<>();
        final List<String> zonesList = new ArrayList<>(zones);
        zonesList.forEach(zone -> levels.put(zone, Integer.MAX_VALUE));
    }


    public ContextSearchNodeRelevance(final ContextSearchNodeRelevance reference)
    {
        this.levels = new LinkedHashMap<>();
        this.levels.putAll(reference.levels);
    }


    /**
     * Sets relevance level form specified zone.
     * <P>
     * The lower level, the higher relevance is.
     *
     * @param zone
     *           name of relevance zone
     * @param level
     *           relevance zone for this zone
     * @throws IllegalArgumentException
     *            thrown if zone of provided name is not defined for this relevance
     */
    public void setLevel(final String zone, final int level) throws IllegalArgumentException
    {
        if(!levels.containsKey(zone))
        {
            throw new IllegalArgumentException("Unknown relevance zone: " + zone);
        }
        levels.put(zone, level);
    }


    /**
     * Gets relevance level form specified zone.
     * <P>
     * The lower level, the higher relevance is.
     *
     * @param zone
     *           name of relevance zone
     * @return relevance for provided zone or {@link Integer#MAX_VALUE} if not relevant in this zone
     */
    public int getLevel(final String zone)
    {
        if(!levels.containsKey(zone))
        {
            return Integer.MAX_VALUE;
        }
        else
        {
            return levels.get(zone);
        }
    }


    /**
     * Decreases relevance level in specified zone by 1.
     *
     * @param zone
     *           name of relevance zone
     * @return new level of relevance
     * @throws IllegalArgumentException
     *            thrown if zone of provided name is not defined for this relevance
     */
    public int decreaseLevel(final String zone) throws IllegalArgumentException
    {
        int currentLevel = getLevel(zone);
        if(currentLevel < Integer.MAX_VALUE)
        {
            setLevel(zone, ++currentLevel);
        }
        return currentLevel;
    }


    /**
     * @deprecated since 6.7 Use copy constructor instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    @Override
    public ContextSearchNodeRelevance clone()
    {
        final ContextSearchNodeRelevance result = new ContextSearchNodeRelevance(levels.keySet());
        result.levels.putAll(levels);
        return result;
    }


    @Override
    public int compareTo(final Object o)
    {
        if(!(o instanceof ContextSearchNodeRelevance))
        {
            return 1;
        }
        int result = 0;
        for(final Iterator<String> it = levels.keySet().iterator(); it.hasNext() && result == 0; )
        {
            final String zone = it.next();
            result = -Integer.compare(getLevel(zone), ((ContextSearchNodeRelevance)o).getLevel(zone));
        }
        return result;
    }


    @Override
    public String toString()
    {
        final List<String> strings = levels.values().stream()
                        .map(level -> Integer.valueOf(Integer.MAX_VALUE).equals(level) ? "MAX_VALUE" : Integer.toString(level))
                        .collect(Collectors.toList());
        return StringUtils.join(strings, ",");
    }


    /**
     * Creates a search node relevance that is totally irrelevant in specified zones
     *
     * @param zones
     *           relevance zones
     * @return lowest relevance
     */
    public static ContextSearchNodeRelevance irrelevant(final String... zones)
    {
        return irrelevant(Arrays.asList(zones));
    }


    /**
     * Creates a search node relevance that is totally irrelevant in specified zones
     *
     * @param zones
     *           relevance zones
     * @return lowest relevance
     */
    public static ContextSearchNodeRelevance irrelevant(final List<String> zones)
    {
        return new ContextSearchNodeRelevance(zones);
    }


    /**
     * Creates a search node relevance with highest relevance in specified zones
     *
     * @param zones
     *           relevance zones
     * @return highest relevance
     */
    public static ContextSearchNodeRelevance mostRelevant(final String... zones)
    {
        return mostRelevant(Arrays.asList(zones));
    }


    /**
     * Creates a search node relevance with highest relevance in specified zones
     *
     * @param zones
     *           relevance zones
     * @return highest relevance
     */
    public static ContextSearchNodeRelevance mostRelevant(final Collection<String> zones)
    {
        final ContextSearchNodeRelevance result = new ContextSearchNodeRelevance(zones);
        zones.forEach(zone -> result.setLevel(zone, 0));
        return result;
    }
}
