package de.hybris.bootstrap.typesystem.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "persistenceType", propOrder = {"columntype"})
public class PersistenceType
{
    protected List<ColumntypeType> columntype;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String type;
    @XmlAttribute
    protected String qualifier;
    @XmlAttribute
    protected String attributeHandler;


    public List<ColumntypeType> getColumntype()
    {
        if(this.columntype == null)
        {
            this.columntype = new ArrayList<>();
        }
        return this.columntype;
    }


    public String getType()
    {
        return this.type;
    }


    public void setType(String value)
    {
        this.type = value;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public void setQualifier(String value)
    {
        this.qualifier = value;
    }


    public String getAttributeHandler()
    {
        return this.attributeHandler;
    }


    public void setAttributeHandler(String value)
    {
        this.attributeHandler = value;
    }
}
