package de.hybris.platform.cockpit.services.config.jaxb.listview;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "language")
public class Language
{
    @XmlAttribute(required = true)
    protected String isocode;


    public String getIsocode()
    {
        return this.isocode;
    }


    public void setIsocode(String value)
    {
        this.isocode = value;
    }
}
