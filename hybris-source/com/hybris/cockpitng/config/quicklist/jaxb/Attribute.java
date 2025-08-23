/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.quicklist.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for attribute complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="attribute"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="qualifier" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "attribute", namespace = "http://www.hybris.com/cockpitng/component/quick-list")
public class Attribute
{
    @XmlAttribute(name = "qualifier", required = true)
    protected String qualifier;


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
}
