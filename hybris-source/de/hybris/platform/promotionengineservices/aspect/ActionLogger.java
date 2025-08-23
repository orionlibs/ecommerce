package de.hybris.platform.promotionengineservices.aspect;

import de.hybris.platform.ruleengineservices.aspect.AbstractLoggingAspect;
import java.util.function.Predicate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class ActionLogger extends AbstractLoggingAspect
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ActionLogger.class);
    private static final Predicate<Object> eligibleForTarget;

    static
    {
        eligibleForTarget = (obj -> obj instanceof de.hybris.platform.promotionengineservices.action.impl.AbstractRuleActionStrategy);
    }

    @Before("execution(public * de.hybris.platform.promotionengineservices.action.impl.*.*(..))")
    public void decideBefore(JoinPoint joinPoint)
    {
        logJoinPoint(joinPoint);
    }


    protected boolean isEligibleForJoinPoint(JoinPoint joinPoint)
    {
        return eligibleForTarget.test(joinPoint.getTarget());
    }


    protected Logger getLogger()
    {
        return LOGGER;
    }
}
