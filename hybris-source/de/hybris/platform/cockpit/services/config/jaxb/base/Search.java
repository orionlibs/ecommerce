package de.hybris.platform.cockpit.services.config.jaxb.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "search", propOrder = {"searchProperties", "sortProperties"})
public class Search
{
    @XmlElement(name = "search-properties", required = true)
    protected PropertyList searchProperties;
    @XmlElement(name = "sort-properties")
    protected PropertyList sortProperties;


    public PropertyList getSearchProperties()
    {
        return this.searchProperties;
    }


    public void setSearchProperties(PropertyList value)
    {
        this.searchProperties = value;
    }


    public PropertyList getSortProperties()
    {
        return this.sortProperties;
    }


    public void setSortProperties(PropertyList value)
    {
        this.sortProperties = value;
    }
}
