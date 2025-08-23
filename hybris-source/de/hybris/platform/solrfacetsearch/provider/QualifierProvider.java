package de.hybris.platform.solrfacetsearch.provider;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import java.util.Collection;
import java.util.Set;

public interface QualifierProvider
{
    Set<Class<?>> getSupportedTypes();


    Collection<Qualifier> getAvailableQualifiers(FacetSearchConfig paramFacetSearchConfig, IndexedType paramIndexedType);


    boolean canApply(IndexedProperty paramIndexedProperty);


    void applyQualifier(Qualifier paramQualifier);


    Qualifier getCurrentQualifier();
}
