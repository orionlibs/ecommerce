/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.jaxb.wizard;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for InitializeType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="InitializeType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="property" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="template-bean" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="merge-mode" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InitializeType", namespace = "http://www.hybris.com/cockpitng/config/wizard-config")
public class InitializeType
{
    @XmlAttribute(name = "property", required = true)
    protected String property;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "template-bean")
    protected String templateBean;
    @XmlAttribute(name = "merge-mode")
    protected String mergeMode;


    /**
     * Gets the value of the property property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getProperty()
    {
        return property;
    }


    /**
     * Sets the value of the property property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setProperty(String value)
    {
        this.property = value;
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
     * Gets the value of the templateBean property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTemplateBean()
    {
        return templateBean;
    }


    /**
     * Sets the value of the templateBean property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTemplateBean(String value)
    {
        this.templateBean = value;
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
