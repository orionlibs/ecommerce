/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.jaxb.wizard;

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
 * <p>Java class for PropertyType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="PropertyType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="editor-parameter" type="{http://www.hybris.com/cockpitng/config/wizard-config}parameter" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="qualifier" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="validate" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="readonly" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="merge-mode" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="editor" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="position" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *       &lt;attribute name="exclude" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PropertyType", namespace = "http://www.hybris.com/cockpitng/config/wizard-config", propOrder = {
                "editorParameter"
})
public class PropertyType
{
    @XmlElement(name = "editor-parameter", namespace = "http://www.hybris.com/cockpitng/config/wizard-config")
    protected List<Parameter> editorParameter;
    @XmlAttribute(name = "qualifier", required = true)
    protected String qualifier;
    @XmlAttribute(name = "description")
    protected String description;
    @XmlAttribute(name = "label")
    protected String label;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "validate")
    protected Boolean validate;
    @XmlAttribute(name = "readonly")
    protected Boolean readonly;
    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "merge-mode")
    protected String mergeMode;
    @XmlAttribute(name = "editor")
    protected String editor;
    @XmlAttribute(name = "position")
    protected BigInteger position;
    @XmlAttribute(name = "exclude")
    protected Boolean exclude;
    @XmlTransient
    protected String generatedId;


    /**
     * Gets the value of the editorParameter property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the editorParameter property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEditorParameter().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Parameter }
     *
     *
     */
    public List<Parameter> getEditorParameter()
    {
        if(editorParameter == null)
        {
            editorParameter = new ArrayList<Parameter>();
        }
        return this.editorParameter;
    }


    /**
     * Gets the value of the qualifier property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getQualifier()
    {
        return qualifier;
    }


    /**
     * Sets the value of the qualifier property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setQualifier(String value)
    {
        this.qualifier = value;
    }


    /**
     * Gets the value of the label property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLabel()
    {
        return label;
    }


    /**
     * Sets the value of the label property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLabel(String value)
    {
        this.label = value;
    }


    /**
     * Gets the value of the description property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Sets the value of the description property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDescription(String value)
    {
        this.description = value;
    }


    /**
     * Gets the value of the type property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getType()
    {
        return type;
    }


    /**
     * Sets the value of the type property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setType(String value)
    {
        this.type = value;
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
    public void setValidate(Boolean value)
    {
        this.validate = value;
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
    public void setReadonly(Boolean value)
    {
        this.readonly = value;
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
    public void setId(String value)
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
    public void setMergeMode(String value)
    {
        this.mergeMode = value;
    }


    /**
     * Gets the value of the editor property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEditor()
    {
        return editor;
    }


    /**
     * Sets the value of the editor property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEditor(String value)
    {
        this.editor = value;
    }


    public String getGeneratedId()
    {
        return generatedId;
    }


    public void afterUnmarshal(final Unmarshaller u, final Object parent)
    {
        generatedId = String.format("%s_%s", getClass().getCanonicalName(), qualifier);
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
    public void setPosition(BigInteger value)
    {
        this.position = value;
    }


    /**
     * Gets the value of the exclude property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isExclude()
    {
        if(exclude == null)
        {
            return false;
        }
        else
        {
            return exclude;
        }
    }


    /**
     * Sets the value of the exclude property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setExclude(Boolean value)
    {
        this.exclude = value;
    }
}
