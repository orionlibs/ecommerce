package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.solrfacetsearch.provider.QualifierProvider;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class NoOpQualifierProvider implements QualifierProvider
{
    public Set<Class<?>> getSupportedTypes()
    {
        return Collections.emptySet();
    }


    public Collection<Qualifier> getAvailableQualifiers(FacetSearchConfig facetSearchConfig, IndexedType indexedType)
    {
        return Collections.emptyList();
    }


    public boolean canApply(IndexedProperty indexedProperty)
    {
        return false;
    }


    public void applyQualifier(Qualifier qualifier)
    {
    }


    public Qualifier getCurrentQualifier()
    {
        return null;
    }
}
