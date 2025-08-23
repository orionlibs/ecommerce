package de.hybris.bootstrap.typesystem.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "relationElementType", propOrder = {"description", "modifiers", "model", "customProperties"})
public class RelationElementType
{
    protected String description;
    protected ModifiersType modifiers;
    protected AttributeModelType model;
    @XmlElement(name = "custom-properties")
    protected CustomPropertiesType customProperties;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String type;
    @XmlAttribute
    protected String qualifier;
    @XmlAttribute
    protected String metatype;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String cardinality;
    @XmlAttribute
    protected Boolean navigable;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String collectiontype;
    @XmlAttribute
    protected Boolean ordered;


    public String getDescription()
    {
        return this.description;
    }


    public void setDescription(String value)
    {
        this.description = value;
    }


    public ModifiersType getModifiers()
    {
        return this.modifiers;
    }


    public void setModifiers(ModifiersType value)
    {
        this.modifiers = value;
    }


    public AttributeModelType getModel()
    {
        return this.model;
    }


    public void setModel(AttributeModelType value)
    {
        this.model = value;
    }


    public CustomPropertiesType getCustomProperties()
    {
        return this.customProperties;
    }


    public void setCustomProperties(CustomPropertiesType value)
    {
        this.customProperties = value;
    }


    public String getType()
    {
        return this.type;
    }


    public void setType(String value)
    {
        this.type = value;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public void setQualifier(String value)
    {
        this.qualifier = value;
    }


    public String getMetatype()
    {
        return this.metatype;
    }


    public void setMetatype(String value)
    {
        this.metatype = value;
    }


    public String getCardinality()
    {
        return this.cardinality;
    }


    public void setCardinality(String value)
    {
        this.cardinality = value;
    }


    public Boolean isNavigable()
    {
        return this.navigable;
    }


    public void setNavigable(Boolean value)
    {
        this.navigable = value;
    }


    public String getCollectiontype()
    {
        return this.collectiontype;
    }


    public void setCollectiontype(String value)
    {
        this.collectiontype = value;
    }


    public Boolean isOrdered()
    {
        return this.ordered;
    }


    public void setOrdered(Boolean value)
    {
        this.ordered = value;
    }
}
