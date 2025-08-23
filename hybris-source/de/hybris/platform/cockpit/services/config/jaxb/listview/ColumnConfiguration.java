package de.hybris.platform.cockpit.services.config.jaxb.listview;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "columnConfiguration")
@XmlSeeAlso({Property.class, Custom.class, CellRenderer.class})
public abstract class ColumnConfiguration
{
    @XmlAttribute
    protected Boolean visible;
    @XmlAttribute
    protected Boolean sortable;
    @XmlAttribute
    protected Boolean editable;
    @XmlAttribute
    protected String name;
    @XmlAttribute
    protected String width;
    @XmlAttribute
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger position;
    protected CellRenderer renderer;


    public boolean isVisible()
    {
        if(this.visible == null)
        {
            return false;
        }
        return this.visible.booleanValue();
    }


    public void setVisible(Boolean value)
    {
        this.visible = value;
    }


    public boolean isSortable()
    {
        if(this.sortable == null)
        {
            return false;
        }
        return this.sortable.booleanValue();
    }


    public void setSortable(Boolean value)
    {
        this.sortable = value;
    }


    public boolean isEditable()
    {
        if(this.editable == null)
        {
            return true;
        }
        return this.editable.booleanValue();
    }


    public void setEditable(Boolean value)
    {
        this.editable = value;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String value)
    {
        this.name = value;
    }


    public String getWidth()
    {
        return this.width;
    }


    public void setWidth(String value)
    {
        this.width = value;
    }


    public void setRenderer(CellRenderer renderer)
    {
        this.renderer = renderer;
    }


    public CellRenderer getRenderer()
    {
        return this.renderer;
    }


    public BigInteger getPosition()
    {
        if(this.position == null)
        {
            return new BigInteger("1");
        }
        return this.position;
    }


    public void setPosition(BigInteger value)
    {
        this.position = value;
    }
}
