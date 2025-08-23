package de.hybris.datasupplier.generator.tomcat.data;

public class TomcatHost extends BaseTomcatConfigObject
{
    public static final String ATTR_APP_BASE = "appBase";
    public static final String ATTR_NAME = "name";


    public String getName()
    {
        return (String)getAttribute("name");
    }


    public void setName(String name)
    {
        setAttribute("name", name);
    }


    public String getBaseDir()
    {
        return (String)getAttribute("appBase");
    }


    public void setBaseDir(String str)
    {
        setAttribute("appBase", str);
    }


    protected String buildTechnicalName()
    {
        return getName();
    }


    protected String buildCaption()
    {
        return getName();
    }


    public String getDisplayName()
    {
        return getName();
    }
}
