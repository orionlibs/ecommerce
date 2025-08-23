package de.hybris.platform.cockpit.services.config.jaxb.editor.preview;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TitleType")
public class TitleType
{
    @XmlAttribute(name = "columnID")
    protected String columnID;
    @XmlAttribute(name = "value")
    protected String value;


    public String getColumnID()
    {
        return this.columnID;
    }


    public void setColumnID(String value)
    {
        this.columnID = value;
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
