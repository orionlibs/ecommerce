package de.hybris.platform.commercefacades.quote.data;

import java.io.Serializable;

@Deprecated(since = "6.4", forRemoval = true)
public class DiscountTypeData implements Serializable
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
