package de.hybris.bootstrap.beangenerator.definitions.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractPojo")
@XmlSeeAlso({Bean.class, Enum.class})
public abstract class AbstractPojo
{
    @XmlAttribute(name = "class", required = true)
    protected String clazz;
    @XmlAttribute(name = "template")
    protected String template;


    public String getClazz()
    {
        return this.clazz;
    }


    public void setClazz(String value)
    {
        this.clazz = value;
    }


    public String getTemplate()
    {
        return this.template;
    }


    public void setTemplate(String value)
    {
        this.template = value;
    }
}
