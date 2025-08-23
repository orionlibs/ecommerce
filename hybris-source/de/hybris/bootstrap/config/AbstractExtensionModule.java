package de.hybris.bootstrap.config;

public abstract class AbstractExtensionModule
{
    private String name;
    private boolean sourceavailable = false;
    private String additionalclasspath = "";


    protected AbstractExtensionModule(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public boolean isSourceAvailable()
    {
        return this.sourceavailable;
    }


    public void setSourceAvailable(boolean sourceavailable)
    {
        this.sourceavailable = sourceavailable;
    }


    public String getAdditionalClassPath()
    {
        return (this.additionalclasspath == null) ? "" : this.additionalclasspath;
    }


    public void setAdditionalClassPath(String additionalclasspath)
    {
        this.additionalclasspath = additionalclasspath;
    }
}
