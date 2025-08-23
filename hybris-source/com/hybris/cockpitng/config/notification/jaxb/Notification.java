/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.notification.jaxb;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.MergeMode;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for Notification complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Notification"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.hybris.com/cockpitng/config/notifications}NotificationRenderer"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="timeout" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/&gt;
 *         &lt;element name="references" type="{http://www.hybris.com/cockpitng/config/notifications}NotificationReferences" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="eventType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="level" use="required" type="{http://www.hybris.com/cockpitng/config/notifications}ImportanceLevel" /&gt;
 *       &lt;attribute name="destination" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Notification", propOrder = {
                "message",
                "timeout",
                "references"
})
public class Notification
                extends NotificationRenderingInfo
{
    protected String message;
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger timeout;
    protected NotificationReferences references;
    @XmlAttribute(name = "eventType", required = true)
    protected String eventType;
    @XmlAttribute(name = "level", required = true)
    protected ImportanceLevel level;
    @XmlAttribute(name = "destination")
    protected Destination destination;
    @XmlAttribute(name = "merge-mode")
    protected MergeMode mergeMode;
    @XmlAttribute(name = "referencesType")
    protected String referencesType;


    /**
     * Gets the value of the message property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMessage()
    {
        return message;
    }


    /**
     * Sets the value of the message property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMessage(String value)
    {
        this.message = value;
    }


    /**
     * Gets the value of the timeout property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getTimeout()
    {
        return timeout;
    }


    /**
     * Sets the value of the timeout property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setTimeout(BigInteger value)
    {
        this.timeout = value;
    }


    /**
     * Gets the value of the references property.
     *
     * @return
     *     possible object is
     *     {@link NotificationReferences }
     *
     */
    public NotificationReferences getReferences()
    {
        return references;
    }


    /**
     * Sets the value of the references property.
     *
     * @param value
     *     allowed object is
     *     {@link NotificationReferences }
     *
     */
    public void setReferences(NotificationReferences value)
    {
        this.references = value;
    }


    /**
     * Gets the value of the type property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEventType()
    {
        return eventType;
    }


    /**
     * Sets the value of the type property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEventType(String value)
    {
        this.eventType = value;
    }


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
     * Gets the value of the destination property.
     *
     * @return
     *     possible object is
     *     {@link Destination }
     *
     */
    public Destination getDestination()
    {
        return destination;
    }


    /**
     * Sets the value of the destination property.
     *
     * @param value
     *     allowed object is
     *     {@link Destination }
     *
     */
    public void setDestination(Destination value)
    {
        this.destination = value;
    }


    /**
     * Gets the value of the mergeMode property.
     *
     * @return
     *     possible object is
     *     {@link MergeMode }
     *
     */
    public MergeMode getMergeMode()
    {
        return mergeMode;
    }


    /**
     * Sets the value of the mergeMode property.
     *
     * @param mergeMode
     *     allowed object is
     *     {@link MergeMode }
     *
     */
    public void setMergeMode(MergeMode mergeMode)
    {
        this.mergeMode = mergeMode;
    }


    /**
     * Gets the value of the referencesType property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getReferencesType()
    {
        return referencesType;
    }


    /**
     * Sets the value of the referencesType property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setReferencesType(String value)
    {
        this.referencesType = value;
    }
}
