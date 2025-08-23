/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.editorarea;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for abstractSection complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="abstractSection"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.hybris.com/cockpitng/component/editorArea}abstractPositioned"&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="initiallyOpened" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *       &lt;attribute name="merge-mode" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractSection", namespace = "http://www.hybris.com/cockpitng/component/editorArea")
@XmlSeeAlso({
                Section.class,
                CustomSection.class
})
public abstract class AbstractSection
                extends AbstractPositioned
{
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "description")
    protected String description;
    @XmlAttribute(name = "initiallyOpened")
    protected Boolean initiallyOpened;
    @XmlAttribute(name = "merge-mode")
    protected String mergeMode;


    /**
     * Gets the value of the name property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName()
    {
        return name;
    }


    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value)
    {
        this.name = value;
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
     * Gets the value of the initiallyOpened property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isInitiallyOpened()
    {
        if(initiallyOpened == null)
        {
            return true;
        }
        else
        {
            return initiallyOpened;
        }
    }


    /**
     * Sets the value of the initiallyOpened property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setInitiallyOpened(Boolean value)
    {
        this.initiallyOpened = value;
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
}
