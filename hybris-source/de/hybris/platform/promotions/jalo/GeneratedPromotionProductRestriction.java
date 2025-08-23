package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPromotionProductRestriction extends AbstractPromotionRestriction
{
    public static final String PRODUCTS = "products";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractPromotionRestriction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("products", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<Product> getProducts(SessionContext ctx)
    {
        Collection<Product> coll = (Collection<Product>)getProperty(ctx, "products");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Product> getProducts()
    {
        return getProducts(getSession().getSessionContext());
    }


    public void setProducts(SessionContext ctx, Collection<Product> value)
    {
        setProperty(ctx, "products", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setProducts(Collection<Product> value)
    {
        setProducts(getSession().getSessionContext(), value);
    }
}
