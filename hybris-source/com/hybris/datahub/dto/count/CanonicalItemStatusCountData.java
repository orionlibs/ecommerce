package com.hybris.datahub.dto.count;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@XmlRootElement(name = "canonicalItemStatusCounts")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CanonicalItemStatusCountData extends AbstractItemStatusCountData
{
    private Long successCount;
    private Long archivedCount;
    private Long errorCount;
    private Long deletedCount;


    public Long getSuccessCount()
    {
        return this.successCount;
    }


    public void setSuccessCount(Long successCount)
    {
        this.successCount = successCount;
    }


    public Long getArchivedCount()
    {
        return this.archivedCount;
    }


    public void setArchivedCount(Long archivedCount)
    {
        this.archivedCount = archivedCount;
    }


    public Long getErrorCount()
    {
        return this.errorCount;
    }


    public void setErrorCount(Long errorCount)
    {
        this.errorCount = errorCount;
    }


    public Long getDeletedCount()
    {
        return this.deletedCount;
    }


    public void setDeletedCount(Long deletedCount)
    {
        this.deletedCount = deletedCount;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof CanonicalItemStatusCountData))
        {
            return false;
        }
        CanonicalItemStatusCountData that = (CanonicalItemStatusCountData)o;
        return (new EqualsBuilder())
                        .append(this.poolName, that.poolName)
                        .append(this.successCount, that.successCount)
                        .append(this.archivedCount, that.archivedCount)
                        .append(this.errorCount, that.errorCount)
                        .append(this.deletedCount, that.deletedCount)
                        .isEquals();
    }


    public int hashCode()
    {
        return (new HashCodeBuilder(17, 37))
                        .append(this.successCount)
                        .append(this.archivedCount)
                        .append(this.errorCount)
                        .append(this.deletedCount)
                        .toHashCode();
    }


    public String toString()
    {
        return "CanonicalItemStatusCountData{" + ((this.poolName != null) ? ("poolName='" + this.poolName + "', ") : "") + "successCount=" + this.successCount + ", archivedCount=" + this.archivedCount + ", errorCount=" + this.errorCount + ", deletedCount=" + this.deletedCount + "}";
    }
}
