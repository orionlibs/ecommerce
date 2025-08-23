package de.hybris.bootstrap.typesystem.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deploymentType")
public class DeploymentType
{
    @XmlAttribute(required = true)
    protected String table;
    @XmlAttribute(required = true)
    protected int typecode;
    @XmlAttribute
    protected String propertytable;


    public String getTable()
    {
        return this.table;
    }


    public void setTable(String value)
    {
        this.table = value;
    }


    public int getTypecode()
    {
        return this.typecode;
    }


    public void setTypecode(int value)
    {
        this.typecode = value;
    }


    public String getPropertytable()
    {
        return this.propertytable;
    }


    public void setPropertytable(String value)
    {
        this.propertytable = value;
    }
}
