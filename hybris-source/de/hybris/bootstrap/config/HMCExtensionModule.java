package de.hybris.bootstrap.config;

public class HMCExtensionModule extends AbstractExtensionModule
{
    private String extensionclassname;


    public HMCExtensionModule()
    {
        super("hmc");
    }


    public String getExtensionClassName()
    {
        return this.extensionclassname;
    }


    public void setExtensionClassName(String extensionclassname)
    {
        this.extensionclassname = extensionclassname;
    }
}
