package de.hybris.bootstrap.typesystem;

public abstract class YNameSpaceElement
{
    private final YNamespace container;
    private String loaderInfo;


    protected YNameSpaceElement(YNamespace container)
    {
        this.container = container;
    }


    public void resetCaches()
    {
    }


    public void validate()
    {
        if(getNamespace() == null)
        {
            throw new IllegalStateException("namespace element " + this + " got no namespace");
        }
    }


    public YNamespace getNamespace()
    {
        return this.container;
    }


    public YTypeSystem getTypeSystem()
    {
        return getNamespace().getTypeSystem();
    }


    public String getLoaderInfo()
    {
        return this.loaderInfo;
    }


    public void setLoaderInfo(String loaderInfo)
    {
        this.loaderInfo = loaderInfo;
    }


    public String toString()
    {
        return "" + getNamespace() + "::" + getNamespace() + getClass().getSimpleName();
    }
}
