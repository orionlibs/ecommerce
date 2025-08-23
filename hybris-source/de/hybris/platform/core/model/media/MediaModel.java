package de.hybris.platform.core.model.media;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class MediaModel extends AbstractMediaModel
{
    public static final String _TYPECODE = "Media";
    public static final String _MEDIACONTAINER2MEDIAREL = "MediaContainer2MediaRel";
    public static final String _CATEGORYMEDIARELATION = "CategoryMediaRelation";
    public static final String CODE = "code";
    public static final String INTERNALURL = "internalURL";
    public static final String URL = "URL";
    public static final String URL2 = "URL2";
    public static final String DOWNLOADURL = "downloadURL";
    public static final String DESCRIPTION = "description";
    public static final String ALTTEXT = "altText";
    public static final String REMOVABLE = "removable";
    public static final String MEDIAFORMAT = "mediaFormat";
    public static final String FOLDER = "folder";
    public static final String SUBFOLDERPATH = "subFolderPath";
    public static final String FOREIGNDATAOWNERS = "foreignDataOwners";
    public static final String PERMITTEDPRINCIPALS = "permittedPrincipals";
    public static final String DENIEDPRINCIPALS = "deniedPrincipals";
    public static final String MEDIACONTAINER = "mediaContainer";
    public static final String DERIVEDMEDIAS = "derivedMedias";
    public static final String CATALOG = "catalog";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String SUPERCATEGORIES = "supercategories";


    public MediaModel()
    {
    }


    public MediaModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MediaModel(CatalogVersionModel _catalogVersion, String _code)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MediaModel(CatalogVersionModel _catalogVersion, String _code, ItemModel _owner)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setOwner(_owner);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public String getAlttext()
    {
        return getAltText();
    }


    @Accessor(qualifier = "altText", type = Accessor.Type.GETTER)
    public String getAltText()
    {
        return (String)getPersistenceContext().getPropertyValue("altText");
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getCatalogVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("catalogVersion");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "deniedPrincipals", type = Accessor.Type.GETTER)
    public Collection<PrincipalModel> getDeniedPrincipals()
    {
        return (Collection<PrincipalModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "deniedPrincipals");
    }


    @Accessor(qualifier = "derivedMedias", type = Accessor.Type.GETTER)
    public Collection<DerivedMediaModel> getDerivedMedias()
    {
        return (Collection<DerivedMediaModel>)getPersistenceContext().getPropertyValue("derivedMedias");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return (String)getPersistenceContext().getPropertyValue("description");
    }


    @Deprecated(since = "ages", forRemoval = true)
    public String getDownloadurl()
    {
        return getDownloadURL();
    }


    @Accessor(qualifier = "downloadURL", type = Accessor.Type.GETTER)
    public String getDownloadURL()
    {
        return (String)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "downloadURL");
    }


    @Accessor(qualifier = "folder", type = Accessor.Type.GETTER)
    public MediaFolderModel getFolder()
    {
        return (MediaFolderModel)getPersistenceContext().getPropertyValue("folder");
    }


    @Accessor(qualifier = "foreignDataOwners", type = Accessor.Type.GETTER)
    public Collection<MediaModel> getForeignDataOwners()
    {
        return (Collection<MediaModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "foreignDataOwners");
    }


    @Accessor(qualifier = "internalURL", type = Accessor.Type.GETTER)
    public String getInternalURL()
    {
        return (String)getPersistenceContext().getPropertyValue("internalURL");
    }


    @Accessor(qualifier = "mediaContainer", type = Accessor.Type.GETTER)
    public MediaContainerModel getMediaContainer()
    {
        return (MediaContainerModel)getPersistenceContext().getPropertyValue("mediaContainer");
    }


    @Accessor(qualifier = "mediaFormat", type = Accessor.Type.GETTER)
    public MediaFormatModel getMediaFormat()
    {
        return (MediaFormatModel)getPersistenceContext().getPropertyValue("mediaFormat");
    }


    @Accessor(qualifier = "permittedPrincipals", type = Accessor.Type.GETTER)
    public Collection<PrincipalModel> getPermittedPrincipals()
    {
        return (Collection<PrincipalModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "permittedPrincipals");
    }


    @Accessor(qualifier = "removable", type = Accessor.Type.GETTER)
    public Boolean getRemovable()
    {
        Boolean value = (Boolean)getPersistenceContext().getPropertyValue("removable");
        return (value != null) ? value : Boolean.valueOf(true);
    }


    @Accessor(qualifier = "subFolderPath", type = Accessor.Type.GETTER)
    public String getSubFolderPath()
    {
        return (String)getPersistenceContext().getPropertyValue("subFolderPath");
    }


    @Accessor(qualifier = "supercategories", type = Accessor.Type.GETTER)
    public Collection<CategoryModel> getSupercategories()
    {
        return (Collection<CategoryModel>)getPersistenceContext().getPropertyValue("supercategories");
    }


    @Deprecated(since = "ages", forRemoval = true)
    public String getUrl()
    {
        return getURL();
    }


    @Accessor(qualifier = "URL", type = Accessor.Type.GETTER)
    public String getURL()
    {
        return (String)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "URL");
    }


    @Deprecated(since = "ages", forRemoval = true)
    public String getUrl2()
    {
        return getURL2();
    }


    @Accessor(qualifier = "URL2", type = Accessor.Type.GETTER)
    public String getURL2()
    {
        return (String)getPersistenceContext().getPropertyValue("URL2");
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void setAlttext(String value)
    {
        setAltText(value);
    }


    @Accessor(qualifier = "altText", type = Accessor.Type.SETTER)
    public void setAltText(String value)
    {
        getPersistenceContext().setPropertyValue("altText", value);
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
    public void setCatalogVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("catalogVersion", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "deniedPrincipals", type = Accessor.Type.SETTER)
    public void setDeniedPrincipals(Collection<PrincipalModel> value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "deniedPrincipals", value);
    }


    @Accessor(qualifier = "derivedMedias", type = Accessor.Type.SETTER)
    public void setDerivedMedias(Collection<DerivedMediaModel> value)
    {
        getPersistenceContext().setPropertyValue("derivedMedias", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        getPersistenceContext().setPropertyValue("description", value);
    }


    @Accessor(qualifier = "folder", type = Accessor.Type.SETTER)
    public void setFolder(MediaFolderModel value)
    {
        getPersistenceContext().setPropertyValue("folder", value);
    }


    @Accessor(qualifier = "internalURL", type = Accessor.Type.SETTER)
    public void setInternalURL(String value)
    {
        getPersistenceContext().setPropertyValue("internalURL", value);
    }


    @Accessor(qualifier = "mediaContainer", type = Accessor.Type.SETTER)
    public void setMediaContainer(MediaContainerModel value)
    {
        getPersistenceContext().setPropertyValue("mediaContainer", value);
    }


    @Accessor(qualifier = "mediaFormat", type = Accessor.Type.SETTER)
    public void setMediaFormat(MediaFormatModel value)
    {
        getPersistenceContext().setPropertyValue("mediaFormat", value);
    }


    @Accessor(qualifier = "permittedPrincipals", type = Accessor.Type.SETTER)
    public void setPermittedPrincipals(Collection<PrincipalModel> value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "permittedPrincipals", value);
    }


    @Accessor(qualifier = "removable", type = Accessor.Type.SETTER)
    public void setRemovable(Boolean value)
    {
        getPersistenceContext().setPropertyValue("removable", value);
    }


    @Accessor(qualifier = "subFolderPath", type = Accessor.Type.SETTER)
    public void setSubFolderPath(String value)
    {
        getPersistenceContext().setPropertyValue("subFolderPath", value);
    }


    @Accessor(qualifier = "supercategories", type = Accessor.Type.SETTER)
    public void setSupercategories(Collection<CategoryModel> value)
    {
        getPersistenceContext().setPropertyValue("supercategories", value);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void setUrl(String value)
    {
        setURL(value);
    }


    @Accessor(qualifier = "URL", type = Accessor.Type.SETTER)
    public void setURL(String value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "URL", value);
    }
}
