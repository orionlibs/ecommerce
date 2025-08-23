package de.hybris.platform.adaptivesearchsolr.strategies;

import de.hybris.platform.adaptivesearch.enums.AsBoostOperator;
import de.hybris.platform.adaptivesearch.enums.AsBoostType;
import de.hybris.platform.adaptivesearch.enums.AsFacetType;
import de.hybris.platform.solrfacetsearch.config.FacetType;
import de.hybris.platform.solrfacetsearch.search.BoostField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

public interface SolrAsTypeMappingRegistry
{
    FacetType toFacetType(AsFacetType paramAsFacetType);


    AsFacetType toAsFacetType(FacetType paramFacetType);


    SearchQuery.QueryOperator toQueryOperator(AsBoostOperator paramAsBoostOperator);


    AsBoostOperator toAsBoostOperator(SearchQuery.QueryOperator paramQueryOperator);


    BoostField.BoostType toBoostType(AsBoostType paramAsBoostType);


    AsBoostType toAsBoostType(BoostField.BoostType paramBoostType);
}
