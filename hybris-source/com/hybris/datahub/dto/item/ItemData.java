package com.hybris.datahub.dto.item;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hybris.datahub.dto.publication.CanonicalItemPublicationStatusData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({CanonicalItemPublicationStatusData.class})
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemData
{
    @JsonProperty
    private String type;
    @JsonProperty
    private String dataPool;
    @JsonProperty
    private Long id;
    @XmlElement(name = "attribute")
    @JsonProperty("itemAttributeDataList")
    private List<ItemAttributeData> itemAttributeDataList = new LinkedList<>();
    @XmlElement(name = "link")
    @JsonProperty("links")
    private List<Link> links = new ArrayList<>();


    public String getType()
    {
        return this.type;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public List<ItemAttributeData> getItemAttributeDataList()
    {
        return this.itemAttributeDataList;
    }


    public void setItemAttributeDataList(List<ItemAttributeData> attributes)
    {
        this.itemAttributeDataList = new LinkedList<>();
        if(attributes != null)
        {
            this.itemAttributeDataList = attributes;
        }
    }


    public void addItemAttributeData(ItemAttributeData attributeData)
    {
        if(attributeData != null)
        {
            this.itemAttributeDataList.add(attributeData);
        }
    }


    public ItemAttributeData getItemAttributeData(String name)
    {
        for(ItemAttributeData attrib : this.itemAttributeDataList)
        {
            if(Objects.equals(name, attrib.getName()))
            {
                return attrib;
            }
        }
        return null;
    }


    public List<Link> getLinks()
    {
        return this.links;
    }


    public void setLinks(List<Link> links)
    {
        this.links = links;
    }


    public boolean isMatching(ItemData sample)
    {
        if(sample != null && Objects.equals(this.type, sample.type))
        {
            for(ItemAttributeData expectedAttr : sample.getItemAttributeDataList())
            {
                ItemAttributeData attributeMatch = getItemAttributeData(expectedAttr.getName());
                if(!expectedAttr.equals(attributeMatch))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    public String toString()
    {
        return this.type + " - " + this.type;
    }


    public Object getAttributeValue(String attrName)
    {
        ItemAttributeData attr = getItemAttributeData(attrName);
        if(attr != null)
        {
            Collection<AttributeValueData> valueData = attr.getAttributeValues();
            if(!valueData.isEmpty())
            {
                Collection<?> values = ((AttributeValueData)valueData.iterator().next()).getValue();
                if(!values.isEmpty())
                {
                    return values.iterator().next();
                }
            }
        }
        return null;
    }


    public String getAttributeValue(String attribute, Locale loc)
    {
        if(loc == null)
        {
            return null;
        }
        ItemAttributeData attr = getItemAttributeData(attribute);
        if(attr == null)
        {
            return null;
        }
        for(AttributeValueData valueData : attr.getAttributeValues())
        {
            if(loc.toLanguageTag().equals(valueData.getIsoCode()))
            {
                Collection<?> values = valueData.getValue();
                return values.isEmpty() ? null : values.iterator().next();
            }
        }
        return null;
    }


    public String getStatus()
    {
        Object status = getAttributeValue("status");
        return (status != null) ? status.toString() : null;
    }


    public String getIntegrationKey()
    {
        Object key = getAttributeValue("integrationKey");
        return (key != null) ? key.toString() : null;
    }


    public String getDataPool()
    {
        return this.dataPool;
    }


    public void setDataPool(String dataPool)
    {
        this.dataPool = dataPool;
    }


    public Long getId()
    {
        return this.id;
    }


    public void setId(Long id)
    {
        this.id = id;
    }
}
