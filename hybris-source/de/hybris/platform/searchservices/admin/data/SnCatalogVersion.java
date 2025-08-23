package de.hybris.platform.searchservices.admin.data;

import java.io.Serializable;
import java.util.Objects;

public class SnCatalogVersion implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String catalogId;
    private String version;


    public void setCatalogId(String catalogId)
    {
        this.catalogId = catalogId;
    }


    public String getCatalogId()
    {
        return this.catalogId;
    }


    public void setVersion(String version)
    {
        this.version = version;
    }


    public String getVersion()
    {
        return this.version;
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
        SnCatalogVersion other = (SnCatalogVersion)o;
        return (Objects.equals(getCatalogId(), other.getCatalogId()) &&
                        Objects.equals(getVersion(), other.getVersion()));
    }


    public int hashCode()
    {
        int result = 1;
        Object attribute = this.catalogId;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = this.version;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        return result;
    }
}
