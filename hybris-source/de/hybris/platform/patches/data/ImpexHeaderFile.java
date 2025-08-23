package de.hybris.platform.patches.data;

public class ImpexHeaderFile extends AbstractImpexFile
{
    private boolean isOptional;


    public ImpexHeaderFile clone()
    {
        return clone(new ImpexHeaderFile());
    }


    public ImpexHeaderFile clone(ImpexHeaderFile clone)
    {
        clone(clone);
        clone.setOptional(isOptional());
        return clone;
    }


    public boolean isOptional()
    {
        return this.isOptional;
    }


    public void setOptional(boolean isOptional)
    {
        this.isOptional = isOptional;
    }
}
