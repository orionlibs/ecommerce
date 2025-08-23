package de.hybris.platform.processengine.definition;

public class NoSuchProcessDefinitionException extends RuntimeException
{
    private final String processDefinitionName;


    NoSuchProcessDefinitionException(String name)
    {
        super("No process definition known by name '" + name + "'.");
        this.processDefinitionName = name;
    }


    public String getProcessDefinitionName()
    {
        return this.processDefinitionName;
    }
}
