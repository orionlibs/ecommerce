package de.hybris.platform.scripting.engine.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.scripting.engine.AutoDisablingScriptStrategy;
import de.hybris.platform.scripting.engine.ScriptExecutable;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Map;

public class DoNotExecuteWhenDisabledStrategy implements AutoDisablingScriptStrategy
{
    private final PK pk;
    private final ModelService modelService;


    public DoNotExecuteWhenDisabledStrategy(PK pk, ModelService modelService)
    {
        this.pk = pk;
        this.modelService = modelService;
    }


    public void onException(Exception exception, ScriptExecutable executable, Map<String, Object> scriptContext)
    {
    }


    public boolean isDisabled(ScriptExecutable executable)
    {
        ScriptModel scriptModel = getScriptModel();
        return scriptModel.isDisabled();
    }


    protected ScriptModel getScriptModel()
    {
        return (ScriptModel)this.modelService.get(this.pk);
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }
}
