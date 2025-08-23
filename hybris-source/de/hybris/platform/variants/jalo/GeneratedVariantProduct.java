package de.hybris.platform.variants.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedVariantProduct extends Product
{
    public static final String BASEPRODUCT = "baseProduct";
    protected static final BidirectionalOneToManyHandler<GeneratedVariantProduct> BASEPRODUCTHANDLER = new BidirectionalOneToManyHandler(GeneratedCatalogConstants.TC.VARIANTPRODUCT, false, "baseProduct", "code", false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Product.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("baseProduct", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Product getBaseProduct(SessionContext ctx)
    {
        return (Product)getProperty(ctx, "baseProduct");
    }


    public Product getBaseProduct()
    {
        return getBaseProduct(getSession().getSessionContext());
    }


    public void setBaseProduct(SessionContext ctx, Product value)
    {
        BASEPRODUCTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setBaseProduct(Product value)
    {
        setBaseProduct(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        BASEPRODUCTHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }
}
