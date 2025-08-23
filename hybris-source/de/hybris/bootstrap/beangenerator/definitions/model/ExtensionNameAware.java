package de.hybris.bootstrap.beangenerator.definitions.model;

public class ExtensionNameAware
{
    protected String extensionName;


    public ExtensionNameAware(String extensionName)
    {
        if(extensionName == null)
        {
            throw new IllegalArgumentException("Extension name cannot be null");
        }
        this.extensionName = extensionName;
    }


    public String getExtensionName()
    {
        return this.extensionName;
    }
}
