package de.hybris.platform.cockpit.services.config.jaxb.advancedsearch;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "mode")
@XmlEnum
public enum Mode
{
    APPEND("append"),
    REPLACE("replace"),
    EXCLUDE("exclude");
    private final String value;


    Mode(String v)
    {
        this.value = v;
    }


    public String value()
    {
        return this.value;
    }


    public static Mode fromValue(String v)
    {
        for(Mode c : values())
        {
            if(c.value.equals(v))
            {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
