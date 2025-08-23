package de.hybris.platform.solrfacetsearch.search;

public interface FacetSearchQueryOperatorTranslator
{
    String translate(String paramString, SearchQuery.QueryOperator paramQueryOperator);
}
