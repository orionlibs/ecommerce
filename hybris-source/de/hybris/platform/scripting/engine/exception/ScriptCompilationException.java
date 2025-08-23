package de.hybris.platform.scripting.engine.exception;

public class ScriptCompilationException extends ScriptingException
{
    public ScriptCompilationException(String message)
    {
        super(message);
    }


    public ScriptCompilationException(String message, Throwable cause)
    {
        super("Script compilation has failed [reason: " + message + "]", cause);
    }
}
