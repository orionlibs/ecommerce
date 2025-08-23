package de.hybris.platform.cockpit.model.search;

import java.util.Set;

public interface FacetsResult
{
    ExtendedSearchResult getSearchResult();


    Set<Facet> getFacets();


    Set<FacetValue> getValues(Facet paramFacet);
}
