package de.hybris.platform.scripting.engine.exception;

public class DisabledScriptException extends ScriptingException
{
    public DisabledScriptException(String message)
    {
        super(message);
    }


    public DisabledScriptException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
