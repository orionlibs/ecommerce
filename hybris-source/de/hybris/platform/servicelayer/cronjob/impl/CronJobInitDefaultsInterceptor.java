package de.hybris.platform.servicelayer.cronjob.impl;

import de.hybris.platform.commons.jalo.renderer.RendererTemplate;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.cronjob.jalo.CronJobManager;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.user.UserService;
import org.springframework.beans.factory.annotation.Required;

public class CronJobInitDefaultsInterceptor implements InitDefaultsInterceptor
{
    private UserService userService;
    private CommonI18NService commonI18NService;


    public void onInitDefaults(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof CronJobModel)
        {
            CronJobModel cronJobModel = (CronJobModel)model;
            cronJobModel.setSessionUser(this.userService.getCurrentUser());
            cronJobModel.setSessionCurrency(this.commonI18NService.getCurrentCurrency());
            cronJobModel.setSessionLanguage(this.commonI18NService.getCurrentLanguage());
            RendererTemplate template = CronJobManager.getInstance().getDefaultCronJobFinishNotificationTemplate();
            if(template != null)
            {
                cronJobModel.setEmailNotificationTemplate((RendererTemplateModel)ctx.getModelService().get(template));
            }
        }
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }
}
