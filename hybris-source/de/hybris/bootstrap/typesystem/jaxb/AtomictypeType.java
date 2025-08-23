package de.hybris.bootstrap.typesystem.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "atomictypeType")
public class AtomictypeType
{
    @XmlAttribute(name = "class", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String clazz;
    @XmlAttribute
    protected Boolean autocreate;
    @XmlAttribute
    protected Boolean generate;
    @XmlAttribute(name = "extends")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String _extends;


    public String getClazz()
    {
        return this.clazz;
    }


    public void setClazz(String value)
    {
        this.clazz = value;
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


    public String getExtends()
    {
        return this._extends;
    }


    public void setExtends(String value)
    {
        this._extends = value;
    }
}
