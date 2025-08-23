package de.hybris.platform.ordercancel;

public class DefaultOrderCancelDenialReason implements OrderCancelDenialReason
{
    private int code;
    private String description;


    public DefaultOrderCancelDenialReason(int code, String description)
    {
        this.code = code;
        this.description = description;
    }


    public DefaultOrderCancelDenialReason()
    {
    }


    public int getCode()
    {
        return this.code;
    }


    public void setCode(int code)
    {
        this.code = code;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }
}
