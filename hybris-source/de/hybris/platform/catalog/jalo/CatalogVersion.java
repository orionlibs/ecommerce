package de.hybris.platform.catalog.jalo;

import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.localization.Localization;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CatalogVersion extends GeneratedCatalogVersion
{
    private static final Logger LOG = Logger.getLogger(CatalogVersion.class.getName());
    public static final int ERROR = 1715;


    @SLDSafe(portingClass = "CatalogVersionPrepareInterceptor")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute("catalog", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("version", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("Missing " + missing + " for creating a new CatalogVersion", 0);
        }
        allAttributes.setAttributeMode("catalog", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("version", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("active", Item.AttributeMode.INITIAL);
        Boolean active = (Boolean)allAttributes.get("active");
        CatalogVersion catalogVersion = (CatalogVersion)super.createItem(ctx, type, allAttributes);
        if(Boolean.TRUE.equals(active))
        {
            ((Catalog)allAttributes.get("catalog")).setActiveCatalogVersion(ctx, catalogVersion);
        }
        if(!allAttributes.containsKey("writePrincipals") && !JaloSession.getCurrentSession().getUser().isAdmin())
        {
            catalogVersion.addToWritePrincipals(ctx, (Principal)JaloSession.getCurrentSession().getUser());
            catalogVersion.addToReadPrincipals(ctx, (Principal)JaloSession.getCurrentSession().getUser());
        }
        return (Item)catalogVersion;
    }


    @SLDSafe(portingClass = "de.hybris.platform.catalog.interceptors.CheckVersionsRemoveInterceptor")
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        if(isItemCheckBeforeRemoveableDisabled(ctx) || isRemovable(ctx, true))
        {
            for(SyncItemJob job : getSyncJobs(true))
            {
                job.remove(ctx);
            }
            for(SyncItemJob job : getSyncJobs(false))
            {
                job.remove(ctx);
            }
            super.remove(ctx);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe
    protected void setCatalog(SessionContext ctx, Catalog param)
    {
        Catalog prev = getCatalog(ctx);
        if(prev == null)
        {
            super.setCatalog(ctx, param);
        }
        else if(!prev.equals(param))
        {
            throw new JaloInvalidParameterException("cannot re-assign catalog version " + this + " from " + prev + " to " + param, 0);
        }
    }


    @SLDSafe(portingClass = "de.hybris.platform.catalog.interceptors.CheckVersionsRemoveInterceptor", portingMethod = "isRemovable")
    protected void checkRemovable(SessionContext ctx) throws ConsistencyCheckException
    {
        isRemovable(ctx, true);
        super.checkRemovable(ctx);
    }


    protected boolean isRemovable(SessionContext ctx, boolean throwException) throws ConsistencyCheckException
    {
        if(isActiveAsPrimitive(ctx))
        {
            if(throwException)
            {
                throw new ConsistencyCheckException(
                                Localization.getLocalizedString("exception.catalogversion.dont_remove_active_catalog_version"), 0);
            }
            return false;
        }
        int categoryCount = 0;
        if((categoryCount = getAllCategoryCount(ctx)) > 0)
        {
            if(throwException)
            {
                throw new ConsistencyCheckException(Localization.getLocalizedString("exception.catalogversion.still_catategories", new Object[] {Integer.valueOf(categoryCount)}), 0);
            }
            return false;
        }
        if((categoryCount = getAllProductCount(ctx)) > 0)
        {
            if(throwException)
            {
                throw new ConsistencyCheckException(Localization.getLocalizedString("exception.catalogversion.still_products", new Object[] {Integer.valueOf(categoryCount)}), 0);
            }
            return false;
        }
        if((categoryCount = getAllKeywordCount(ctx)) > 0)
        {
            if(throwException)
            {
                throw new ConsistencyCheckException(Localization.getLocalizedString("exception.catalogversion.still_keywords", new Object[] {Integer.valueOf(categoryCount)}), 0);
            }
            return false;
        }
        if((categoryCount = getAllMediaCount()) > 0)
        {
            if(throwException)
            {
                throw new ConsistencyCheckException(Localization.getLocalizedString("exception.catalogversion.still_medias", new Object[] {Integer.valueOf(categoryCount)}), 0);
            }
            return false;
        }
        return true;
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "CatalogVersionPrepareInterceptor")
    public void setActive(SessionContext ctx, Boolean param)
    {
        boolean newActive = Boolean.TRUE.equals(param);
        if(isActive(ctx) == null || newActive != isActiveAsPrimitive(ctx))
        {
            if(getCatalog(ctx) != null)
            {
                if(newActive)
                {
                    getCatalog(ctx).setActiveCatalogVersion(ctx, this);
                }
                else
                {
                    getCatalog(ctx).setActiveCatalogVersion(ctx, null);
                }
            }
            super.setActive(ctx, (param != null) ? param : Boolean.FALSE);
        }
    }


    protected void setActiveInternal(SessionContext ctx, Boolean param)
    {
        super.setActive(ctx, (param != null) ? param : Boolean.FALSE);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean hasAgreement(String id)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("id", id);
        return
                        (((Integer)getSession().getFlexibleSearch().search("SELECT COUNT( {" + Item.PK + "} ) FROM {" + GeneratedCatalogConstants.TC.AGREEMENT + "} WHERE {catalogVersion}=?me AND {id}=?id", params, Collections.singletonList(Integer.class), true, true, 0, -1).getResult().iterator()
                                        .next()).intValue() > 0);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isImportedLanguage(String isoCode)
    {
        Collection importedLanguages = getLanguages();
        if(importedLanguages == null || importedLanguages.isEmpty())
        {
            return false;
        }
        for(Iterator<Language> it = importedLanguages.iterator(); it.hasNext(); )
        {
            Language language = it.next();
            if(isoCode.equalsIgnoreCase(language.getIsoCode()))
            {
                return true;
            }
        }
        return false;
    }


    public void addLanguage(Language language)
    {
        Collection<Language> newImportedLanguages = new ArrayList();
        Collection importedLanguages = getLanguages();
        if(importedLanguages != null)
        {
            if(importedLanguages.contains(language))
            {
                LOG.info("Language '" + language.getIsoCode() + "' is already in the collection!");
                return;
            }
            newImportedLanguages.addAll(importedLanguages);
        }
        newImportedLanguages.add(language);
        setLanguages(newImportedLanguages);
    }


    public Collection<Keyword> getAllKeywords()
    {
        return getAllKeywords(0, -1);
    }


    public Collection<Keyword> getAllKeywords(int start, int count)
    {
        return getSession()
                        .getFlexibleSearch()
                        .search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.KEYWORD + "} WHERE {catalogVersion}=?me ORDER BY {keyword} ASC, {" + Item.PK + "} ASC",
                                        Collections.singletonMap("me", this), Collections.singletonList(Keyword.class), true, true, start, count)
                        .getResult();
    }


    public int getAllKeywordCount()
    {
        return getAllKeywordCount(getSession().getSessionContext());
    }


    public int getAllKeywordCount(SessionContext ctx)
    {
        return ((Integer)getSession()
                        .getFlexibleSearch()
                        .search(ctx, "SELECT COUNT( {" + Item.PK + "} ) FROM {" + GeneratedCatalogConstants.TC.KEYWORD + "} WHERE {catalogVersion}=?me ",
                                        Collections.singletonMap("me", this),
                                        Collections.singletonList(Integer.class), true, true, 0, -1).getResult().iterator().next()).intValue();
    }


    public Collection getKeywords(String keyword)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("keyword", keyword);
        return
                        FlexibleSearch.getInstance()
                                        .search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.KEYWORD + "} WHERE {catalogVersion}=?me AND {keyword}=?keyword ORDER BY  {" + Item.PK + "} ASC ", params,
                                                        Collections.singletonList(Product.class), true, true, 0, -1).getResult();
    }


    public Collection<Product> getAllProducts()
    {
        return getAllProducts(0, -1);
    }


    public Collection<Product> getAllProducts(int start, int count)
    {
        return
                        FlexibleSearch.getInstance()
                                        .search("SELECT {" + Item.PK + "} FROM {" +
                                                                        TypeManager.getInstance()
                                                                                        .getComposedType(Product.class)
                                                                                        .getCode() + "} WHERE {" + GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION + "}=?me ORDER BY {code} ASC , {" + Item.PK + "} ASC ",
                                                        Collections.singletonMap("me", this),
                                                        Collections.singletonList(Product.class), true, true, start, count).getResult();
    }


    public Collection getAllVisibleProducts(int start, int count)
    {
        SessionContext ctx = getSession().createSessionContext();
        ctx.setAttribute("catalogversions", Collections.singleton(this));
        return
                        FlexibleSearch.getInstance()
                                        .search(ctx, "SELECT {" + Item.PK + "} FROM {" +
                                                                        TypeManager.getInstance()
                                                                                        .getComposedType(Product.class)
                                                                                        .getCode() + "} ORDER BY {code} ASC , {" + Item.PK + "} ASC ", Collections.EMPTY_MAP,
                                                        Collections.singletonList(Product.class), true, true, start, count).getResult();
    }


    public int getAllProductCount()
    {
        return getAllProductCount(getSession().getSessionContext());
    }


    public int getAllProductCount(SessionContext ctx)
    {
        String typeCode = getRequiredType(Product.class).getCode();
        return ((Integer)getSession()
                        .getFlexibleSearch()
                        .search(ctx, "SELECT COUNT( {" + Item.PK + "} ) FROM {" + typeCode + "} WHERE {" + GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION + "}=?me ",
                                        Collections.singletonMap("me", this),
                                        Collections.singletonList(Integer.class), true, true, 0, -1).getResult().iterator().next()).intValue();
    }


    public int getAllVisibleProductCount()
    {
        SessionContext ctx = getSession().createSessionContext();
        ctx.setAttribute("catalogversions", Collections.singleton(this));
        return ((Integer)getSession()
                        .getFlexibleSearch()
                        .search(ctx, "SELECT COUNT( {" + Item.PK + "} ) FROM {" + getRequiredType(Product.class).getCode() + "} ", Collections.EMPTY_MAP,
                                        Collections.singletonList(Integer.class), true, true, 0, -1).getResult().iterator()
                        .next()).intValue();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Product> getProducts(String code)
    {
        return getProducts(getSession().getSessionContext(), code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Product> getProducts(SessionContext ctx, String code)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("code", code);
        return
                        FlexibleSearch.getInstance()
                                        .search(ctx, "SELECT {" + Item.PK + "} FROM {" +
                                                                        TypeManager.getInstance()
                                                                                        .getComposedType(Product.class)
                                                                                        .getCode() + "} WHERE {" + GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION + "}=?me AND {code}=?code ORDER BY {" + Item.PK + "} ASC ", params,
                                                        Collections.singletonList(Product.class), true, true, 0, -1)
                                        .getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Product getProduct(String code)
    {
        return getProduct(getSession().getSessionContext(), code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Product getProduct(SessionContext ctx, String code)
    {
        Collection<Product> result = getProducts(ctx, code);
        if(result.isEmpty())
        {
            return null;
        }
        return result.iterator().next();
    }


    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogVersionRootCategoriesHandler", portingMethod = "get")
    public List<Category> getRootCategories(SessionContext ctx)
    {
        return getSession()
                        .getFlexibleSearch()
                        .search(ctx, "SELECT {v:" + PK + "} FROM {" + GeneratedCatalogConstants.TC.CATEGORY + " AS v}WHERE {v:" + CatalogConstants.Attributes.Category.CATALOGVERSION + "}=?me AND NOT EXISTS ({{SELECT {rel:" + PK + "} FROM {"
                                                        + GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION + " AS rel JOIN " + GeneratedCatalogConstants.TC.CATEGORY + " AS c ON {c:" + PK + "}={rel:source} } WHERE {c:" + CatalogConstants.Attributes.Category.CATALOGVERSION + "}=?me AND {rel:target}={v:"
                                                        + Item.PK + "} }})ORDER BY {v:" + CatalogConstants.Attributes.Category.ORDER + "} ASC",
                                        Collections.singletonMap("me", this), Collections.singletonList(Category.class), true, true, 0, -1)
                        .getResult();
    }


    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogVersionRootCategoriesHandler", portingMethod = "get")
    public List<Category> getRootCategories()
    {
        return getRootCategories(getSession().getSessionContext());
    }


    public int getRootCategoriesCount()
    {
        return getRootCategoriesCount(getSession().getSessionContext());
    }


    public int getRootCategoriesCount(SessionContext ctx)
    {
        Integer integer = getSession().getFlexibleSearch().search(ctx, "SELECT count( {v:" + Item.PK + "}) FROM {" + GeneratedCatalogConstants.TC.CATEGORY + " AS v}WHERE {v:" + CatalogConstants.Attributes.Category.CATALOGVERSION + "}=?me AND NOT EXISTS ({{SELECT {rel:" + PK + "} FROM {"
                        + GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION + " AS rel JOIN " + GeneratedCatalogConstants.TC.CATEGORY + " AS c ON {c:" + PK + "}={rel:source} } WHERE {c:" + CatalogConstants.Attributes.Category.CATALOGVERSION + "}=?me AND {rel:target}={v:" + Item.PK
                        + "} }})", Collections.singletonMap("me", this), Collections.singletonList(Integer.class), true, true, 0, -1).getResult().iterator().next();
        return integer.intValue();
    }


    public Collection<Category> getCategories(String code)
    {
        return getCategories(getSession().getSessionContext(), code);
    }


    public Collection<Category> getCategories(SessionContext ctx, String code)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("code", code);
        return
                        FlexibleSearch.getInstance()
                                        .search(ctx, "SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.CATEGORY + "} WHERE {" + CatalogConstants.Attributes.Category.CATALOGVERSION + "}=?me AND {code}=?code ORDER BY {" + Item.PK + "} ASC ", params,
                                                        Collections.singletonList(Category.class), true, true, 0, -1)
                                        .getResult();
    }


    public Category getCategory(String code)
    {
        return getCategory(getSession().getSessionContext(), code);
    }


    public Category getCategory(SessionContext ctx, String code)
    {
        Collection<Category> result = getCategories(ctx, code);
        if(result.isEmpty())
        {
            return null;
        }
        return result.iterator().next();
    }


    @SLDSafe
    public void setRootCategories(SessionContext ctx, List<Category> categories) throws JaloInvalidParameterException
    {
        Collection<Category> toRemove = new HashSet<>(getRootCategories(ctx));
        if(categories != null)
        {
            Collection<Category> newOnes = new HashSet<>(categories);
            newOnes.removeAll(toRemove);
            if(!ownsCategories(CatalogManager.getInstance(), newOnes))
            {
                throw new JaloInvalidParameterException("version does not own all new root categories from " + newOnes, 0);
            }
            toRemove.removeAll(categories);
            CatalogManager catalogManager = CatalogManager.getInstance();
            int index = 0;
            for(Iterator<Category> iter = categories.iterator(); iter.hasNext(); index++)
            {
                Category cat = iter.next();
                catalogManager.setOrder(cat, index);
            }
        }
        for(Iterator<Category> it = toRemove.iterator(); it.hasNext(); )
        {
            Category cat = it.next();
            try
            {
                cat.remove(ctx);
            }
            catch(ConsistencyCheckException e)
            {
                throw new JaloInvalidParameterException("could not remove old root category because of '" + e.getMessage() + "'", 0);
            }
        }
    }


    @SLDSafe
    public void setRootCategories(List<Category> value)
    {
        setRootCategories(getSession().getSessionContext(), value);
    }


    protected boolean ownsCategories(CatalogManager catalogManager, Collection categories)
    {
        for(Iterator<Category> it = categories.iterator(); it.hasNext(); )
        {
            if(!owns(catalogManager, it.next()))
            {
                return false;
            }
        }
        return true;
    }


    protected boolean owns(Category cat)
    {
        return owns(CatalogManager.getInstance(), cat);
    }


    protected boolean owns(CatalogManager catalogManager, Category cat)
    {
        return equals(catalogManager.getCatalogVersion(cat));
    }


    public Collection<Category> getAllCategories()
    {
        return getAllCategories(0, -1);
    }


    public Collection<Category> getAllCategories(int start, int count)
    {
        return getSession()
                        .getFlexibleSearch()
                        .search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.CATEGORY + "} WHERE {" + CatalogConstants.Attributes.Category.CATALOGVERSION + "}=?me ORDER BY {code} ASC, {" + Item.PK + "} ASC",
                                        Collections.singletonMap("me", this),
                                        Collections.singletonList(Category.class), true, true, start, count).getResult();
    }


    public Collection getAllVisibleCategories(int start, int count)
    {
        SessionContext ctx = getSession().createSessionContext();
        ctx.setAttribute("catalogversions", Collections.singleton(this));
        return getSession()
                        .getFlexibleSearch()
                        .search(ctx, "SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.CATEGORY + "} ORDER BY {code} ASC, {" + Item.PK + "} ASC", Collections.EMPTY_MAP,
                                        Collections.singletonList(Category.class), true, true, start, count)
                        .getResult();
    }


    public int getAllCategoryCount()
    {
        return getAllCategoryCount(getSession().getSessionContext());
    }


    public int getAllCategoryCount(SessionContext ctx)
    {
        return ((Integer)getSession()
                        .getFlexibleSearch()
                        .search(ctx, "SELECT COUNT( {" + Item.PK + "} ) FROM {" + GeneratedCatalogConstants.TC.CATEGORY + "} WHERE {" + CatalogConstants.Attributes.Category.CATALOGVERSION + "}=?me ",
                                        Collections.singletonMap("me", this),
                                        Collections.singletonList(Integer.class), true, true, 0, -1).getResult().iterator().next()).intValue();
    }


    public int getAllVisibleCategoryCount()
    {
        SessionContext ctx = getSession().createSessionContext();
        ctx.setAttribute("catalogversions", Collections.singleton(this));
        return ((Integer)getSession()
                        .getFlexibleSearch()
                        .search(ctx, "SELECT COUNT( {" + Item.PK + "} ) FROM {" + GeneratedCatalogConstants.TC.CATEGORY + "}", Collections.EMPTY_MAP,
                                        Collections.singletonList(Integer.class), true, true, 0, -1).getResult().iterator().next()).intValue();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Media> getMedias(String code)
    {
        return getMedias(getSession().getSessionContext(), code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Media> getMedias(SessionContext ctx, String code)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("code", code);
        return getSession()
                        .getFlexibleSearch()
                        .search(ctx, "SELECT {" + Item.PK + "} FROM {" +
                                                        TypeManager.getInstance()
                                                                        .getComposedType(Media.class)
                                                                        .getCode() + "} WHERE {" + GeneratedCatalogConstants.Attributes.Media.CATALOGVERSION + "}=?me AND {code}=?code ORDER BY {" + Item.PK + "} ASC", params,
                                        Collections.singletonList(Media.class), true, true, 0, -1)
                        .getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Media getMedia(String code)
    {
        return getMedia(getSession().getSessionContext(), code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Media getMedia(SessionContext ctx, String code)
    {
        Collection<Media> result = getMedias(ctx, code);
        if(result.isEmpty())
        {
            return null;
        }
        return result.iterator().next();
    }


    public Collection<Media> getAllMedias()
    {
        return getAllMedias(0, -1);
    }


    public Collection<Media> getAllMedias(int start, int count)
    {
        return getSession()
                        .getFlexibleSearch()
                        .search("SELECT {" + Item.PK + "} FROM {" +
                                                        TypeManager.getInstance()
                                                                        .getComposedType(Media.class)
                                                                        .getCode() + "} WHERE {" + GeneratedCatalogConstants.Attributes.Media.CATALOGVERSION + "}=?me ORDER BY {code} ASC, {" + Item.PK + "} ASC",
                                        Collections.singletonMap("me", this),
                                        Collections.singletonList(Media.class), true, true, start, count).getResult();
    }


    public int getAllMediaCount()
    {
        String typeCode = getRequiredType(Media.class).getCode();
        return ((Integer)getSession()
                        .getFlexibleSearch()
                        .search("SELECT COUNT( {" + Item.PK + "} ) FROM {" + typeCode + "} WHERE {" + GeneratedCatalogConstants.Attributes.Media.CATALOGVERSION + "}=?me ",
                                        Collections.singletonMap("me", this),
                                        Collections.singletonList(Integer.class), true, true, 0, -1).getResult().iterator().next()).intValue();
    }


    private ComposedType getRequiredType(Class jaloClass)
    {
        try
        {
            ComposedType type = getSession().getTypeManager().getComposedType(jaloClass);
            if(type == null)
            {
                throw new JaloSystemException(null, "got type null for " + jaloClass, 0);
            }
            return type;
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e, "required type missing", 0);
        }
    }


    public SearchResult getCatalogVersionItems(Class itemclass, int start, int count)
    {
        return getCatalogVersionItems(getSession().getSessionContext(), itemclass, start, count);
    }


    public SearchResult getCatalogVersionItems(SessionContext ctx, Class<?> itemclass, int start, int count)
    {
        return getSession().getFlexibleSearch().search(ctx, "SELECT {" + Item.PK + "} FROM {" +
                                        getSession().getTypeManager().getComposedType(itemclass).getCode() + "} WHERE {catalogversion} = ?me ORDER BY {" + Item.CREATION_TIME + "} ASC, {" + Item.PK + "} ASC",
                        Collections.singletonMap("me", this), Collections.singletonList(itemclass), true, true, start, count);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getSameProducts(Product product)
    {
        return getSameProducts(getSession().getSessionContext(), product);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getSameProducts(SessionContext ctx, Product product)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("code", product.getCode());
        SearchResult result = getSession().getFlexibleSearch().search(ctx, "SELECT {" + Item.PK + "} FROM {" +
                                        getSession().getTypeManager().getComposedType(Product.class).getCode() + "}WHERE {code} = ?code AND {" + GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION + "} = ?me ORDER BY {" + Item.CREATION_TIME + "} ASC", params,
                        Collections.singletonList(Product.class), true, true, 0, -1);
        return result.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getSameCategories(Category category)
    {
        return getSameCategories(getSession().getSessionContext(), category);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getSameCategories(SessionContext ctx, Category category)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("code", category.getCode(ctx));
        return getSession()
                        .getFlexibleSearch()
                        .search(ctx, "SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.CATEGORY + "} WHERE {code} = ?code AND {" + CatalogConstants.Attributes.Category.CATALOGVERSION + "} = ?me ORDER BY {" + Item.CREATION_TIME + "} ASC", params,
                                        Collections.singletonList(Category.class), true, true, 0, -1)
                        .getResult();
    }


    public Collection getSameKeywords(Keyword keyword)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("kw", keyword.getKeyword());
        return getSession()
                        .getFlexibleSearch()
                        .search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.KEYWORD + "} WHERE {keyword} = ?kw AND {catalogVersion} = ?me ORDER BY {" + Item.CREATION_TIME + "} ASC", params,
                                        Collections.singletonList(Keyword.class), true, true, 0, -1)
                        .getResult();
    }


    public Collection getSameMedias(Media media)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("code", media.getCode());
        return getSession()
                        .getFlexibleSearch()
                        .search("SELECT {" + Item.PK + "} FROM {" +
                                                        getSession().getTypeManager()
                                                                        .getComposedType(Media.class)
                                                                        .getCode() + "}WHERE {code} = ?code AND {" + GeneratedCatalogConstants.Attributes.Media.CATALOGVERSION + "} = ?me ORDER BY {" + Item.CREATION_TIME + "} ASC", params,
                                        Collections.singletonList(Media.class), true, true, 0, -1)
                        .getResult();
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return getCatalog().getId() + "/" + getCatalog().getId() + "(" + getVersion() + ")";
    }


    protected List getSyncJobs(boolean asSource)
    {
        return getSyncJobs(getSession().getSessionContext(), asSource);
    }


    protected List getSyncJobs(SessionContext ctx, boolean asSource)
    {
        return
                        FlexibleSearch.getInstance()
                                        .search(ctx, "SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.SYNCITEMJOB + "} WHERE {" + (
                                                                        asSource ? "sourceVersion" : "targetVersion") + "}=?me ORDER BY {syncOrder} ASC ",
                                                        Collections.singletonMap("me", this),
                                                        Collections.singletonList(SyncItemJob.class), true, true, 0, -1).getResult();
    }


    public boolean hasSynchronizations()
    {
        return
                        (((Integer)FlexibleSearch.getInstance().search("SELECT count({PK}) FROM {" + GeneratedCatalogConstants.TC.SYNCITEMJOB + "} WHERE {sourceVersion}=?me ", Collections.singletonMap("me", this), Collections.singletonList(Integer.class), true, true, 0, -1).getResult().iterator()
                                        .next()).intValue() > 0);
    }


    public Set getDuplicateCatalogItemIDs(SessionContext ctx, String itemTypeCode, String versionAttr, String idAttribute) throws JaloItemNotFoundException
    {
        ComposedType type = TypeManager.getInstance().getComposedType(itemTypeCode);
        type.getAttributeDescriptorIncludingPrivate(idAttribute);
        type.getAttributeDescriptorIncludingPrivate(versionAttr);
        return new HashSet(
                        FlexibleSearch.getInstance()
                                        .search(ctx, "SELECT {" + idAttribute + "} FROM {" + type
                                                                        .getCode() + "} WHERE {" + versionAttr + "}=?version GROUP BY {" + idAttribute + "} HAVING count(*) > 1",
                                                        Collections.singletonMap("version", this), String.class)
                                        .getResult());
    }


    public long getDuplicatedCatalogItemsCount(SessionContext ctx, ComposedType type)
    {
        CatalogManager catman = CatalogManager.getInstance();
        String cvAD = catman.getCatalogVersionAttribute(type).getQualifier();
        Set<String> adquals = new HashSet<>();
        for(AttributeDescriptor ad : catman.getUniqueKeyAttributes(type))
        {
            adquals.add("{" + ad.getQualifier() + "}");
        }
        String uids = StringUtils.join(adquals, ",");
        Map<String, CatalogVersion> values = Collections.singletonMap("version", this);
        String query = "SELECT count(*) FROM ({{SELECT " + uids + " FROM {" + type.getCode() + "} WHERE {" + cvAD + "}=?version GROUP BY " + uids + " HAVING count(*) > 1}}) x";
        List<Long> reslist = FlexibleSearch.getInstance().search(ctx, query, values, Long.class).getResult();
        if(reslist.isEmpty())
        {
            return 0L;
        }
        return ((Long)reslist.get(0)).longValue();
    }


    @SLDSafe(portingClass = "CatalogVersionPrepareInterceptor")
    public void setReadPrincipals(SessionContext ctx, List<Principal> param)
    {
        super.setReadPrincipals(ctx, new ArrayList(new TreeSet(param)));
    }


    @SLDSafe(portingClass = "CatalogVersionPrepareInterceptor")
    public void setWritePrincipals(SessionContext ctx, List<Principal> param)
    {
        super.setWritePrincipals(ctx, new ArrayList(new TreeSet(param)));
        if(param != null)
        {
            Set<Principal> readers = new TreeSet(getReadPrincipals(ctx));
            if(readers.addAll(param))
            {
                setReadPrincipals(ctx, new ArrayList<>(readers));
            }
        }
    }


    @SLDSafe(portingClass = "de.hybris.platform.catalog.interceptors.CatalogVersionPrepareInterceptor")
    public void setLanguages(SessionContext ctx, Collection<?> value)
    {
        super.setLanguages(ctx, (value != null && !value.isEmpty()) ? new ArrayList(new LinkedHashSet(value)) : value);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set getDuplicateKeywords(SessionContext ctx)
    {
        return getDuplicateCatalogItemIDs(ctx, GeneratedCatalogConstants.TC.KEYWORD, "catalogVersion", "keyword");
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set getDuplicateMediaIDs(SessionContext ctx)
    {
        return getDuplicateCatalogItemIDs(ctx, TypeManager.getInstance().getComposedType(Media.class).getCode(), GeneratedCatalogConstants.Attributes.Media.CATALOGVERSION, "code");
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set getDuplicateCategoryIDs(SessionContext ctx)
    {
        return getDuplicateCatalogItemIDs(ctx, GeneratedCatalogConstants.TC.CATEGORY, CatalogConstants.Attributes.Category.CATALOGVERSION, "code");
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set getDuplicateProductIDs(SessionContext ctx)
    {
        return getDuplicateCatalogItemIDs(ctx, TypeManager.getInstance().getComposedType(Product.class).getCode(), GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION, "code");
    }
}
