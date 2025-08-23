package de.hybris.platform.core.model.media;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

public class MediaContainerModel extends ItemModel
{
    public static final String _TYPECODE = "MediaContainer";
    public static final String QUALIFIER = "qualifier";
    public static final String NAME = "name";
    public static final String MEDIAS = "medias";
    public static final String CATALOGVERSION = "catalogVersion";


    public MediaContainerModel()
    {
    }


    public MediaContainerModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MediaContainerModel(CatalogVersionModel _catalogVersion, String _qualifier)
    {
        setCatalogVersion(_catalogVersion);
        setQualifier(_qualifier);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public MediaContainerModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _qualifier)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setQualifier(_qualifier);
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getCatalogVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("catalogVersion");
    }


    @Accessor(qualifier = "medias", type = Accessor.Type.GETTER)
    public Collection<MediaModel> getMedias()
    {
        return (Collection<MediaModel>)getPersistenceContext().getPropertyValue("medias");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
    public String getQualifier()
    {
        return (String)getPersistenceContext().getPropertyValue("qualifier");
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
    public void setCatalogVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("catalogVersion", value);
    }


    @Accessor(qualifier = "medias", type = Accessor.Type.SETTER)
    public void setMedias(Collection<MediaModel> value)
    {
        getPersistenceContext().setPropertyValue("medias", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
    public void setQualifier(String value)
    {
        getPersistenceContext().setPropertyValue("qualifier", value);
    }
}
