package com.hybris.datahub.dto.pool;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PoolData
{
    private Long poolId;
    private String poolName;
    private String compositionStrategy;
    private String publicationStrategy;
    private Boolean deletable;


    public static PoolData poolData()
    {
        return new PoolData();
    }


    public Long getPoolId()
    {
        return this.poolId;
    }


    public void setPoolId(Long poolId)
    {
        this.poolId = poolId;
    }


    public PoolData withId(Long id)
    {
        setPoolId(id);
        return this;
    }


    public String getPoolName()
    {
        return this.poolName;
    }


    public void setPoolName(String poolName)
    {
        this.poolName = poolName;
    }


    public PoolData withName(String name)
    {
        setPoolName(name);
        return this;
    }


    public PoolData withDeletable(boolean deletable)
    {
        setDeletable(Boolean.valueOf(deletable));
        return this;
    }


    public String getCompositionStrategy()
    {
        return this.compositionStrategy;
    }


    public void setCompositionStrategy(String compositionStrategy)
    {
        this.compositionStrategy = compositionStrategy;
    }


    public PoolData withCompositionStrategy(String strategy)
    {
        setCompositionStrategy(strategy);
        return this;
    }


    public String getPublicationStrategy()
    {
        return this.publicationStrategy;
    }


    public void setPublicationStrategy(String publicationStrategy)
    {
        this.publicationStrategy = publicationStrategy;
    }


    public PoolData withPublicationStrategy(String strategy)
    {
        setPublicationStrategy(strategy);
        return this;
    }


    public Boolean getDeletable()
    {
        return this.deletable;
    }


    public void setDeletable(Boolean deletable)
    {
        this.deletable = deletable;
    }


    public String toString()
    {
        return "PoolData{poolId=" + this.poolId + ", poolName='" + this.poolName + "', compositionStrategy='" + this.compositionStrategy + "', publicationStrategy='" + this.publicationStrategy + "', deletable=" + this.deletable + "}";
    }
}
