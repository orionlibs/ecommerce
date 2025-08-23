package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForTemplateModel;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

public class ContentSlotForTemplateInitDefaultsInterceptor implements InitDefaultsInterceptor
{
    public void onInitDefaults(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof ContentSlotForTemplateModel)
        {
            PageTemplateModel pageTemplate = ((ContentSlotForTemplateModel)model).getPageTemplate();
            if(((ContentSlotForTemplateModel)model).getCatalogVersion() == null && pageTemplate != null)
            {
                ((ContentSlotForTemplateModel)model).setCatalogVersion(pageTemplate.getCatalogVersion());
            }
        }
    }
}
