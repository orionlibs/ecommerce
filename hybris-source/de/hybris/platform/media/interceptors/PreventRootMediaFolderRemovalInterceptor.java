package de.hybris.platform.media.interceptors;

import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;

public class PreventRootMediaFolderRemovalInterceptor implements RemoveInterceptor<MediaFolderModel>
{
    private final MediaRootFolderRemovalLogic mediaRootFolderRemovalLogic = new MediaRootFolderRemovalLogic();


    public void onRemove(MediaFolderModel mediaFolder, InterceptorContext ctx) throws InterceptorException
    {
        if(!ctx.isNew(mediaFolder) && this.mediaRootFolderRemovalLogic
                        .isRootMediaFolder(mediaFolder.getQualifier()) &&
                        !this.mediaRootFolderRemovalLogic.canRemoveRootMediaFolder())
        {
            throw new InterceptorException(this.mediaRootFolderRemovalLogic.getErrorMessage());
        }
    }
}
