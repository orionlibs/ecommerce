package de.hybris.platform.aop;

import de.hybris.platform.core.Tenant;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Required;

public class ProfilingAspect extends AbstractProfilingAspect
{
    private static final Logger LOG = Logger.getLogger(ProfilingAspect.class.getName());
    private Tenant tenant;


    protected Tenant getOwnTenant()
    {
        return this.tenant;
    }


    @Required
    public void setTenant(Tenant tenant)
    {
        if(tenant == null)
        {
            throw new IllegalArgumentException("tenant cannot be null");
        }
        this.tenant = tenant;
    }


    public Object logExecutionTime(ProceedingJoinPoint pjp) throws Throwable
    {
        String profiledMethod = pjp.getTarget().getClass().getSimpleName() + "." + pjp.getTarget().getClass().getSimpleName();
        Object[] args = pjp.getArgs();
        Object ret = null;
        long calledAt = 0L;
        long executionTime = 0L;
        if(args != null)
        {
            calledAt = getTime();
            try
            {
                ret = pjp.proceed(args);
            }
            catch(Throwable e)
            {
                logException(getOrCreateResource(this.template, profiledMethod));
                throw e;
            }
            executionTime = calculateExecutionTime(calledAt);
        }
        else
        {
            calledAt = System.nanoTime();
            ret = pjp.proceed();
            executionTime = System.nanoTime() - calledAt;
        }
        if(executionTime >= this.limit)
        {
            logExecutionTime(getOrCreateResource(this.template, profiledMethod), executionTime, calledAt);
        }
        return ret;
    }
}
