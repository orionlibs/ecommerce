package de.hybris.platform.scripting.engine.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.scripting.engine.ScriptExecutable;
import de.hybris.platform.scripting.engine.ScriptExecutionResult;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import de.hybris.platform.scripting.engine.content.ScriptContent;
import de.hybris.platform.scripting.engine.content.impl.ResourceScriptContent;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

public class CmdScriptExecutor
{
    private static final Logger LOG = Logger.getLogger(CmdScriptExecutor.class);
    private static final Splitter.MapSplitter PARAMS_SPLITTER = Splitter.on(',').trimResults().withKeyValueSeparator('=');
    private ScriptingLanguagesService service;


    public void execute(String scriptURI)
    {
        ScriptExecutable executable = getExecutableByURI(scriptURI);
        ScriptExecutionResult executionResult = executable.execute();
        printOutResult(executionResult);
    }


    public void execute(String scriptURI, String paramsAsKeyValue)
    {
        ScriptExecutable executable = getExecutableByURI(scriptURI);
        ScriptExecutionResult executionResult = executable.execute(explodeParams(paramsAsKeyValue));
        printOutResult(executionResult);
    }


    private ScriptExecutable getExecutableByURI(String scriptURI)
    {
        if(isUrl(scriptURI))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Getting resource from remote file system by protocol: " + scriptURI);
            }
            return this.service.getExecutableByURI(scriptURI);
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Getting resource from local file system by shortcut: " + scriptURI);
        }
        FileSystemResource resource = new FileSystemResource(scriptURI);
        return this.service.getExecutableByContent((ScriptContent)new ResourceScriptContent((Resource)resource));
    }


    private boolean isUrl(String scriptURI)
    {
        return (scriptURI.startsWith("model:") || ResourceUtils.isUrl(scriptURI));
    }


    private Map<String, Object> explodeParams(String paramsAsKeyValue)
    {
        return (Map<String, Object>)ImmutableMap.builder().putAll(PARAMS_SPLITTER.split(paramsAsKeyValue)).build();
    }


    private void printOutResult(ScriptExecutionResult result)
    {
        if(result.getScriptResult() != null)
        {
            LOG.info("Script execution result: ");
            LOG.info(result.getScriptResult());
        }
        if(StringUtils.isNotEmpty(result.getOutputWriter().toString()))
        {
            LOG.info("Script execution std out: ");
            LOG.info(result.getOutputWriter().toString());
        }
        if(StringUtils.isNotEmpty(result.getErrorWriter().toString()))
        {
            LOG.error("Script execution std err: ");
            LOG.error(result.getErrorWriter().toString());
        }
    }


    @Required
    public void setService(ScriptingLanguagesService service)
    {
        this.service = service;
    }
}
