package de.hybris.platform.catalog.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
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

public abstract class GeneratedKeyword extends GenericItem
{
    public static final String KEYWORD = "keyword";
    public static final String LANGUAGE = "language";
    public static final String CATALOG = "catalog";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String PRODUCTS = "products";
    protected static String PRODUCT2KEYWORDRELATION_SRC_ORDERED = "relation.Product2KeywordRelation.source.ordered";
    protected static String PRODUCT2KEYWORDRELATION_TGT_ORDERED = "relation.Product2KeywordRelation.target.ordered";
    protected static String PRODUCT2KEYWORDRELATION_MARKMODIFIED = "relation.Product2KeywordRelation.markmodified";
    public static final String CATEGORIES = "categories";
    protected static String CATEGORY2KEYWORDRELATION_SRC_ORDERED = "relation.Category2KeywordRelation.source.ordered";
    protected static String CATEGORY2KEYWORDRELATION_TGT_ORDERED = "relation.Category2KeywordRelation.target.ordered";
    protected static String CATEGORY2KEYWORDRELATION_MARKMODIFIED = "relation.Category2KeywordRelation.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("keyword", Item.AttributeMode.INITIAL);
        tmp.put("language", Item.AttributeMode.INITIAL);
        tmp.put("catalog", Item.AttributeMode.INITIAL);
        tmp.put("catalogVersion", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    Catalog getCatalog(SessionContext ctx)
    {
        return (Catalog)getProperty(ctx, "catalog");
    }


    Catalog getCatalog()
    {
        return getCatalog(getSession().getSessionContext());
    }


    void setCatalog(SessionContext ctx, Catalog value)
    {
        setProperty(ctx, "catalog", value);
    }


    void setCatalog(Catalog value)
    {
        setCatalog(getSession().getSessionContext(), value);
    }


    public CatalogVersion getCatalogVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "catalogVersion");
    }


    public CatalogVersion getCatalogVersion()
    {
        return getCatalogVersion(getSession().getSessionContext());
    }


    public void setCatalogVersion(SessionContext ctx, CatalogVersion value)
    {
        setProperty(ctx, "catalogVersion", value);
    }


    public void setCatalogVersion(CatalogVersion value)
    {
        setCatalogVersion(getSession().getSessionContext(), value);
    }


    public Collection<Category> getCategories(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedKeyword.getCategories requires a session language", 0);
        }
        List<Category> items = getLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.CATEGORY2KEYWORDRELATION, "Category", ctx
                                        .getLanguage(),
                        Utilities.getRelationOrderingOverride(CATEGORY2KEYWORDRELATION_SRC_ORDERED, true), false);
        return items;
    }


    public Collection<Category> getCategories()
    {
        return getCategories(getSession().getSessionContext());
    }


    public Map<Language, Collection<Category>> getAllCategories(SessionContext ctx)
    {
        Map<Language, Collection<Category>> values = getAllLinkedItems(false, GeneratedCatalogConstants.Relations.CATEGORY2KEYWORDRELATION);
        return values;
    }


    public Map<Language, Collection<Category>> getAllCategories()
    {
        return getAllCategories(getSession().getSessionContext());
    }


    public long getCategoriesCount(SessionContext ctx, Language lang)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCatalogConstants.Relations.CATEGORY2KEYWORDRELATION, "Category", lang);
    }


    public long getCategoriesCount(Language lang)
    {
        return getCategoriesCount(getSession().getSessionContext(), lang);
    }


    public long getCategoriesCount(SessionContext ctx)
    {
        return getCategoriesCount(ctx, ctx.getLanguage());
    }


    public long getCategoriesCount()
    {
        return getCategoriesCount(getSession().getSessionContext(), getSession().getSessionContext().getLanguage());
    }


    public void setCategories(SessionContext ctx, Collection<Category> value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedKeyword.setCategories requires a session language", 0);
        }
        setLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.CATEGORY2KEYWORDRELATION, ctx
                                        .getLanguage(), value,
                        Utilities.getRelationOrderingOverride(CATEGORY2KEYWORDRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORY2KEYWORDRELATION_MARKMODIFIED));
    }


    public void setCategories(Collection<Category> value)
    {
        setCategories(getSession().getSessionContext(), value);
    }


    public void setAllCategories(SessionContext ctx, Map<Language, Collection<Category>> value)
    {
        setAllLinkedItems(
                        getAllValuesSessionContext(ctx), false, GeneratedCatalogConstants.Relations.CATEGORY2KEYWORDRELATION, value);
    }


    public void setAllCategories(Map<Language, Collection<Category>> value)
    {
        setAllCategories(getSession().getSessionContext(), value);
    }


    public void addToCategories(SessionContext ctx, Language lang, Category value)
    {
        if(lang == null)
        {
            throw new JaloInvalidParameterException("GeneratedKeyword.addToCategories requires a language language", 0);
        }
        addLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.CATEGORY2KEYWORDRELATION, lang,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATEGORY2KEYWORDRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORY2KEYWORDRELATION_MARKMODIFIED));
    }


    public void addToCategories(Language lang, Category value)
    {
        addToCategories(getSession().getSessionContext(), lang, value);
    }


    public void removeFromCategories(SessionContext ctx, Language lang, Category value)
    {
        if(lang == null)
        {
            throw new JaloInvalidParameterException("GeneratedKeyword.removeFromCategories requires a session language", 0);
        }
        removeLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.CATEGORY2KEYWORDRELATION, lang,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATEGORY2KEYWORDRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORY2KEYWORDRELATION_MARKMODIFIED));
    }


    public void removeFromCategories(Language lang, Category value)
    {
        removeFromCategories(getSession().getSessionContext(), lang, value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Product");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(PRODUCT2KEYWORDRELATION_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("Category");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CATEGORY2KEYWORDRELATION_MARKMODIFIED);
        }
        return true;
    }


    public String getKeyword(SessionContext ctx)
    {
        return (String)getProperty(ctx, "keyword");
    }


    public String getKeyword()
    {
        return getKeyword(getSession().getSessionContext());
    }


    public void setKeyword(SessionContext ctx, String value)
    {
        setProperty(ctx, "keyword", value);
    }


    public void setKeyword(String value)
    {
        setKeyword(getSession().getSessionContext(), value);
    }


    public Language getLanguage(SessionContext ctx)
    {
        return (Language)getProperty(ctx, "language");
    }


    public Language getLanguage()
    {
        return getLanguage(getSession().getSessionContext());
    }


    protected void setLanguage(SessionContext ctx, Language value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'language' is not changeable", 0);
        }
        setProperty(ctx, "language", value);
    }


    protected void setLanguage(Language value)
    {
        setLanguage(getSession().getSessionContext(), value);
    }


    public Collection<Product> getProducts(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedKeyword.getProducts requires a session language", 0);
        }
        List<Product> items = getLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.PRODUCT2KEYWORDRELATION, "Product", ctx
                                        .getLanguage(), false,
                        Utilities.getRelationOrderingOverride(PRODUCT2KEYWORDRELATION_TGT_ORDERED, true));
        return items;
    }


    public Collection<Product> getProducts()
    {
        return getProducts(getSession().getSessionContext());
    }


    public Map<Language, Collection<Product>> getAllProducts(SessionContext ctx)
    {
        Map<Language, Collection<Product>> values = getAllLinkedItems(true, GeneratedCatalogConstants.Relations.PRODUCT2KEYWORDRELATION);
        return values;
    }


    public Map<Language, Collection<Product>> getAllProducts()
    {
        return getAllProducts(getSession().getSessionContext());
    }


    public long getProductsCount(SessionContext ctx, Language lang)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCatalogConstants.Relations.PRODUCT2KEYWORDRELATION, "Product", lang);
    }


    public long getProductsCount(Language lang)
    {
        return getProductsCount(getSession().getSessionContext(), lang);
    }


    public long getProductsCount(SessionContext ctx)
    {
        return getProductsCount(ctx, ctx.getLanguage());
    }


    public long getProductsCount()
    {
        return getProductsCount(getSession().getSessionContext(), getSession().getSessionContext().getLanguage());
    }


    public void setProducts(SessionContext ctx, Collection<Product> value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedKeyword.setProducts requires a session language", 0);
        }
        setLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.PRODUCT2KEYWORDRELATION, ctx
                                        .getLanguage(), value, false,
                        Utilities.getRelationOrderingOverride(PRODUCT2KEYWORDRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(PRODUCT2KEYWORDRELATION_MARKMODIFIED));
    }


    public void setProducts(Collection<Product> value)
    {
        setProducts(getSession().getSessionContext(), value);
    }


    public void setAllProducts(SessionContext ctx, Map<Language, Collection<Product>> value)
    {
        setAllLinkedItems(
                        getAllValuesSessionContext(ctx), true, GeneratedCatalogConstants.Relations.PRODUCT2KEYWORDRELATION, value);
    }


    public void setAllProducts(Map<Language, Collection<Product>> value)
    {
        setAllProducts(getSession().getSessionContext(), value);
    }


    public void addToProducts(SessionContext ctx, Language lang, Product value)
    {
        if(lang == null)
        {
            throw new JaloInvalidParameterException("GeneratedKeyword.addToProducts requires a language language", 0);
        }
        addLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.PRODUCT2KEYWORDRELATION, lang,
                        Collections.singletonList(value), false,
                        Utilities.getRelationOrderingOverride(PRODUCT2KEYWORDRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(PRODUCT2KEYWORDRELATION_MARKMODIFIED));
    }


    public void addToProducts(Language lang, Product value)
    {
        addToProducts(getSession().getSessionContext(), lang, value);
    }


    public void removeFromProducts(SessionContext ctx, Language lang, Product value)
    {
        if(lang == null)
        {
            throw new JaloInvalidParameterException("GeneratedKeyword.removeFromProducts requires a session language", 0);
        }
        removeLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.PRODUCT2KEYWORDRELATION, lang,
                        Collections.singletonList(value), false,
                        Utilities.getRelationOrderingOverride(PRODUCT2KEYWORDRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(PRODUCT2KEYWORDRELATION_MARKMODIFIED));
    }


    public void removeFromProducts(Language lang, Product value)
    {
        removeFromProducts(getSession().getSessionContext(), lang, value);
    }
}
