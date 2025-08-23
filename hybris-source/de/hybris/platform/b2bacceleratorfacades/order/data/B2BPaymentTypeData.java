package de.hybris.platform.b2bacceleratorfacades.order.data;

import java.io.Serializable;

public class B2BPaymentTypeData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String displayName;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    public String getDisplayName()
    {
        return this.displayName;
    }
}
