package de.hybris.platform.cms2.model.site;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorservices.model.SiteMapConfigModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class CMSSiteModel extends BaseSiteModel
{
    public static final String _TYPECODE = "CMSSite";
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
    public static final String VALIDCOMPONENTTYPES = "validComponentTypes";
    public static final String URLENCODINGATTRIBUTES = "urlEncodingAttributes";
    public static final String SITEMAPS = "siteMaps";
    public static final String SITEMAPCONFIG = "siteMapConfig";


    public CMSSiteModel()
    {
    }


    public CMSSiteModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSSiteModel(String _uid)
    {
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSSiteModel(ItemModel _owner, String _uid)
    {
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public Boolean getActive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("active");
    }


    @Accessor(qualifier = "activeFrom", type = Accessor.Type.GETTER)
    public Date getActiveFrom()
    {
        return (Date)getPersistenceContext().getPropertyValue("activeFrom");
    }


    @Accessor(qualifier = "activeUntil", type = Accessor.Type.GETTER)
    public Date getActiveUntil()
    {
        return (Date)getPersistenceContext().getPropertyValue("activeUntil");
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "classificationCatalogs", type = Accessor.Type.GETTER)
    public List<CatalogModel> getClassificationCatalogs()
    {
        return (List<CatalogModel>)getPersistenceContext().getPropertyValue("classificationCatalogs");
    }


    @Accessor(qualifier = "contentCatalogs", type = Accessor.Type.GETTER)
    public List<ContentCatalogModel> getContentCatalogs()
    {
        return (List<ContentCatalogModel>)getPersistenceContext().getPropertyValue("contentCatalogs");
    }


    @Accessor(qualifier = "defaultCatalog", type = Accessor.Type.GETTER)
    public CatalogModel getDefaultCatalog()
    {
        return (CatalogModel)getPersistenceContext().getPropertyValue("defaultCatalog");
    }


    @Accessor(qualifier = "defaultContentCatalog", type = Accessor.Type.GETTER)
    public ContentCatalogModel getDefaultContentCatalog()
    {
        return (ContentCatalogModel)getPersistenceContext().getPropertyValue("defaultContentCatalog");
    }


    @Accessor(qualifier = "defaultPreviewCatalog", type = Accessor.Type.GETTER)
    public CatalogModel getDefaultPreviewCatalog()
    {
        return (CatalogModel)getPersistenceContext().getPropertyValue("defaultPreviewCatalog");
    }


    @Accessor(qualifier = "defaultPreviewCategory", type = Accessor.Type.GETTER)
    public CategoryModel getDefaultPreviewCategory()
    {
        return (CategoryModel)getPersistenceContext().getPropertyValue("defaultPreviewCategory");
    }


    @Accessor(qualifier = "defaultPreviewProduct", type = Accessor.Type.GETTER)
    public ProductModel getDefaultPreviewProduct()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("defaultPreviewProduct");
    }


    @Accessor(qualifier = "previewURL", type = Accessor.Type.GETTER)
    public String getPreviewURL()
    {
        return (String)getPersistenceContext().getPropertyValue("previewURL");
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "productCatalogs", type = Accessor.Type.GETTER)
    public List<CatalogModel> getProductCatalogs()
    {
        return (List<CatalogModel>)getPersistenceContext().getPropertyValue("productCatalogs");
    }


    @Accessor(qualifier = "redirectURL", type = Accessor.Type.GETTER)
    public String getRedirectURL()
    {
        return (String)getPersistenceContext().getPropertyValue("redirectURL");
    }


    @Accessor(qualifier = "siteMapConfig", type = Accessor.Type.GETTER)
    public SiteMapConfigModel getSiteMapConfig()
    {
        return (SiteMapConfigModel)getPersistenceContext().getPropertyValue("siteMapConfig");
    }


    @Accessor(qualifier = "siteMaps", type = Accessor.Type.GETTER)
    public Collection<MediaModel> getSiteMaps()
    {
        return (Collection<MediaModel>)getPersistenceContext().getPropertyValue("siteMaps");
    }


    @Accessor(qualifier = "startingPage", type = Accessor.Type.GETTER)
    public ContentPageModel getStartingPage()
    {
        return (ContentPageModel)getPersistenceContext().getPropertyValue("startingPage");
    }


    @Deprecated(since = "4.3", forRemoval = true)
    @Accessor(qualifier = "startPageLabel", type = Accessor.Type.GETTER)
    public String getStartPageLabel()
    {
        return (String)getPersistenceContext().getPropertyValue("startPageLabel");
    }


    @Accessor(qualifier = "urlEncodingAttributes", type = Accessor.Type.GETTER)
    public Collection<String> getUrlEncodingAttributes()
    {
        return (Collection<String>)getPersistenceContext().getPropertyValue("urlEncodingAttributes");
    }


    @Accessor(qualifier = "urlPatterns", type = Accessor.Type.GETTER)
    public Collection<String> getUrlPatterns()
    {
        return (Collection<String>)getPersistenceContext().getPropertyValue("urlPatterns");
    }


    @Accessor(qualifier = "validComponentTypes", type = Accessor.Type.GETTER)
    public Set<CMSComponentTypeModel> getValidComponentTypes()
    {
        return (Set<CMSComponentTypeModel>)getPersistenceContext().getPropertyValue("validComponentTypes");
    }


    @Accessor(qualifier = "openPreviewInNewTab", type = Accessor.Type.GETTER)
    public boolean isOpenPreviewInNewTab()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("openPreviewInNewTab"));
    }


    @Accessor(qualifier = "active", type = Accessor.Type.SETTER)
    public void setActive(Boolean value)
    {
        getPersistenceContext().setPropertyValue("active", value);
    }


    @Accessor(qualifier = "activeFrom", type = Accessor.Type.SETTER)
    public void setActiveFrom(Date value)
    {
        getPersistenceContext().setPropertyValue("activeFrom", value);
    }


    @Accessor(qualifier = "activeUntil", type = Accessor.Type.SETTER)
    public void setActiveUntil(Date value)
    {
        getPersistenceContext().setPropertyValue("activeUntil", value);
    }


    @Accessor(qualifier = "contentCatalogs", type = Accessor.Type.SETTER)
    public void setContentCatalogs(List<ContentCatalogModel> value)
    {
        getPersistenceContext().setPropertyValue("contentCatalogs", value);
    }


    @Accessor(qualifier = "defaultCatalog", type = Accessor.Type.SETTER)
    public void setDefaultCatalog(CatalogModel value)
    {
        getPersistenceContext().setPropertyValue("defaultCatalog", value);
    }


    @Accessor(qualifier = "defaultContentCatalog", type = Accessor.Type.SETTER)
    public void setDefaultContentCatalog(ContentCatalogModel value)
    {
        getPersistenceContext().setPropertyValue("defaultContentCatalog", value);
    }


    @Accessor(qualifier = "defaultPreviewCatalog", type = Accessor.Type.SETTER)
    public void setDefaultPreviewCatalog(CatalogModel value)
    {
        getPersistenceContext().setPropertyValue("defaultPreviewCatalog", value);
    }


    @Accessor(qualifier = "defaultPreviewCategory", type = Accessor.Type.SETTER)
    public void setDefaultPreviewCategory(CategoryModel value)
    {
        getPersistenceContext().setPropertyValue("defaultPreviewCategory", value);
    }


    @Accessor(qualifier = "defaultPreviewProduct", type = Accessor.Type.SETTER)
    public void setDefaultPreviewProduct(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("defaultPreviewProduct", value);
    }


    @Accessor(qualifier = "openPreviewInNewTab", type = Accessor.Type.SETTER)
    public void setOpenPreviewInNewTab(boolean value)
    {
        getPersistenceContext().setPropertyValue("openPreviewInNewTab", toObject(value));
    }


    @Accessor(qualifier = "previewURL", type = Accessor.Type.SETTER)
    public void setPreviewURL(String value)
    {
        getPersistenceContext().setPropertyValue("previewURL", value);
    }


    @Accessor(qualifier = "redirectURL", type = Accessor.Type.SETTER)
    public void setRedirectURL(String value)
    {
        getPersistenceContext().setPropertyValue("redirectURL", value);
    }


    @Accessor(qualifier = "siteMapConfig", type = Accessor.Type.SETTER)
    public void setSiteMapConfig(SiteMapConfigModel value)
    {
        getPersistenceContext().setPropertyValue("siteMapConfig", value);
    }


    @Accessor(qualifier = "siteMaps", type = Accessor.Type.SETTER)
    public void setSiteMaps(Collection<MediaModel> value)
    {
        getPersistenceContext().setPropertyValue("siteMaps", value);
    }


    @Accessor(qualifier = "startingPage", type = Accessor.Type.SETTER)
    public void setStartingPage(ContentPageModel value)
    {
        getPersistenceContext().setPropertyValue("startingPage", value);
    }


    @Accessor(qualifier = "urlEncodingAttributes", type = Accessor.Type.SETTER)
    public void setUrlEncodingAttributes(Collection<String> value)
    {
        getPersistenceContext().setPropertyValue("urlEncodingAttributes", value);
    }


    @Accessor(qualifier = "urlPatterns", type = Accessor.Type.SETTER)
    public void setUrlPatterns(Collection<String> value)
    {
        getPersistenceContext().setPropertyValue("urlPatterns", value);
    }


    @Accessor(qualifier = "validComponentTypes", type = Accessor.Type.SETTER)
    public void setValidComponentTypes(Set<CMSComponentTypeModel> value)
    {
        getPersistenceContext().setPropertyValue("validComponentTypes", value);
    }
}
