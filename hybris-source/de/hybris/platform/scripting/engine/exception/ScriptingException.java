package de.hybris.platform.scripting.engine.exception;

public class ScriptingException extends RuntimeException
{
    public ScriptingException(String message)
    {
        super(message);
    }


    public ScriptingException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
