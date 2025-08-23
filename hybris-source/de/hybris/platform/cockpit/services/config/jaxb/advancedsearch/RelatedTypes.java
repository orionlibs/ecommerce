package de.hybris.platform.cockpit.services.config.jaxb.advancedsearch;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "related-types", propOrder = {"type"})
public class RelatedTypes
{
    @XmlElement(required = true)
    protected List<Type> type;
    @XmlAttribute
    protected Boolean includeSubTypes;


    public List<Type> getType()
    {
        if(this.type == null)
        {
            this.type = new ArrayList<>();
        }
        return this.type;
    }


    public boolean isIncludeSubTypes()
    {
        if(this.includeSubTypes == null)
        {
            return true;
        }
        return this.includeSubTypes.booleanValue();
    }


    public void setIncludeSubTypes(Boolean value)
    {
        this.includeSubTypes = value;
    }
}
