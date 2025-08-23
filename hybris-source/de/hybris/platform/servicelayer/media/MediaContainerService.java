package de.hybris.platform.servicelayer.media;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaContextModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.util.List;

public interface MediaContainerService
{
    MediaModel getMediaForFormat(MediaContainerModel paramMediaContainerModel, MediaFormatModel paramMediaFormatModel) throws ModelNotFoundException;


    void addMediaToContainer(MediaContainerModel paramMediaContainerModel, List<MediaModel> paramList) throws IllegalArgumentException;


    void removeMediaFromContainer(MediaContainerModel paramMediaContainerModel, List<MediaModel> paramList) throws IllegalArgumentException;


    MediaFormatModel getMediaFormatForSourceFormat(MediaContextModel paramMediaContextModel, MediaFormatModel paramMediaFormatModel) throws IllegalArgumentException, ModelNotFoundException;


    MediaContainerModel getMediaContainerForQualifier(String paramString) throws UnknownIdentifierException, AmbiguousIdentifierException;


    MediaContextModel getMediaContextForQualifier(String paramString) throws UnknownIdentifierException, AmbiguousIdentifierException;
}
