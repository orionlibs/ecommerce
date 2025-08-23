package de.hybris.platform.promotions.jalo;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.promotions.constants.GeneratedPromotionsConstants;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedProductPromotion extends AbstractPromotion
{
    public static final String PRODUCTBANNER = "productBanner";
    public static final String PRODUCTS = "products";
    protected static String PRODUCTPROMOTIONRELATION_SRC_ORDERED = "relation.ProductPromotionRelation.source.ordered";
    protected static String PRODUCTPROMOTIONRELATION_TGT_ORDERED = "relation.ProductPromotionRelation.target.ordered";
    protected static String PRODUCTPROMOTIONRELATION_MARKMODIFIED = "relation.ProductPromotionRelation.markmodified";
    public static final String CATEGORIES = "categories";
    protected static String CATEGORYPROMOTIONRELATION_SRC_ORDERED = "relation.CategoryPromotionRelation.source.ordered";
    protected static String CATEGORYPROMOTIONRELATION_TGT_ORDERED = "relation.CategoryPromotionRelation.target.ordered";
    protected static String CATEGORYPROMOTIONRELATION_MARKMODIFIED = "relation.CategoryPromotionRelation.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractPromotion.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("productBanner", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<Category> getCategories(SessionContext ctx)
    {
        List<Category> items = getLinkedItems(ctx, false, GeneratedPromotionsConstants.Relations.CATEGORYPROMOTIONRELATION, "Category", null, false, false);
        return items;
    }


    public Collection<Category> getCategories()
    {
        return getCategories(getSession().getSessionContext());
    }


    public long getCategoriesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedPromotionsConstants.Relations.CATEGORYPROMOTIONRELATION, "Category", null);
    }


    public long getCategoriesCount()
    {
        return getCategoriesCount(getSession().getSessionContext());
    }


    public void setCategories(SessionContext ctx, Collection<Category> value)
    {
        setLinkedItems(ctx, false, GeneratedPromotionsConstants.Relations.CATEGORYPROMOTIONRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(CATEGORYPROMOTIONRELATION_MARKMODIFIED));
    }


    public void setCategories(Collection<Category> value)
    {
        setCategories(getSession().getSessionContext(), value);
    }


    public void addToCategories(SessionContext ctx, Category value)
    {
        addLinkedItems(ctx, false, GeneratedPromotionsConstants.Relations.CATEGORYPROMOTIONRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CATEGORYPROMOTIONRELATION_MARKMODIFIED));
    }


    public void addToCategories(Category value)
    {
        addToCategories(getSession().getSessionContext(), value);
    }


    public void removeFromCategories(SessionContext ctx, Category value)
    {
        removeLinkedItems(ctx, false, GeneratedPromotionsConstants.Relations.CATEGORYPROMOTIONRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CATEGORYPROMOTIONRELATION_MARKMODIFIED));
    }


    public void removeFromCategories(Category value)
    {
        removeFromCategories(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Product");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(PRODUCTPROMOTIONRELATION_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("Category");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CATEGORYPROMOTIONRELATION_MARKMODIFIED);
        }
        return true;
    }


    public Media getProductBanner(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "productBanner");
    }


    public Media getProductBanner()
    {
        return getProductBanner(getSession().getSessionContext());
    }


    public void setProductBanner(SessionContext ctx, Media value)
    {
        setProperty(ctx, "productBanner", value);
    }


    public void setProductBanner(Media value)
    {
        setProductBanner(getSession().getSessionContext(), value);
    }


    public Collection<Product> getProducts(SessionContext ctx)
    {
        List<Product> items = getLinkedItems(ctx, false, GeneratedPromotionsConstants.Relations.PRODUCTPROMOTIONRELATION, "Product", null, false, false);
        return items;
    }


    public Collection<Product> getProducts()
    {
        return getProducts(getSession().getSessionContext());
    }


    public long getProductsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedPromotionsConstants.Relations.PRODUCTPROMOTIONRELATION, "Product", null);
    }


    public long getProductsCount()
    {
        return getProductsCount(getSession().getSessionContext());
    }


    public void setProducts(SessionContext ctx, Collection<Product> value)
    {
        setLinkedItems(ctx, false, GeneratedPromotionsConstants.Relations.PRODUCTPROMOTIONRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(PRODUCTPROMOTIONRELATION_MARKMODIFIED));
    }


    public void setProducts(Collection<Product> value)
    {
        setProducts(getSession().getSessionContext(), value);
    }


    public void addToProducts(SessionContext ctx, Product value)
    {
        addLinkedItems(ctx, false, GeneratedPromotionsConstants.Relations.PRODUCTPROMOTIONRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PRODUCTPROMOTIONRELATION_MARKMODIFIED));
    }


    public void addToProducts(Product value)
    {
        addToProducts(getSession().getSessionContext(), value);
    }


    public void removeFromProducts(SessionContext ctx, Product value)
    {
        removeLinkedItems(ctx, false, GeneratedPromotionsConstants.Relations.PRODUCTPROMOTIONRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PRODUCTPROMOTIONRELATION_MARKMODIFIED));
    }


    public void removeFromProducts(Product value)
    {
        removeFromProducts(getSession().getSessionContext(), value);
    }
}
