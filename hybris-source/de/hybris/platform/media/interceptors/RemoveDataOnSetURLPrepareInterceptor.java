package de.hybris.platform.media.interceptors;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.ModelContextUtils;

public class RemoveDataOnSetURLPrepareInterceptor implements PrepareInterceptor<MediaModel>
{
    public void onPrepare(MediaModel media, InterceptorContext ctx) throws InterceptorException
    {
        if(!ctx.isNew(media) && ctx.isModified(media, "internalURL"))
        {
            if(hadDataBeforeChangingURL(media, ctx))
            {
                removePreviousData(media);
            }
        }
    }


    private boolean hadDataBeforeChangingURL(MediaModel media, InterceptorContext ctx)
    {
        if(ctx.isModified(media, "internalURL"))
        {
            String prev = getPreviousValueForProperty(media, "internalURL");
            return "replicated273654712".equals(prev);
        }
        return "replicated273654712".equals(media.getInternalURL());
    }


    private void removePreviousData(MediaModel media)
    {
        PK mediaPK = media.getPk();
        Long dataPK = getPreviousValueForProperty(media, "dataPK");
        MediaFolderModel folder = getPreviousValueForProperty(media, "folder");
        String location = getPreviousValueForProperty(media, "location");
        MediaManager mediaManager = MediaManager.getInstance();
        mediaManager.deleteMediaDataUnlessReferenced(mediaPK, dataPK, folder.getQualifier(), location);
    }


    private <T> T getPreviousValueForProperty(MediaModel media, String propertyName)
    {
        ItemModelContext iCtx = ModelContextUtils.getItemModelContext((AbstractItemModel)media);
        return (T)iCtx.getOriginalValue(propertyName);
    }
}
