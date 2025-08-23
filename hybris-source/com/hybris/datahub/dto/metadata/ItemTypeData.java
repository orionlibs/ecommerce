package com.hybris.datahub.dto.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ItemType")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemTypeData implements Serializable
{
    private String name;
    private String description;


    public String getName()
    {
        return this.name;
    }


    public ItemTypeData setName(String name)
    {
        this.name = name;
        return this;
    }


    public String getDescription()
    {
        return this.description;
    }


    public ItemTypeData setDescription(String description)
    {
        this.description = description;
        return this;
    }


    public boolean equals(Object obj)
    {
        if(obj instanceof ItemTypeData)
        {
            ItemTypeData other = (ItemTypeData)obj;
            return ((this.name == null && other.getName() == null) || (this.name != null && this.name.equals(other.getName())));
        }
        return false;
    }


    public int hashCode()
    {
        return (this.name != null) ? this.name.hashCode() : -1;
    }


    public String toString()
    {
        return "ItemTypeData{name='" + this.name + "'}";
    }
}
