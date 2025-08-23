package com.hybris.datahub.dto.publication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TargetSystemPublicationData
{
    private Long publicationId;
    private Date startTime;
    private Date endTime;
    private String status;
    private String targetSystemName;
    private Long actionId;
    private String poolName;
    private long canonicalItemCount;
    private long internalErrorCount;
    private long externalErrorCount;
    private long ignoredCount;


    public Long getPublicationId()
    {
        return this.publicationId;
    }


    public void setPublicationId(Long publicationId)
    {
        this.publicationId = publicationId;
    }


    public Date getStartTime()
    {
        return this.startTime;
    }


    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }


    public Date getEndTime()
    {
        return this.endTime;
    }


    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }


    public String getStatus()
    {
        return this.status;
    }


    public void setStatus(String status)
    {
        this.status = status;
    }


    public String getTargetSystemName()
    {
        return this.targetSystemName;
    }


    public void setTargetSystemName(String targetSystemName)
    {
        this.targetSystemName = targetSystemName;
    }


    public TargetSystemPublicationData toSystem(String system)
    {
        setTargetSystemName(system);
        return this;
    }


    public Long getActionId()
    {
        return this.actionId;
    }


    public void setActionId(Long actionId)
    {
        this.actionId = actionId;
    }


    public String getPoolName()
    {
        return this.poolName;
    }


    public void setPoolName(String poolName)
    {
        this.poolName = poolName;
    }


    public TargetSystemPublicationData fromPool(String poolName)
    {
        setPoolName(poolName);
        return this;
    }


    @XmlTransient
    @JsonIgnore
    public int getNumberOfErrors()
    {
        return (int)(this.internalErrorCount + this.externalErrorCount);
    }


    public long getCanonicalItemCount()
    {
        return this.canonicalItemCount;
    }


    public void setCanonicalItemCount(long canonicalItemCount)
    {
        this.canonicalItemCount = canonicalItemCount;
    }


    public long getInternalErrorCount()
    {
        return this.internalErrorCount;
    }


    public void setInternalErrorCount(long internalErrorCount)
    {
        this.internalErrorCount = internalErrorCount;
    }


    public long getExternalErrorCount()
    {
        return this.externalErrorCount;
    }


    public void setExternalErrorCount(long externalErrorCount)
    {
        this.externalErrorCount = externalErrorCount;
    }


    public long getIgnoredCount()
    {
        return this.ignoredCount;
    }


    public void setIgnoredCount(long ignoredCount)
    {
        this.ignoredCount = ignoredCount;
    }


    @XmlTransient
    @JsonIgnore
    public long getSuccessfulCount()
    {
        return (this.canonicalItemCount > 0L) ? (this.canonicalItemCount - this.externalErrorCount - this.internalErrorCount - this.ignoredCount) : 0L;
    }


    public String toString()
    {
        return "TargetSystemPublicationData{publicationId=" + this.publicationId + ", startTime=" + this.startTime + ", endTime=" + this.endTime + ", status='" + this.status + "', targetSystemName='" + this.targetSystemName + "', actionId=" + this.actionId + ", poolName='" + this.poolName
                        + "', canonicalItemCount=" + this.canonicalItemCount + ", internalErrorCount=" + this.internalErrorCount + ", externalErrorCount=" + this.externalErrorCount + ", numberOfErrors=" +
                        getNumberOfErrors() + ", ignoredCount=" + this.ignoredCount + "}";
    }
}
