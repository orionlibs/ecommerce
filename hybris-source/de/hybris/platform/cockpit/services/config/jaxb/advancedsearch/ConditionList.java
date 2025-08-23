package de.hybris.platform.cockpit.services.config.jaxb.advancedsearch;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "conditionList", propOrder = {"condition"})
public class ConditionList
{
    @XmlElement(required = true)
    protected List<Condition> condition;
    @XmlAttribute
    protected Mode mode;


    public List<Condition> getCondition()
    {
        if(this.condition == null)
        {
            this.condition = new ArrayList<>();
        }
        return this.condition;
    }


    public Mode getMode()
    {
        if(this.mode == null)
        {
            return Mode.REPLACE;
        }
        return this.mode;
    }


    public void setMode(Mode value)
    {
        this.mode = value;
    }
}
