package de.hybris.platform.scripting.engine.impl;

import de.hybris.platform.scripting.engine.AutoDisablingScriptStrategy;
import de.hybris.platform.scripting.engine.ScriptExecutionResult;
import de.hybris.platform.scripting.engine.exception.ScriptExecutionException;
import java.util.Map;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import org.springframework.context.ApplicationContext;

public class PrecompiledExecutable extends AbstractScriptExecutable
{
    private final CompiledScript compiledScript;


    public PrecompiledExecutable(CompiledScript compiledScript, Map<String, Object> globalContext, ApplicationContext appContext, AutoDisablingScriptStrategy autoDisablingScriptStrategy)
    {
        super(appContext, globalContext, autoDisablingScriptStrategy);
        this.compiledScript = compiledScript;
    }


    protected ScriptEngine getEngine()
    {
        return this.compiledScript.getEngine();
    }


    protected ScriptExecutionResult execute(ScriptContext scriptContext)
    {
        try
        {
            Object result = this.compiledScript.eval(scriptContext);
            return (ScriptExecutionResult)new DefaultScriptExecutionResult(result, scriptContext);
        }
        catch(Exception e)
        {
            throw new ScriptExecutionException(e.getMessage(), e);
        }
    }


    public String toString()
    {
        return "PrecompiledExecutable{compiledScript=" + this.compiledScript + "}";
    }
}
