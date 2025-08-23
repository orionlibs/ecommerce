package de.hybris.platform.solrfacetsearch.search;

import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.solrfacetsearch.enums.KeywordRedirectMatchType;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrAbstractKeywordRedirectModel;
import java.io.Serializable;

public class KeywordRedirectValue implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final String keyword;
    private final KeywordRedirectMatchType matchType;
    private final SolrAbstractKeywordRedirectModel redirect;


    public KeywordRedirectValue(String keyword, KeywordRedirectMatchType matchType, SolrAbstractKeywordRedirectModel redirect)
    {
        ServicesUtil.validateParameterNotNull(keyword, "Keyword required");
        ServicesUtil.validateParameterNotNull(matchType, "Match type required");
        ServicesUtil.validateParameterNotNull(redirect, "Redirect required");
        this.keyword = keyword;
        this.matchType = matchType;
        this.redirect = redirect;
    }


    public String getKeyword()
    {
        return this.keyword;
    }


    public KeywordRedirectMatchType getMatchType()
    {
        return this.matchType;
    }


    public SolrAbstractKeywordRedirectModel getRedirect()
    {
        return this.redirect;
    }


    public int hashCode()
    {
        int prime = 31;
        int result = 1;
        result = 31 * result + this.keyword.hashCode();
        result = 31 * result + this.matchType.hashCode();
        result = 31 * result + this.redirect.hashCode();
        return result;
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(getClass() != obj.getClass())
        {
            return false;
        }
        KeywordRedirectValue other = (KeywordRedirectValue)obj;
        if(!this.keyword.equals(other.keyword))
        {
            return false;
        }
        if(this.matchType != other.matchType)
        {
            return false;
        }
        if(!this.redirect.equals(other.redirect))
        {
            return false;
        }
        return true;
    }
}
