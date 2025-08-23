package de.hybris.platform.hac.scripting.exceptions;

import de.hybris.platform.servicelayer.exceptions.BusinessException;

public class ScriptCompilationException extends BusinessException
{
    public ScriptCompilationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
