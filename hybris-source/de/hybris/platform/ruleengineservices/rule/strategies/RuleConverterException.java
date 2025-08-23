package de.hybris.platform.ruleengineservices.rule.strategies;

import de.hybris.platform.ruleengineservices.RuleEngineServiceException;

public class RuleConverterException extends RuleEngineServiceException
{
    public RuleConverterException()
    {
    }


    public RuleConverterException(String message)
    {
        super(message);
    }


    public RuleConverterException(Throwable cause)
    {
        super(cause);
    }


    public RuleConverterException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
