package de.hybris.platform.ruleengineservices.aspect;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLoggingAspect
{
    public static final String DEBUG = "DEBUG";
    public static final String WARN = "WARN";
    public static final String TRACE = "TRACE";
    public static final String INFO = "INFO";
    public static final String DEFAULT_LOG_LEVEL = "DEBUG";
    private static final String LOGGING_TEMPLATE = "{} triggered : {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLoggingAspect.class);
    private String logLevel = "DEBUG";


    protected Logger getLogger()
    {
        return LOGGER;
    }


    protected void logJoinPoint(JoinPoint joinPoint)
    {
        if(isLoggingEligibleForJoinPoint(joinPoint))
        {
            String signatureName = getSignatureName(joinPoint);
            List<?> args = (joinPoint.getArgs() == null) ? Collections.emptyList() : Arrays.asList(joinPoint.getArgs());
            String logInfo = getLogInfoFromArgs(args);
            logAtDebugLevel(signatureName, logInfo);
            logAtTraceLevel(signatureName, logInfo);
            logAtInfoLevel(signatureName, logInfo);
            logAtWarnLevel(signatureName, logInfo);
        }
    }


    protected String getSignatureName(JoinPoint joinPoint)
    {
        return String.format("%s.%s", new Object[] {joinPoint.getSignature().getDeclaringType().getSimpleName(), joinPoint
                        .getSignature().getName()});
    }


    protected String getLongSignatureName(JoinPoint joinPoint)
    {
        return String.format("%s.%s", new Object[] {joinPoint.getSignature().getDeclaringType().getName(), joinPoint
                        .getSignature().getName()});
    }


    protected void logAtDebugLevel(String signatureName, String logInfo)
    {
        if("DEBUG".equalsIgnoreCase(getLogLevel()) && getLogger().isDebugEnabled())
        {
            getLogger().debug("{} triggered : {}", signatureName, logInfo);
        }
    }


    protected void logAtTraceLevel(String signatureName, String logInfo)
    {
        if("TRACE".equalsIgnoreCase(getLogLevel()) && getLogger().isTraceEnabled())
        {
            getLogger().trace("{} triggered : {}", signatureName, logInfo);
        }
    }


    protected void logAtInfoLevel(String signatureName, String logInfo)
    {
        if("INFO".equalsIgnoreCase(getLogLevel()) && getLogger().isInfoEnabled())
        {
            getLogger().info("{} triggered : {}", signatureName, logInfo);
        }
    }


    protected void logAtWarnLevel(String signatureName, String logInfo)
    {
        if("WARN".equalsIgnoreCase(getLogLevel()) && getLogger().isWarnEnabled())
        {
            getLogger().warn("{} triggered : {}", signatureName, logInfo);
        }
    }


    protected boolean isLoggingEligibleForJoinPoint(JoinPoint joinPoint)
    {
        return isEligibleForJoinPoint(joinPoint);
    }


    protected abstract boolean isEligibleForJoinPoint(JoinPoint paramJoinPoint);


    protected String getLogInfoFromArgs(List<?> args)
    {
        return args.stream().map(ToStringBuilder::reflectionToString).collect(Collectors.joining(", "));
    }


    protected String getLogLevel()
    {
        return this.logLevel;
    }


    public void setLogLevel(String logLevel)
    {
        this.logLevel = logLevel;
    }
}
