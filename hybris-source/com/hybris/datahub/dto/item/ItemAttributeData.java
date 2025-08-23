package com.hybris.datahub.dto.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemAttributeData
{
    private String name;
    @XmlElement(name = "values")
    @JsonProperty("attributeValues")
    private Collection<AttributeValueData> attributeValues;


    public ItemAttributeData(String name, Collection<AttributeValueData> values)
    {
        this.name = name;
        this.attributeValues = new HashSet<>();
        if(values != null)
        {
            this.attributeValues.addAll(values);
        }
    }


    public ItemAttributeData(String name, AttributeValueData... values)
    {
        this(name, Arrays.asList(values));
    }


    public ItemAttributeData()
    {
        this(null, (Collection<AttributeValueData>)null);
    }


    public ItemAttributeData(String attrName)
    {
        this(attrName, (Collection<AttributeValueData>)null);
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public Collection<AttributeValueData> getAttributeValues()
    {
        return this.attributeValues;
    }


    public void setAttributeValues(Collection<AttributeValueData> values)
    {
        this.attributeValues = new HashSet<>();
        if(values != null)
        {
            this.attributeValues.addAll(values);
        }
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof ItemAttributeData))
        {
            return false;
        }
        ItemAttributeData that = (ItemAttributeData)o;
        return (Objects.equals(this.name, that.name) && this.attributeValues.equals(that.attributeValues));
    }


    public int hashCode()
    {
        int result = this.name.hashCode();
        result = 31 * result + this.attributeValues.hashCode();
        return result;
    }


    public String toString()
    {
        return this.name + " " + this.name;
    }
}
