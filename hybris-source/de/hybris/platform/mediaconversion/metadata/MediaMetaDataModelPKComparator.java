package de.hybris.platform.mediaconversion.metadata;

import de.hybris.platform.mediaconversion.model.MediaMetaDataModel;
import java.io.Serializable;
import java.util.Comparator;

final class MediaMetaDataModelPKComparator implements Comparator<MediaMetaDataModel>, Serializable
{
    static final Comparator<MediaMetaDataModel> INSTANCE = new MediaMetaDataModelPKComparator();


    public int compare(MediaMetaDataModel one, MediaMetaDataModel other)
    {
        return one.getPk().compareTo(other.getPk());
    }
}
