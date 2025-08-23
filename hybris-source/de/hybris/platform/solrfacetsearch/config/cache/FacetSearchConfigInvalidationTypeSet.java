package de.hybris.platform.solrfacetsearch.config.cache;

import java.util.Set;

public class FacetSearchConfigInvalidationTypeSet
{
    final Set<String> invalidationTypes;


    public FacetSearchConfigInvalidationTypeSet(Set<String> invalidationTypes)
    {
        this.invalidationTypes = invalidationTypes;
    }


    public Set<String> getInvalidationTypes()
    {
        return this.invalidationTypes;
    }
}
