package de.hybris.platform.scripting.engine;

import java.io.Writer;

public interface ScriptExecutionResult
{
    Object getScriptResult();


    Writer getOutputWriter();


    Writer getErrorWriter();


    boolean isSuccessful();
}
