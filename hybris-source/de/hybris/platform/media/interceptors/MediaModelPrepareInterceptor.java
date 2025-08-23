package de.hybris.platform.media.interceptors;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.media.MediaService;
import org.springframework.beans.factory.annotation.Required;

public class MediaModelPrepareInterceptor implements PrepareInterceptor<MediaModel>
{
    private MediaService mediaService;


    public void onPrepare(MediaModel mediaModel, InterceptorContext ctx) throws InterceptorException
    {
        if(ctx.isModified(mediaModel, "folder") && mediaModel.getFolder() == null)
        {
            mediaModel.setFolder(this.mediaService.getRootFolder());
        }
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }
}
