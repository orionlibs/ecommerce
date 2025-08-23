package de.hybris.bootstrap.beangenerator.definitions.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "property", propOrder = {"description", "hints", "annotations"})
public class Property
{
    protected String description;
    protected Hints hints;
    protected List<Annotations> annotations;
    @XmlAttribute(required = true)
    protected String name;
    @XmlAttribute(required = true)
    protected String type;
    @XmlAttribute(name = "deprecated")
    protected String deprecated;
    @XmlAttribute(name = "deprecatedSince")
    protected String deprecatedSince;
    @XmlAttribute(name = "equals")
    protected boolean _equals;


    public String getDescription()
    {
        return this.description;
    }


    public void setDescription(String value)
    {
        this.description = value;
    }


    public Hints getHints()
    {
        return this.hints;
    }


    public void setHints(Hints value)
    {
        this.hints = value;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String value)
    {
        this.name = value;
    }


    public String getType()
    {
        return this.type;
    }


    public void setType(String value)
    {
        this.type = value;
    }


    public List<Annotations> getAnnotations()
    {
        if(this.annotations == null)
        {
            this.annotations = new ArrayList<>();
        }
        return this.annotations;
    }


    public String getDeprecated()
    {
        return this.deprecated;
    }


    public void setDeprecated(String deprecated)
    {
        this.deprecated = deprecated;
    }


    public String getDeprecatedSince()
    {
        return this.deprecatedSince;
    }


    public void setDeprecatedSince(String deprecatedSince)
    {
        this.deprecatedSince = deprecatedSince;
    }


    public boolean isEquals()
    {
        return this._equals;
    }


    public void setEquals(boolean _equals)
    {
        this._equals = _equals;
    }
}
