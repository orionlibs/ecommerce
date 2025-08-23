package de.hybris.platform.ordermanagementfacades.workflow.data;

import java.io.Serializable;
import java.util.List;

public class WorkflowData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String name;
    private String description;
    private List<WorkflowActionData> actions;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setActions(List<WorkflowActionData> actions)
    {
        this.actions = actions;
    }


    public List<WorkflowActionData> getActions()
    {
        return this.actions;
    }
}
