package de.hybris.platform.cockpit.services.config.jaxb.base;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "default-property", propOrder = {"parameter"})
public class DefaultProperty
{
    protected List<Parameter> parameter;
    @XmlAttribute(required = true)
    protected String qualifier;
    @XmlAttribute
    protected String defaultEditor;
    @XmlAttribute
    protected Boolean baseProperty;


    public List<Parameter> getParameter()
    {
        if(this.parameter == null)
        {
            this.parameter = new ArrayList<>();
        }
        return this.parameter;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public void setQualifier(String value)
    {
        this.qualifier = value;
    }


    public String getDefaultEditor()
    {
        return this.defaultEditor;
    }


    public void setDefaultEditor(String value)
    {
        this.defaultEditor = value;
    }


    public boolean isBaseProperty()
    {
        if(this.baseProperty == null)
        {
            return true;
        }
        return this.baseProperty.booleanValue();
    }


    public void setBaseProperty(Boolean value)
    {
        this.baseProperty = value;
    }
}
