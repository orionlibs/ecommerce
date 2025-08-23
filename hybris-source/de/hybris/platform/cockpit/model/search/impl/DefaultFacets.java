package de.hybris.platform.cockpit.model.search.impl;

import de.hybris.platform.cockpit.model.search.ExtendedSearchResult;
import de.hybris.platform.cockpit.model.search.Facet;
import de.hybris.platform.cockpit.model.search.FacetValue;
import de.hybris.platform.cockpit.model.search.FacetsResult;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultFacets implements FacetsResult
{
    private final ExtendedSearchResult searchResult;
    private final Map<Facet, Set<FacetValue>> valuesMap = new HashMap<>();


    public DefaultFacets(ExtendedSearchResult extendedSearchResult)
    {
        this.searchResult = extendedSearchResult;
    }


    public Set<Facet> getFacets()
    {
        return this.valuesMap.keySet();
    }


    public ExtendedSearchResult getSearchResult()
    {
        return this.searchResult;
    }


    public Set<FacetValue> getValues(Facet facet)
    {
        Set<FacetValue> values = this.valuesMap.get(facet);
        return (values != null) ? Collections.<FacetValue>unmodifiableSet(values) : Collections.EMPTY_SET;
    }
}
