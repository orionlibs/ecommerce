package de.hybris.platform.solrfacetsearch.common.impl;

import de.hybris.platform.solrfacetsearch.common.SolrQueryContextProvider;
import java.util.List;

public class CoreSolrQueryContextProvider implements SolrQueryContextProvider
{
    protected static final List<String> QUERY_CONTEXTS = List.of("DEFAULT");


    public List<String> getQueryContexts()
    {
        return QUERY_CONTEXTS;
    }
}
