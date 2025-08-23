package de.hybris.platform.patches.data;

public class ImpexHeaderOption implements Cloneable
{
    private String macro;


    public ImpexHeaderOption clone()
    {
        return clone(new ImpexHeaderOption());
    }


    public String getMacro()
    {
        return this.macro;
    }


    public void setMacro(String macro)
    {
        this.macro = macro;
    }


    protected ImpexHeaderOption clone(ImpexHeaderOption clone)
    {
        clone.setMacro(getMacro());
        return clone;
    }
}
