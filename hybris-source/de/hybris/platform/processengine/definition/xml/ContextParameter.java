package de.hybris.platform.processengine.definition.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contextParameter")
public class ContextParameter
{
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "use")
    protected ParameterUse use;
    @XmlAttribute(name = "type", required = true)
    protected String type;


    public String getName()
    {
        return this.name;
    }


    public void setName(String value)
    {
        this.name = value;
    }


    public ParameterUse getUse()
    {
        return this.use;
    }


    public void setUse(ParameterUse value)
    {
        this.use = value;
    }


    public String getType()
    {
        return this.type;
    }


    public void setType(String value)
    {
        this.type = value;
    }
}
