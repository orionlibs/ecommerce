package de.hybris.platform.servicelayer.interceptor.impl;

import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import org.apache.commons.lang.StringUtils;

public class LinkPrepareInterceptor implements PrepareInterceptor
{
    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof LinkModel && ctx.isNew(model))
        {
            LinkModel link = (LinkModel)model;
            String type = link.getItemtype();
            if(StringUtils.isNotBlank(type) && !"Link".equalsIgnoreCase(type))
            {
                link.setQualifier(type);
            }
        }
    }
}
