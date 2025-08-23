package de.hybris.platform.hac.data.form;

import java.util.Map;

public class InitFormData
{
    private boolean dropTables;
    private String initMethod;
    private boolean clearHmc;
    private boolean essential;
    private boolean localizeTypes;
    private Map<String, Boolean> extensionNames;


    public Map<String, Boolean> getExtensionNames()
    {
        return this.extensionNames;
    }


    public void setExtensionNames(Map<String, Boolean> extensionNames)
    {
        this.extensionNames = extensionNames;
    }


    public String getInitMethod()
    {
        return this.initMethod;
    }


    public boolean isClearHmc()
    {
        return this.clearHmc;
    }


    public boolean isDropTables()
    {
        return this.dropTables;
    }


    public boolean isEssential()
    {
        return this.essential;
    }


    public boolean isLocalizeTypes()
    {
        return this.localizeTypes;
    }


    public void setClearHmc(boolean clearHmc)
    {
        this.clearHmc = clearHmc;
    }


    public void setDropTables(boolean dropTables)
    {
        this.dropTables = dropTables;
    }


    public void setEssential(boolean essential)
    {
        this.essential = essential;
    }


    public void setInitMethod(String initMethod)
    {
        this.initMethod = initMethod;
    }


    public void setLocalizeTypes(boolean localizeTypes)
    {
        this.localizeTypes = localizeTypes;
    }
}
