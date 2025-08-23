/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.notification.jaxb;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for NotificationTimeout complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="NotificationTimeout"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="level" use="required" type="{http://www.hybris.com/cockpitng/config/notifications}ImportanceLevel" /&gt;
 *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NotificationTimeout")
public class NotificationTimeout
{
    @XmlAttribute(name = "level", required = true)
    protected ImportanceLevel level;
    @XmlAttribute(name = "value", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger value;


    /**
     * Gets the value of the level property.
     *
     * @return
     *     possible object is
     *     {@link ImportanceLevel }
     *
     */
    public ImportanceLevel getLevel()
    {
        return level;
    }


    /**
     * Sets the value of the level property.
     *
     * @param value
     *     allowed object is
     *     {@link ImportanceLevel }
     *
     */
    public void setLevel(ImportanceLevel value)
    {
        this.level = value;
    }


    /**
     * Gets the value of the value property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getValue()
    {
        return value;
    }


    /**
     * Sets the value of the value property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setValue(BigInteger value)
    {
        this.value = value;
    }
}
