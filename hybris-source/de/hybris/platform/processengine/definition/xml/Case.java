package de.hybris.platform.processengine.definition.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "case", propOrder = {"choice"})
public class Case
{
    protected List<Choice> choice;
    @XmlAttribute(name = "event", required = true)
    protected String event;


    public List<Choice> getChoice()
    {
        if(this.choice == null)
        {
            this.choice = new ArrayList<>();
        }
        return this.choice;
    }


    public String getEvent()
    {
        return this.event;
    }


    public void setEvent(String value)
    {
        this.event = value;
    }
}
