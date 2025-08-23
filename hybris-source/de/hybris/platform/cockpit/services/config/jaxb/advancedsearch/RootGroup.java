package de.hybris.platform.cockpit.services.config.jaxb.advancedsearch;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "root-group")
public class RootGroup extends Group
{
    @XmlAttribute(required = true)
    protected String type;
    @XmlAttribute
    protected Boolean excludeRootType;
    @XmlAttribute
    protected Boolean includeSubTypes;


    public String getType()
    {
        return this.type;
    }


    public void setType(String value)
    {
        this.type = value;
    }


    public boolean isExcludeRootType()
    {
        if(this.excludeRootType == null)
        {
            return true;
        }
        return this.excludeRootType.booleanValue();
    }


    public void setExcludeRootType(Boolean value)
    {
        this.excludeRootType = value;
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
