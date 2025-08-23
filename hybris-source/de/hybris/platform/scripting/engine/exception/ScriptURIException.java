package de.hybris.platform.scripting.engine.exception;

public class ScriptURIException extends ScriptingException
{
    public ScriptURIException(String message)
    {
        super(message);
    }


    public ScriptURIException(String message, Throwable cause)
    {
        super("Incorrect Script URI [reason: " + message + "]", cause);
    }
}
