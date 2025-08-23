package com.hybris.datahub.dto.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RawItemType")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawItemTypeData extends ItemTypeData
{
    public RawItemTypeData()
    {
    }


    public RawItemTypeData(String type)
    {
        setName(type);
    }


    public RawItemTypeData(String type, String desc)
    {
        setName(type);
        setDescription(desc);
    }
}
