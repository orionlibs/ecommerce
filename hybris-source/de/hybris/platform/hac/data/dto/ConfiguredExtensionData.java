package de.hybris.platform.hac.data.dto;

public class ConfiguredExtensionData
{
    private String extensionName;
    private boolean enabled;
    private boolean webExtension;
    private String contextName;


    public String getContextName()
    {
        return this.contextName;
    }


    public void setContextName(String contextName)
    {
        this.contextName = contextName;
    }


    public boolean isWebExtension()
    {
        return this.webExtension;
    }


    public void setWebExtension(boolean webExtension)
    {
        this.webExtension = webExtension;
    }


    public boolean isEnabled()
    {
        return this.enabled;
    }


    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }


    public void setExtensionName(String extensionName)
    {
        this.extensionName = extensionName;
    }


    public String getExtensionName()
    {
        return this.extensionName;
    }


    public boolean isMissingContextName()
    {
        return (this.enabled && this.webExtension && this.contextName == null);
    }
}
