package de.hybris.platform.category.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.flexiblesearch.SavedQuery;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.JspContext;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CategoryManager
{
    public static CategoryManager getInstance()
    {
        return CatalogManager.getInstance().getCategoryManager();
    }


    public Category createCategory(String code)
    {
        try
        {
            ComposedType categoryType = getSession().getTypeManager().getComposedType(Category.class);
            Map<Object, Object> parameters = new HashMap<>();
            parameters.put("code", code);
            return (Category)categoryType.newInstance(parameters);
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating category.", 0);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Category createCategory(String code, boolean root)
    {
        return createCategory(code);
    }


    public Collection<Category> getAllRootCategories()
    {
        return getSession()
                        .getFlexibleSearch()
                        .search("SELECT {cat:" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.CATEGORY + " AS cat} WHERE NOT EXISTS ({{SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION + " AS rel} WHERE {rel:target}={cat:" + Item.PK + "} }})",
                                        Collections.EMPTY_MAP,
                                        Collections.singletonList(Category.class), true, true, 0, -1)
                        .getResult();
    }


    public Collection<Category> getCategoriesByCode(String code)
    {
        Map<Object, Object> value = new HashMap<>();
        value.put("code", code);
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.CATEGORY + "} WHERE {code} LIKE ?code", value,
                        Collections.singletonList(Category.class), true, true, 0, -1);
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Category getCategoryByCode(String code)
    {
        Collection<Category> categories = getCategoriesByCode(code);
        return categories.isEmpty() ? null : categories.iterator().next();
    }


    public Collection<Category> getCategoriesByProduct(Product product)
    {
        return getCategoriesByProduct(product, product.getSession().getSessionContext());
    }


    public Collection<Category> getCategoriesByProduct(Product product, SessionContext ctx)
    {
        return getSupercategories(ctx, product);
    }


    private final void localizeSavedQuery(SavedQuery savedQuery, String deName, String enName, String deDescr, String enDescr)
    {
        C2LManager c2LManager = getSession().getC2LManager();
        SessionContext deCtx = null, enCtx = null;
        try
        {
            Language language = c2LManager.getLanguageByIsoCode("de");
            deCtx = getSession().createSessionContext();
            deCtx.setLanguage(language);
        }
        catch(JaloItemNotFoundException jaloItemNotFoundException)
        {
        }
        try
        {
            Language language = c2LManager.getLanguageByIsoCode("en");
            enCtx = getSession().createSessionContext();
            enCtx.setLanguage(language);
        }
        catch(JaloItemNotFoundException jaloItemNotFoundException)
        {
        }
        if(deCtx != null)
        {
            savedQuery.setName(deCtx, deName);
            savedQuery.setLocalizedProperty(deCtx, "description", deDescr);
        }
        if(enCtx != null)
        {
            savedQuery.setName(enCtx, enName);
            savedQuery.setLocalizedProperty(enCtx, "description", enDescr);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isCreatorDisabled()
    {
        return false;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void createEssentialData(Map params, JspContext jspc) throws Exception
    {
        TypeManager typeManager = getSession().getTypeManager();
        FlexibleSearch flexibleSearch = getSession().getFlexibleSearch();
        SavedQuery savedQuery = flexibleSearch.getSavedQuery("ProductsByCategorySearch");
        if(savedQuery == null)
        {
            savedQuery = flexibleSearch.createSavedQuery("ProductsByCategorySearch", typeManager.getComposedType(Product.class), "SELECT DISTINCT {p:" + Item.PK + "}, {p:code}, {p:name:o} FROM {$$$ AS p JOIN " + GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION + " AS rel ON {p:" + Item.PK
                                            + "} = {rel:target} AND {rel:source} IN (?category, ?category.allSubcategories ) } ORDER BY {p:name:o} ASC, {p:code} ASC",
                            Collections.singletonMap("category", typeManager
                                            .getComposedType(Category.class)));
            localizeSavedQuery(savedQuery, "In Kategorie (rekursiv)", "In category (recursive)", "findet alle Produkte in einer Kategorie", "finds all products within a category including subcategories");
        }
        savedQuery = flexibleSearch.getSavedQuery("CategoriesByCategorySearch");
        if(savedQuery == null)
        {
            savedQuery = flexibleSearch.createSavedQuery("CategoriesByCategorySearch", typeManager
                                            .getComposedType(Category.class), "SELECT {" + Item.PK + "} FROM {$$$ AS cat} WHERE {" + Item.PK + "} IN ( ?category.allSubcategories ) ORDER BY {cat:name:o} ASC, {code} ASC",
                            Collections.singletonMap("category", typeManager
                                            .getComposedType(Category.class)));
            localizeSavedQuery(savedQuery, "In Kategorie (rekursiv)", "In category (recursive)", "findet alle Subkategorien einer Kategorie", "finds all subcategories within a category");
        }
        savedQuery = flexibleSearch.getSavedQuery("RootCategorySearch");
        if(savedQuery == null)
        {
            savedQuery = flexibleSearch.createSavedQuery("RootCategorySearch", typeManager.getComposedType(Category.class),
                            "SELECT {cat:" + Item.PK + "} FROM {$$$ AS cat} WHERE NOT EXISTS ({{SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION + "} WHERE {target}={cat:" + Item.PK + "} }})ORDER BY {cat:name:o} ASC, {cat:code} ASC", null);
            localizeSavedQuery(savedQuery, "Top-Level Kategorien", "Root Categories", "findet alle Top-Level Kategorien", "finds all root categories");
        }
    }


    public Collection<Category> getSupercategories(Product item)
    {
        return getSupercategories(getSession().getSessionContext(), item);
    }


    public Collection<Category> getSupercategories(SessionContext ctx, Product item)
    {
        return getCatalogManager().getSupercategories(ctx, item);
    }


    private JaloSession getSession()
    {
        return getCatalogManager().getSession();
    }


    private CatalogManager getCatalogManager()
    {
        return CatalogManager.getInstance();
    }


    public Category createCategory(SessionContext ctx, Map attributeValues)
    {
        return getCatalogManager().createCategory(ctx, attributeValues);
    }


    public Category createCategory(Map attributeValues)
    {
        return getCatalogManager().createCategory(attributeValues);
    }


    public long getSupercategoriesCount(SessionContext ctx, Product item)
    {
        return getCatalogManager().getSupercategoriesCount(ctx, item);
    }


    public long getSupercategoriesCount(Product item)
    {
        return getCatalogManager().getSupercategoriesCount(item);
    }


    public void setSupercategories(SessionContext ctx, Product item, Collection<Category> value)
    {
        getCatalogManager().setSupercategories(ctx, item, value);
    }


    public void setSupercategories(Product item, Collection<Category> value)
    {
        getCatalogManager().setSupercategories(item, value);
    }


    public void addToSupercategories(SessionContext ctx, Product item, Category value)
    {
        getCatalogManager().addToSupercategories(ctx, item, value);
    }


    public void addToSupercategories(Product item, Category value)
    {
        getCatalogManager().addToSupercategories(item, value);
    }


    public void removeFromSupercategories(SessionContext ctx, Product item, Category value)
    {
        getCatalogManager().removeFromSupercategories(ctx, item, value);
    }


    public void removeFromSupercategories(Product item, Category value)
    {
        getCatalogManager().removeFromSupercategories(item, value);
    }


    public Collection<Category> getSupercategories(SessionContext ctx, Media item)
    {
        return getCatalogManager().getSupercategories(ctx, item);
    }


    public Collection<Category> getSupercategories(Media item)
    {
        return getCatalogManager().getSupercategories(item);
    }


    public long getSupercategoriesCount(SessionContext ctx, Media item)
    {
        return getCatalogManager().getSupercategoriesCount(ctx, item);
    }


    public long getSupercategoriesCount(Media item)
    {
        return getCatalogManager().getSupercategoriesCount(item);
    }


    public void setSupercategories(SessionContext ctx, Media item, Collection<Category> value)
    {
        getCatalogManager().setSupercategories(ctx, item, value);
    }


    public void setSupercategories(Media item, Collection<Category> value)
    {
        getCatalogManager().setSupercategories(item, value);
    }


    public void addToSupercategories(SessionContext ctx, Media item, Category value)
    {
        getCatalogManager().addToSupercategories(ctx, item, value);
    }


    public void addToSupercategories(Media item, Category value)
    {
        getCatalogManager().addToSupercategories(item, value);
    }


    public void removeFromSupercategories(SessionContext ctx, Media item, Category value)
    {
        getCatalogManager().removeFromSupercategories(ctx, item, value);
    }


    public void removeFromSupercategories(Media item, Category value)
    {
        getCatalogManager().removeFromSupercategories(item, value);
    }
}
