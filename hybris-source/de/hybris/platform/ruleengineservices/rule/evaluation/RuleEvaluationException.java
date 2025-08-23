package de.hybris.platform.ruleengineservices.rule.evaluation;

public class RuleEvaluationException extends RuntimeException
{
    public RuleEvaluationException(String message)
    {
        super(message);
    }


    public RuleEvaluationException(String message, Throwable throwable)
    {
        super(message, throwable);
    }
}
