package de.hybris.platform.ruleengineservices.compiler;

public class RuleCompilerException extends RuntimeException
{
    public RuleCompilerException()
    {
    }


    public RuleCompilerException(String message)
    {
        super(message);
    }


    public RuleCompilerException(Throwable cause)
    {
        super(cause);
    }


    public RuleCompilerException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
