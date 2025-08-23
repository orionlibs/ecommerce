package de.hybris.platform.searchservices.indexer.data;

import de.hybris.platform.searchservices.enums.SnIndexerOperationStatus;
import de.hybris.platform.searchservices.enums.SnIndexerOperationType;
import java.io.Serializable;
import java.util.Objects;

public class SnIndexerOperation implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private String indexTypeId;
    private String indexId;
    private SnIndexerOperationType operationType;
    private SnIndexerOperationStatus status;


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


    public void setIndexId(String indexId)
    {
        this.indexId = indexId;
    }


    public String getIndexId()
    {
        return this.indexId;
    }


    public void setOperationType(SnIndexerOperationType operationType)
    {
        this.operationType = operationType;
    }


    public SnIndexerOperationType getOperationType()
    {
        return this.operationType;
    }


    public void setStatus(SnIndexerOperationStatus status)
    {
        this.status = status;
    }


    public SnIndexerOperationStatus getStatus()
    {
        return this.status;
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
        SnIndexerOperation other = (SnIndexerOperation)o;
        return (Objects.equals(getId(), other.getId()) &&
                        Objects.equals(getIndexTypeId(), other.getIndexTypeId()) &&
                        Objects.equals(getIndexId(), other.getIndexId()) &&
                        Objects.equals(getOperationType(), other.getOperationType()) &&
                        Objects.equals(getStatus(), other.getStatus()));
    }


    public int hashCode()
    {
        int result = 1;
        Object attribute = this.id;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = this.indexTypeId;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = this.indexId;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = this.operationType;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = this.status;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        return result;
    }
}
