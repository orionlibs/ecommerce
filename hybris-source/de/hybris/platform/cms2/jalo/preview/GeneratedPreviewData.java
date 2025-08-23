package de.hybris.platform.cms2.jalo.preview;

import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.CMSVersion;
import de.hybris.platform.cms2.jalo.pages.AbstractPage;
import de.hybris.platform.cms2.jalo.site.CMSSite;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedPreviewData extends GenericItem
{
    public static final String USER = "user";
    public static final String USERGROUP = "userGroup";
    public static final String COUNTRY = "country";
    public static final String LANGUAGE = "language";
    public static final String TIME = "time";
    public static final String LIVEEDIT = "liveEdit";
    public static final String EDITMODE = "editMode";
    public static final String RESOURCEPATH = "resourcePath";
    public static final String PAGE = "page";
    public static final String PREVIEWCATEGORY = "previewCategory";
    public static final String PREVIEWPRODUCT = "previewProduct";
    public static final String PREVIEWCATALOG = "previewCatalog";
    public static final String ACTIVESITE = "activeSite";
    public static final String ACTIVECATALOGVERSION = "activeCatalogVersion";
    public static final String VERSION = "version";
    public static final String PREVIEWCONTENTCATALOGVERSION = "previewContentCatalogVersion";
    public static final String CATALOGVERSIONS = "catalogVersions";
    protected static String PREVIEWDATATOCATALOGVERSION_SRC_ORDERED = "relation.PreviewDataToCatalogVersion.source.ordered";
    protected static String PREVIEWDATATOCATALOGVERSION_TGT_ORDERED = "relation.PreviewDataToCatalogVersion.target.ordered";
    protected static String PREVIEWDATATOCATALOGVERSION_MARKMODIFIED = "relation.PreviewDataToCatalogVersion.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("user", Item.AttributeMode.INITIAL);
        tmp.put("userGroup", Item.AttributeMode.INITIAL);
        tmp.put("country", Item.AttributeMode.INITIAL);
        tmp.put("language", Item.AttributeMode.INITIAL);
        tmp.put("time", Item.AttributeMode.INITIAL);
        tmp.put("liveEdit", Item.AttributeMode.INITIAL);
        tmp.put("editMode", Item.AttributeMode.INITIAL);
        tmp.put("resourcePath", Item.AttributeMode.INITIAL);
        tmp.put("page", Item.AttributeMode.INITIAL);
        tmp.put("previewCategory", Item.AttributeMode.INITIAL);
        tmp.put("previewProduct", Item.AttributeMode.INITIAL);
        tmp.put("previewCatalog", Item.AttributeMode.INITIAL);
        tmp.put("activeSite", Item.AttributeMode.INITIAL);
        tmp.put("activeCatalogVersion", Item.AttributeMode.INITIAL);
        tmp.put("version", Item.AttributeMode.INITIAL);
        tmp.put("previewContentCatalogVersion", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public CatalogVersion getActiveCatalogVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "activeCatalogVersion");
    }


    public CatalogVersion getActiveCatalogVersion()
    {
        return getActiveCatalogVersion(getSession().getSessionContext());
    }


    public void setActiveCatalogVersion(SessionContext ctx, CatalogVersion value)
    {
        setProperty(ctx, "activeCatalogVersion", value);
    }


    public void setActiveCatalogVersion(CatalogVersion value)
    {
        setActiveCatalogVersion(getSession().getSessionContext(), value);
    }


    public CMSSite getActiveSite(SessionContext ctx)
    {
        return (CMSSite)getProperty(ctx, "activeSite");
    }


    public CMSSite getActiveSite()
    {
        return getActiveSite(getSession().getSessionContext());
    }


    public void setActiveSite(SessionContext ctx, CMSSite value)
    {
        setProperty(ctx, "activeSite", value);
    }


    public void setActiveSite(CMSSite value)
    {
        setActiveSite(getSession().getSessionContext(), value);
    }


    public Collection<CatalogVersion> getCatalogVersions(SessionContext ctx)
    {
        List<CatalogVersion> items = getLinkedItems(ctx, true, GeneratedCms2Constants.Relations.PREVIEWDATATOCATALOGVERSION, "CatalogVersion", null, false, false);
        return items;
    }


    public Collection<CatalogVersion> getCatalogVersions()
    {
        return getCatalogVersions(getSession().getSessionContext());
    }


    public long getCatalogVersionsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCms2Constants.Relations.PREVIEWDATATOCATALOGVERSION, "CatalogVersion", null);
    }


    public long getCatalogVersionsCount()
    {
        return getCatalogVersionsCount(getSession().getSessionContext());
    }


    public void setCatalogVersions(SessionContext ctx, Collection<CatalogVersion> value)
    {
        setLinkedItems(ctx, true, GeneratedCms2Constants.Relations.PREVIEWDATATOCATALOGVERSION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(PREVIEWDATATOCATALOGVERSION_MARKMODIFIED));
    }


    public void setCatalogVersions(Collection<CatalogVersion> value)
    {
        setCatalogVersions(getSession().getSessionContext(), value);
    }


    public void addToCatalogVersions(SessionContext ctx, CatalogVersion value)
    {
        addLinkedItems(ctx, true, GeneratedCms2Constants.Relations.PREVIEWDATATOCATALOGVERSION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PREVIEWDATATOCATALOGVERSION_MARKMODIFIED));
    }


    public void addToCatalogVersions(CatalogVersion value)
    {
        addToCatalogVersions(getSession().getSessionContext(), value);
    }


    public void removeFromCatalogVersions(SessionContext ctx, CatalogVersion value)
    {
        removeLinkedItems(ctx, true, GeneratedCms2Constants.Relations.PREVIEWDATATOCATALOGVERSION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(PREVIEWDATATOCATALOGVERSION_MARKMODIFIED));
    }


    public void removeFromCatalogVersions(CatalogVersion value)
    {
        removeFromCatalogVersions(getSession().getSessionContext(), value);
    }


    public Country getCountry(SessionContext ctx)
    {
        return (Country)getProperty(ctx, "country");
    }


    public Country getCountry()
    {
        return getCountry(getSession().getSessionContext());
    }


    public void setCountry(SessionContext ctx, Country value)
    {
        setProperty(ctx, "country", value);
    }


    public void setCountry(Country value)
    {
        setCountry(getSession().getSessionContext(), value);
    }


    public Boolean isEditMode(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "editMode");
    }


    public Boolean isEditMode()
    {
        return isEditMode(getSession().getSessionContext());
    }


    public boolean isEditModeAsPrimitive(SessionContext ctx)
    {
        Boolean value = isEditMode(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isEditModeAsPrimitive()
    {
        return isEditModeAsPrimitive(getSession().getSessionContext());
    }


    public void setEditMode(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "editMode", value);
    }


    public void setEditMode(Boolean value)
    {
        setEditMode(getSession().getSessionContext(), value);
    }


    public void setEditMode(SessionContext ctx, boolean value)
    {
        setEditMode(ctx, Boolean.valueOf(value));
    }


    public void setEditMode(boolean value)
    {
        setEditMode(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("CatalogVersion");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(PREVIEWDATATOCATALOGVERSION_MARKMODIFIED);
        }
        return true;
    }


    public Language getLanguage(SessionContext ctx)
    {
        return (Language)getProperty(ctx, "language");
    }


    public Language getLanguage()
    {
        return getLanguage(getSession().getSessionContext());
    }


    public void setLanguage(SessionContext ctx, Language value)
    {
        setProperty(ctx, "language", value);
    }


    public void setLanguage(Language value)
    {
        setLanguage(getSession().getSessionContext(), value);
    }


    public Boolean isLiveEdit(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "liveEdit");
    }


    public Boolean isLiveEdit()
    {
        return isLiveEdit(getSession().getSessionContext());
    }


    public boolean isLiveEditAsPrimitive(SessionContext ctx)
    {
        Boolean value = isLiveEdit(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isLiveEditAsPrimitive()
    {
        return isLiveEditAsPrimitive(getSession().getSessionContext());
    }


    public void setLiveEdit(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "liveEdit", value);
    }


    public void setLiveEdit(Boolean value)
    {
        setLiveEdit(getSession().getSessionContext(), value);
    }


    public void setLiveEdit(SessionContext ctx, boolean value)
    {
        setLiveEdit(ctx, Boolean.valueOf(value));
    }


    public void setLiveEdit(boolean value)
    {
        setLiveEdit(getSession().getSessionContext(), value);
    }


    public AbstractPage getPage(SessionContext ctx)
    {
        return (AbstractPage)getProperty(ctx, "page");
    }


    public AbstractPage getPage()
    {
        return getPage(getSession().getSessionContext());
    }


    public void setPage(SessionContext ctx, AbstractPage value)
    {
        setProperty(ctx, "page", value);
    }


    public void setPage(AbstractPage value)
    {
        setPage(getSession().getSessionContext(), value);
    }


    public Catalog getPreviewCatalog(SessionContext ctx)
    {
        return (Catalog)getProperty(ctx, "previewCatalog");
    }


    public Catalog getPreviewCatalog()
    {
        return getPreviewCatalog(getSession().getSessionContext());
    }


    public void setPreviewCatalog(SessionContext ctx, Catalog value)
    {
        setProperty(ctx, "previewCatalog", value);
    }


    public void setPreviewCatalog(Catalog value)
    {
        setPreviewCatalog(getSession().getSessionContext(), value);
    }


    public Category getPreviewCategory(SessionContext ctx)
    {
        return (Category)getProperty(ctx, "previewCategory");
    }


    public Category getPreviewCategory()
    {
        return getPreviewCategory(getSession().getSessionContext());
    }


    public void setPreviewCategory(SessionContext ctx, Category value)
    {
        setProperty(ctx, "previewCategory", value);
    }


    public void setPreviewCategory(Category value)
    {
        setPreviewCategory(getSession().getSessionContext(), value);
    }


    public CatalogVersion getPreviewContentCatalogVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "previewContentCatalogVersion");
    }


    public CatalogVersion getPreviewContentCatalogVersion()
    {
        return getPreviewContentCatalogVersion(getSession().getSessionContext());
    }


    public void setPreviewContentCatalogVersion(SessionContext ctx, CatalogVersion value)
    {
        setProperty(ctx, "previewContentCatalogVersion", value);
    }


    public void setPreviewContentCatalogVersion(CatalogVersion value)
    {
        setPreviewContentCatalogVersion(getSession().getSessionContext(), value);
    }


    public Product getPreviewProduct(SessionContext ctx)
    {
        return (Product)getProperty(ctx, "previewProduct");
    }


    public Product getPreviewProduct()
    {
        return getPreviewProduct(getSession().getSessionContext());
    }


    public void setPreviewProduct(SessionContext ctx, Product value)
    {
        setProperty(ctx, "previewProduct", value);
    }


    public void setPreviewProduct(Product value)
    {
        setPreviewProduct(getSession().getSessionContext(), value);
    }


    public String getResourcePath(SessionContext ctx)
    {
        return (String)getProperty(ctx, "resourcePath");
    }


    public String getResourcePath()
    {
        return getResourcePath(getSession().getSessionContext());
    }


    public void setResourcePath(SessionContext ctx, String value)
    {
        setProperty(ctx, "resourcePath", value);
    }


    public void setResourcePath(String value)
    {
        setResourcePath(getSession().getSessionContext(), value);
    }


    public Date getTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "time");
    }


    public Date getTime()
    {
        return getTime(getSession().getSessionContext());
    }


    public void setTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "time", value);
    }


    public void setTime(Date value)
    {
        setTime(getSession().getSessionContext(), value);
    }


    public User getUser(SessionContext ctx)
    {
        return (User)getProperty(ctx, "user");
    }


    public User getUser()
    {
        return getUser(getSession().getSessionContext());
    }


    public void setUser(SessionContext ctx, User value)
    {
        setProperty(ctx, "user", value);
    }


    public void setUser(User value)
    {
        setUser(getSession().getSessionContext(), value);
    }


    public UserGroup getUserGroup(SessionContext ctx)
    {
        return (UserGroup)getProperty(ctx, "userGroup");
    }


    public UserGroup getUserGroup()
    {
        return getUserGroup(getSession().getSessionContext());
    }


    public void setUserGroup(SessionContext ctx, UserGroup value)
    {
        setProperty(ctx, "userGroup", value);
    }


    public void setUserGroup(UserGroup value)
    {
        setUserGroup(getSession().getSessionContext(), value);
    }


    public CMSVersion getVersion(SessionContext ctx)
    {
        return (CMSVersion)getProperty(ctx, "version");
    }


    public CMSVersion getVersion()
    {
        return getVersion(getSession().getSessionContext());
    }


    public void setVersion(SessionContext ctx, CMSVersion value)
    {
        setProperty(ctx, "version", value);
    }


    public void setVersion(CMSVersion value)
    {
        setVersion(getSession().getSessionContext(), value);
    }
}
