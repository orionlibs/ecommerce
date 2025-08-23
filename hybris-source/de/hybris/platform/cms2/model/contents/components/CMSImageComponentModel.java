package de.hybris.platform.cms2.model.contents.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class CMSImageComponentModel extends SimpleCMSComponentModel
{
    public static final String _TYPECODE = "CMSImageComponent";
    public static final String MEDIA = "media";


    public CMSImageComponentModel()
    {
    }


    public CMSImageComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSImageComponentModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSImageComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
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
}
