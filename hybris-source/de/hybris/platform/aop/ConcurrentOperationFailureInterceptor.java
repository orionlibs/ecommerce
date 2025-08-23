package de.hybris.platform.aop;

import de.hybris.platform.directpersistence.annotation.RetryConcurrentModification;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Required;

public class ConcurrentOperationFailureInterceptor
{
    private static final Logger LOG = Logger.getLogger(ConcurrentOperationFailureInterceptor.class);
    private int maxRetries;
    private ModelService modelService;


    public Object performOperation(ProceedingJoinPoint joinPoint, RetryConcurrentModification retryConcurrentModification) throws Throwable
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Entering pointcut " + Thread.currentThread());
        }
        int retries = retryConcurrentModification.retries();
        if(retries <= 0)
        {
            retries = this.maxRetries;
        }
        int numAttempts = 0;
        while(true)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("pointcut attempt " + Thread.currentThread() + " => " + numAttempts);
            }
            numAttempts++;
            try
            {
                return joinPoint.proceed();
            }
            catch(Throwable e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Exception throwed => " + e.getMessage());
                }
                if(e instanceof de.hybris.platform.servicelayer.exceptions.ModelSavingException && !isRegisteredException(e.getCause(), retryConcurrentModification))
                {
                    throw e;
                }
                if(numAttempts > retries)
                {
                    throw e;
                }
                this.modelService.detachAll();
                if(retryConcurrentModification.sleepIntervalInMillis() > 0)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Sleeping for " + retryConcurrentModification.sleepIntervalInMillis() + " before next retry");
                    }
                    Thread.sleep(retryConcurrentModification.sleepIntervalInMillis());
                }
                if(numAttempts > retries)
                {
                    break;
                }
            }
        }
        return null;
    }


    private boolean isRegisteredException(Throwable throwable, RetryConcurrentModification retryConcurrentModification)
    {
        Class[] registeredExceptions = retryConcurrentModification.exception();
        for(Class exceptionClass : registeredExceptions)
        {
            if(exceptionClass.isInstance(throwable))
            {
                return true;
            }
        }
        return false;
    }


    @Required
    public void setMaxRetries(int maxRetries)
    {
        this.maxRetries = maxRetries;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
