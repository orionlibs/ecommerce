package de.hybris.platform.cockpit.services.config.jaxb.editor.preview;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SectionType", propOrder = {"row"})
public class SectionType
{
    @XmlElement(required = true)
    protected List<Object> row;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "type")
    protected String type;


    public List<Object> getRow()
    {
        if(this.row == null)
        {
            this.row = new ArrayList();
        }
        return this.row;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String value)
    {
        this.name = value;
    }


    public String getType()
    {
        return this.type;
    }


    public void setType(String value)
    {
        this.type = value;
    }
}
