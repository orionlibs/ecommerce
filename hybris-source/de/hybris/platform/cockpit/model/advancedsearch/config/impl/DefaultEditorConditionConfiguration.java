package de.hybris.platform.cockpit.model.advancedsearch.config.impl;

import de.hybris.platform.cockpit.model.advancedsearch.config.EditorConditionConfiguration;
import de.hybris.platform.cockpit.model.advancedsearch.config.EditorConditionEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultEditorConditionConfiguration implements EditorConditionConfiguration
{
    private Map<String, List<EditorConditionEntry>> conditionMap;
    private Map<String, String> defaultConditionsMap;


    public List<EditorConditionEntry> getConditionEntries(String editorType)
    {
        List<EditorConditionEntry> list = getConditionMap().get(editorType);
        return (list == null) ? Collections.EMPTY_LIST : list;
    }


    public Map<String, List<EditorConditionEntry>> getConditionMap()
    {
        return (this.conditionMap == null) ? Collections.EMPTY_MAP : this.conditionMap;
    }


    public void setConditions(List<EditorConditionEntry> conditions)
    {
        this.conditionMap = new HashMap<>();
        for(EditorConditionEntry editorConditionEntry : conditions)
        {
            for(String type : editorConditionEntry.getValidAttributeTypesAsList())
            {
                List<EditorConditionEntry> list = this.conditionMap.get(type);
                if(list == null)
                {
                    list = new ArrayList<>();
                    this.conditionMap.put(type, list);
                }
                list.add(editorConditionEntry);
            }
        }
    }


    public void setDefaultConditionsMap(Map<String, String> defaultConditionsMap)
    {
        this.defaultConditionsMap = defaultConditionsMap;
    }


    public Map<String, String> getDefaultConditionsMap()
    {
        return this.defaultConditionsMap;
    }


    public String getDefaultConditions(String attributeType)
    {
        if(this.defaultConditionsMap != null)
        {
            return this.defaultConditionsMap.get(attributeType);
        }
        return null;
    }
}
