package de.hybris.platform.servicelayer.cronjob.impl;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import org.springframework.beans.factory.annotation.Required;

public class PrepareCronJobInterceptor implements PrepareInterceptor
{
    private KeyGenerator keyGenerator;


    public void onPrepare(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof CronJobModel)
        {
            CronJobModel cronJobModel = (CronJobModel)model;
            if(cronJobModel.getCode() == null)
            {
                cronJobModel.setCode((String)this.keyGenerator.generate());
            }
        }
    }


    @Required
    public void setKeyGenerator(KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
    }
}
