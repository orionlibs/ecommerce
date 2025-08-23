package de.hybris.platform.cockpit.services.config.jaxb.advancedsearch;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "group", propOrder = {"label", "property", "group"})
@XmlSeeAlso({RootGroup.class})
public class Group
{
    protected List<Label> label;
    protected List<Property> property;
    protected List<Group> group;
    @XmlAttribute
    protected String name;
    @XmlAttribute
    protected Boolean visible;


    public List<Label> getLabel()
    {
        if(this.label == null)
        {
            this.label = new ArrayList<>();
        }
        return this.label;
    }


    public List<Property> getProperty()
    {
        if(this.property == null)
        {
            this.property = new ArrayList<>();
        }
        return this.property;
    }


    public List<Group> getGroup()
    {
        if(this.group == null)
        {
            this.group = new ArrayList<>();
        }
        return this.group;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String value)
    {
        this.name = value;
    }


    public boolean isVisible()
    {
        if(this.visible == null)
        {
            return true;
        }
        return this.visible.booleanValue();
    }


    public void setVisible(Boolean value)
    {
        this.visible = value;
    }
}
