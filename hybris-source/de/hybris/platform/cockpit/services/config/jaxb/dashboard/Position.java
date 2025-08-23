package de.hybris.platform.cockpit.services.config.jaxb.dashboard;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "position")
public class Position
{
    @XmlAttribute
    protected String widgetID;
    @XmlAttribute
    protected BigInteger column;
    @XmlAttribute
    protected BigInteger row;


    public String getWidgetID()
    {
        return this.widgetID;
    }


    public void setWidgetID(String value)
    {
        this.widgetID = value;
    }


    public BigInteger getColumn()
    {
        return this.column;
    }


    public void setColumn(BigInteger value)
    {
        this.column = value;
    }


    public BigInteger getRow()
    {
        return this.row;
    }


    public void setRow(BigInteger value)
    {
        this.row = value;
    }
}
