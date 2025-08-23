package de.hybris.platform.searchservices.index.data;

import java.io.Serializable;
import java.util.Objects;

public class SnIndex implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String indexTypeId;
    private Boolean active;


    public void setId(String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return this.id;
    }


    public void setIndexTypeId(String indexTypeId)
    {
        this.indexTypeId = indexTypeId;
    }


    public String getIndexTypeId()
    {
        return this.indexTypeId;
    }


    public void setActive(Boolean active)
    {
        this.active = active;
    }


    public Boolean getActive()
    {
        return this.active;
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
        SnIndex other = (SnIndex)o;
        return (Objects.equals(getId(), other.getId()) &&
                        Objects.equals(getIndexTypeId(), other.getIndexTypeId()) &&
                        Objects.equals(getActive(), other.getActive()));
    }


    public int hashCode()
    {
        int result = 1;
        Object attribute = this.id;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = this.indexTypeId;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = this.active;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        return result;
    }
}
