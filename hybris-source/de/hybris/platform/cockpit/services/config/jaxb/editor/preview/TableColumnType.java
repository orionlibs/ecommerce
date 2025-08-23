package de.hybris.platform.cockpit.services.config.jaxb.editor.preview;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TableColumnType")
public class TableColumnType
{
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "value")
    protected String value;


    public String getType()
    {
        return this.type;
    }


    public void setType(String value)
    {
        this.type = value;
    }


    public String getId()
    {
        return this.id;
    }


    public void setId(String value)
    {
        this.id = value;
    }


    public String getValue()
    {
        return this.value;
    }


    public void setValue(String value)
    {
        this.value = value;
    }
}
