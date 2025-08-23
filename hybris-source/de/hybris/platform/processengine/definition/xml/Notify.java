package de.hybris.platform.processengine.definition.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "notify", propOrder = {"userGroup"})
public class Notify
{
    @XmlElement(required = true)
    protected List<UserGroupType> userGroup;
    @XmlAttribute(name = "then")
    protected String then;
    @XmlAttribute(name = "id", required = true)
    protected String id;


    public List<UserGroupType> getUserGroup()
    {
        if(this.userGroup == null)
        {
            this.userGroup = new ArrayList<>();
        }
        return this.userGroup;
    }


    public String getThen()
    {
        return this.then;
    }


    public void setThen(String value)
    {
        this.then = value;
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
