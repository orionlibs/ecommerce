package de.hybris.platform.odata2services.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class ExportEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String requestUrl;
    private Set<String> requestBodies;


    public void setRequestUrl(String requestUrl)
    {
        this.requestUrl = requestUrl;
    }


    public String getRequestUrl()
    {
        return this.requestUrl;
    }


    public void setRequestBodies(Set<String> requestBodies)
    {
        this.requestBodies = requestBodies;
    }


    public Set<String> getRequestBodies()
    {
        return this.requestBodies;
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
        ExportEntity other = (ExportEntity)o;
        return Objects.equals(getRequestUrl(), other.getRequestUrl());
    }


    public int hashCode()
    {
        int result = 1;
        Object attribute = this.requestUrl;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        return result;
    }
}
