package de.hybris.platform.patches.data;

public abstract class AbstractImpexFile
{
    private String filePath;
    private String name;


    protected AbstractImpexFile clone(AbstractImpexFile clone)
    {
        clone.setName(getName());
        clone.setFilePath(getFilePath());
        return clone;
    }


    public String getFilePath()
    {
        return this.filePath;
    }


    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }
}
