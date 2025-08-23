package com.hybris.datahub.dto.count;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@XmlRootElement(name = "canonicalPublicationStatusCounts")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CanonicalPublicationStatusCountData extends AbstractItemStatusCountData
{
    private Long successCount;
    private Long internalErrorCount;
    private Long externalErrorCount;
    private Long ignoredCount;


    public Long getSuccessCount()
    {
        return this.successCount;
    }


    public void setSuccessCount(Long successCount)
    {
        this.successCount = successCount;
    }


    public Long getInternalErrorCount()
    {
        return this.internalErrorCount;
    }


    public void setInternalErrorCount(Long internalErrorCount)
    {
        this.internalErrorCount = internalErrorCount;
    }


    public Long getExternalErrorCount()
    {
        return this.externalErrorCount;
    }


    public void setExternalErrorCount(Long externalErrorCount)
    {
        this.externalErrorCount = externalErrorCount;
    }


    public Long getIgnoredCount()
    {
        return this.ignoredCount;
    }


    public void setIgnoredCount(Long ignoredCount)
    {
        this.ignoredCount = ignoredCount;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof CanonicalPublicationStatusCountData))
        {
            return false;
        }
        CanonicalPublicationStatusCountData that = (CanonicalPublicationStatusCountData)o;
        return (new EqualsBuilder())
                        .append(this.successCount, that.successCount)
                        .append(this.internalErrorCount, that.internalErrorCount)
                        .append(this.externalErrorCount, that.externalErrorCount)
                        .append(this.ignoredCount, that.ignoredCount)
                        .isEquals();
    }


    public int hashCode()
    {
        return (new HashCodeBuilder(17, 37))
                        .append(this.successCount)
                        .append(this.internalErrorCount)
                        .append(this.externalErrorCount)
                        .append(this.ignoredCount)
                        .toHashCode();
    }


    public String toString()
    {
        return "CanonicalPublicationStatusCountData{" + ((this.poolName != null) ? ("poolName='" + this.poolName + "', ") : "") + "successCount=" + this.successCount + ", internalErrorCount=" + this.internalErrorCount + ", externalErrorCount=" + this.externalErrorCount + ", ignoredCount="
                        + this.ignoredCount + "}";
    }
}
