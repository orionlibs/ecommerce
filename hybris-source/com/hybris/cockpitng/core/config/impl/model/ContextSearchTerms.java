/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.model;

import com.hybris.cockpitng.core.config.CockpitConfigurationContextStrategy;
import com.hybris.cockpitng.core.config.impl.cache.ConfigurationSearchFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Current search terms.
 * <P>
 * A object does not define any search progress or request, yet some additional conditions and state of search.
 * <P>
 * Search conditions may vary between two different branches of search tree and is used for comparison, if a node is
 * particular search is already executed and may be only referenced.
 */
public class ContextSearchTerms
{
    private final Map<String, List<String>> resetState;
    private final Map<String, Integer> resetRelevance;
    private NodeRelevance highestRelevance;
    private ConfigurationSearchFilter exclusions;


    public ContextSearchTerms(final Map<String, List<String>> resetState, final Map<String, Integer> resetRelevance)
    {
        this.resetRelevance = resetRelevance;
        this.resetState = new HashMap<>();
        resetState.entrySet().forEach(
                        state -> this.resetState.put(state.getKey(), Collections.unmodifiableList(new ArrayList<>(state.getValue()))));
    }


    /**
     * State to which attributes should be reset if needed.
     * <P>
     * Some attributes may be reset on merge. This is defined by {@link CockpitConfigurationContextStrategy#isResettable()}.
     * If a strategy allows to reset attribute, then whenever a match is found that defines some <code>merge-by</code>, then
     * children are searched with strategy's attributed reset (if not pointed by <code>merge-by</code>).
     *
     * @return map of attributes and their values to which a search should be reset
     */
    public Map<String, List<String>> getResetState()
    {
        return Collections.unmodifiableMap(resetState);
    }


    /**
     * Relevance to which attributes should reset if needed.
     * <P>
     * Some attributes may be reset on merge. This is defined by {@link CockpitConfigurationContextStrategy#isResettable()}.
     * If a strategy allows to reset attribute, then whenever a match is found that defines some <code>merge-by</code>, then
     * children are searched with strategy's attributed reset (if not pointed by <code>merge-by</code>).
     *
     * @return map of attributes and their relevance to which a search should be reset
     */
    public Map<String, Integer> getResetRelevance()
    {
        return Collections.unmodifiableMap(resetRelevance);
    }


    /**
     * Gets highest relevance of from all children with matches of the closest parent.
     *
     * @return highest relevance or <code>null</code> if no matches yet
     */
    public NodeRelevance getHighestMatchingRelevance()
    {
        return highestRelevance;
    }


    /**
     * Sets highest relevance of from all children with matches of the closest parent.
     *
     * @param highestRelevance
     *           highest relevance or <code>null</code> if no matches yet
     */
    public void setHighestMatchingRelevance(final NodeRelevance highestRelevance)
    {
        this.highestRelevance = highestRelevance;
    }


    /**
     * Gets an object able to decide which queries should be excluded from search.
     *
     * @return exclusions or <code>null</code> if all queries should be applied
     */
    public ConfigurationSearchFilter getExclusions()
    {
        return exclusions;
    }


    /**
     * Sets an object able to decide which queries should be excluded from search.
     *
     * @param exclusions
     *           new exclusions or <code>null</code> if all queries should be applied
     */
    public void setExclusions(final ConfigurationSearchFilter exclusions)
    {
        this.exclusions = exclusions;
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null)
        {
            return false;
        }
        if(o.getClass() != this.getClass())
        {
            return false;
        }
        final ContextSearchTerms that = (ContextSearchTerms)o;
        if(!resetState.equals(that.resetState))
        {
            return false;
        }
        return resetRelevance.equals(that.resetRelevance);
    }


    @Override
    public int hashCode()
    {
        int result = resetState.hashCode();
        result = 31 * result + resetRelevance.hashCode();
        return result;
    }
}
