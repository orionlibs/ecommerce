package de.hybris.platform.ocafacades.order.data;

import java.io.Serializable;

public class RecentlyOrderedProductData implements Serializable
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
