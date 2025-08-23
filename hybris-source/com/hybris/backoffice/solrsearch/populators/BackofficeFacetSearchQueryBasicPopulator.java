package com.hybris.backoffice.solrsearch.populators;

import com.hybris.backoffice.solrsearch.dataaccess.BackofficeQueryField;
import de.hybris.platform.solrfacetsearch.search.QueryField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.populators.FacetSearchQueryBasicPopulator;

public class BackofficeFacetSearchQueryBasicPopulator extends FacetSearchQueryBasicPopulator
{
    private FieldNamePostProcessor fieldNamePostProcessor;


    protected String convertQueryField(SearchQuery searchQuery, QueryField queryField)
    {
        if(searchQuery instanceof com.hybris.backoffice.solrsearch.dataaccess.BackofficeSearchQuery && queryField instanceof BackofficeQueryField)
        {
            BackofficeQueryField backofficeQueryField = (BackofficeQueryField)queryField;
            String convertedQuery = super.convertQueryField(searchQuery, queryField);
            return getFieldNamePostProcessor().process(searchQuery, backofficeQueryField.getLocale(), convertedQuery);
        }
        return "";
    }


    public FieldNamePostProcessor getFieldNamePostProcessor()
    {
        return this.fieldNamePostProcessor;
    }


    public void setFieldNamePostProcessor(FieldNamePostProcessor fieldNamePostProcessor)
    {
        this.fieldNamePostProcessor = fieldNamePostProcessor;
    }
}
