package de.hybris.platform.core.model.media;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class DerivedMediaModel extends AbstractMediaModel
{
    public static final String _TYPECODE = "DerivedMedia";
    public static final String _MEDIA2DERIVEDMEDIAREL = "Media2DerivedMediaRel";
    public static final String VERSION = "version";
    public static final String MEDIA = "media";


    public DerivedMediaModel()
    {
    }


    public DerivedMediaModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DerivedMediaModel(MediaModel _media, String _version)
    {
        setMedia(_media);
        setVersion(_version);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DerivedMediaModel(MediaModel _media, ItemModel _owner, String _version)
    {
        setMedia(_media);
        setOwner(_owner);
        setVersion(_version);
    }


    @Accessor(qualifier = "media", type = Accessor.Type.GETTER)
    public MediaModel getMedia()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("media");
    }


    @Accessor(qualifier = "version", type = Accessor.Type.GETTER)
    public String getVersion()
    {
        return (String)getPersistenceContext().getPropertyValue("version");
    }


    @Accessor(qualifier = "media", type = Accessor.Type.SETTER)
    public void setMedia(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("media", value);
    }


    @Accessor(qualifier = "version", type = Accessor.Type.SETTER)
    public void setVersion(String value)
    {
        getPersistenceContext().setPropertyValue("version", value);
    }
}
