package de.hybris.datasupplier.generator.tomcat.data;

public class TomcatWebModule extends BaseTomcatConfigObject
{
    private static final long serialVersionUID = -6460721576438159307L;
    public static final String ATTR_ENGINE_NAME = "engineName";
    public static final String ATTR_DOC_BASE = "docBase";
    public static final String ATTR_PATH = "path";
    public static final String ATTR_MANAGED_RESOURCE = "managedResource";


    public String getEngineName()
    {
        return (String)getAttribute("engineName");
    }


    public void setEngineName(String name)
    {
        setAttribute("engineName", name);
    }


    public String getDocBase()
    {
        return (String)getAttribute("docBase");
    }


    public void setDocBase(String str)
    {
        setAttribute("docBase", str);
    }


    public String getPath()
    {
        return (String)getAttribute("path");
    }


    public void setPath(String str)
    {
        setAttribute("path", str);
    }


    protected String buildTechnicalName()
    {
        return getEngineName();
    }


    protected String buildCaption()
    {
        return getEngineName();
    }


    public String getManagedResource()
    {
        return (String)getAttribute("managedResource");
    }


    public void setManagedResource(String name)
    {
        setAttribute("managedResource", name);
    }
}
