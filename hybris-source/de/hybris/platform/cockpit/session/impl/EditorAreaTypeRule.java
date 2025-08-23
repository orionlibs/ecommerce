package de.hybris.platform.cockpit.session.impl;

public class EditorAreaTypeRule
{
    private String typeCode;
    private Boolean recursive = Boolean.FALSE;
    private Boolean allowed = Boolean.TRUE;


    public String getTypeCode()
    {
        return this.typeCode;
    }


    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }


    public Boolean getRecursive()
    {
        return this.recursive;
    }


    public void setRecursive(Boolean recursive)
    {
        this.recursive = recursive;
    }


    public Boolean getAllowed()
    {
        return this.allowed;
    }


    public void setAllowed(Boolean allowed)
    {
        this.allowed = allowed;
    }
}
