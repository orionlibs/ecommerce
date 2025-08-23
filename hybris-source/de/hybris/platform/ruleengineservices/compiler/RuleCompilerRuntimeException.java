package de.hybris.platform.ruleengineservices.compiler;

public class RuleCompilerRuntimeException extends RuntimeException
{
    public RuleCompilerRuntimeException()
    {
    }


    public RuleCompilerRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    public RuleCompilerRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public RuleCompilerRuntimeException(String message)
    {
        super(message);
    }


    public RuleCompilerRuntimeException(Throwable cause)
    {
        super(cause);
    }
}
