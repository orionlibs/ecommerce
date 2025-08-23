package de.hybris.platform.cockpit.services.config.jaxb.gridview;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "property", propOrder = {"parameter"})
public class Property
{
    protected List<Parameter> parameter;
    @XmlAttribute(required = true)
    protected String qualifier;
    @XmlAttribute
    protected String prefix;


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


    public String getPrefix()
    {
        return this.prefix;
    }


    public void setPrefix(String value)
    {
        this.prefix = value;
    }
}
