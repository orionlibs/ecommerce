package de.hybris.platform.cms2.data;

import java.io.Serializable;

public class AttributePermissionForType implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String typecode;
    private String include;
    private String exclude;


    public void setTypecode(String typecode)
    {
        this.typecode = typecode;
    }


    public String getTypecode()
    {
        return this.typecode;
    }


    public void setInclude(String include)
    {
        this.include = include;
    }


    public String getInclude()
    {
        return this.include;
    }


    public void setExclude(String exclude)
    {
        this.exclude = exclude;
    }


    public String getExclude()
    {
        return this.exclude;
    }
}
