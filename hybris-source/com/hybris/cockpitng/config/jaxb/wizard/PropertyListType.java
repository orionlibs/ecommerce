/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.jaxb.wizard;

import com.hybris.cockpitng.core.config.annotations.Mergeable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for PropertyListType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="PropertyListType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="property" type="{http://www.hybris.com/cockpitng/config/wizard-config}PropertyType" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="root" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="readonly" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="validate" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="merge-mode" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="position" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *       &lt;attribute name="include-non-declared-mandatory" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *       &lt;attribute name="include-non-declared-unique" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *       &lt;attribute name="include-non-declared-writable-on-creation" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *       &lt;attribute name="enable-non-declared-includes" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PropertyListType", namespace = "http://www.hybris.com/cockpitng/config/wizard-config", propOrder = {
                "property"
})
public class PropertyListType
{
    @Mergeable(key = "qualifier")
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/config/wizard-config", required = true)
    protected List<PropertyType> property;
    @XmlAttribute(name = "root")
    protected String root;
    @XmlAttribute(name = "readonly")
    protected Boolean readonly;
    @XmlAttribute(name = "validate")
    protected Boolean validate;
    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "merge-mode")
    protected String mergeMode;
    @XmlTransient
    protected String generatedId;
    @XmlAttribute(name = "position")
    protected BigInteger position;
    @XmlAttribute(name = "include-non-declared-mandatory")
    protected Boolean includeNonDeclaredMandatory;
    @XmlAttribute(name = "include-non-declared-unique")
    protected Boolean includeNonDeclaredUnique;
    @XmlAttribute(name = "include-non-declared-writable-on-creation")
    protected Boolean includeNonDeclaredWritableOnCreation;
    @XmlAttribute(name = "enable-non-declared-includes")
    protected Boolean enableNonDeclaredIncludes;


    /**
     * Gets the value of the property property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the property property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProperty().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PropertyType }
     *
     *
     */
    public List<PropertyType> getProperty()
    {
        if(property == null)
        {
            property = new ArrayList<PropertyType>();
        }
        return this.property;
    }


    /**
     * Gets the value of the root property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRoot()
    {
        return root;
    }


    /**
     * Sets the value of the root property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRoot(final String value)
    {
        this.root = value;
    }


    /**
     * Gets the value of the readonly property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isReadonly()
    {
        return readonly;
    }


    /**
     * Sets the value of the readonly property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setReadonly(final Boolean value)
    {
        this.readonly = value;
    }


    /**
     * Gets the value of the validate property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isValidate()
    {
        return validate;
    }


    /**
     * Sets the value of the validate property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setValidate(final Boolean value)
    {
        this.validate = value;
    }


    /**
     * Gets the value of the id property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getId()
    {
        return id;
    }


    /**
     * Sets the value of the id property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setId(final String value)
    {
        this.id = value;
    }


    /**
     * Gets the value of the mergeMode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMergeMode()
    {
        return mergeMode;
    }


    /**
     * Sets the value of the mergeMode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMergeMode(final String value)
    {
        this.mergeMode = value;
    }


    public String getGeneratedId()
    {
        return generatedId;
    }


    public void afterUnmarshal(final Unmarshaller u, final Object parent)
    {
        generatedId = String.format("%s_%s", getClass().getCanonicalName(), root);
    }


    /**
     * Gets the value of the position property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getPosition()
    {
        return position;
    }


    /**
     * Sets the value of the position property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setPosition(final BigInteger value)
    {
        this.position = value;
    }


    /**
     * Gets the value of the includeNonDeclaredMandatory property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isIncludeNonDeclaredMandatory()
    {
        if(includeNonDeclaredMandatory == null)
        {
            return true;
        }
        else
        {
            return includeNonDeclaredMandatory;
        }
    }


    /**
     * Sets the value of the includeNonDeclaredMandatory property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIncludeNonDeclaredMandatory(Boolean value)
    {
        this.includeNonDeclaredMandatory = value;
    }


    /**
     * Gets the value of the includeNonDeclaredUnique property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isIncludeNonDeclaredUnique()
    {
        if(includeNonDeclaredUnique == null)
        {
            return true;
        }
        else
        {
            return includeNonDeclaredUnique;
        }
    }


    /**
     * Sets the value of the includeNonDeclaredUnique property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIncludeNonDeclaredUnique(Boolean value)
    {
        this.includeNonDeclaredUnique = value;
    }


    /**
     * Gets the value of the includeNonDeclaredWritableOnCreation property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isIncludeNonDeclaredWritableOnCreation()
    {
        if(includeNonDeclaredWritableOnCreation == null)
        {
            return true;
        }
        else
        {
            return includeNonDeclaredWritableOnCreation;
        }
    }


    /**
     * Sets the value of the includeNonDeclaredWritableOnCreation property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setIncludeNonDeclaredWritableOnCreation(Boolean value)
    {
        this.includeNonDeclaredWritableOnCreation = value;
    }


    /**
     * Gets the value of the enableNonDeclaredIncludes property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isEnableNonDeclaredIncludes()
    {
        if(enableNonDeclaredIncludes == null)
        {
            return false;
        }
        else
        {
            return enableNonDeclaredIncludes;
        }
    }


    /**
     * Sets the value of the enableNonDeclaredIncludes property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setEnableNonDeclaredIncludes(Boolean value)
    {
        this.enableNonDeclaredIncludes = value;
    }
}
