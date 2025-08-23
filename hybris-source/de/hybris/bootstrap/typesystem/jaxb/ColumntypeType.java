package de.hybris.bootstrap.typesystem.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "columntypeType", propOrder = {"value"})
public class ColumntypeType
{
    @XmlElement(required = true)
    protected String value;
    @XmlAttribute
    protected String database;


    public String getValue()
    {
        return this.value;
    }


    public void setValue(String value)
    {
        this.value = value;
    }


    public String getDatabase()
    {
        return this.database;
    }


    public void setDatabase(String value)
    {
        this.database = value;
    }
}
