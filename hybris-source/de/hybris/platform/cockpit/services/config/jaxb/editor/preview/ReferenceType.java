package de.hybris.platform.cockpit.services.config.jaxb.editor.preview;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReferenceType", propOrder = {"columnsTitles", "rows"})
public class ReferenceType
{
    @XmlElement(required = true)
    protected ColumnsTitlesType columnsTitles;
    @XmlElement(required = true)
    protected TableRowsType rows;
    @XmlAttribute(name = "name")
    protected String name;


    public ColumnsTitlesType getColumnsTitles()
    {
        return this.columnsTitles;
    }


    public void setColumnsTitles(ColumnsTitlesType value)
    {
        this.columnsTitles = value;
    }


    public TableRowsType getRows()
    {
        return this.rows;
    }


    public void setRows(TableRowsType value)
    {
        this.rows = value;
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
