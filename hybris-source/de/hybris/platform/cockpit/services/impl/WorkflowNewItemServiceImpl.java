package de.hybris.platform.cockpit.services.impl;

import bsh.EvalError;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.util.ValueContainerMap;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.workflow.ScriptEvaluationService;
import java.util.Collections;
import java.util.Map;
import org.jfree.util.Log;

public class WorkflowNewItemServiceImpl extends NewItemServiceImpl
{
    private static final String PASSWORD_ATTRIBUTE = "password";
    private ScriptEvaluationService scriptEvaluationService;


    public TypedObject createNewItem(ObjectValueContainer valueContainer, ObjectTemplate template) throws ValueHandlerException
    {
        TypedObject typedItem = super.createNewItem(valueContainer, template);
        ValueContainerMap valueContainerMap = new ValueContainerMap(valueContainer, true, (ValueContainerMap.PropertyFilter)new Object(this, typedItem));
        if(typedItem != null)
        {
            try
            {
                getScriptEvaluationService().evaluateActivationScripts((ItemModel)typedItem.getObject(), Collections.EMPTY_MAP, (Map)valueContainerMap, "create");
            }
            catch(EvalError e)
            {
                Log.error(e, (Exception)e);
            }
        }
        return typedItem;
    }


    public void setScriptEvaluationService(ScriptEvaluationService scriptEvaluationService)
    {
        this.scriptEvaluationService = scriptEvaluationService;
    }


    private ScriptEvaluationService getScriptEvaluationService()
    {
        return this.scriptEvaluationService;
    }
}
