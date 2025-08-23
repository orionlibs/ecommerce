package com.hybris.datahub.dto.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ItemClass")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public enum ItemClassData
{
    RAW("raw"),
    CANONICAL("canonical");
    private final String itemClass;


    ItemClassData(String value)
    {
        this.itemClass = value;
    }


    public String getItemClass()
    {
        return this.itemClass;
    }
}
