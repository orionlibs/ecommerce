package de.hybris.platform.ruleengineservices.rao;

import java.io.Serializable;
import java.util.Objects;

public class WebsiteGroupRAO implements Serializable
{
    private String id;


    public void setId(String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return this.id;
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
        WebsiteGroupRAO other = (WebsiteGroupRAO)o;
        return Objects.equals(getId(), other.getId());
    }


    public int hashCode()
    {
        int result = 1;
        Object attribute = this.id;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        return result;
    }
}
