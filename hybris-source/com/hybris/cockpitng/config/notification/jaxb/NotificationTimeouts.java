/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.notification.jaxb;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for NotificationTimeouts complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="NotificationTimeouts"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="timeout" type="{http://www.hybris.com/cockpitng/config/notifications}NotificationTimeout" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="default" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NotificationTimeouts", propOrder = {
                "timeout"
})
public class NotificationTimeouts
{
    protected List<NotificationTimeout> timeout;
    @XmlAttribute(name = "default")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger _default;


    /**
     * Gets the value of the timeout property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the timeout property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTimeout().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NotificationTimeout }
     *
     *
     */
    public List<NotificationTimeout> getTimeout()
    {
        if(timeout == null)
        {
            timeout = new ArrayList<NotificationTimeout>();
        }
        return this.timeout;
    }


    /**
     * Gets the value of the default property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getDefault()
    {
        return _default;
    }


    /**
     * Sets the value of the default property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setDefault(BigInteger value)
    {
        this._default = value;
    }
}
