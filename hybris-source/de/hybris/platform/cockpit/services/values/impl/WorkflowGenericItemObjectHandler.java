package de.hybris.platform.cockpit.services.values.impl;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.services.values.ValueHandlerWorkflowException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.workflow.ScriptEvaluationService;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.map.CaseInsensitiveMap;

public class WorkflowGenericItemObjectHandler extends GenericItemObjectValueHandler
{
    private ScriptEvaluationService scriptEvaluationService;


    public void storeValues(ObjectValueContainer container, boolean forceWrite) throws ValueHandlerException
    {
        Map<ItemAttributePropertyDescriptor, Object> originalValues = new HashMap<>();
        Map<ItemAttributePropertyDescriptor, Object> modifiedValues = new HashMap<>();
        collectValues(container, forceWrite, originalValues, modifiedValues);
        CaseInsensitiveMap<String, Object> caseInsensitiveMap1 = new CaseInsensitiveMap();
        CaseInsensitiveMap<String, Object> caseInsensitiveMap2 = new CaseInsensitiveMap();
        for(Map.Entry<ItemAttributePropertyDescriptor, Object> entry : originalValues.entrySet())
        {
            ItemAttributePropertyDescriptor iapd = entry.getKey();
            if(iapd.isSingleAttribute())
            {
                caseInsensitiveMap1.put(iapd.getAttributeQualifier().toLowerCase(), toPersistenceLayer(iapd, entry.getValue()));
                caseInsensitiveMap2.put(iapd.getAttributeQualifier().toLowerCase(),
                                toPersistenceLayer(iapd, modifiedValues.get(entry.getKey())));
            }
        }
        super.storeValues(container, forceWrite);
        try
        {
            boolean modified = container.isModified();
            if(modified && !getModelService().isNew(container.getObject()))
            {
                Item item = (Item)getModelService().getSource(container.getObject());
                getScriptEvaluationService().evaluateActivationScripts((ItemModel)getModelService().get(item.getPK()), (Map)caseInsensitiveMap2, (Map)caseInsensitiveMap1, "save");
            }
        }
        catch(Exception e)
        {
            throw new ValueHandlerWorkflowException(e.getLocalizedMessage(), e,
                            (modifiedValues.size() == 1) ? Collections.singleton((PropertyDescriptor)modifiedValues.keySet().iterator().next()) :
                                            Collections.EMPTY_SET);
        }
    }


    public void setScriptEvaluationService(ScriptEvaluationService scriptEvaluationService)
    {
        this.scriptEvaluationService = scriptEvaluationService;
    }


    protected ScriptEvaluationService getScriptEvaluationService()
    {
        return this.scriptEvaluationService;
    }
}
