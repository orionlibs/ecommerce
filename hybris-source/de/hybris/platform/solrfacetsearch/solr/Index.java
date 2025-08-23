package de.hybris.platform.solrfacetsearch.solr;

import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import java.io.Serializable;

public interface Index extends Serializable
{
    String getName();


    FacetSearchConfig getFacetSearchConfig();


    IndexedType getIndexedType();


    String getQualifier();
}
