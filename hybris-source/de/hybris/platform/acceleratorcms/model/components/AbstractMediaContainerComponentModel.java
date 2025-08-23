package de.hybris.platform.acceleratorcms.model.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class AbstractMediaContainerComponentModel extends SimpleCMSComponentModel
{
    public static final String _TYPECODE = "AbstractMediaContainerComponent";
    public static final String MEDIA = "media";


    public AbstractMediaContainerComponentModel()
    {
    }


    public AbstractMediaContainerComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractMediaContainerComponentModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractMediaContainerComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "media", type = Accessor.Type.GETTER)
    public MediaContainerModel getMedia()
    {
        return getMedia(null);
    }


    @Accessor(qualifier = "media", type = Accessor.Type.GETTER)
    public MediaContainerModel getMedia(Locale loc)
    {
        return (MediaContainerModel)getPersistenceContext().getLocalizedValue("media", loc);
    }


    @Accessor(qualifier = "media", type = Accessor.Type.SETTER)
    public void setMedia(MediaContainerModel value)
    {
        setMedia(value, null);
    }


    @Accessor(qualifier = "media", type = Accessor.Type.SETTER)
    public void setMedia(MediaContainerModel value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("media", loc, value);
    }
}
