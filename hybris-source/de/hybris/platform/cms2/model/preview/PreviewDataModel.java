package de.hybris.platform.cms2.model.preview;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;

public class PreviewDataModel extends ItemModel
{
    public static final String _TYPECODE = "PreviewData";
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
    public static final String UIEXPERIENCE = "uiExperience";


    public PreviewDataModel()
    {
    }


    public PreviewDataModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PreviewDataModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "activeCatalogVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getActiveCatalogVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("activeCatalogVersion");
    }


    @Accessor(qualifier = "activeSite", type = Accessor.Type.GETTER)
    public CMSSiteModel getActiveSite()
    {
        return (CMSSiteModel)getPersistenceContext().getPropertyValue("activeSite");
    }


    @Accessor(qualifier = "catalogVersions", type = Accessor.Type.GETTER)
    public Collection<CatalogVersionModel> getCatalogVersions()
    {
        return (Collection<CatalogVersionModel>)getPersistenceContext().getPropertyValue("catalogVersions");
    }


    @Accessor(qualifier = "country", type = Accessor.Type.GETTER)
    public CountryModel getCountry()
    {
        return (CountryModel)getPersistenceContext().getPropertyValue("country");
    }


    @Accessor(qualifier = "editMode", type = Accessor.Type.GETTER)
    public Boolean getEditMode()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("editMode");
    }


    @Accessor(qualifier = "language", type = Accessor.Type.GETTER)
    public LanguageModel getLanguage()
    {
        return (LanguageModel)getPersistenceContext().getPropertyValue("language");
    }


    @Deprecated(since = "1811", forRemoval = true)
    @Accessor(qualifier = "liveEdit", type = Accessor.Type.GETTER)
    public Boolean getLiveEdit()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("liveEdit");
    }


    @Accessor(qualifier = "page", type = Accessor.Type.GETTER)
    public AbstractPageModel getPage()
    {
        return (AbstractPageModel)getPersistenceContext().getPropertyValue("page");
    }


    @Accessor(qualifier = "previewCatalog", type = Accessor.Type.GETTER)
    public CatalogModel getPreviewCatalog()
    {
        return (CatalogModel)getPersistenceContext().getPropertyValue("previewCatalog");
    }


    @Accessor(qualifier = "previewCategory", type = Accessor.Type.GETTER)
    public CategoryModel getPreviewCategory()
    {
        return (CategoryModel)getPersistenceContext().getPropertyValue("previewCategory");
    }


    @Accessor(qualifier = "previewContentCatalogVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getPreviewContentCatalogVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("previewContentCatalogVersion");
    }


    @Accessor(qualifier = "previewProduct", type = Accessor.Type.GETTER)
    public ProductModel getPreviewProduct()
    {
        return (ProductModel)getPersistenceContext().getPropertyValue("previewProduct");
    }


    @Accessor(qualifier = "resourcePath", type = Accessor.Type.GETTER)
    public String getResourcePath()
    {
        return (String)getPersistenceContext().getPropertyValue("resourcePath");
    }


    @Accessor(qualifier = "time", type = Accessor.Type.GETTER)
    public Date getTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("time");
    }


    @Accessor(qualifier = "uiExperience", type = Accessor.Type.GETTER)
    public UiExperienceLevel getUiExperience()
    {
        return (UiExperienceLevel)getPersistenceContext().getPropertyValue("uiExperience");
    }


    @Accessor(qualifier = "user", type = Accessor.Type.GETTER)
    public UserModel getUser()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("user");
    }


    @Accessor(qualifier = "userGroup", type = Accessor.Type.GETTER)
    public UserGroupModel getUserGroup()
    {
        return (UserGroupModel)getPersistenceContext().getPropertyValue("userGroup");
    }


    @Accessor(qualifier = "version", type = Accessor.Type.GETTER)
    public CMSVersionModel getVersion()
    {
        return (CMSVersionModel)getPersistenceContext().getPropertyValue("version");
    }


    @Accessor(qualifier = "activeCatalogVersion", type = Accessor.Type.SETTER)
    public void setActiveCatalogVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("activeCatalogVersion", value);
    }


    @Accessor(qualifier = "activeSite", type = Accessor.Type.SETTER)
    public void setActiveSite(CMSSiteModel value)
    {
        getPersistenceContext().setPropertyValue("activeSite", value);
    }


    @Accessor(qualifier = "catalogVersions", type = Accessor.Type.SETTER)
    public void setCatalogVersions(Collection<CatalogVersionModel> value)
    {
        getPersistenceContext().setPropertyValue("catalogVersions", value);
    }


    @Accessor(qualifier = "country", type = Accessor.Type.SETTER)
    public void setCountry(CountryModel value)
    {
        getPersistenceContext().setPropertyValue("country", value);
    }


    @Accessor(qualifier = "editMode", type = Accessor.Type.SETTER)
    public void setEditMode(Boolean value)
    {
        getPersistenceContext().setPropertyValue("editMode", value);
    }


    @Accessor(qualifier = "language", type = Accessor.Type.SETTER)
    public void setLanguage(LanguageModel value)
    {
        getPersistenceContext().setPropertyValue("language", value);
    }


    @Deprecated(since = "1811", forRemoval = true)
    @Accessor(qualifier = "liveEdit", type = Accessor.Type.SETTER)
    public void setLiveEdit(Boolean value)
    {
        getPersistenceContext().setPropertyValue("liveEdit", value);
    }


    @Accessor(qualifier = "page", type = Accessor.Type.SETTER)
    public void setPage(AbstractPageModel value)
    {
        getPersistenceContext().setPropertyValue("page", value);
    }


    @Accessor(qualifier = "previewCatalog", type = Accessor.Type.SETTER)
    public void setPreviewCatalog(CatalogModel value)
    {
        getPersistenceContext().setPropertyValue("previewCatalog", value);
    }


    @Accessor(qualifier = "previewCategory", type = Accessor.Type.SETTER)
    public void setPreviewCategory(CategoryModel value)
    {
        getPersistenceContext().setPropertyValue("previewCategory", value);
    }


    @Accessor(qualifier = "previewContentCatalogVersion", type = Accessor.Type.SETTER)
    public void setPreviewContentCatalogVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("previewContentCatalogVersion", value);
    }


    @Accessor(qualifier = "previewProduct", type = Accessor.Type.SETTER)
    public void setPreviewProduct(ProductModel value)
    {
        getPersistenceContext().setPropertyValue("previewProduct", value);
    }


    @Accessor(qualifier = "resourcePath", type = Accessor.Type.SETTER)
    public void setResourcePath(String value)
    {
        getPersistenceContext().setPropertyValue("resourcePath", value);
    }


    @Accessor(qualifier = "time", type = Accessor.Type.SETTER)
    public void setTime(Date value)
    {
        getPersistenceContext().setPropertyValue("time", value);
    }


    @Accessor(qualifier = "uiExperience", type = Accessor.Type.SETTER)
    public void setUiExperience(UiExperienceLevel value)
    {
        getPersistenceContext().setPropertyValue("uiExperience", value);
    }


    @Accessor(qualifier = "user", type = Accessor.Type.SETTER)
    public void setUser(UserModel value)
    {
        getPersistenceContext().setPropertyValue("user", value);
    }


    @Accessor(qualifier = "userGroup", type = Accessor.Type.SETTER)
    public void setUserGroup(UserGroupModel value)
    {
        getPersistenceContext().setPropertyValue("userGroup", value);
    }


    @Accessor(qualifier = "version", type = Accessor.Type.SETTER)
    public void setVersion(CMSVersionModel value)
    {
        getPersistenceContext().setPropertyValue("version", value);
    }
}
