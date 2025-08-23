package de.hybris.platform.cockpit.services.config.jaxb.base;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"search", "label", "initialProperties", "defaultPropertySettings"})
@XmlRootElement(name = "base")
public class Base
{
    protected Search search;
    protected Label label;
    @XmlElement(name = "initial-properties")
    protected List<InitialProperties> initialProperties;
    @XmlElement(name = "default-property-settings")
    protected DefaultPropertyList defaultPropertySettings;


    public Search getSearch()
    {
        return this.search;
    }


    public void setSearch(Search value)
    {
        this.search = value;
    }


    public Label getLabel()
    {
        return this.label;
    }


    public void setLabel(Label value)
    {
        this.label = value;
    }


    public List<InitialProperties> getInitialProperties()
    {
        if(this.initialProperties == null)
        {
            this.initialProperties = new ArrayList<>();
        }
        return this.initialProperties;
    }


    public DefaultPropertyList getDefaultPropertySettings()
    {
        return this.defaultPropertySettings;
    }


    public void setDefaultPropertySettings(DefaultPropertyList value)
    {
        this.defaultPropertySettings = value;
    }
}
