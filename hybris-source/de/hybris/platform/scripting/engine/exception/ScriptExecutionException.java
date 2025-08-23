package de.hybris.platform.scripting.engine.exception;

public class ScriptExecutionException extends ScriptingException
{
    public ScriptExecutionException(String message)
    {
        super(message);
    }


    public ScriptExecutionException(String message, Throwable cause)
    {
        super("Script execution has failed [reason: " + message + "]", cause);
    }
}
