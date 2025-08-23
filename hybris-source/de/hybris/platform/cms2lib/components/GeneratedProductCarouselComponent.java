package de.hybris.platform.cms2lib.components;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
import de.hybris.platform.cms2lib.constants.GeneratedCms2LibConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedProductCarouselComponent extends SimpleCMSComponent
{
    public static final String SCROLL = "scroll";
    public static final String PRODUCTCODES = "productCodes";
    public static final String CATEGORYCODES = "categoryCodes";
    public static final String PRODUCTS = "products";
    protected static String PRODUCTSFORPRODUCTCAROUSELCOMPONENT_SRC_ORDERED = "relation.ProductsForProductCarouselComponent.source.ordered";
    protected static String PRODUCTSFORPRODUCTCAROUSELCOMPONENT_TGT_ORDERED = "relation.ProductsForProductCarouselComponent.target.ordered";
    protected static String PRODUCTSFORPRODUCTCAROUSELCOMPONENT_MARKMODIFIED = "relation.ProductsForProductCarouselComponent.markmodified";
    public static final String CATEGORIES = "categories";
    protected static String CATEGORIESFORPRODUCTCAROUSELCOMPONENT_SRC_ORDERED = "relation.CategoriesForProductCarouselComponent.source.ordered";
    protected static String CATEGORIESFORPRODUCTCAROUSELCOMPONENT_TGT_ORDERED = "relation.CategoriesForProductCarouselComponent.target.ordered";
    protected static String CATEGORIESFORPRODUCTCAROUSELCOMPONENT_MARKMODIFIED = "relation.CategoriesForProductCarouselComponent.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SimpleCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("scroll", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<Category> getCategories(SessionContext ctx)
    {
        List<Category> items = getLinkedItems(ctx, true, GeneratedCms2LibConstants.Relations.CATEGORIESFORPRODUCTCAROUSELCOMPONENT, "Category", null,
                        Utilities.getRelationOrderingOverride(CATEGORIESFORPRODUCTCAROUSELCOMPONENT_SRC_ORDERED, true), false);
        return items;
    }


    public List<Category> getCategories()
    {
        return getCategories(getSession().getSessionContext());
    }


    public long getCategoriesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2LibConstants.Relations.CATEGORIESFORPRODUCTCAROUSELCOMPONENT, "Category", null);
    }


    public long getCategoriesCount()
    {
        return getCategoriesCount(getSession().getSessionContext());
    }


    public void setCategories(SessionContext ctx, List<Category> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2LibConstants.Relations.CATEGORIESFORPRODUCTCAROUSELCOMPONENT, null, value,
                        Utilities.getRelationOrderingOverride(CATEGORIESFORPRODUCTCAROUSELCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORIESFORPRODUCTCAROUSELCOMPONENT_MARKMODIFIED));
    }


    public void setCategories(List<Category> value)
    {
        setCategories(getSession().getSessionContext(), value);
    }


    public void addToCategories(SessionContext ctx, Category value)
    {
        addLinkedItems(ctx, true, GeneratedCms2LibConstants.Relations.CATEGORIESFORPRODUCTCAROUSELCOMPONENT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATEGORIESFORPRODUCTCAROUSELCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORIESFORPRODUCTCAROUSELCOMPONENT_MARKMODIFIED));
    }


    public void addToCategories(Category value)
    {
        addToCategories(getSession().getSessionContext(), value);
    }


    public void removeFromCategories(SessionContext ctx, Category value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2LibConstants.Relations.CATEGORIESFORPRODUCTCAROUSELCOMPONENT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATEGORIESFORPRODUCTCAROUSELCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORIESFORPRODUCTCAROUSELCOMPONENT_MARKMODIFIED));
    }


    public void removeFromCategories(Category value)
    {
        removeFromCategories(getSession().getSessionContext(), value);
    }


    public List<String> getCategoryCodes()
    {
        return getCategoryCodes(getSession().getSessionContext());
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Product");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(PRODUCTSFORPRODUCTCAROUSELCOMPONENT_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("Category");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CATEGORIESFORPRODUCTCAROUSELCOMPONENT_MARKMODIFIED);
        }
        return true;
    }


    public List<String> getProductCodes()
    {
        return getProductCodes(getSession().getSessionContext());
    }


    public List<Product> getProducts(SessionContext ctx)
    {
        List<Product> items = getLinkedItems(ctx, true, GeneratedCms2LibConstants.Relations.PRODUCTSFORPRODUCTCAROUSELCOMPONENT, "Product", null,
                        Utilities.getRelationOrderingOverride(PRODUCTSFORPRODUCTCAROUSELCOMPONENT_SRC_ORDERED, true), false);
        return items;
    }


    public List<Product> getProducts()
    {
        return getProducts(getSession().getSessionContext());
    }


    public long getProductsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2LibConstants.Relations.PRODUCTSFORPRODUCTCAROUSELCOMPONENT, "Product", null);
    }


    public long getProductsCount()
    {
        return getProductsCount(getSession().getSessionContext());
    }


    public void setProducts(SessionContext ctx, List<Product> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2LibConstants.Relations.PRODUCTSFORPRODUCTCAROUSELCOMPONENT, null, value,
                        Utilities.getRelationOrderingOverride(PRODUCTSFORPRODUCTCAROUSELCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(PRODUCTSFORPRODUCTCAROUSELCOMPONENT_MARKMODIFIED));
    }


    public void setProducts(List<Product> value)
    {
        setProducts(getSession().getSessionContext(), value);
    }


    public void addToProducts(SessionContext ctx, Product value)
    {
        addLinkedItems(ctx, true, GeneratedCms2LibConstants.Relations.PRODUCTSFORPRODUCTCAROUSELCOMPONENT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(PRODUCTSFORPRODUCTCAROUSELCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(PRODUCTSFORPRODUCTCAROUSELCOMPONENT_MARKMODIFIED));
    }


    public void addToProducts(Product value)
    {
        addToProducts(getSession().getSessionContext(), value);
    }


    public void removeFromProducts(SessionContext ctx, Product value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2LibConstants.Relations.PRODUCTSFORPRODUCTCAROUSELCOMPONENT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(PRODUCTSFORPRODUCTCAROUSELCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(PRODUCTSFORPRODUCTCAROUSELCOMPONENT_MARKMODIFIED));
    }


    public void removeFromProducts(Product value)
    {
        removeFromProducts(getSession().getSessionContext(), value);
    }


    public EnumerationValue getScroll(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "scroll");
    }


    public EnumerationValue getScroll()
    {
        return getScroll(getSession().getSessionContext());
    }


    public void setScroll(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "scroll", value);
    }


    public void setScroll(EnumerationValue value)
    {
        setScroll(getSession().getSessionContext(), value);
    }


    public abstract List<String> getCategoryCodes(SessionContext paramSessionContext);


    public abstract List<String> getProductCodes(SessionContext paramSessionContext);
}
