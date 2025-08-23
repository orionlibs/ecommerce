package de.hybris.platform.b2bacceleratorfacades.order.data;

import java.io.Serializable;

public class B2BDaysOfWeekData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String name;
    private String code;


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }
}
