package de.hybris.platform.cockpit.services.config.jaxb.editor.preview;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AttributesType", propOrder = {"attribute"})
public class AttributesType
{
    @XmlElement(required = true)
    protected List<AttributeType> attribute;
    @XmlAttribute(name = "name")
    protected String name;


    public List<AttributeType> getAttribute()
    {
        if(this.attribute == null)
        {
            this.attribute = new ArrayList<>();
        }
        return this.attribute;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String value)
    {
        this.name = value;
    }
}
