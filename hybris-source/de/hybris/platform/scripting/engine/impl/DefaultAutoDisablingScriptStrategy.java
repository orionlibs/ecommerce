package de.hybris.platform.scripting.engine.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.scripting.engine.ScriptExecutable;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.Map;

public class DefaultAutoDisablingScriptStrategy extends DoNotExecuteWhenDisabledStrategy
{
    private final SessionService sessionService;


    public DefaultAutoDisablingScriptStrategy(PK pk, ModelService modelService, SessionService sessionService)
    {
        super(pk, modelService);
        this.sessionService = sessionService;
    }


    public void onException(Exception exception, ScriptExecutable executable, Map<String, Object> scriptContext)
    {
        ScriptModel script = getScriptModel();
        if(!script.isDisabled())
        {
            script.setDisabled(true);
            saveWithoutOptimisticLock(script);
        }
    }


    void saveWithoutOptimisticLock(ScriptModel script)
    {
        this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, script));
    }
}
