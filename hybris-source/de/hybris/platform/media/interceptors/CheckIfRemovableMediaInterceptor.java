package de.hybris.platform.media.interceptors;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import org.apache.commons.lang3.BooleanUtils;

public class CheckIfRemovableMediaInterceptor implements RemoveInterceptor<MediaModel>
{
    public void onRemove(MediaModel mediaModel, InterceptorContext ctx) throws InterceptorException
    {
        if(BooleanUtils.isFalse(mediaModel.getRemovable()))
        {
            throw new InterceptorException("Could not remove data because it is not removable at the moment");
        }
    }
}
