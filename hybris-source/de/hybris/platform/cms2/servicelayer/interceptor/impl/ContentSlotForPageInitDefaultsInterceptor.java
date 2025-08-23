package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

public class ContentSlotForPageInitDefaultsInterceptor implements InitDefaultsInterceptor
{
    public void onInitDefaults(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof ContentSlotForPageModel)
        {
            AbstractPageModel page = ((ContentSlotForPageModel)model).getPage();
            if(((ContentSlotForPageModel)model).getCatalogVersion() == null && page != null)
            {
                ((ContentSlotForPageModel)model).setCatalogVersion(page.getCatalogVersion());
            }
        }
    }
}
