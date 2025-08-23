package de.hybris.platform.adaptivesearchbackoffice.widgets.navigationcontext;

import de.hybris.platform.adaptivesearchbackoffice.data.CatalogVersionData;
import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class CatalogVersionModel implements Serializable
{
    private static final long serialVersionUID = 1L;
    private CatalogVersionData catalogVersion;
    private boolean active;
    private String name;


    public CatalogVersionData getCatalogVersion()
    {
        return this.catalogVersion;
    }


    public void setCatalogVersion(CatalogVersionData catalogVersion)
    {
        this.catalogVersion = catalogVersion;
    }


    public boolean isActive()
    {
        return this.active;
    }


    public void setActive(boolean active)
    {
        this.active = active;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        CatalogVersionModel that = (CatalogVersionModel)obj;
        return (new EqualsBuilder())
                        .append(getCatalogVersion(), that.getCatalogVersion())
                        .isEquals();
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.catalogVersion});
    }
}
