package de.hybris.platform.scripting.engine.impl;

import de.hybris.platform.scripting.engine.ScriptExecutionResult;
import java.io.Writer;
import javax.script.ScriptContext;

public class DefaultScriptExecutionResult implements ScriptExecutionResult
{
    private final Object result;
    private final ScriptContext context;


    public DefaultScriptExecutionResult(Object result, ScriptContext context)
    {
        this.result = result;
        this.context = context;
    }


    public Object getScriptResult()
    {
        return this.result;
    }


    public Writer getOutputWriter()
    {
        return this.context.getWriter();
    }


    public Writer getErrorWriter()
    {
        return this.context.getErrorWriter();
    }


    public boolean isSuccessful()
    {
        return true;
    }


    public String toString()
    {
        return "DefaultScriptExecutionResult{result=" + this.result + ", context=" + this.context + "}";
    }
}
