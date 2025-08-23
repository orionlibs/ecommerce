package de.hybris.platform.solrfacetsearch.search;

import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;

public interface FieldNameTranslator
{
    String translate(SearchQuery paramSearchQuery, String paramString, FieldNameProvider.FieldType paramFieldType);


    String translate(FacetSearchContext paramFacetSearchContext, String paramString, FieldNameProvider.FieldType paramFieldType);


    String translate(FacetSearchContext paramFacetSearchContext, String paramString);


    FieldInfosMapping getFieldInfos(FacetSearchContext paramFacetSearchContext);
}
