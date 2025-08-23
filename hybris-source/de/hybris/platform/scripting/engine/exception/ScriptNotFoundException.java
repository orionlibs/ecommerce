package de.hybris.platform.scripting.engine.exception;

public class ScriptNotFoundException extends ScriptingException
{
    public ScriptNotFoundException(String message)
    {
        super(message);
    }


    public ScriptNotFoundException(String message, Throwable cause)
    {
        super("Script not found [reason: " + message + "]", cause);
    }
}
