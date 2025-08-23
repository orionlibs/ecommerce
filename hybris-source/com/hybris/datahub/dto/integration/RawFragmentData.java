package com.hybris.datahub.dto.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawFragmentData implements Serializable
{
    private static final long serialVersionUID = -4232072738531830883L;
    public static final String DELETE = "delete";
    public static final String ISO_CODE = "isoCode";
    public static final String DH_SOURCE_ID = "dh_sourceId";
    public static final String DH_BATCH_ID = "dh_batchId";
    public static final String DH_TYPE = "dh_type";
    private String type;
    private String dataFeedName;
    private String extensionSource;
    private Map<String, String> valueMap;


    public RawFragmentData(String type, Map<String, String> valueMap, String dataFeedName, String extensionSource)
    {
        this(type, valueMap);
        this.dataFeedName = dataFeedName;
        this.extensionSource = extensionSource;
    }


    public RawFragmentData(String type, Map<String, String> valueMap)
    {
        this.type = type;
        this.valueMap = valueMap;
    }


    public RawFragmentData()
    {
    }


    public String getType()
    {
        return this.type;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public RawFragmentData forType(String type)
    {
        setType(type);
        return this;
    }


    public Map<String, String> getValueMap()
    {
        return this.valueMap;
    }


    public void setValueMap(Map<String, String> valueMap)
    {
        this.valueMap = valueMap;
    }


    public RawFragmentData withAttributeValue(String name, String value)
    {
        valueMap().put(name, value);
        return this;
    }


    private Map<String, String> valueMap()
    {
        if(this.valueMap == null)
        {
            setValueMap(new HashMap<>());
        }
        return this.valueMap;
    }


    public String getDataFeedName()
    {
        return this.dataFeedName;
    }


    public void setDataFeedName(String dataFeedName)
    {
        this.dataFeedName = dataFeedName;
    }


    public RawFragmentData forFeed(String feedName)
    {
        setDataFeedName(feedName);
        return this;
    }


    public String getExtensionSource()
    {
        return this.extensionSource;
    }


    public void setExtensionSource(String extensionSource)
    {
        this.extensionSource = extensionSource;
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
        RawFragmentData that = (RawFragmentData)o;
        if((this.dataFeedName != null) ? !this.dataFeedName.equals(that.dataFeedName) : (that.dataFeedName != null))
        {
            return false;
        }
        if((this.extensionSource != null) ? !this.extensionSource.equals(that.extensionSource) : (that.extensionSource != null))
        {
            return false;
        }
        if((this.type != null) ? !this.type.equals(that.type) : (that.type != null))
        {
            return false;
        }
        if((this.valueMap != null) ? !this.valueMap.equals(that.valueMap) : (that.valueMap != null))
        {
            return false;
        }
        return true;
    }


    public int hashCode()
    {
        int result = (this.type != null) ? this.type.hashCode() : 0;
        result = 31 * result + ((this.dataFeedName != null) ? this.dataFeedName.hashCode() : 0);
        result = 31 * result + ((this.extensionSource != null) ? this.extensionSource.hashCode() : 0);
        result = 31 * result + ((this.valueMap != null) ? this.valueMap.hashCode() : 0);
        return result;
    }


    public String toString()
    {
        return "RawFragmentData{type='" + this.type + "', dataFeedName='" + this.dataFeedName + "', extensionSource='" + this.extensionSource + "'}";
    }
}
