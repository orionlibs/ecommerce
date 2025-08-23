package de.hybris.platform.cockpit.services.config.jaxb.editor.preview;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AttributeType", propOrder = {"name", "value", "imgUrl"})
public class AttributeType
{
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String value;
    @XmlElement(required = true)
    protected String imgUrl;


    public String getName()
    {
        return this.name;
    }


    public void setName(String value)
    {
        this.name = value;
    }


    public String getValue()
    {
        return this.value;
    }


    public void setValue(String value)
    {
        this.value = value;
    }


    public String getImgUrl()
    {
        return this.imgUrl;
    }


    public void setImgUrl(String value)
    {
        this.imgUrl = value;
    }
}
