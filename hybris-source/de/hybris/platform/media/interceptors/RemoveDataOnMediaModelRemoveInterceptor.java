package de.hybris.platform.media.interceptors;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.ModelContextUtils;

public class RemoveDataOnMediaModelRemoveInterceptor implements RemoveInterceptor<MediaModel>
{
    public void onRemove(MediaModel media, InterceptorContext ctx) throws InterceptorException
    {
        if(hadDataToRemove(media, ctx))
        {
            PK mediaPK = media.getPk();
            Long dataPK = getPreviousValueForProperty(media, "dataPK");
            MediaFolderModel folder = getPreviousValueForProperty(media, "folder");
            String location = getPreviousValueForProperty(media, "location");
            MediaManager mediaManager = MediaManager.getInstance();
            mediaManager.deleteMediaDataUnlessReferenced(mediaPK, dataPK, folder.getQualifier(), location);
        }
    }


    private boolean hadDataToRemove(MediaModel media, InterceptorContext ctx)
    {
        if(ctx.isModified(media, "internalURL"))
        {
            String prev = getPreviousValueForProperty(media, "internalURL");
            return "replicated273654712".equals(prev);
        }
        return "replicated273654712".equals(media.getInternalURL());
    }


    private <T> T getPreviousValueForProperty(MediaModel media, String propertyName)
    {
        ItemModelContext iCtx = ModelContextUtils.getItemModelContext((AbstractItemModel)media);
        return (T)iCtx.getOriginalValue(propertyName);
    }
}
