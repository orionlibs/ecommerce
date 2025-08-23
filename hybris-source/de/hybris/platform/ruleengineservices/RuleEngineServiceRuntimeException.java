package de.hybris.platform.ruleengineservices;

public class RuleEngineServiceRuntimeException extends RuntimeException
{
    public RuleEngineServiceRuntimeException()
    {
    }


    public RuleEngineServiceRuntimeException(String message)
    {
        super(message);
    }


    public RuleEngineServiceRuntimeException(Throwable cause)
    {
        super(cause);
    }


    public RuleEngineServiceRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
