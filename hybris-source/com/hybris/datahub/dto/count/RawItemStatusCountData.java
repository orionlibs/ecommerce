package com.hybris.datahub.dto.count;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rawItemStatusCounts")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawItemStatusCountData extends AbstractItemStatusCountData
{
    private Long ignoredCount;
    private Long pendingCount;
    private Long processedCount;


    public Long getIgnoredCount()
    {
        return this.ignoredCount;
    }


    public void setIgnoredCount(Long ignoredCount)
    {
        this.ignoredCount = ignoredCount;
    }


    public Long getPendingCount()
    {
        return this.pendingCount;
    }


    public void setPendingCount(Long pendingCount)
    {
        this.pendingCount = pendingCount;
    }


    public Long getProcessedCount()
    {
        return this.processedCount;
    }


    public void setProcessedCount(Long processedCount)
    {
        this.processedCount = processedCount;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        RawItemStatusCountData that = (RawItemStatusCountData)o;
        if((this.poolName != null) ? !this.poolName.equals(that.poolName) : (that.poolName != null))
        {
            return false;
        }
        if((this.ignoredCount != null) ? !this.ignoredCount.equals(that.ignoredCount) : (that.ignoredCount != null))
        {
            return false;
        }
        if((this.pendingCount != null) ? !this.pendingCount.equals(that.pendingCount) : (that.pendingCount != null))
        {
            return false;
        }
        return (this.processedCount != null) ? this.processedCount.equals(that.processedCount) : ((that.processedCount == null));
    }


    public int hashCode()
    {
        int result = (this.poolName != null) ? this.poolName.hashCode() : 0;
        result = 31 * result + ((this.ignoredCount != null) ? this.ignoredCount.hashCode() : 0);
        result = 31 * result + ((this.pendingCount != null) ? this.pendingCount.hashCode() : 0);
        result = 31 * result + ((this.processedCount != null) ? this.processedCount.hashCode() : 0);
        return result;
    }


    public String toString()
    {
        return "RawItemStatusCountData{" + ((this.poolName != null) ? ("poolName='" + this.poolName + "', ") : "") + "ignoredCount=" + this.ignoredCount + ", pendingCount=" + this.pendingCount + ", processedCount=" + this.processedCount + "}";
    }
}
