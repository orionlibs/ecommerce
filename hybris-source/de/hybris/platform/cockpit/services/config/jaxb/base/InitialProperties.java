package de.hybris.platform.cockpit.services.config.jaxb.base;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "initial-properties", propOrder = {"propertyMapping"})
public class InitialProperties
{
    @XmlElement(name = "property-mapping", required = true)
    protected List<PropertyMapping> propertyMapping;
    @XmlAttribute(name = "source-object-template", required = true)
    protected String sourceObjectTemplate;


    public List<PropertyMapping> getPropertyMapping()
    {
        if(this.propertyMapping == null)
        {
            this.propertyMapping = new ArrayList<>();
        }
        return this.propertyMapping;
    }


    public String getSourceObjectTemplate()
    {
        return this.sourceObjectTemplate;
    }


    public void setSourceObjectTemplate(String value)
    {
        this.sourceObjectTemplate = value;
    }
}
