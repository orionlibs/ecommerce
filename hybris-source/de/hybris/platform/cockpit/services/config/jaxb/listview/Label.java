package de.hybris.platform.cockpit.services.config.jaxb.listview;

import de.hybris.platform.cockpit.services.config.jaxb.ConfigLabel;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "label", propOrder = {"value"})
public class Label implements ConfigLabel
{
    @XmlValue
    protected String value;
    @XmlAttribute
    protected String key;
    @XmlAttribute
    protected String lang;


    public String getValue()
    {
        return this.value;
    }


    public void setValue(String value)
    {
        this.value = value;
    }


    public String getKey()
    {
        return this.key;
    }


    public void setKey(String value)
    {
        this.key = value;
    }


    public String getLang()
    {
        return this.lang;
    }


    public void setLang(String value)
    {
        this.lang = value;
    }
}
