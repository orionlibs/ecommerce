package de.hybris.platform.cms2.jalo.site;

import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.CMSComponentType;
import de.hybris.platform.cms2.jalo.contents.ContentCatalog;
import de.hybris.platform.cms2.jalo.pages.ContentPage;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedCMSSite extends BaseSite
{
    public static final String URLPATTERNS = "urlPatterns";
    public static final String ACTIVE = "active";
    public static final String ACTIVEFROM = "activeFrom";
    public static final String ACTIVEUNTIL = "activeUntil";
    public static final String DEFAULTCATALOG = "defaultCatalog";
    public static final String DEFAULTCONTENTCATALOG = "defaultContentCatalog";
    public static final String STARTINGPAGE = "startingPage";
    public static final String REDIRECTURL = "redirectURL";
    public static final String PREVIEWURL = "previewURL";
    public static final String CLASSIFICATIONCATALOGS = "classificationCatalogs";
    public static final String PRODUCTCATALOGS = "productCatalogs";
    public static final String STARTPAGELABEL = "startPageLabel";
    public static final String OPENPREVIEWINNEWTAB = "openPreviewInNewTab";
    public static final String DEFAULTPREVIEWCATEGORY = "defaultPreviewCategory";
    public static final String DEFAULTPREVIEWPRODUCT = "defaultPreviewProduct";
    public static final String DEFAULTPREVIEWCATALOG = "defaultPreviewCatalog";
    public static final String CONTENTCATALOGS = "contentCatalogs";
    protected static String CATALOGSFORCMSSITE_SRC_ORDERED = "relation.CatalogsForCMSSite.source.ordered";
    protected static String CATALOGSFORCMSSITE_TGT_ORDERED = "relation.CatalogsForCMSSite.target.ordered";
    protected static String CATALOGSFORCMSSITE_MARKMODIFIED = "relation.CatalogsForCMSSite.markmodified";
    public static final String VALIDCOMPONENTTYPES = "validComponentTypes";
    protected static String VALIDCOMPONENTTYPESFORSITE_SRC_ORDERED = "relation.ValidComponentTypesForSite.source.ordered";
    protected static String VALIDCOMPONENTTYPESFORSITE_TGT_ORDERED = "relation.ValidComponentTypesForSite.target.ordered";
    protected static String VALIDCOMPONENTTYPESFORSITE_MARKMODIFIED = "relation.ValidComponentTypesForSite.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(BaseSite.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("urlPatterns", Item.AttributeMode.INITIAL);
        tmp.put("active", Item.AttributeMode.INITIAL);
        tmp.put("activeFrom", Item.AttributeMode.INITIAL);
        tmp.put("activeUntil", Item.AttributeMode.INITIAL);
        tmp.put("defaultCatalog", Item.AttributeMode.INITIAL);
        tmp.put("defaultContentCatalog", Item.AttributeMode.INITIAL);
        tmp.put("startingPage", Item.AttributeMode.INITIAL);
        tmp.put("redirectURL", Item.AttributeMode.INITIAL);
        tmp.put("previewURL", Item.AttributeMode.INITIAL);
        tmp.put("openPreviewInNewTab", Item.AttributeMode.INITIAL);
        tmp.put("defaultPreviewCategory", Item.AttributeMode.INITIAL);
        tmp.put("defaultPreviewProduct", Item.AttributeMode.INITIAL);
        tmp.put("defaultPreviewCatalog", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isActive(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "active");
    }


    public Boolean isActive()
    {
        return isActive(getSession().getSessionContext());
    }


    public boolean isActiveAsPrimitive(SessionContext ctx)
    {
        Boolean value = isActive(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isActiveAsPrimitive()
    {
        return isActiveAsPrimitive(getSession().getSessionContext());
    }


    public void setActive(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "active", value);
    }


    public void setActive(Boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    public void setActive(SessionContext ctx, boolean value)
    {
        setActive(ctx, Boolean.valueOf(value));
    }


    public void setActive(boolean value)
    {
        setActive(getSession().getSessionContext(), value);
    }


    public Date getActiveFrom(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "activeFrom");
    }


    public Date getActiveFrom()
    {
        return getActiveFrom(getSession().getSessionContext());
    }


    public void setActiveFrom(SessionContext ctx, Date value)
    {
        setProperty(ctx, "activeFrom", value);
    }


    public void setActiveFrom(Date value)
    {
        setActiveFrom(getSession().getSessionContext(), value);
    }


    public Date getActiveUntil(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "activeUntil");
    }


    public Date getActiveUntil()
    {
        return getActiveUntil(getSession().getSessionContext());
    }


    public void setActiveUntil(SessionContext ctx, Date value)
    {
        setProperty(ctx, "activeUntil", value);
    }


    public void setActiveUntil(Date value)
    {
        setActiveUntil(getSession().getSessionContext(), value);
    }


    public List<Catalog> getClassificationCatalogs()
    {
        return getClassificationCatalogs(getSession().getSessionContext());
    }


    public List<ContentCatalog> getContentCatalogs(SessionContext ctx)
    {
        List<ContentCatalog> items = getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CATALOGSFORCMSSITE, "ContentCatalog", null,
                        Utilities.getRelationOrderingOverride(CATALOGSFORCMSSITE_SRC_ORDERED, true), false);
        return items;
    }


    public List<ContentCatalog> getContentCatalogs()
    {
        return getContentCatalogs(getSession().getSessionContext());
    }


    public long getContentCatalogsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.CATALOGSFORCMSSITE, "ContentCatalog", null);
    }


    public long getContentCatalogsCount()
    {
        return getContentCatalogsCount(getSession().getSessionContext());
    }


    public void setContentCatalogs(SessionContext ctx, List<ContentCatalog> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CATALOGSFORCMSSITE, null, value,
                        Utilities.getRelationOrderingOverride(CATALOGSFORCMSSITE_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATALOGSFORCMSSITE_MARKMODIFIED));
    }


    public void setContentCatalogs(List<ContentCatalog> value)
    {
        setContentCatalogs(getSession().getSessionContext(), value);
    }


    public void addToContentCatalogs(SessionContext ctx, ContentCatalog value)
    {
        addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CATALOGSFORCMSSITE, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATALOGSFORCMSSITE_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATALOGSFORCMSSITE_MARKMODIFIED));
    }


    public void addToContentCatalogs(ContentCatalog value)
    {
        addToContentCatalogs(getSession().getSessionContext(), value);
    }


    public void removeFromContentCatalogs(SessionContext ctx, ContentCatalog value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.CATALOGSFORCMSSITE, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATALOGSFORCMSSITE_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATALOGSFORCMSSITE_MARKMODIFIED));
    }


    public void removeFromContentCatalogs(ContentCatalog value)
    {
        removeFromContentCatalogs(getSession().getSessionContext(), value);
    }


    public Catalog getDefaultCatalog(SessionContext ctx)
    {
        return (Catalog)getProperty(ctx, "defaultCatalog");
    }


    public Catalog getDefaultCatalog()
    {
        return getDefaultCatalog(getSession().getSessionContext());
    }


    public void setDefaultCatalog(SessionContext ctx, Catalog value)
    {
        setProperty(ctx, "defaultCatalog", value);
    }


    public void setDefaultCatalog(Catalog value)
    {
        setDefaultCatalog(getSession().getSessionContext(), value);
    }


    public ContentCatalog getDefaultContentCatalog(SessionContext ctx)
    {
        return (ContentCatalog)getProperty(ctx, "defaultContentCatalog");
    }


    public ContentCatalog getDefaultContentCatalog()
    {
        return getDefaultContentCatalog(getSession().getSessionContext());
    }


    public void setDefaultContentCatalog(SessionContext ctx, ContentCatalog value)
    {
        setProperty(ctx, "defaultContentCatalog", value);
    }


    public void setDefaultContentCatalog(ContentCatalog value)
    {
        setDefaultContentCatalog(getSession().getSessionContext(), value);
    }


    public Catalog getDefaultPreviewCatalog(SessionContext ctx)
    {
        return (Catalog)getProperty(ctx, "defaultPreviewCatalog");
    }


    public Catalog getDefaultPreviewCatalog()
    {
        return getDefaultPreviewCatalog(getSession().getSessionContext());
    }


    public void setDefaultPreviewCatalog(SessionContext ctx, Catalog value)
    {
        setProperty(ctx, "defaultPreviewCatalog", value);
    }


    public void setDefaultPreviewCatalog(Catalog value)
    {
        setDefaultPreviewCatalog(getSession().getSessionContext(), value);
    }


    public Category getDefaultPreviewCategory(SessionContext ctx)
    {
        return (Category)getProperty(ctx, "defaultPreviewCategory");
    }


    public Category getDefaultPreviewCategory()
    {
        return getDefaultPreviewCategory(getSession().getSessionContext());
    }


    public void setDefaultPreviewCategory(SessionContext ctx, Category value)
    {
        setProperty(ctx, "defaultPreviewCategory", value);
    }


    public void setDefaultPreviewCategory(Category value)
    {
        setDefaultPreviewCategory(getSession().getSessionContext(), value);
    }


    public Product getDefaultPreviewProduct(SessionContext ctx)
    {
        return (Product)getProperty(ctx, "defaultPreviewProduct");
    }


    public Product getDefaultPreviewProduct()
    {
        return getDefaultPreviewProduct(getSession().getSessionContext());
    }


    public void setDefaultPreviewProduct(SessionContext ctx, Product value)
    {
        setProperty(ctx, "defaultPreviewProduct", value);
    }


    public void setDefaultPreviewProduct(Product value)
    {
        setDefaultPreviewProduct(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("ContentCatalog");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CATALOGSFORCMSSITE_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("CMSComponentType");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(VALIDCOMPONENTTYPESFORSITE_MARKMODIFIED);
        }
        return true;
    }


    public Boolean isOpenPreviewInNewTab(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "openPreviewInNewTab");
    }


    public Boolean isOpenPreviewInNewTab()
    {
        return isOpenPreviewInNewTab(getSession().getSessionContext());
    }


    public boolean isOpenPreviewInNewTabAsPrimitive(SessionContext ctx)
    {
        Boolean value = isOpenPreviewInNewTab(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isOpenPreviewInNewTabAsPrimitive()
    {
        return isOpenPreviewInNewTabAsPrimitive(getSession().getSessionContext());
    }


    public void setOpenPreviewInNewTab(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "openPreviewInNewTab", value);
    }


    public void setOpenPreviewInNewTab(Boolean value)
    {
        setOpenPreviewInNewTab(getSession().getSessionContext(), value);
    }


    public void setOpenPreviewInNewTab(SessionContext ctx, boolean value)
    {
        setOpenPreviewInNewTab(ctx, Boolean.valueOf(value));
    }


    public void setOpenPreviewInNewTab(boolean value)
    {
        setOpenPreviewInNewTab(getSession().getSessionContext(), value);
    }


    public String getPreviewURL(SessionContext ctx)
    {
        return (String)getProperty(ctx, "previewURL");
    }


    public String getPreviewURL()
    {
        return getPreviewURL(getSession().getSessionContext());
    }


    public void setPreviewURL(SessionContext ctx, String value)
    {
        setProperty(ctx, "previewURL", value);
    }


    public void setPreviewURL(String value)
    {
        setPreviewURL(getSession().getSessionContext(), value);
    }


    public List<Catalog> getProductCatalogs()
    {
        return getProductCatalogs(getSession().getSessionContext());
    }


    public String getRedirectURL(SessionContext ctx)
    {
        return (String)getProperty(ctx, "redirectURL");
    }


    public String getRedirectURL()
    {
        return getRedirectURL(getSession().getSessionContext());
    }


    public void setRedirectURL(SessionContext ctx, String value)
    {
        setProperty(ctx, "redirectURL", value);
    }


    public void setRedirectURL(String value)
    {
        setRedirectURL(getSession().getSessionContext(), value);
    }


    public ContentPage getStartingPage(SessionContext ctx)
    {
        return (ContentPage)getProperty(ctx, "startingPage");
    }


    public ContentPage getStartingPage()
    {
        return getStartingPage(getSession().getSessionContext());
    }


    public void setStartingPage(SessionContext ctx, ContentPage value)
    {
        setProperty(ctx, "startingPage", value);
    }


    public void setStartingPage(ContentPage value)
    {
        setStartingPage(getSession().getSessionContext(), value);
    }


    public String getStartPageLabel()
    {
        return getStartPageLabel(getSession().getSessionContext());
    }


    public Collection<String> getUrlPatterns(SessionContext ctx)
    {
        Collection<String> coll = (Collection<String>)getProperty(ctx, "urlPatterns");
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<String> getUrlPatterns()
    {
        return getUrlPatterns(getSession().getSessionContext());
    }


    public void setUrlPatterns(SessionContext ctx, Collection<String> value)
    {
        setProperty(ctx, "urlPatterns", (value == null || !value.isEmpty()) ? value : null);
    }


    public void setUrlPatterns(Collection<String> value)
    {
        setUrlPatterns(getSession().getSessionContext(), value);
    }


    public Set<CMSComponentType> getValidComponentTypes(SessionContext ctx)
    {
        List<CMSComponentType> items = getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.VALIDCOMPONENTTYPESFORSITE, "CMSComponentType", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<CMSComponentType> getValidComponentTypes()
    {
        return getValidComponentTypes(getSession().getSessionContext());
    }


    public long getValidComponentTypesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.VALIDCOMPONENTTYPESFORSITE, "CMSComponentType", null);
    }


    public long getValidComponentTypesCount()
    {
        return getValidComponentTypesCount(getSession().getSessionContext());
    }


    public void setValidComponentTypes(SessionContext ctx, Set<CMSComponentType> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.VALIDCOMPONENTTYPESFORSITE, null, value, false, false,
                        Utilities.getMarkModifiedOverride(VALIDCOMPONENTTYPESFORSITE_MARKMODIFIED));
    }


    public void setValidComponentTypes(Set<CMSComponentType> value)
    {
        setValidComponentTypes(getSession().getSessionContext(), value);
    }


    public void addToValidComponentTypes(SessionContext ctx, CMSComponentType value)
    {
        addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.VALIDCOMPONENTTYPESFORSITE, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(VALIDCOMPONENTTYPESFORSITE_MARKMODIFIED));
    }


    public void addToValidComponentTypes(CMSComponentType value)
    {
        addToValidComponentTypes(getSession().getSessionContext(), value);
    }


    public void removeFromValidComponentTypes(SessionContext ctx, CMSComponentType value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.VALIDCOMPONENTTYPESFORSITE, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(VALIDCOMPONENTTYPESFORSITE_MARKMODIFIED));
    }


    public void removeFromValidComponentTypes(CMSComponentType value)
    {
        removeFromValidComponentTypes(getSession().getSessionContext(), value);
    }


    public abstract List<Catalog> getClassificationCatalogs(SessionContext paramSessionContext);


    public abstract List<Catalog> getProductCatalogs(SessionContext paramSessionContext);


    public abstract String getStartPageLabel(SessionContext paramSessionContext);
}
