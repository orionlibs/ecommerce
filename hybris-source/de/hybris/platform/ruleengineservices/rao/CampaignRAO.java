package de.hybris.platform.ruleengineservices.rao;

import java.io.Serializable;
import java.util.Objects;

public class CampaignRAO implements Serializable
{
    private String code;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public boolean equals(Object o)
    {
        if(o == null)
        {
            return false;
        }
        if(o == this)
        {
            return true;
        }
        if(getClass() != o.getClass())
        {
            return false;
        }
        CampaignRAO other = (CampaignRAO)o;
        return Objects.equals(getCode(), other.getCode());
    }


    public int hashCode()
    {
        int result = 1;
        Object attribute = this.code;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        return result;
    }
}
