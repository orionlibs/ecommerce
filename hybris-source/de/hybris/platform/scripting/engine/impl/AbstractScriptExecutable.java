package de.hybris.platform.scripting.engine.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.scripting.engine.AutoDisablingScriptStrategy;
import de.hybris.platform.scripting.engine.ScriptExecutable;
import de.hybris.platform.scripting.engine.ScriptExecutionResult;
import de.hybris.platform.scripting.engine.exception.DisabledScriptException;
import de.hybris.platform.scripting.engine.exception.ScriptExecutionException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.SimpleBindings;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

public abstract class AbstractScriptExecutable implements ScriptExecutable
{
    private static final Logger LOG = Logger.getLogger(AbstractScriptExecutable.class);
    private final Map<String, Object> globalContext;
    private final ApplicationContext appContext;
    private final AutoDisablingScriptStrategy autoDisablingScriptStrategy;


    public AbstractScriptExecutable(ApplicationContext appContext, Map<String, Object> globalContext, AutoDisablingScriptStrategy autoDisablingScriptStrategy)
    {
        this.appContext = appContext;
        this.globalContext = globalContext;
        this.autoDisablingScriptStrategy = autoDisablingScriptStrategy;
    }


    public <T> T getAsInterface(Class<T> clazz)
    {
        return getAsInterface(clazz, Collections.emptyMap());
    }


    public <T> T getAsInterface(Class<T> clazz, Map<String, Object> context)
    {
        ScriptExecutionResult executionResult = execute(context);
        Object result = executionResult.getScriptResult();
        if(clazz.isInstance(result))
        {
            return (T)result;
        }
        if(result instanceof Class && clazz.isAssignableFrom((Class)result))
        {
            try
            {
                return ((Class<T>)result).newInstance();
            }
            catch(InstantiationException | IllegalAccessException e)
            {
                throw new ScriptExecutionException(e.getMessage(), e);
            }
        }
        ScriptEngine engine = getEngine();
        Preconditions.checkState(engine instanceof Invocable, "Engine %s is not invocabvle. To use this function you must use language engine which implements Invocable.", engine);
        return ((Invocable)engine).getInterface(executionResult.getScriptResult(), clazz);
    }


    public ScriptExecutionResult execute()
    {
        return execute(Collections.emptyMap());
    }


    public ScriptExecutionResult execute(Map<String, Object> context)
    {
        return execute(context, new StringWriter(), new StringWriter());
    }


    public ScriptExecutionResult execute(Map<String, Object> context, Writer outputWriter, Writer errorWriter)
    {
        HashMap<String, Object> ctx = new HashMap<>(context);
        ctx.putAll(this.globalContext);
        ctx.put("spring", getApplicationContext());
        ScriptContext scriptContext = prepareEngineContext(ctx, outputWriter, errorWriter);
        if(this.autoDisablingScriptStrategy == null)
        {
            return execute(scriptContext);
        }
        return executeWithAutoDisablingStrategy(scriptContext, ctx);
    }


    private ScriptExecutionResult executeWithAutoDisablingStrategy(ScriptContext scriptContext, HashMap<String, Object> ctx)
    {
        if(isDisabled())
        {
            throw new DisabledScriptException("Script executable is disabled for execution");
        }
        try
        {
            return execute(scriptContext);
        }
        catch(ScriptExecutionException e)
        {
            this.autoDisablingScriptStrategy.onException((Exception)e, this, ctx);
            if(isDisabled())
            {
                throw new DisabledScriptException("Script executable contains errors and was disabled for execution");
            }
            throw e;
        }
    }


    public boolean isDisabled()
    {
        return (this.autoDisablingScriptStrategy != null && this.autoDisablingScriptStrategy.isDisabled(this));
    }


    protected abstract ScriptExecutionResult execute(ScriptContext paramScriptContext);


    protected abstract ScriptEngine getEngine();


    protected ScriptContext prepareEngineContext(Map<String, Object> scriptContext, Writer outputWriter, Writer errorWriter)
    {
        ApplicationContextAwareScriptContext context = new ApplicationContextAwareScriptContext(this);
        context.setWriter(outputWriter);
        context.setErrorWriter(errorWriter);
        context.setBindings(new SimpleBindings(scriptContext), 100);
        return (ScriptContext)context;
    }


    ApplicationContext getApplicationContext()
    {
        return this.appContext;
    }
}
