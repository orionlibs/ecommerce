package de.hybris.bootstrap.typesystem.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "maptypeType")
public class MaptypeType
{
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String code;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String argumenttype;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String returntype;
    @XmlAttribute
    protected Boolean autocreate;
    @XmlAttribute
    protected Boolean generate;
    @XmlAttribute
    protected Boolean redeclare;


    public String getCode()
    {
        return this.code;
    }


    public void setCode(String value)
    {
        this.code = value;
    }


    public String getArgumenttype()
    {
        return this.argumenttype;
    }


    public void setArgumenttype(String value)
    {
        this.argumenttype = value;
    }


    public String getReturntype()
    {
        return this.returntype;
    }


    public void setReturntype(String value)
    {
        this.returntype = value;
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


    public Boolean isRedeclare()
    {
        return this.redeclare;
    }


    public void setRedeclare(Boolean value)
    {
        this.redeclare = value;
    }
}
