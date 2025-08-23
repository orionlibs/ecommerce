package de.hybris.platform.solrfacetsearch.config;

import java.util.Map;

public interface FlexibleSearchQuerySpec
{
    String getQuery();


    Map<String, Object> createParameters();


    String getUser();
}
