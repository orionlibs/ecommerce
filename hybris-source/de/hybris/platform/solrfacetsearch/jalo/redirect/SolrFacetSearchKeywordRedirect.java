package de.hybris.platform.solrfacetsearch.jalo.redirect;

import de.hybris.platform.jalo.SessionContext;

public class SolrFacetSearchKeywordRedirect extends GeneratedSolrFacetSearchKeywordRedirect
{
    public void setKeyword(SessionContext ctx, String value)
    {
        super.setKeyword(ctx, value.trim());
    }


    public void setKeyword(String value)
    {
        super.setKeyword(value.trim());
    }
}
