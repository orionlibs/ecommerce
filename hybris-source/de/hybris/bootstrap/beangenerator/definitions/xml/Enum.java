package de.hybris.bootstrap.beangenerator.definitions.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "enum", propOrder = {"description", "value"})
public class Enum extends AbstractPojo
{
    protected String description;
    @XmlAttribute(name = "deprecated")
    protected String deprecated;
    @XmlAttribute(name = "deprecatedSince")
    protected String deprecatedSince;
    @XmlElement(required = true)
    protected List<String> value;


    public String getDescription()
    {
        return this.description;
    }


    public void setDescription(String value)
    {
        this.description = value;
    }


    public String getDeprecated()
    {
        return this.deprecated;
    }


    public void setDeprecated(String value)
    {
        this.deprecated = value;
    }


    public String getDeprecatedSince()
    {
        return this.deprecatedSince;
    }


    public void setDeprecatedSince(String value)
    {
        this.deprecatedSince = value;
    }


    public List<String> getValue()
    {
        if(this.value == null)
        {
            this.value = new ArrayList<>();
        }
        return this.value;
    }
}
