package de.hybris.platform.persistence.extension;

public class GeneralExtensionEJB extends ExtensionEJB
{
    private final String name;


    public GeneralExtensionEJB(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }
}
