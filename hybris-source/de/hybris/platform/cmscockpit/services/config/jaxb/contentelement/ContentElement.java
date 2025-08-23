package de.hybris.platform.cmscockpit.services.config.jaxb.contentelement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "content-element")
public class ContentElement
{
    @XmlAttribute(required = true)
    protected String type;
    @XmlAttribute(required = true)
    protected String name;
    @XmlAttribute
    protected String description;
    @XmlAttribute
    protected String image;
    @XmlAttribute
    protected String refImage;
    @XmlAttribute
    protected String imageSmall;


    public String getType()
    {
        return this.type;
    }


    public void setType(String value)
    {
        this.type = value;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String value)
    {
        this.name = value;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setDescription(String value)
    {
        this.description = value;
    }


    public String getImage()
    {
        return this.image;
    }


    public void setImage(String value)
    {
        this.image = value;
    }


    public String getRefImage()
    {
        return this.refImage;
    }


    public void setRefImage(String value)
    {
        this.refImage = value;
    }


    public String getImageSmall()
    {
        return this.imageSmall;
    }


    public void setImageSmall(String value)
    {
        this.imageSmall = value;
    }
}
