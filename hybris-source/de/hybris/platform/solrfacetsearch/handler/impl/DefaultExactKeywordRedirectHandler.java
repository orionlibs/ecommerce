package de.hybris.platform.solrfacetsearch.handler.impl;

import de.hybris.platform.solrfacetsearch.handler.KeywordRedirectHandler;

public class DefaultExactKeywordRedirectHandler implements KeywordRedirectHandler
{
    public boolean keywordMatches(String theQuery, String keyword, boolean ignoreCase)
    {
        if(ignoreCase)
        {
            return keyword.equalsIgnoreCase(theQuery);
        }
        return keyword.equals(theQuery);
    }
}
