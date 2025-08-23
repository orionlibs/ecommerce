package com.hybris.datahub.dto.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RawAttribute")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawAttributeData extends AttributeData
{
    public RawAttributeData()
    {
    }


    public RawAttributeData(String rawType, String attrName)
    {
        setItemType(rawType);
        setName(attrName);
    }
}
