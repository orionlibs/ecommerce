package de.hybris.platform.mediaconversion.model.attributes;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.model.MediaMetaDataModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import java.util.Collection;
import java.util.Collections;

public class MediaContainerMetaDataAttributeHandler implements DynamicAttributeHandler<Collection<MediaMetaDataModel>, MediaContainerModel>
{
    public Collection<MediaMetaDataModel> get(MediaContainerModel model)
    {
        MediaModel master = model.getMaster();
        return (master == null) ? Collections.<MediaMetaDataModel>emptyList() : master.getMetaData();
    }


    public void set(MediaContainerModel model, Collection<MediaMetaDataModel> value)
    {
        throw new UnsupportedOperationException("Cannot set metadata on container.");
    }
}
