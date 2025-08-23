package com.hybris.datahub.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PoolActionData
{
    private Long actionId;
    private String type;
    private Date startTime;
    private Date endTime;
    private String status;
    private String poolName;


    public Long getActionId()
    {
        return this.actionId;
    }


    public void setActionId(Long actionId)
    {
        this.actionId = actionId;
    }


    public String getType()
    {
        return this.type;
    }


    public void setType(String type)
    {
        this.type = type;
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


    public String getPoolName()
    {
        return this.poolName;
    }


    public void setPoolName(String poolName)
    {
        this.poolName = poolName;
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
        PoolActionData that = (PoolActionData)o;
        if((this.actionId != null) ? !this.actionId.equals(that.actionId) : (that.actionId != null))
        {
            return false;
        }
        if((this.endTime != null) ? !this.endTime.equals(that.endTime) : (that.endTime != null))
        {
            return false;
        }
        if((this.poolName != null) ? !this.poolName.equals(that.poolName) : (that.poolName != null))
        {
            return false;
        }
        if((this.startTime != null) ? !this.startTime.equals(that.startTime) : (that.startTime != null))
        {
            return false;
        }
        if((this.status != null) ? !this.status.equals(that.status) : (that.status != null))
        {
            return false;
        }
        if((this.type != null) ? !this.type.equals(that.type) : (that.type != null))
        {
            return false;
        }
    }


    public int hashCode()
    {
        int result = (this.actionId != null) ? this.actionId.hashCode() : 0;
        result = 31 * result + ((this.type != null) ? this.type.hashCode() : 0);
        result = 31 * result + ((this.startTime != null) ? this.startTime.hashCode() : 0);
        result = 31 * result + ((this.endTime != null) ? this.endTime.hashCode() : 0);
        result = 31 * result + ((this.status != null) ? this.status.hashCode() : 0);
        result = 31 * result + ((this.poolName != null) ? this.poolName.hashCode() : 0);
        return result;
    }


    public String toString()
    {
        return "PoolActionData{actionId=" + this.actionId + ", type='" + this.type + "', startTime=" + this.startTime + ", endTime=" + this.endTime + ", status='" + this.status + "', poolName='" + this.poolName + "'}";
    }
}
