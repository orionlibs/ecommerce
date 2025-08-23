package de.hybris.platform.cockpit.model.editor.search.impl;

import de.hybris.platform.cockpit.model.advancedsearch.ConditionValueContainer;
import de.hybris.platform.cockpit.model.editor.impl.AbstractUIEditor;
import de.hybris.platform.cockpit.model.editor.search.ConditionUIEditor;
import java.util.Collections;
import java.util.Map;

public abstract class AbstractConditionUIEditor extends AbstractUIEditor implements ConditionUIEditor
{
    private Map<String, Object> parameters;


    public String getEditorType()
    {
        return "CONDITION";
    }


    public boolean isInline()
    {
        return false;
    }


    public void setParameters(Map<String, Object> parameters)
    {
        this.parameters = parameters;
    }


    public Map<String, Object> getParameters()
    {
        return (this.parameters == null) ? Collections.EMPTY_MAP : this.parameters;
    }


    public abstract ConditionValueContainer getValue();


    protected abstract String getValueType();
}
