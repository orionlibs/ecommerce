package de.hybris.bootstrap.typesystem.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "modelMethodType")
public class ModelMethodType
{
    @XmlAttribute(required = true)
    protected String name;
    @XmlAttribute
    protected Boolean deprecated;
    @XmlAttribute(name = "default")
    protected Boolean _default;


    public String getName()
    {
        return this.name;
    }


    public void setName(String value)
    {
        this.name = value;
    }


    public boolean isDeprecated()
    {
        if(this.deprecated == null)
        {
            return false;
        }
        return this.deprecated.booleanValue();
    }


    public void setDeprecated(Boolean value)
    {
        this.deprecated = value;
    }


    public boolean isDefault()
    {
        if(this._default == null)
        {
            return false;
        }
        return this._default.booleanValue();
    }


    public void setDefault(Boolean value)
    {
        this._default = value;
    }
}
