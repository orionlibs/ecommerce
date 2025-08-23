package de.hybris.platform.processengine.definition.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.Duration;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "timeout")
public class Timeout
{
    @XmlAttribute(name = "delay", required = true)
    protected Duration delay;
    @XmlAttribute(name = "then", required = true)
    protected String then;


    public Duration getDelay()
    {
        return this.delay;
    }


    public void setDelay(Duration value)
    {
        this.delay = value;
    }


    public String getThen()
    {
        return this.then;
    }


    public void setThen(String value)
    {
        this.then = value;
    }
}
