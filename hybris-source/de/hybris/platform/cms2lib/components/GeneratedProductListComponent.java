package de.hybris.platform.cms2lib.components;

import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
import de.hybris.platform.cms2lib.constants.GeneratedCms2LibConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedProductListComponent extends SimpleCMSComponent
{
    public static final String HEADLINE = "headline";
    public static final String CATEGORYCODE = "categoryCode";
    public static final String PRODUCTSFROMCONTEXT = "productsFromContext";
    public static final String SEARCHQUERY = "searchQuery";
    public static final String PAGINATION = "pagination";
    public static final String LAYOUT = "layout";
    public static final String PRODUCTCODES = "productCodes";
    public static final String PRODUCTS = "products";
    protected static String PRODUCTSFORPRODUCTLISTCOMPONENT_SRC_ORDERED = "relation.ProductsForProductListComponent.source.ordered";
    protected static String PRODUCTSFORPRODUCTLISTCOMPONENT_TGT_ORDERED = "relation.ProductsForProductListComponent.target.ordered";
    protected static String PRODUCTSFORPRODUCTLISTCOMPONENT_MARKMODIFIED = "relation.ProductsForProductListComponent.markmodified";
    public static final String CATEGORY = "category";
    protected static final BidirectionalOneToManyHandler<GeneratedProductListComponent> CATEGORYHANDLER = new BidirectionalOneToManyHandler(GeneratedCms2LibConstants.TC.PRODUCTLISTCOMPONENT, false, "category", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SimpleCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("headline", Item.AttributeMode.INITIAL);
        tmp.put("productsFromContext", Item.AttributeMode.INITIAL);
        tmp.put("searchQuery", Item.AttributeMode.INITIAL);
        tmp.put("pagination", Item.AttributeMode.INITIAL);
        tmp.put("layout", Item.AttributeMode.INITIAL);
        tmp.put("category", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Category getCategory(SessionContext ctx)
    {
        return (Category)getProperty(ctx, "category");
    }


    public Category getCategory()
    {
        return getCategory(getSession().getSessionContext());
    }


    public void setCategory(SessionContext ctx, Category value)
    {
        CATEGORYHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setCategory(Category value)
    {
        setCategory(getSession().getSessionContext(), value);
    }


    public String getCategoryCode()
    {
        return getCategoryCode(getSession().getSessionContext());
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        CATEGORYHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getHeadline(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedProductListComponent.getHeadline requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "headline");
    }


    public String getHeadline()
    {
        return getHeadline(getSession().getSessionContext());
    }


    public Map<Language, String> getAllHeadline(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "headline", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllHeadline()
    {
        return getAllHeadline(getSession().getSessionContext());
    }


    public void setHeadline(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedProductListComponent.setHeadline requires a session language", 0);
        }
        setLocalizedProperty(ctx, "headline", value);
    }


    public void setHeadline(String value)
    {
        setHeadline(getSession().getSessionContext(), value);
    }


    public void setAllHeadline(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "headline", value);
    }


    public void setAllHeadline(Map<Language, String> value)
    {
        setAllHeadline(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Product");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(PRODUCTSFORPRODUCTLISTCOMPONENT_MARKMODIFIED);
        }
        return true;
    }


    public EnumerationValue getLayout(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "layout");
    }


    public EnumerationValue getLayout()
    {
        return getLayout(getSession().getSessionContext());
    }


    public void setLayout(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "layout", value);
    }


    public void setLayout(EnumerationValue value)
    {
        setLayout(getSession().getSessionContext(), value);
    }


    public Boolean isPagination(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "pagination");
    }


    public Boolean isPagination()
    {
        return isPagination(getSession().getSessionContext());
    }


    public boolean isPaginationAsPrimitive(SessionContext ctx)
    {
        Boolean value = isPagination(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isPaginationAsPrimitive()
    {
        return isPaginationAsPrimitive(getSession().getSessionContext());
    }


    public void setPagination(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "pagination", value);
    }


    public void setPagination(Boolean value)
    {
        setPagination(getSession().getSessionContext(), value);
    }


    public void setPagination(SessionContext ctx, boolean value)
    {
        setPagination(ctx, Boolean.valueOf(value));
    }


    public void setPagination(boolean value)
    {
        setPagination(getSession().getSessionContext(), value);
    }


    public List<String> getProductCodes()
    {
        return getProductCodes(getSession().getSessionContext());
    }


    public List<Product> getProducts(SessionContext ctx)
    {
        List<Product> items = getLinkedItems(ctx, true, GeneratedCms2LibConstants.Relations.PRODUCTSFORPRODUCTLISTCOMPONENT, "Product", null,
                        Utilities.getRelationOrderingOverride(PRODUCTSFORPRODUCTLISTCOMPONENT_SRC_ORDERED, true), false);
        return items;
    }


    public List<Product> getProducts()
    {
        return getProducts(getSession().getSessionContext());
    }


    public long getProductsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2LibConstants.Relations.PRODUCTSFORPRODUCTLISTCOMPONENT, "Product", null);
    }


    public long getProductsCount()
    {
        return getProductsCount(getSession().getSessionContext());
    }


    public void setProducts(SessionContext ctx, List<Product> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2LibConstants.Relations.PRODUCTSFORPRODUCTLISTCOMPONENT, null, value,
                        Utilities.getRelationOrderingOverride(PRODUCTSFORPRODUCTLISTCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(PRODUCTSFORPRODUCTLISTCOMPONENT_MARKMODIFIED));
    }


    public void setProducts(List<Product> value)
    {
        setProducts(getSession().getSessionContext(), value);
    }


    public void addToProducts(SessionContext ctx, Product value)
    {
        addLinkedItems(ctx, true, GeneratedCms2LibConstants.Relations.PRODUCTSFORPRODUCTLISTCOMPONENT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(PRODUCTSFORPRODUCTLISTCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(PRODUCTSFORPRODUCTLISTCOMPONENT_MARKMODIFIED));
    }


    public void addToProducts(Product value)
    {
        addToProducts(getSession().getSessionContext(), value);
    }


    public void removeFromProducts(SessionContext ctx, Product value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2LibConstants.Relations.PRODUCTSFORPRODUCTLISTCOMPONENT, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(PRODUCTSFORPRODUCTLISTCOMPONENT_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(PRODUCTSFORPRODUCTLISTCOMPONENT_MARKMODIFIED));
    }


    public void removeFromProducts(Product value)
    {
        removeFromProducts(getSession().getSessionContext(), value);
    }


    public Boolean isProductsFromContext(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "productsFromContext");
    }


    public Boolean isProductsFromContext()
    {
        return isProductsFromContext(getSession().getSessionContext());
    }


    public boolean isProductsFromContextAsPrimitive(SessionContext ctx)
    {
        Boolean value = isProductsFromContext(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isProductsFromContextAsPrimitive()
    {
        return isProductsFromContextAsPrimitive(getSession().getSessionContext());
    }


    public void setProductsFromContext(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "productsFromContext", value);
    }


    public void setProductsFromContext(Boolean value)
    {
        setProductsFromContext(getSession().getSessionContext(), value);
    }


    public void setProductsFromContext(SessionContext ctx, boolean value)
    {
        setProductsFromContext(ctx, Boolean.valueOf(value));
    }


    public void setProductsFromContext(boolean value)
    {
        setProductsFromContext(getSession().getSessionContext(), value);
    }


    public String getSearchQuery(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedProductListComponent.getSearchQuery requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "searchQuery");
    }


    public String getSearchQuery()
    {
        return getSearchQuery(getSession().getSessionContext());
    }


    public Map<Language, String> getAllSearchQuery(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "searchQuery", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllSearchQuery()
    {
        return getAllSearchQuery(getSession().getSessionContext());
    }


    public void setSearchQuery(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedProductListComponent.setSearchQuery requires a session language", 0);
        }
        setLocalizedProperty(ctx, "searchQuery", value);
    }


    public void setSearchQuery(String value)
    {
        setSearchQuery(getSession().getSessionContext(), value);
    }


    public void setAllSearchQuery(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "searchQuery", value);
    }


    public void setAllSearchQuery(Map<Language, String> value)
    {
        setAllSearchQuery(getSession().getSessionContext(), value);
    }


    public abstract String getCategoryCode(SessionContext paramSessionContext);


    public abstract List<String> getProductCodes(SessionContext paramSessionContext);
}
