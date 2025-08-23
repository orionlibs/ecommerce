package de.hybris.platform.cms2lib.components.interceptors;

import de.hybris.platform.cms2lib.model.components.FlashComponentModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.util.localization.Localization;

public class FlashComponentValidator implements ValidateInterceptor
{
    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof FlashComponentModel)
        {
            Integer width = ((FlashComponentModel)model).getWidth();
            Integer height = ((FlashComponentModel)model).getHeight();
            if(width != null && height != null && (width.intValue() < 1 || height.intValue() < 1))
            {
                throw new InterceptorException(Localization.getLocalizedString("flash.banner.width_or_height.negative.value"));
            }
        }
    }
}
