package de.hybris.platform.cms2.jalo.restrictions;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedProductRestriction extends AbstractRestriction
{
    public static final String PRODUCTCODES = "productCodes";
    public static final String PRODUCTS = "products";
    protected static String PRODUCTSFORRESTRICTION_SRC_ORDERED = "relation.ProductsForRestriction.source.ordered";
    protected static String PRODUCTSFORRESTRICTION_TGT_ORDERED = "relation.ProductsForRestriction.target.ordered";
    protected static String PRODUCTSFORRESTRICTION_MARKMODIFIED = "relation.ProductsForRestriction.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractRestriction.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Product");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(PRODUCTSFORRESTRICTION_MARKMODIFIED);
        }
        return true;
    }


    public List<String> getProductCodes()
    {
        return getProductCodes(getSession().getSessionContext());
    }


    public Collection<Product> getProducts(SessionContext ctx)
    {
        List<Product> items = getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.PRODUCTSFORRESTRICTION, "Product", null, false, false);
        return items;
    }


    public Collection<Product> getProducts()
    {
        return getProducts(getSession().getSessionContext());
    }


    public long getProductsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.PRODUCTSFORRESTRICTION, "Product", null);
    }


    public long getProductsCount()
    {
        return getProductsCount(getSession().getSessionContext());
    }


    public void setProducts(SessionContext ctx, Collection<Product> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.PRODUCTSFORRESTRICTION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(PRODUCTSFORRESTRICTION_MARKMODIFIED));
    }


    public void setProducts(Collection<Product> value)
    {
        setProducts(getSession().getSessionContext(), value);
    }


    public void addToProducts(SessionContext ctx, Product value)
    {
        addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.PRODUCTSFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PRODUCTSFORRESTRICTION_MARKMODIFIED));
    }


    public void addToProducts(Product value)
    {
        addToProducts(getSession().getSessionContext(), value);
    }


    public void removeFromProducts(SessionContext ctx, Product value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.PRODUCTSFORRESTRICTION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PRODUCTSFORRESTRICTION_MARKMODIFIED));
    }


    public void removeFromProducts(Product value)
    {
        removeFromProducts(getSession().getSessionContext(), value);
    }


    public abstract List<String> getProductCodes(SessionContext paramSessionContext);
}
