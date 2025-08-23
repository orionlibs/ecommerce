package de.hybris.platform.hac.data.dto;

import java.util.Enumeration;
import java.util.Properties;

public class ExtensionTableData
{
    private String name;
    private boolean deprecated;
    private String webRoot;
    private String version;
    private boolean coreModule;
    private boolean webModule;
    private boolean corePlusModule;
    private String properties = "";


    public boolean isCoreModule()
    {
        return this.coreModule;
    }


    public void setCoreModule(boolean coreModule)
    {
        this.coreModule = coreModule;
    }


    public boolean isWebModule()
    {
        return this.webModule;
    }


    public void setWebModule(boolean webModule)
    {
        this.webModule = webModule;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public boolean isDeprecated()
    {
        return this.deprecated;
    }


    public void setDeprecated(boolean deprecated)
    {
        this.deprecated = deprecated;
    }


    public String getWebRoot()
    {
        if(isWebModule())
        {
            return this.webRoot;
        }
        return "";
    }


    public boolean isMissingContextName()
    {
        if(isWebModule())
        {
            return (this.webRoot == null);
        }
        return false;
    }


    public void setWebRoot(String webRoot)
    {
        this.webRoot = webRoot;
    }


    public String getVersion()
    {
        return this.version;
    }


    public void setVersion(String version)
    {
        this.version = version;
    }


    public void setProperties(Properties props)
    {
        Enumeration<?> eumeration = props.propertyNames();
        while(eumeration.hasMoreElements())
        {
            String key = (String)eumeration.nextElement();
            this.properties = this.properties + this.properties + "=" + key + "  ";
        }
    }


    public String getProperties()
    {
        return this.properties;
    }


    public boolean isCorePlusModule()
    {
        return this.corePlusModule;
    }


    public void setCorePlusModule(boolean corePlusModule)
    {
        this.corePlusModule = corePlusModule;
    }
}
