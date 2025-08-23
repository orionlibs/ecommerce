package de.hybris.platform.scripting.engine.impl;

import de.hybris.platform.scripting.engine.AutoDisablingScriptStrategy;
import de.hybris.platform.scripting.engine.ScriptExecutionResult;
import de.hybris.platform.scripting.engine.exception.ScriptExecutionException;
import java.util.Map;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

public class InterpretedScriptExecutable extends AbstractScriptExecutable
{
    private static final Logger LOG = Logger.getLogger(InterpretedScriptExecutable.class);
    private final String scriptContent;
    private final ScriptEngineFactory scriptEngineFactory;


    public InterpretedScriptExecutable(String scriptContent, ScriptEngineFactory scriptEngineFactory, ApplicationContext appContext, Map<String, Object> globalContext, AutoDisablingScriptStrategy autoDisablingScriptStrategy)
    {
        super(appContext, globalContext, autoDisablingScriptStrategy);
        this.scriptContent = scriptContent;
        this.scriptEngineFactory = scriptEngineFactory;
    }


    protected ScriptExecutionResult execute(ScriptContext scriptContext)
    {
        try
        {
            ScriptEngine engine = getEngine();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Handle execution [engine: " + engine + ", scriptContent: " + this.scriptContent + "]");
            }
            Object result = engine.eval(this.scriptContent, scriptContext);
            return (ScriptExecutionResult)new DefaultScriptExecutionResult(result, scriptContext);
        }
        catch(Exception e)
        {
            throw new ScriptExecutionException(e.getMessage(), e);
        }
    }


    protected ScriptEngine getEngine()
    {
        return this.scriptEngineFactory.getScriptEngine();
    }


    public String toString()
    {
        return "InterpretedScriptExecutable{scriptContent='" + this.scriptContent + "', scriptEngineFactory=" + this.scriptEngineFactory + "}";
    }
}
