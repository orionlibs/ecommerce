package de.hybris.bootstrap.beangenerator.definitions.model;

public abstract class AttributePrototype extends ExtensionNameAware
{
    private final String name;
    private final String description;


    public AttributePrototype(String extensionName, String name, String description)
    {
        super(extensionName);
        if(name == null)
        {
            throw new IllegalArgumentException("name cannot be null");
        }
        this.name = name;
        this.description = description;
    }


    public String getName()
    {
        return this.name;
    }


    public String getDescription()
    {
        return this.description;
    }
}
