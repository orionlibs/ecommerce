package de.hybris.platform.b2bwebservicescommons.dto.order;

import java.io.Serializable;

public class B2BPaymentTypeWsDTO implements Serializable
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
