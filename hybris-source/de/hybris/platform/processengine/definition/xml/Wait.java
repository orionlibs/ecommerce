package de.hybris.platform.processengine.definition.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wait", propOrder = {"event", "_case", "timeout"})
public class Wait
{
    protected String event;
    @XmlElement(name = "case")
    protected Case _case;
    protected Timeout timeout;
    @XmlAttribute(name = "then")
    protected String then;
    @XmlAttribute(name = "prependProcessCode")
    protected Boolean prependProcessCode;
    @XmlAttribute(name = "id", required = true)
    protected String id;


    public String getEvent()
    {
        return this.event;
    }


    public void setEvent(String value)
    {
        this.event = value;
    }


    public Case getCase()
    {
        return this._case;
    }


    public void setCase(Case value)
    {
        this._case = value;
    }


    public Timeout getTimeout()
    {
        return this.timeout;
    }


    public void setTimeout(Timeout value)
    {
        this.timeout = value;
    }


    public String getThen()
    {
        return this.then;
    }


    public void setThen(String value)
    {
        this.then = value;
    }


    public boolean isPrependProcessCode()
    {
        if(this.prependProcessCode == null)
        {
            return true;
        }
        return this.prependProcessCode.booleanValue();
    }


    public void setPrependProcessCode(Boolean value)
    {
        this.prependProcessCode = value;
    }


    public String getId()
    {
        return this.id;
    }


    public void setId(String value)
    {
        this.id = value;
    }
}
