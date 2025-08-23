package de.hybris.platform.servicelayer.interceptor.impl;

import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class JaloInitDefaultsInterceptor implements InitDefaultsInterceptor, PrepareInterceptor
{
    private static final Logger LOG = Logger.getLogger(JaloInitDefaultsInterceptor.class.getName());
    private TypeService typeService;
    private SessionService sessionService;
    private CommonI18NService commonI18NService;


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        loadDefaults(model, ctx);
    }


    public void onInitDefaults(Object model, InterceptorContext ctx) throws InterceptorException
    {
        loadDefaults(model, ctx);
    }


    private void loadDefaults(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model == null)
        {
            throw new InterceptorException("Given model is null");
        }
        if(ctx.isNew(model))
        {
            this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, ctx, model));
        }
    }
}
