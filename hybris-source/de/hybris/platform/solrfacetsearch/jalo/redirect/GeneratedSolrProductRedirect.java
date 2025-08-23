package de.hybris.platform.solrfacetsearch.jalo.redirect;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedSolrProductRedirect extends SolrAbstractKeywordRedirect
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


    public Product getRedirectItem(SessionContext ctx)
    {
        return (Product)getProperty(ctx, "redirectItem");
    }


    public Product getRedirectItem()
    {
        return getRedirectItem(getSession().getSessionContext());
    }


    public void setRedirectItem(SessionContext ctx, Product value)
    {
        setProperty(ctx, "redirectItem", value);
    }


    public void setRedirectItem(Product value)
    {
        setRedirectItem(getSession().getSessionContext(), value);
    }
}
