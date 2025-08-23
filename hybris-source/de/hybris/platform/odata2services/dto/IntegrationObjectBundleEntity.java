package de.hybris.platform.odata2services.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class IntegrationObjectBundleEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String integrationObjectCode;
    private Set<String> rootItemInstancePks;


    public void setIntegrationObjectCode(String integrationObjectCode)
    {
        this.integrationObjectCode = integrationObjectCode;
    }


    public String getIntegrationObjectCode()
    {
        return this.integrationObjectCode;
    }


    public void setRootItemInstancePks(Set<String> rootItemInstancePks)
    {
        this.rootItemInstancePks = rootItemInstancePks;
    }


    public Set<String> getRootItemInstancePks()
    {
        return this.rootItemInstancePks;
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
        IntegrationObjectBundleEntity other = (IntegrationObjectBundleEntity)o;
        return Objects.equals(getIntegrationObjectCode(), other.getIntegrationObjectCode());
    }


    public int hashCode()
    {
        int result = 1;
        Object attribute = this.integrationObjectCode;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        return result;
    }
}
