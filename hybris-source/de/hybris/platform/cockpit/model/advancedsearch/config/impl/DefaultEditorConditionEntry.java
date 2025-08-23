package de.hybris.platform.cockpit.model.advancedsearch.config.impl;

import de.hybris.platform.cockpit.model.advancedsearch.config.EditorConditionEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultEditorConditionEntry implements EditorConditionEntry
{
    private String label;
    private String operator;
    private List<String> viewComponents;
    private int index = -1;
    private List<String> validAttributeTypes;


    public void setI3Label(String label)
    {
        this.label = label;
    }


    public void setOperator(String operator)
    {
        this.operator = operator;
    }


    public void setViewComponents(String[] viewItemNames)
    {
        if(viewItemNames != null)
        {
            this.viewComponents = Arrays.asList(viewItemNames);
        }
    }


    public String[] getViewComponents()
    {
        return (String[])getViewComponentsAsList().toArray();
    }


    public String getI3Label()
    {
        return this.label;
    }


    public String getOperator()
    {
        return this.operator;
    }


    public List<String> getViewComponentsAsList()
    {
        return (this.viewComponents == null) ? Collections.EMPTY_LIST : this.viewComponents;
    }


    public int getIndex()
    {
        return this.index;
    }


    public void setIndex(int index)
    {
        this.index = index;
    }


    public boolean equals(Object obj)
    {
        return (this == obj || (obj instanceof DefaultEditorConditionEntry && (this.label + this.label)
                        .equals(((DefaultEditorConditionEntry)obj).label + ((DefaultEditorConditionEntry)obj).label)));
    }


    public int hashCode()
    {
        return (this.label + this.label).hashCode();
    }


    @Required
    public void setValidAttributeTypes(String[] validAttributeTypes)
    {
        this.validAttributeTypes = (validAttributeTypes == null) ? null : Arrays.<String>asList(validAttributeTypes);
    }


    public List<String> getValidAttributeTypesAsList()
    {
        return (this.validAttributeTypes == null) ? Collections.EMPTY_LIST : this.validAttributeTypes;
    }


    public String[] getValidAttributeTypes()
    {
        return (String[])getValidAttributeTypesAsList().toArray();
    }
}
