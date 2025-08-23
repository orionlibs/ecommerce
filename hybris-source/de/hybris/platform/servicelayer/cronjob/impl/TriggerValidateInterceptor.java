package de.hybris.platform.servicelayer.cronjob.impl;

import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.servicelayer.cronjob.TriggerService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Required;

public class TriggerValidateInterceptor implements ValidateInterceptor
{
    private static final Logger LOG = Logger.getLogger(TriggerValidateInterceptor.class);
    protected TriggerService triggerService;


    @Required
    public void setTriggerService(TriggerService triggerService)
    {
        this.triggerService = triggerService;
    }


    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(" @@ TriggerValidateInterceptor.onValidate() : " + model + ", " + ctx);
        }
        if(model instanceof TriggerModel)
        {
            TriggerModel triggerModel = (TriggerModel)model;
            if(!StringUtils.isEmpty(triggerModel.getCronExpression()))
            {
                try
                {
                    CronExpression expression = new CronExpression(triggerModel.getCronExpression());
                    if(ctx.isModified(triggerModel, "cronExpression") || triggerModel
                                    .getActivationTime() == null ||
                                    getCurrentTime().after(triggerModel.getActivationTime()))
                    {
                        triggerModel.setActivationTime(expression.getNextValidTimeAfter(getCurrentTime()));
                    }
                }
                catch(ParseException e)
                {
                    throw new InterceptorException("Provided cronExpression '" + triggerModel
                                    .getCronExpression() + "' was invalid " + e
                                    .getMessage(), e);
                }
            }
            if(ctx.isModified(triggerModel, "maxAcceptableDelay"))
            {
                Integer innerValue = triggerModel.getMaxAcceptableDelay();
                if(innerValue == null)
                {
                    innerValue = Integer.valueOf(-1);
                }
                if(validDelay(innerValue.intValue()))
                {
                    triggerModel.setMaxAcceptableDelay(innerValue);
                }
            }
        }
    }


    protected Date getCurrentTime()
    {
        return new Date(System.currentTimeMillis());
    }


    private boolean validDelay(int delay)
    {
        if(delay == -1)
        {
            return true;
        }
        if(delay < this.triggerService.getPulseSeconds())
        {
            if(LOG.isInfoEnabled())
            {
                LOG.debug("Trying to set a maxAcceptableDelayS of " + delay + " seconds but regular pulse is " + this.triggerService
                                .getPulseSeconds() + " s.");
            }
            return false;
        }
        return true;
    }
}
