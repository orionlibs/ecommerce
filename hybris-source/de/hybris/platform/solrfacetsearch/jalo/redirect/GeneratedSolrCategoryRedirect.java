package de.hybris.platform.solrfacetsearch.jalo.redirect;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrCategoryRedirect extends SolrAbstractKeywordRedirect
{
    public static final String REDIRECTITEM = "redirectItem";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SolrAbstractKeywordRedirect.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("redirectItem", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Category getRedirectItem(SessionContext ctx)
    {
        return (Category)getProperty(ctx, "redirectItem");
    }


    public Category getRedirectItem()
    {
        return getRedirectItem(getSession().getSessionContext());
    }


    public void setRedirectItem(SessionContext ctx, Category value)
    {
        setProperty(ctx, "redirectItem", value);
    }


    public void setRedirectItem(Category value)
    {
        setRedirectItem(getSession().getSessionContext(), value);
    }
}
