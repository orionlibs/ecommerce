package com.hybris.datahub.dto.dataloading;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hybris.datahub.dto.feed.FeedData;
import java.util.Date;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataLoadingActionData
{
    private Long actionId;
    private String status;
    private Long count;
    private FeedData feed;
    private Date startTime;
    private Date endTime;
    private String message;


    public Long getActionId()
    {
        return this.actionId;
    }


    public DataLoadingActionData setActionId(Long actionId)
    {
        this.actionId = actionId;
        return this;
    }


    public String getStatus()
    {
        return this.status;
    }


    public DataLoadingActionData setStatus(String status)
    {
        this.status = status;
        return this;
    }


    public Long getCount()
    {
        return this.count;
    }


    public DataLoadingActionData setCount(Long count)
    {
        this.count = count;
        return this;
    }


    public FeedData getFeed()
    {
        return this.feed;
    }


    public DataLoadingActionData setFeed(FeedData feed)
    {
        this.feed = feed;
        return this;
    }


    public Date getStartTime()
    {
        return this.startTime;
    }


    public DataLoadingActionData setStartTime(Date startTime)
    {
        this.startTime = startTime;
        return this;
    }


    public Date getEndTime()
    {
        return this.endTime;
    }


    public DataLoadingActionData setEndTime(Date endTime)
    {
        this.endTime = endTime;
        return this;
    }


    public String getMessage()
    {
        return this.message;
    }


    public DataLoadingActionData setMessage(String msg)
    {
        this.message = msg;
        return this;
    }


    public String toString()
    {
        return "DataLoadingActionData{actionId=" + this.actionId + ", status='" + this.status + "', count=" + this.count + ", feed=" + this.feed + ", startTime=" + this.startTime + ", endTime=" + this.endTime + "}";
    }


    public boolean equals(Object o)
    {
        return (o instanceof DataLoadingActionData && Objects.equals(this.actionId, ((DataLoadingActionData)o).actionId));
    }


    public int hashCode()
    {
        return (this.actionId != null) ? this.actionId.hashCode() : 0;
    }
}
