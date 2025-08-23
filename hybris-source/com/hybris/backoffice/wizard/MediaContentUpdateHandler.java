/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.wizard;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectDeletionException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.editor.defaultfileupload.FileUploadResult;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class MediaContentUpdateHandler implements FlowActionHandler
{
    public static final String MEDIA_CONTENT_PROPERTY = "mediaContentProperty";
    public static final String MEDIA_PROPERTY = "mediaProperty";
    private static final Logger LOG = LoggerFactory.getLogger(MediaContentUpdateHandler.class);
    private MediaService mediaService;
    private ModelService modelService;
    private ObjectFacade objectFacade;
    private NotificationService notificationService;


    @Override
    public void perform(final CustomType customType, final FlowActionHandlerAdapter adapter, final Map<String, String> map)
    {
        final FileUploadResult mediaContent = getMediaContent(adapter, map);
        final MediaModel mediaToUpdate = getMediaToUpdate(adapter, map);
        tryToSave(adapter, map, mediaContent, mediaToUpdate);
        adapter.done();
    }


    protected void tryToSave(final FlowActionHandlerAdapter adapter, final Map<String, String> map,
                    final FileUploadResult mediaContent, final MediaModel mediaToUpdate)
    {
        try
        {
            save(mediaToUpdate, mediaContent);
            notifyAboutSuccess(mediaToUpdate);
        }
        catch(final ObjectSavingException | RuntimeException e)
        {
            LOG.error("Cannot save media", e);
            rollback(adapter, map, mediaToUpdate);
            throw new ModelSavingException(e.getMessage(), e);
        }
    }


    protected void save(final MediaModel mediaToUpdate, final FileUploadResult mediaContent) throws ObjectSavingException
    {
        final boolean update = !modelService.isNew(mediaToUpdate);
        objectFacade.save(mediaToUpdate);
        if(mediaContent == null)
        {
            if(update)
            {
                mediaService.removeDataFromMedia(mediaToUpdate);
            }
        }
        else
        {
            mediaToUpdate.setRealFileName(mediaContent.getName());
            mediaToUpdate.setMime(mediaContent.getContentType());
            mediaService.setDataForMedia(mediaToUpdate, mediaContent.getData());
        }
    }


    protected void rollback(final FlowActionHandlerAdapter adapter, final Map<String, String> params,
                    final MediaModel mediaToUpdate)
    {
        try
        {
            setMediaToUpdate(adapter, params, modelService.clone(mediaToUpdate));
            objectFacade.delete(mediaToUpdate);
        }
        catch(final ObjectDeletionException e)
        {
            LOG.trace("Cannot remove or clone media", e);
        }
    }


    protected void notifyAboutSuccess(final MediaModel model)
    {
        notificationService.notifyUser("notification-area", NotificationEventTypes.EVENT_TYPE_OBJECT_CREATION,
                        NotificationEvent.Level.SUCCESS, model);
    }


    protected MediaModel getMediaToUpdate(final FlowActionHandlerAdapter adapter, final Map<String, String> params)
    {
        final String mediaProperty = params.get(MEDIA_PROPERTY);
        if(StringUtils.isNotEmpty(mediaProperty))
        {
            return adapter.getWidgetInstanceManager().getModel().getValue(mediaProperty, MediaModel.class);
        }
        else
        {
            LOG.warn("Missing {} param which specifies media to update", MEDIA_PROPERTY);
            return null;
        }
    }


    protected void setMediaToUpdate(final FlowActionHandlerAdapter adapter, final Map<String, String> params,
                    final MediaModel mediaModel)
    {
        final String mediaProperty = params.get(MEDIA_PROPERTY);
        if(StringUtils.isNotEmpty(mediaProperty))
        {
            adapter.getWidgetInstanceManager().getModel().setValue(mediaProperty, mediaModel);
        }
        else
        {
            LOG.warn("Missing {} param which specifies media to update", MEDIA_PROPERTY);
        }
    }


    protected FileUploadResult getMediaContent(final FlowActionHandlerAdapter adapter, final Map<String, String> params)
    {
        final String mediaProperty = params.get(MEDIA_CONTENT_PROPERTY);
        if(StringUtils.isNotEmpty(mediaProperty))
        {
            return adapter.getWidgetInstanceManager().getModel().getValue(mediaProperty, FileUploadResult.class);
        }
        else
        {
            LOG.warn("Missing {} param which specifies media content", MEDIA_CONTENT_PROPERTY);
            return null;
        }
    }


    public MediaService getMediaService()
    {
        return mediaService;
    }


    @Required
    public void setMediaService(final MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    public ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    @Required
    public void setObjectFacade(final ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    public NotificationService getNotificationService()
    {
        return notificationService;
    }


    @Required
    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    public ModelService getModelService()
    {
        return modelService;
    }


    @Required
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }
}

