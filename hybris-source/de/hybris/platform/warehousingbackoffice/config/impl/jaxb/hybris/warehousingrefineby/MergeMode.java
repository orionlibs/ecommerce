package de.hybris.platform.warehousingbackoffice.config.impl.jaxb.hybris.warehousingrefineby;

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
