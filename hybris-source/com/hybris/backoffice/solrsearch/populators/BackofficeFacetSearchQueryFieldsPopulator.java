package com.hybris.backoffice.solrsearch.populators;

import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;
import de.hybris.platform.solrfacetsearch.search.impl.populators.FacetSearchQueryFieldsPopulator;
import org.apache.solr.client.solrj.SolrQuery;

public class BackofficeFacetSearchQueryFieldsPopulator extends FacetSearchQueryFieldsPopulator
{
    public void populate(SearchQueryConverterData source, SolrQuery target) throws ConversionException
    {
        super.populate(source, target);
        if(source.getSearchQuery().getFacetSearchConfig().getSearchConfig().isRestrictFieldsInResponse())
        {
            target.addField("pk");
        }
    }
}
