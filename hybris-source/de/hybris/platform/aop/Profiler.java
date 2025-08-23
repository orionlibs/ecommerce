package de.hybris.platform.aop;

import org.aspectj.lang.ProceedingJoinPoint;

public interface Profiler
{
    Object logExecutionTime(ProceedingJoinPoint paramProceedingJoinPoint) throws Throwable;


    void setLimit(long paramLong);


    void setDomain(String paramString);
}
