package de.hybris.bootstrap.typesystem.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "attributeType", propOrder = {})
public class AttributeType
{
    protected String defaultvalue;
    protected String description;
    protected PersistenceType persistence;
    protected ModifiersType modifiers;
    @XmlElement(name = "custom-properties")
    protected CustomPropertiesType customProperties;
    protected AttributeModelType model;
    @XmlAttribute
    protected Boolean redeclare;
    @XmlAttribute(required = true)
    protected String qualifier;
    @XmlAttribute(required = true)
    protected String type;
    @XmlAttribute
    protected String metatype;
    @XmlAttribute
    protected Boolean autocreate;
    @XmlAttribute
    protected Boolean generate;
    @XmlAttribute
    protected String isSelectionOf;


    public String getDefaultvalue()
    {
        return this.defaultvalue;
    }


    public void setDefaultvalue(String value)
    {
        this.defaultvalue = value;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setDescription(String value)
    {
        this.description = value;
    }


    public PersistenceType getPersistence()
    {
        return this.persistence;
    }


    public void setPersistence(PersistenceType value)
    {
        this.persistence = value;
    }


    public ModifiersType getModifiers()
    {
        return this.modifiers;
    }


    public void setModifiers(ModifiersType value)
    {
        this.modifiers = value;
    }


    public CustomPropertiesType getCustomProperties()
    {
        return this.customProperties;
    }


    public void setCustomProperties(CustomPropertiesType value)
    {
        this.customProperties = value;
    }


    public AttributeModelType getModel()
    {
        return this.model;
    }


    public void setModel(AttributeModelType value)
    {
        this.model = value;
    }


    public Boolean isRedeclare()
    {
        return this.redeclare;
    }


    public void setRedeclare(Boolean value)
    {
        this.redeclare = value;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public void setQualifier(String value)
    {
        this.qualifier = value;
    }


    public String getType()
    {
        return this.type;
    }


    public void setType(String value)
    {
        this.type = value;
    }


    public String getMetatype()
    {
        return this.metatype;
    }


    public void setMetatype(String value)
    {
        this.metatype = value;
    }


    public boolean isAutocreate()
    {
        if(this.autocreate == null)
        {
            return true;
        }
        return this.autocreate.booleanValue();
    }


    public void setAutocreate(Boolean value)
    {
        this.autocreate = value;
    }


    public boolean isGenerate()
    {
        if(this.generate == null)
        {
            return true;
        }
        return this.generate.booleanValue();
    }


    public void setGenerate(Boolean value)
    {
        this.generate = value;
    }


    public String getIsSelectionOf()
    {
        return this.isSelectionOf;
    }


    public void setIsSelectionOf(String value)
    {
        this.isSelectionOf = value;
    }
}
