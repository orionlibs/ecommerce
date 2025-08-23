package de.hybris.platform.cms2lib.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class AbstractBannerComponentModel extends SimpleCMSComponentModel
{
    public static final String _TYPECODE = "AbstractBannerComponent";
    public static final String MEDIA = "media";
    public static final String URLLINK = "urlLink";
    public static final String EXTERNAL = "external";


    public AbstractBannerComponentModel()
    {
    }


    public AbstractBannerComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractBannerComponentModel(CatalogVersionModel _catalogVersion, boolean _external, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setExternal(_external);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractBannerComponentModel(CatalogVersionModel _catalogVersion, boolean _external, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setExternal(_external);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "media", type = Accessor.Type.GETTER)
    public MediaModel getMedia()
    {
        return getMedia(null);
    }


    @Accessor(qualifier = "media", type = Accessor.Type.GETTER)
    public MediaModel getMedia(Locale loc)
    {
        return (MediaModel)getPersistenceContext().getLocalizedValue("media", loc);
    }


    @Accessor(qualifier = "urlLink", type = Accessor.Type.GETTER)
    public String getUrlLink()
    {
        return (String)getPersistenceContext().getPropertyValue("urlLink");
    }


    @Accessor(qualifier = "external", type = Accessor.Type.GETTER)
    public boolean isExternal()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("external"));
    }


    @Accessor(qualifier = "external", type = Accessor.Type.SETTER)
    public void setExternal(boolean value)
    {
        getPersistenceContext().setPropertyValue("external", toObject(value));
    }


    @Accessor(qualifier = "media", type = Accessor.Type.SETTER)
    public void setMedia(MediaModel value)
    {
        setMedia(value, null);
    }


    @Accessor(qualifier = "media", type = Accessor.Type.SETTER)
    public void setMedia(MediaModel value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("media", loc, value);
    }


    @Accessor(qualifier = "urlLink", type = Accessor.Type.SETTER)
    public void setUrlLink(String value)
    {
        getPersistenceContext().setPropertyValue("urlLink", value);
    }
}
