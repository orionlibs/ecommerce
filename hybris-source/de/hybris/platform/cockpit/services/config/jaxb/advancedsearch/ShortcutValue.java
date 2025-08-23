package de.hybris.platform.cockpit.services.config.jaxb.advancedsearch;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "shortcutValue", propOrder = {"value"})
public class ShortcutValue
{
    @XmlValue
    protected String value;
    @XmlAttribute
    protected String type;


    public String getValue()
    {
        return this.value;
    }


    public void setValue(String value)
    {
        this.value = value;
    }


    public String getType()
    {
        if(this.type == null)
        {
            return "java.lang.String";
        }
        return this.type;
    }


    public void setType(String value)
    {
        this.type = value;
    }
}
