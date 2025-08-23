package de.hybris.platform.workflow;

import bsh.EvalError;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.Map;

public interface ScriptEvaluationService
{
    WorkflowModel evaluateActivationScripts(ItemModel paramItemModel, Map paramMap1, Map paramMap2, String paramString) throws EvalError;
}
