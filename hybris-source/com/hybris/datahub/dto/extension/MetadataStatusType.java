package com.hybris.datahub.dto.extension;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "metadataStatusType")
@XmlEnum
public enum MetadataStatusType
{
    ACTIVE, INACTIVE;


    public String value()
    {
        return name();
    }


    public static MetadataStatusType fromValue(String v)
    {
        return valueOf(v);
    }
}
