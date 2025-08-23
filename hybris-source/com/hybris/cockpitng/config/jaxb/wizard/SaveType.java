/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.jaxb.wizard;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for SaveType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="SaveType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="property" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SaveType", namespace = "http://www.hybris.com/cockpitng/config/wizard-config")
public class SaveType
{
    @XmlAttribute(name = "property", required = true)
    protected String property;


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
}
