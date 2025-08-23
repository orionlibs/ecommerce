package de.hybris.platform.ruleengineservices.rule.strategies;

import de.hybris.platform.ruleengineservices.RuleEngineServiceRuntimeException;

public class RuleTypeMappingException extends RuleEngineServiceRuntimeException
{
    public RuleTypeMappingException()
    {
    }


    public RuleTypeMappingException(String message)
    {
        super(message);
    }


    public RuleTypeMappingException(Throwable cause)
    {
        super(cause);
    }


    public RuleTypeMappingException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
