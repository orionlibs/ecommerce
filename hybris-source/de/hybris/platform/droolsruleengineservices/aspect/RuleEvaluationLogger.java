package de.hybris.platform.droolsruleengineservices.aspect;

import de.hybris.platform.ruleengineservices.aspect.AbstractLoggingAspect;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.RAOAction;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.drools.core.spi.KnowledgeHelper;
import org.kie.api.definition.rule.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class RuleEvaluationLogger extends AbstractLoggingAspect
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleEvaluationLogger.class);
    private static final Predicate<Object> ELIGIBLE_FOR_TARGET = RAOAction.class::isInstance;

    static
    {
        Objects.requireNonNull(RAOAction.class);
    }

    @Before("execution(public * de.hybris.platform.droolsruleengineservices.commerce.impl.*.*(..))")
    public void decideBefore(JoinPoint joinPoint)
    {
        logJoinPoint(joinPoint);
    }


    protected String getLogInfoFromArgs(List<?> args)
    {
        for(Object arg : args)
        {
            if(arg instanceof KnowledgeHelper)
            {
                return String.format("%s : %s", new Object[] {getDebugInfo((KnowledgeHelper)arg), args.toString()});
            }
        }
        return "[no debug info from ruleeval context] : " + args.toString();
    }


    protected Logger getLogger()
    {
        return LOGGER;
    }


    protected boolean isEligibleForJoinPoint(JoinPoint joinPoint)
    {
        return ELIGIBLE_FOR_TARGET.test(joinPoint.getTarget());
    }


    protected String getDebugInfo(KnowledgeHelper context)
    {
        return String.format("%s: %s", new Object[] {"ruleCode", getMetaDataFromRule((Rule)context.getRule(), "ruleCode")});
    }


    protected String getMetaDataFromRule(Rule rule, String key)
    {
        Object value = rule.getMetaData().get(key);
        return (value == null) ? null : value.toString();
    }
}
