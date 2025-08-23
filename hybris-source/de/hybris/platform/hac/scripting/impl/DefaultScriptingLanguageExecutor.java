package de.hybris.platform.hac.scripting.impl;

import de.hybris.platform.hac.scripting.ScriptingLanguageExecutor;
import de.hybris.platform.scripting.engine.ScriptExecutionResult;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultScriptingLanguageExecutor implements ScriptingLanguageExecutor
{
    private static final Logger LOG = Logger.getLogger(DefaultScriptingLanguageExecutor.class);
    private static final String DEFAULT_SCRIPT_ENCODING = "UTF-8";
    private static final String STACKTRACE_TEXT_KEY = "stacktraceText";
    private static final String OUTPUT_TEXT_KEY = "outputText";
    private static final String EXECUTION_RESULT_KEY = "executionResult";
    private ScriptingLanguagesService scriptingLanguagesService;


    public Map<String, Object> executeScript(String engineName, String script, boolean commit)
    {
        if(GenericValidator.isBlankOrNull(script))
        {
            LOG.info("No script, nothing to execute...");
            return Collections.emptyMap();
        }
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        AtomicReference<ScriptExecutionResult> scriptResultRef = new AtomicReference<>();
        ByteArrayOutputStream tempOutStream = new ByteArrayOutputStream();
        StringWriter stackTraceWriter = new StringWriter();
        try
        {
            PrintStream tempOut = interceptInOut(tempOutStream);
            Map<String, Object> ctx = prepareScriptContext(tempOut);
            Transaction.current().execute((TransactionBody)new Object(this, scriptResultRef, engineName, script, ctx, tempOut, stackTraceWriter, commit));
        }
        catch(Exception e)
        {
            if(mustLogException(commit, e))
            {
                logException(stackTraceWriter, e);
            }
        }
        finally
        {
            restoreInOut(originalOut, originalErr);
        }
        Map<String, Object> result = new HashMap<>();
        buildScriptExecutionResult(result, scriptResultRef.get(), stringifyOutStream(tempOutStream), stackTraceWriter);
        return result;
    }


    private void restoreInOut(PrintStream originalOut, PrintStream originalErr)
    {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }


    private PrintStream interceptInOut(ByteArrayOutputStream tempOutStream)
    {
        PrintStream tempOut = getPrintStreamForOut(tempOutStream);
        restoreInOut(tempOut, tempOut);
        return tempOut;
    }


    private Map<String, Object> prepareScriptContext(PrintStream tempOut)
    {
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("out", tempOut);
        return ctx;
    }


    private void logException(StringWriter stackTraceWriter, Exception exception)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Script contain errors: " + exception.getMessage());
        }
        String exceptionString = exception.getMessage();
        exceptionString = exceptionString.replace("Undefined argument:", "\r\nUndefined argument:");
        exceptionString = exceptionString.replace("Encountered:", "\r\nEncountered:");
        exceptionString = exceptionString.replace("Sourced file:", "Sourced file:\r\n");
        exceptionString = exceptionString.replace("inline evaluation of:", "inline evaluation of:\r\n");
        exceptionString = exceptionString.replaceAll("( {2,})", "\r\n$1");
        stackTraceWriter.append(exceptionString);
    }


    private boolean mustLogException(boolean commit, Exception exception)
    {
        return (commit || !(exception instanceof de.hybris.platform.tx.RollbackOnlyException));
    }


    private void buildScriptExecutionResult(Map<String, Object> result, ScriptExecutionResult scriptResult, String output, StringWriter stackTraceWriter)
    {
        if(scriptResult != null && scriptResult.getScriptResult() != null)
        {
            if(scriptResult.getScriptResult() instanceof String)
            {
                result.put("executionResult", scriptResult.getScriptResult());
            }
            else
            {
                result.put("executionResult", scriptResult.getScriptResult().toString());
            }
        }
        else
        {
            result.put("executionResult", "");
        }
        result.put("outputText", output);
        result.put("stacktraceText", stackTraceWriter.toString());
    }


    private PrintStream getPrintStreamForOut(ByteArrayOutputStream tempOutStream)
    {
        try
        {
            return new PrintStream(tempOutStream, true, "UTF-8");
        }
        catch(UnsupportedEncodingException e)
        {
            throw new SystemException(e.getMessage(), e);
        }
    }


    private String stringifyOutStream(ByteArrayOutputStream tempOutStream)
    {
        try
        {
            return tempOutStream.toString("UTF-8");
        }
        catch(UnsupportedEncodingException e)
        {
            throw new SystemException(e.getMessage(), e);
        }
    }


    @Required
    public void setScriptingLanguagesService(ScriptingLanguagesService scriptingLanguagesService)
    {
        this.scriptingLanguagesService = scriptingLanguagesService;
    }
}
