package de.hybris.platform.servicelayer.media.impl;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaContextModel;
import de.hybris.platform.core.model.media.MediaFormatMappingModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.media.MediaContainerService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.media.dao.MediaContainerDao;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultMediaContainerService extends AbstractBusinessService implements MediaContainerService
{
    private static final Logger LOG = Logger.getLogger(DefaultMediaContainerService.class);
    private MediaContainerDao mediaContainerDao;
    private MediaService mediaService;


    public MediaModel getMediaForFormat(MediaContainerModel mediaContainerModel, MediaFormatModel mediaFormatModel)
    {
        ServicesUtil.validateParameterNotNull(mediaContainerModel, "Media container model cannot be null");
        ServicesUtil.validateParameterNotNull(mediaFormatModel, "Media format model cannot be null");
        return getMediaService().getMediaByFormat(mediaContainerModel, mediaFormatModel);
    }


    public void addMediaToContainer(MediaContainerModel mediaContainerModel, List<MediaModel> mediaModels)
    {
        ServicesUtil.validateParameterNotNull(mediaContainerModel, "Media container model cannot be null");
        ServicesUtil.validateParameterNotNull(mediaModels, "Media models cannot be null");
        Set<MediaModel> mySet = new TreeSet<>((Comparator<? super MediaModel>)new Object(this));
        mySet.addAll(mediaContainerModel.getMedias());
        mySet.addAll(CollectionUtils.select(mediaModels, (Predicate)new Object(this)));
        mediaContainerModel.setMedias(mySet);
        getModelService().save(mediaContainerModel);
    }


    public void removeMediaFromContainer(MediaContainerModel mediaContainerModel, List<MediaModel> mediaModels)
    {
        ServicesUtil.validateParameterNotNull(mediaContainerModel, "Media container model cannot be null");
        ServicesUtil.validateParameterNotNull(mediaModels, "Media models cannot be null");
        Collection<MediaModel> existentMedias = new ArrayList<>(mediaContainerModel.getMedias());
        existentMedias.removeAll(mediaModels);
        mediaContainerModel.setMedias(existentMedias);
        getModelService().save(mediaContainerModel);
    }


    public MediaFormatModel getMediaFormatForSourceFormat(MediaContextModel contextModel, MediaFormatModel sourceFormatModel)
    {
        ServicesUtil.validateParameterNotNull(contextModel, "Media context model cannot be null");
        ServicesUtil.validateParameterNotNull(sourceFormatModel, "Media source format model cannot be null");
        for(MediaFormatMappingModel mapping : contextModel.getMappings())
        {
            if(sourceFormatModel.equals(mapping.getSource()))
            {
                return mapping.getTarget();
            }
        }
        throw new ModelNotFoundException("No media format model for given context " + contextModel.getQualifier() + " and format " + sourceFormatModel
                        .getQualifier() + "was found");
    }


    public MediaContextModel getMediaContextForQualifier(String qualifier)
    {
        ServicesUtil.validateParameterNotNull(qualifier, "Media context qualifier cannot be null");
        List<MediaContextModel> result = this.mediaContainerDao.findMediaContextByQualifier(qualifier);
        ServicesUtil.validateIfSingleResult(result, "No media context with qualifier " + qualifier + " can be found.", "More than one media context with qualifier " + qualifier + " found.");
        return result.iterator().next();
    }


    public MediaContainerModel getMediaContainerForQualifier(String qualifier)
    {
        ServicesUtil.validateParameterNotNull(qualifier, "Media container qualifier cannot be null");
        List<MediaContainerModel> result = this.mediaContainerDao.findMediaContainersByQualifier(qualifier);
        ServicesUtil.validateIfSingleResult(result, "No media container with qualifier " + qualifier + " can be found.", "More than one media container with qualifier " + qualifier + " found.");
        return result.iterator().next();
    }


    @Deprecated(since = "4.4", forRemoval = true)
    protected MediaContainerDao getMediaContainerDao()
    {
        return this.mediaContainerDao;
    }


    @Required
    public void setMediaContainerDao(MediaContainerDao mediaContainerDao)
    {
        this.mediaContainerDao = mediaContainerDao;
    }


    public MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }
}
