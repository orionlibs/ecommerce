package de.hybris.platform.solrfacetsearch.jalo.redirect;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrURIRedirect extends SolrAbstractKeywordRedirect
{
    public static final String URL = "url";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SolrAbstractKeywordRedirect.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("url", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getUrl(SessionContext ctx)
    {
        return (String)getProperty(ctx, "url");
    }


    public String getUrl()
    {
        return getUrl(getSession().getSessionContext());
    }


    public void setUrl(SessionContext ctx, String value)
    {
        setProperty(ctx, "url", value);
    }


    public void setUrl(String value)
    {
        setUrl(getSession().getSessionContext(), value);
    }
}
