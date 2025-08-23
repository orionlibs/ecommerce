package de.hybris.bootstrap.typesystem.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "enumModelType")
public class EnumModelType
{
    @XmlAttribute(name = "package")
    protected String _package;


    public String getPackage()
    {
        return this._package;
    }


    public void setPackage(String value)
    {
        this._package = value;
    }
}
