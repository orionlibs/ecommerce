package de.hybris.platform.processengine.definition.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "parameterUse")
@XmlEnum
public enum ParameterUse
{
    REQUIRED("required"),
    OPTIONAL("optional");
    private final String value;


    ParameterUse(String v)
    {
        this.value = v;
    }


    public String value()
    {
        return this.value;
    }


    public static ParameterUse fromValue(String v)
    {
        for(ParameterUse c : values())
        {
            if(c.value.equals(v))
            {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
