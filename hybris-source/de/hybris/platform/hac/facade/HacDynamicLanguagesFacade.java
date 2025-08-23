package de.hybris.platform.hac.facade;

import de.hybris.platform.hac.data.dto.ScriptData;
import de.hybris.platform.hac.scripting.ScriptingLanguageExecutor;
import de.hybris.platform.scripting.engine.content.ScriptContent;
import de.hybris.platform.scripting.engine.content.impl.SimpleScriptContent;
import de.hybris.platform.scripting.engine.exception.ScriptNotFoundException;
import de.hybris.platform.scripting.engine.exception.ScriptingException;
import de.hybris.platform.scripting.engine.internal.ScriptEngineType;
import de.hybris.platform.scripting.engine.internal.ScriptEnginesRegistry;
import de.hybris.platform.scripting.engine.repository.impl.ModelScriptsRepository;
import de.hybris.platform.scripting.enums.ScriptType;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class HacDynamicLanguagesFacade
{
    private static final Logger LOG = Logger.getLogger(HacDynamicLanguagesFacade.class);
    private ModelService modelService;
    private ScriptingLanguageExecutor executor;
    private ScriptEnginesRegistry scriptEnginesRegistry;
    private ModelScriptsRepository repository;


    public ScriptData saveScript(String code, String content, String engineName)
    {
        ScriptData data = new ScriptData();
        data.setCode(code);
        try
        {
            ScriptModel script;
            if(!this.repository.scriptExists(code))
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("creating new ScriptModel");
                }
                script = (ScriptModel)this.modelService.create(ScriptModel.class);
            }
            else
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("updating existing ScriptModel");
                }
                script = this.repository.findActiveScript(code);
            }
            script.setScriptType(ScriptType.valueOf(engineName.toUpperCase()));
            script.setCode(code);
            script.setContent(content);
            this.modelService.save(script);
            data.setContent((ScriptContent)new SimpleScriptContent(engineName, content));
        }
        catch(ModelSavingException e)
        {
            LOG.warn(e.getMessage());
            data.setException(e.getMessage());
        }
        return data;
    }


    public ScriptData loadScript(String code)
    {
        ScriptData data = new ScriptData();
        try
        {
            data.setContent(this.repository.lookupScript("model", code));
        }
        catch(ScriptNotFoundException e)
        {
            LOG.warn(e.getMessage());
            data.setException(e.getLocalizedMessage());
        }
        return data;
    }


    public Map<String, Object> executeScript(String scriptType, String script, boolean commit)
    {
        try
        {
            return this.executor.executeScript(scriptType, script, commit);
        }
        catch(ScriptingException se)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(se.getMessage(), (Throwable)se);
            }
            return Collections.emptyMap();
        }
    }


    public Set<ScriptEngineType> getRegisteredEngineTypes()
    {
        return this.scriptEnginesRegistry.getRegisteredEngineTypes();
    }


    public List<ScriptModel> findScriptsForEngine(String name)
    {
        return this.repository.findAllActiveScriptsForType(ScriptType.valueOf(name.toUpperCase()));
    }


    public ScriptData deleteScript(String code)
    {
        ScriptData data = new ScriptData();
        try
        {
            this.modelService.remove(this.repository.findActiveScript(code));
        }
        catch(ScriptNotFoundException e)
        {
            data.setException("Nothing to remove.");
        }
        catch(ModelRemovalException e)
        {
            LOG.warn(e.getMessage());
            data.setException(e.getLocalizedMessage());
        }
        return data;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setExecutor(ScriptingLanguageExecutor executor)
    {
        this.executor = executor;
    }


    @Required
    public void setScriptEnginesRegistry(ScriptEnginesRegistry scriptEnginesRegistry)
    {
        this.scriptEnginesRegistry = scriptEnginesRegistry;
    }


    @Required
    public void setRepository(ModelScriptsRepository repository)
    {
        this.repository = repository;
    }
}
