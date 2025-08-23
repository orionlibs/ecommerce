package de.hybris.platform.commercefacades.order.data;

import java.io.Serializable;

public class PaymentModeData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String name;
    private String description;


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


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }
}
