package de.hybris.platform.cockpit.services.config.jaxb.advancedsearch;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"group", "relatedTypes"})
@XmlRootElement(name = "advanced-search")
public class AdvancedSearch
{
    @XmlElement(required = true)
    protected RootGroup group;
    @XmlElement(name = "related-types")
    protected RelatedTypes relatedTypes;


    public RootGroup getGroup()
    {
        return this.group;
    }


    public void setGroup(RootGroup value)
    {
        this.group = value;
    }


    public RelatedTypes getRelatedTypes()
    {
        return this.relatedTypes;
    }


    public void setRelatedTypes(RelatedTypes value)
    {
        this.relatedTypes = value;
    }
}
