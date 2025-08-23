package de.hybris.platform.omsbackoffice.config.impl.jaxb.hybris.customersupportrefineby;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "MergeMode")
@XmlEnum
public enum MergeMode
{
    MERGE, REPLACE, REMOVE;


    public String value()
    {
        return name();
    }


    public static MergeMode fromValue(String v)
    {
        return valueOf(v);
    }
}
