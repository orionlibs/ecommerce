package de.hybris.bootstrap.typesystem.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "collectiontypeType")
public class CollectiontypeType
{
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String code;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String elementtype;
    @XmlAttribute
    protected Boolean autocreate;
    @XmlAttribute
    protected Boolean generate;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String type;


    public String getCode()
    {
        return this.code;
    }


    public void setCode(String value)
    {
        this.code = value;
    }


    public String getElementtype()
    {
        return this.elementtype;
    }


    public void setElementtype(String value)
    {
        this.elementtype = value;
    }


    public boolean isAutocreate()
    {
        if(this.autocreate == null)
        {
            return true;
        }
        return this.autocreate.booleanValue();
    }


    public void setAutocreate(Boolean value)
    {
        this.autocreate = value;
    }


    public boolean isGenerate()
    {
        if(this.generate == null)
        {
            return true;
        }
        return this.generate.booleanValue();
    }


    public void setGenerate(Boolean value)
    {
        this.generate = value;
    }


    public String getType()
    {
        if(this.type == null)
        {
            return "collection";
        }
        return this.type;
    }


    public void setType(String value)
    {
        this.type = value;
    }
}
