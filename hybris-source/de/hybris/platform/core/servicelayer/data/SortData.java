package de.hybris.platform.core.servicelayer.data;

import java.io.Serializable;

public class SortData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private boolean asc;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setAsc(boolean asc)
    {
        this.asc = asc;
    }


    public boolean isAsc()
    {
        return this.asc;
    }
}
