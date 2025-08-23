package de.hybris.platform.processengine.definition.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userGroupType", propOrder = {"locmessage"})
public class UserGroupType
{
    @XmlElement(required = true)
    protected List<Localizedmessage> locmessage;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "message")
    protected String message;


    public List<Localizedmessage> getLocmessage()
    {
        if(this.locmessage == null)
        {
            this.locmessage = new ArrayList<>();
        }
        return this.locmessage;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String value)
    {
        this.name = value;
    }


    public String getMessage()
    {
        return this.message;
    }


    public void setMessage(String value)
    {
        this.message = value;
    }
}
