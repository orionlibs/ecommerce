package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPromotionOrderAddFreeGiftAction extends AbstractPromotionAction
{
    public static final String FREEPRODUCT = "freeProduct";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractPromotionAction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("freeProduct", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Product getFreeProduct(SessionContext ctx)
    {
        return (Product)getProperty(ctx, "freeProduct");
    }


    public Product getFreeProduct()
    {
        return getFreeProduct(getSession().getSessionContext());
    }


    public void setFreeProduct(SessionContext ctx, Product value)
    {
        setProperty(ctx, "freeProduct", value);
    }


    public void setFreeProduct(Product value)
    {
        setFreeProduct(getSession().getSessionContext(), value);
    }
}
