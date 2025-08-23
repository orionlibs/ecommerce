package com.hybris.datahub.dto.feed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedData
{
    private Long feedId;
    private String name;
    private String description;
    private String poolingStrategy;
    private String poolingCondition;
    private String defaultCompositionStrategy;
    private String defaultPublicationStrategy;


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public Long getFeedId()
    {
        return this.feedId;
    }


    public void setFeedId(Long feedId)
    {
        this.feedId = feedId;
    }


    public String getPoolingStrategy()
    {
        return this.poolingStrategy;
    }


    public void setPoolingStrategy(String poolingStrategy)
    {
        this.poolingStrategy = poolingStrategy;
    }


    public String getPoolingCondition()
    {
        return this.poolingCondition;
    }


    public void setPoolingCondition(String poolingCondition)
    {
        this.poolingCondition = poolingCondition;
    }


    public String getDefaultCompositionStrategy()
    {
        return this.defaultCompositionStrategy;
    }


    public void setDefaultCompositionStrategy(String defaultCompositionStrategy)
    {
        this.defaultCompositionStrategy = defaultCompositionStrategy;
    }


    public String getDefaultPublicationStrategy()
    {
        return this.defaultPublicationStrategy;
    }


    public void setDefaultPublicationStrategy(String defaultPublicationStrategy)
    {
        this.defaultPublicationStrategy = defaultPublicationStrategy;
    }


    public String toString()
    {
        return "FeedData{feedId=" + this.feedId + ", name='" + this.name + "', description='" + this.description + "', poolingStrategy='" + this.poolingStrategy + "', poolingCondition='" + this.poolingCondition + "', defaultCompositionStrategy='" + this.defaultCompositionStrategy
                        + "', defaultPublicationStrategy='" + this.defaultPublicationStrategy + "'}";
    }
}
