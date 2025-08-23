/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.draganddrop;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="strategy" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "drag-and-drop", namespace = "http://www.hybris.com/cockpitng/component/dragAndDrop")
public class DragAndDrop
{
    @XmlAttribute(name = "strategy", required = true)
    protected String strategy;


    /**
     * Gets the value of the strategy property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getStrategy()
    {
        return strategy;
    }


    /**
     * Sets the value of the strategy property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setStrategy(String value)
    {
        this.strategy = value;
    }
}
