package de.hybris.platform.cockpit.model.advancedsearch.config.impl;

import de.hybris.platform.cockpit.model.advancedsearch.config.EditorConditionEntry;
import de.hybris.platform.cockpit.model.advancedsearch.config.ShortcutConditionEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultShortcutConditionEntry extends DefaultEditorConditionEntry implements ShortcutConditionEntry
{
    private List<Object> conditionValues = new ArrayList();
    private Map<String, String> allLabels = new HashMap<>();
    private EditorConditionEntry targetEntry = null;


    public List<Object> getConditionValues()
    {
        return (this.conditionValues == null) ? Collections.EMPTY_LIST : this.conditionValues;
    }


    public void setConditionValues(List<Object> conditionValues)
    {
        this.conditionValues = conditionValues;
    }


    public void setAllLabels(Map<String, String> allLabels)
    {
        this.allLabels = allLabels;
    }


    public Map<String, String> getAllLabels()
    {
        return this.allLabels;
    }


    public String getLabel(String iso)
    {
        return this.allLabels.get(iso);
    }


    public void setLabel(String iso, String label)
    {
        this.allLabels.put(iso, label);
    }


    public void setTargetConditionEntry(EditorConditionEntry target)
    {
        setTargetEntry(target);
    }


    public void setTargetEntry(EditorConditionEntry targetEntry)
    {
        this.targetEntry = targetEntry;
    }


    public EditorConditionEntry getTargetEntry()
    {
        return this.targetEntry;
    }
}
