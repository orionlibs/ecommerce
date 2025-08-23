package de.hybris.platform.processengine.definition.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "endState")
@XmlEnum
public enum EndState
{
    SUCCEEDED, FAILED, ERROR;


    public String value()
    {
        return name();
    }


    public static EndState fromValue(String v)
    {
        return valueOf(v);
    }
}
