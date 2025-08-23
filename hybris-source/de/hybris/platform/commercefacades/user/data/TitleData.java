package de.hybris.platform.commercefacades.user.data;

import java.io.Serializable;

public class TitleData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String name;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }
}
