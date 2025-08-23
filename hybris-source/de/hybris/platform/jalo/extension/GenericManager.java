package de.hybris.platform.jalo.extension;

public class GenericManager extends Extension
{
    private final String name;


    public GenericManager(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }
}
