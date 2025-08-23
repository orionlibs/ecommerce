package de.hybris.platform.ruleengine.exception;

public class RuleEngineRuntimeException extends RuntimeException
{
    public RuleEngineRuntimeException()
    {
    }


    public RuleEngineRuntimeException(Throwable t)
    {
        super(t);
    }


    public RuleEngineRuntimeException(String s, Throwable t)
    {
        super(s, t);
    }


    public RuleEngineRuntimeException(String s)
    {
        super(s);
    }
}
