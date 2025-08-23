package de.hybris.platform.category.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.Keyword;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCategory extends GenericItem
{
    public static final String DESCRIPTION = "description";
    public static final String ORDER = "order";
    public static final String CATALOG = "catalog";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String NORMAL = "normal";
    public static final String THUMBNAILS = "thumbnails";
    public static final String DETAIL = "detail";
    public static final String LOGO = "logo";
    public static final String DATA_SHEET = "data_sheet";
    public static final String OTHERS = "others";
    public static final String THUMBNAIL = "thumbnail";
    public static final String PICTURE = "picture";
    public static final String NAME = "name";
    public static final String CODE = "code";
    public static final String KEYWORDS = "keywords";
    protected static String CATEGORY2KEYWORDRELATION_SRC_ORDERED = "relation.Category2KeywordRelation.source.ordered";
    protected static String CATEGORY2KEYWORDRELATION_TGT_ORDERED = "relation.Category2KeywordRelation.target.ordered";
    protected static String CATEGORY2KEYWORDRELATION_MARKMODIFIED = "relation.Category2KeywordRelation.markmodified";
    public static final String ALLOWEDPRINCIPALS = "allowedPrincipals";
    protected static String CATEGORY2PRINCIPALRELATION_SRC_ORDERED = "relation.Category2PrincipalRelation.source.ordered";
    protected static String CATEGORY2PRINCIPALRELATION_TGT_ORDERED = "relation.Category2PrincipalRelation.target.ordered";
    protected static String CATEGORY2PRINCIPALRELATION_MARKMODIFIED = "relation.Category2PrincipalRelation.markmodified";
    public static final String PRODUCTS = "products";
    protected static String CATEGORYPRODUCTRELATION_SRC_ORDERED = "relation.CategoryProductRelation.source.ordered";
    protected static String CATEGORYPRODUCTRELATION_TGT_ORDERED = "relation.CategoryProductRelation.target.ordered";
    protected static String CATEGORYPRODUCTRELATION_MARKMODIFIED = "relation.CategoryProductRelation.markmodified";
    public static final String SUPERCATEGORIES = "supercategories";
    protected static String CATEGORYCATEGORYRELATION_SRC_ORDERED = "relation.CategoryCategoryRelation.source.ordered";
    protected static String CATEGORYCATEGORYRELATION_TGT_ORDERED = "relation.CategoryCategoryRelation.target.ordered";
    protected static String CATEGORYCATEGORYRELATION_MARKMODIFIED = "relation.CategoryCategoryRelation.markmodified";
    public static final String CATEGORIES = "categories";
    public static final String MEDIAS = "medias";
    protected static String CATEGORYMEDIARELATION_SRC_ORDERED = "relation.CategoryMediaRelation.source.ordered";
    protected static String CATEGORYMEDIARELATION_TGT_ORDERED = "relation.CategoryMediaRelation.target.ordered";
    protected static String CATEGORYMEDIARELATION_MARKMODIFIED = "relation.CategoryMediaRelation.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("order", Item.AttributeMode.INITIAL);
        tmp.put("catalog", Item.AttributeMode.INITIAL);
        tmp.put("catalogVersion", Item.AttributeMode.INITIAL);
        tmp.put("normal", Item.AttributeMode.INITIAL);
        tmp.put("thumbnails", Item.AttributeMode.INITIAL);
        tmp.put("detail", Item.AttributeMode.INITIAL);
        tmp.put("logo", Item.AttributeMode.INITIAL);
        tmp.put("data_sheet", Item.AttributeMode.INITIAL);
        tmp.put("others", Item.AttributeMode.INITIAL);
        tmp.put("thumbnail", Item.AttributeMode.INITIAL);
        tmp.put("picture", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("code", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<Principal> getAllowedPrincipals(SessionContext ctx)
    {
        List<Principal> items = getLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORY2PRINCIPALRELATION, "Principal", null,
                        Utilities.getRelationOrderingOverride(CATEGORY2PRINCIPALRELATION_SRC_ORDERED, true), false);
        return items;
    }


    public List<Principal> getAllowedPrincipals()
    {
        return getAllowedPrincipals(getSession().getSessionContext());
    }


    public long getAllowedPrincipalsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCatalogConstants.Relations.CATEGORY2PRINCIPALRELATION, "Principal", null);
    }


    public long getAllowedPrincipalsCount()
    {
        return getAllowedPrincipalsCount(getSession().getSessionContext());
    }


    public void setAllowedPrincipals(SessionContext ctx, List<Principal> value)
    {
        setLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORY2PRINCIPALRELATION, null, value,
                        Utilities.getRelationOrderingOverride(CATEGORY2PRINCIPALRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORY2PRINCIPALRELATION_MARKMODIFIED));
    }


    public void setAllowedPrincipals(List<Principal> value)
    {
        setAllowedPrincipals(getSession().getSessionContext(), value);
    }


    public void addToAllowedPrincipals(SessionContext ctx, Principal value)
    {
        addLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORY2PRINCIPALRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATEGORY2PRINCIPALRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORY2PRINCIPALRELATION_MARKMODIFIED));
    }


    public void addToAllowedPrincipals(Principal value)
    {
        addToAllowedPrincipals(getSession().getSessionContext(), value);
    }


    public void removeFromAllowedPrincipals(SessionContext ctx, Principal value)
    {
        removeLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORY2PRINCIPALRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATEGORY2PRINCIPALRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORY2PRINCIPALRELATION_MARKMODIFIED));
    }


    public void removeFromAllowedPrincipals(Principal value)
    {
        removeFromAllowedPrincipals(getSession().getSessionContext(), value);
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


    public List<Category> getCategories(SessionContext ctx)
    {
        List<Category> items = getLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION, "Category", null,
                        Utilities.getRelationOrderingOverride(CATEGORYCATEGORYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CATEGORYCATEGORYRELATION_TGT_ORDERED, true));
        return items;
    }


    public List<Category> getCategories()
    {
        return getCategories(getSession().getSessionContext());
    }


    public long getCategoriesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION, "Category", null);
    }


    public long getCategoriesCount()
    {
        return getCategoriesCount(getSession().getSessionContext());
    }


    public void setCategories(SessionContext ctx, List<Category> value)
    {
        setLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION, null, value,
                        Utilities.getRelationOrderingOverride(CATEGORYCATEGORYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CATEGORYCATEGORYRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CATEGORYCATEGORYRELATION_MARKMODIFIED));
    }


    public void setCategories(List<Category> value)
    {
        setCategories(getSession().getSessionContext(), value);
    }


    public void addToCategories(SessionContext ctx, Category value)
    {
        addLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATEGORYCATEGORYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CATEGORYCATEGORYRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CATEGORYCATEGORYRELATION_MARKMODIFIED));
    }


    public void addToCategories(Category value)
    {
        addToCategories(getSession().getSessionContext(), value);
    }


    public void removeFromCategories(SessionContext ctx, Category value)
    {
        removeLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATEGORYCATEGORYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CATEGORYCATEGORYRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CATEGORYCATEGORYRELATION_MARKMODIFIED));
    }


    public void removeFromCategories(Category value)
    {
        removeFromCategories(getSession().getSessionContext(), value);
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    public Collection<Media> getData_sheet(SessionContext ctx)
    {
        Collection<Media> coll = (Collection<Media>)getProperty(ctx, "data_sheet");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Media> getData_sheet()
    {
        return getData_sheet(getSession().getSessionContext());
    }


    public void setData_sheet(SessionContext ctx, Collection<Media> value)
    {
        setProperty(ctx, "data_sheet", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setData_sheet(Collection<Media> value)
    {
        setData_sheet(getSession().getSessionContext(), value);
    }


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCategory.getDescription requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public Map<Language, String> getAllDescription(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "description", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllDescription()
    {
        return getAllDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCategory.setDescription requires a session language", 0);
        }
        setLocalizedProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public void setAllDescription(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "description", value);
    }


    public void setAllDescription(Map<Language, String> value)
    {
        setAllDescription(getSession().getSessionContext(), value);
    }


    public Collection<Media> getDetail(SessionContext ctx)
    {
        Collection<Media> coll = (Collection<Media>)getProperty(ctx, "detail");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Media> getDetail()
    {
        return getDetail(getSession().getSessionContext());
    }


    public void setDetail(SessionContext ctx, Collection<Media> value)
    {
        setProperty(ctx, "detail", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setDetail(Collection<Media> value)
    {
        setDetail(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Keyword");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CATEGORY2KEYWORDRELATION_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("Principal");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CATEGORY2PRINCIPALRELATION_MARKMODIFIED);
        }
        ComposedType relationSecondEnd2 = TypeManager.getInstance().getComposedType("Product");
        if(relationSecondEnd2.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CATEGORYPRODUCTRELATION_MARKMODIFIED);
        }
        ComposedType relationSecondEnd3 = TypeManager.getInstance().getComposedType("Category");
        if(relationSecondEnd3.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CATEGORYCATEGORYRELATION_MARKMODIFIED);
        }
        ComposedType relationSecondEnd4 = TypeManager.getInstance().getComposedType("Media");
        if(relationSecondEnd4.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CATEGORYMEDIARELATION_MARKMODIFIED);
        }
        return true;
    }


    public List<Keyword> getKeywords(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCategory.getKeywords requires a session language", 0);
        }
        List<Keyword> items = getLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORY2KEYWORDRELATION, "Keyword", ctx
                                        .getLanguage(),
                        Utilities.getRelationOrderingOverride(CATEGORY2KEYWORDRELATION_SRC_ORDERED, true), false);
        return items;
    }


    public List<Keyword> getKeywords()
    {
        return getKeywords(getSession().getSessionContext());
    }


    public Map<Language, List<Keyword>> getAllKeywords(SessionContext ctx)
    {
        Map<Language, List<Keyword>> values = getAllLinkedItems(true, GeneratedCatalogConstants.Relations.CATEGORY2KEYWORDRELATION);
        return values;
    }


    public Map<Language, List<Keyword>> getAllKeywords()
    {
        return getAllKeywords(getSession().getSessionContext());
    }


    public long getKeywordsCount(SessionContext ctx, Language lang)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCatalogConstants.Relations.CATEGORY2KEYWORDRELATION, "Keyword", lang);
    }


    public long getKeywordsCount(Language lang)
    {
        return getKeywordsCount(getSession().getSessionContext(), lang);
    }


    public long getKeywordsCount(SessionContext ctx)
    {
        return getKeywordsCount(ctx, ctx.getLanguage());
    }


    public long getKeywordsCount()
    {
        return getKeywordsCount(getSession().getSessionContext(), getSession().getSessionContext().getLanguage());
    }


    public void setKeywords(SessionContext ctx, List<Keyword> value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCategory.setKeywords requires a session language", 0);
        }
        setLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORY2KEYWORDRELATION, ctx
                                        .getLanguage(), value,
                        Utilities.getRelationOrderingOverride(CATEGORY2KEYWORDRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORY2KEYWORDRELATION_MARKMODIFIED));
    }


    public void setKeywords(List<Keyword> value)
    {
        setKeywords(getSession().getSessionContext(), value);
    }


    public void setAllKeywords(SessionContext ctx, Map<Language, List<Keyword>> value)
    {
        setAllLinkedItems(
                        getAllValuesSessionContext(ctx), true, GeneratedCatalogConstants.Relations.CATEGORY2KEYWORDRELATION, value);
    }


    public void setAllKeywords(Map<Language, List<Keyword>> value)
    {
        setAllKeywords(getSession().getSessionContext(), value);
    }


    public void addToKeywords(SessionContext ctx, Language lang, Keyword value)
    {
        if(lang == null)
        {
            throw new JaloInvalidParameterException("GeneratedCategory.addToKeywords requires a language language", 0);
        }
        addLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORY2KEYWORDRELATION, lang,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATEGORY2KEYWORDRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORY2KEYWORDRELATION_MARKMODIFIED));
    }


    public void addToKeywords(Language lang, Keyword value)
    {
        addToKeywords(getSession().getSessionContext(), lang, value);
    }


    public void removeFromKeywords(SessionContext ctx, Language lang, Keyword value)
    {
        if(lang == null)
        {
            throw new JaloInvalidParameterException("GeneratedCategory.removeFromKeywords requires a session language", 0);
        }
        removeLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORY2KEYWORDRELATION, lang,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATEGORY2KEYWORDRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORY2KEYWORDRELATION_MARKMODIFIED));
    }


    public void removeFromKeywords(Language lang, Keyword value)
    {
        removeFromKeywords(getSession().getSessionContext(), lang, value);
    }


    public Collection<Media> getLogo(SessionContext ctx)
    {
        Collection<Media> coll = (Collection<Media>)getProperty(ctx, "logo");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Media> getLogo()
    {
        return getLogo(getSession().getSessionContext());
    }


    public void setLogo(SessionContext ctx, Collection<Media> value)
    {
        setProperty(ctx, "logo", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setLogo(Collection<Media> value)
    {
        setLogo(getSession().getSessionContext(), value);
    }


    public List<Media> getMedias(SessionContext ctx)
    {
        List<Media> items = getLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORYMEDIARELATION, "Media", null,
                        Utilities.getRelationOrderingOverride(CATEGORYMEDIARELATION_SRC_ORDERED, true), false);
        return items;
    }


    public List<Media> getMedias()
    {
        return getMedias(getSession().getSessionContext());
    }


    public long getMediasCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCatalogConstants.Relations.CATEGORYMEDIARELATION, "Media", null);
    }


    public long getMediasCount()
    {
        return getMediasCount(getSession().getSessionContext());
    }


    public void setMedias(SessionContext ctx, List<Media> value)
    {
        setLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORYMEDIARELATION, null, value,
                        Utilities.getRelationOrderingOverride(CATEGORYMEDIARELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORYMEDIARELATION_MARKMODIFIED));
    }


    public void setMedias(List<Media> value)
    {
        setMedias(getSession().getSessionContext(), value);
    }


    public void addToMedias(SessionContext ctx, Media value)
    {
        addLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORYMEDIARELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATEGORYMEDIARELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORYMEDIARELATION_MARKMODIFIED));
    }


    public void addToMedias(Media value)
    {
        addToMedias(getSession().getSessionContext(), value);
    }


    public void removeFromMedias(SessionContext ctx, Media value)
    {
        removeLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORYMEDIARELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATEGORYMEDIARELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORYMEDIARELATION_MARKMODIFIED));
    }


    public void removeFromMedias(Media value)
    {
        removeFromMedias(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCategory.getName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "name", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllName()
    {
        return getAllName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCategory.setName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public void setAllName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "name", value);
    }


    public void setAllName(Map<Language, String> value)
    {
        setAllName(getSession().getSessionContext(), value);
    }


    public Collection<Media> getNormal(SessionContext ctx)
    {
        Collection<Media> coll = (Collection<Media>)getProperty(ctx, "normal");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Media> getNormal()
    {
        return getNormal(getSession().getSessionContext());
    }


    public void setNormal(SessionContext ctx, Collection<Media> value)
    {
        setProperty(ctx, "normal", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setNormal(Collection<Media> value)
    {
        setNormal(getSession().getSessionContext(), value);
    }


    public Integer getOrder(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "order");
    }


    public Integer getOrder()
    {
        return getOrder(getSession().getSessionContext());
    }


    public int getOrderAsPrimitive(SessionContext ctx)
    {
        Integer value = getOrder(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getOrderAsPrimitive()
    {
        return getOrderAsPrimitive(getSession().getSessionContext());
    }


    public void setOrder(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "order", value);
    }


    public void setOrder(Integer value)
    {
        setOrder(getSession().getSessionContext(), value);
    }


    public void setOrder(SessionContext ctx, int value)
    {
        setOrder(ctx, Integer.valueOf(value));
    }


    public void setOrder(int value)
    {
        setOrder(getSession().getSessionContext(), value);
    }


    public Collection<Media> getOthers(SessionContext ctx)
    {
        Collection<Media> coll = (Collection<Media>)getProperty(ctx, "others");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Media> getOthers()
    {
        return getOthers(getSession().getSessionContext());
    }


    public void setOthers(SessionContext ctx, Collection<Media> value)
    {
        setProperty(ctx, "others", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setOthers(Collection<Media> value)
    {
        setOthers(getSession().getSessionContext(), value);
    }


    public Media getPicture(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "picture");
    }


    public Media getPicture()
    {
        return getPicture(getSession().getSessionContext());
    }


    public void setPicture(SessionContext ctx, Media value)
    {
        setProperty(ctx, "picture", value);
    }


    public void setPicture(Media value)
    {
        setPicture(getSession().getSessionContext(), value);
    }


    public List<Product> getProducts(SessionContext ctx)
    {
        List<Product> items = getLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION, "Product", null,
                        Utilities.getRelationOrderingOverride(CATEGORYPRODUCTRELATION_SRC_ORDERED, true), false);
        return items;
    }


    public List<Product> getProducts()
    {
        return getProducts(getSession().getSessionContext());
    }


    public long getProductsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION, "Product", null);
    }


    public long getProductsCount()
    {
        return getProductsCount(getSession().getSessionContext());
    }


    public void setProducts(SessionContext ctx, List<Product> value)
    {
        setLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION, null, value,
                        Utilities.getRelationOrderingOverride(CATEGORYPRODUCTRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORYPRODUCTRELATION_MARKMODIFIED));
    }


    public void setProducts(List<Product> value)
    {
        setProducts(getSession().getSessionContext(), value);
    }


    public void addToProducts(SessionContext ctx, Product value)
    {
        addLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATEGORYPRODUCTRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORYPRODUCTRELATION_MARKMODIFIED));
    }


    public void addToProducts(Product value)
    {
        addToProducts(getSession().getSessionContext(), value);
    }


    public void removeFromProducts(SessionContext ctx, Product value)
    {
        removeLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATEGORYPRODUCTRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORYPRODUCTRELATION_MARKMODIFIED));
    }


    public void removeFromProducts(Product value)
    {
        removeFromProducts(getSession().getSessionContext(), value);
    }


    public List<Category> getSupercategories(SessionContext ctx)
    {
        List<Category> items = getLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION, "Category", null,
                        Utilities.getRelationOrderingOverride(CATEGORYCATEGORYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CATEGORYCATEGORYRELATION_TGT_ORDERED, true));
        return items;
    }


    public List<Category> getSupercategories()
    {
        return getSupercategories(getSession().getSessionContext());
    }


    public long getSupercategoriesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION, "Category", null);
    }


    public long getSupercategoriesCount()
    {
        return getSupercategoriesCount(getSession().getSessionContext());
    }


    public void setSupercategories(SessionContext ctx, List<Category> value)
    {
        setLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION, null, value,
                        Utilities.getRelationOrderingOverride(CATEGORYCATEGORYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CATEGORYCATEGORYRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CATEGORYCATEGORYRELATION_MARKMODIFIED));
    }


    public void setSupercategories(List<Category> value)
    {
        setSupercategories(getSession().getSessionContext(), value);
    }


    public void addToSupercategories(SessionContext ctx, Category value)
    {
        addLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATEGORYCATEGORYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CATEGORYCATEGORYRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CATEGORYCATEGORYRELATION_MARKMODIFIED));
    }


    public void addToSupercategories(Category value)
    {
        addToSupercategories(getSession().getSessionContext(), value);
    }


    public void removeFromSupercategories(SessionContext ctx, Category value)
    {
        removeLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.CATEGORYCATEGORYRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATEGORYCATEGORYRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CATEGORYCATEGORYRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CATEGORYCATEGORYRELATION_MARKMODIFIED));
    }


    public void removeFromSupercategories(Category value)
    {
        removeFromSupercategories(getSession().getSessionContext(), value);
    }


    public Media getThumbnail(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "thumbnail");
    }


    public Media getThumbnail()
    {
        return getThumbnail(getSession().getSessionContext());
    }


    public void setThumbnail(SessionContext ctx, Media value)
    {
        setProperty(ctx, "thumbnail", value);
    }


    public void setThumbnail(Media value)
    {
        setThumbnail(getSession().getSessionContext(), value);
    }


    public Collection<Media> getThumbnails(SessionContext ctx)
    {
        Collection<Media> coll = (Collection<Media>)getProperty(ctx, "thumbnails");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Media> getThumbnails()
    {
        return getThumbnails(getSession().getSessionContext());
    }


    public void setThumbnails(SessionContext ctx, Collection<Media> value)
    {
        setProperty(ctx, "thumbnails", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setThumbnails(Collection<Media> value)
    {
        setThumbnails(getSession().getSessionContext(), value);
    }
}
