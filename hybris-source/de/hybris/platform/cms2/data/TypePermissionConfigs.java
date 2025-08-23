package de.hybris.platform.cms2.data;

import java.io.Serializable;
import java.util.List;

public class TypePermissionConfigs implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String typecode;
    private List<AttributePermissionForType> configs;
    private List<String> include;


    public void setTypecode(String typecode)
    {
        this.typecode = typecode;
    }


    public String getTypecode()
    {
        return this.typecode;
    }


    public void setConfigs(List<AttributePermissionForType> configs)
    {
        this.configs = configs;
    }


    public List<AttributePermissionForType> getConfigs()
    {
        return this.configs;
    }


    public void setInclude(List<String> include)
    {
        this.include = include;
    }


    public List<String> getInclude()
    {
        return this.include;
    }
}
