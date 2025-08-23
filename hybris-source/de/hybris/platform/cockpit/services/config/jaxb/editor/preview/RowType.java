package de.hybris.platform.cockpit.services.config.jaxb.editor.preview;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RowType", propOrder = {"attribute", "columnsTitles", "rows", "reference", "attributes"})
public class RowType
{
    protected AttributeType attribute;
    protected ColumnsTitlesType columnsTitles;
    protected TableRowsType rows;
    protected List<ReferenceType> reference;
    protected AttributesType attributes;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "name")
    protected String name;


    public AttributeType getAttribute()
    {
        return this.attribute;
    }


    public void setAttribute(AttributeType value)
    {
        this.attribute = value;
    }


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


    public List<ReferenceType> getReference()
    {
        if(this.reference == null)
        {
            this.reference = new ArrayList<>();
        }
        return this.reference;
    }


    public AttributesType getAttributes()
    {
        return this.attributes;
    }


    public void setAttributes(AttributesType value)
    {
        this.attributes = value;
    }


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
}
