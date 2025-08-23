/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.jaxb.wizard;

import com.hybris.cockpitng.core.config.annotations.Mergeable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for NavigationType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="NavigationType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="cancel" type="{http://www.hybris.com/cockpitng/config/wizard-config}CancelType" minOccurs="0"/&gt;
 *         &lt;element name="back" type="{http://www.hybris.com/cockpitng/config/wizard-config}BackType" minOccurs="0"/&gt;
 *         &lt;element name="next" type="{http://www.hybris.com/cockpitng/config/wizard-config}NextType" minOccurs="0"/&gt;
 *         &lt;element name="done" type="{http://www.hybris.com/cockpitng/config/wizard-config}DoneType" minOccurs="0"/&gt;
 *         &lt;element name="custom" type="{http://www.hybris.com/cockpitng/config/wizard-config}CustomType" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="merge-mode" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NavigationType", namespace = "http://www.hybris.com/cockpitng/config/wizard-config", propOrder = {
})
public class NavigationType
{
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/config/wizard-config")
    protected CancelType cancel;
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/config/wizard-config")
    protected BackType back;
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/config/wizard-config")
    protected NextType next;
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/config/wizard-config")
    @Mergeable(key = {})
    protected DoneType done;
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/config/wizard-config")
    protected CustomType custom;
    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "merge-mode")
    protected String mergeMode;


    /**
     * Gets the value of the cancel property.
     *
     * @return
     *     possible object is
     *     {@link CancelType }
     *
     */
    public CancelType getCancel()
    {
        return cancel;
    }


    /**
     * Sets the value of the cancel property.
     *
     * @param value
     *     allowed object is
     *     {@link CancelType }
     *
     */
    public void setCancel(CancelType value)
    {
        this.cancel = value;
    }


    /**
     * Gets the value of the back property.
     *
     * @return
     *     possible object is
     *     {@link BackType }
     *
     */
    public BackType getBack()
    {
        return back;
    }


    /**
     * Sets the value of the back property.
     *
     * @param value
     *     allowed object is
     *     {@link BackType }
     *
     */
    public void setBack(BackType value)
    {
        this.back = value;
    }


    /**
     * Gets the value of the next property.
     *
     * @return
     *     possible object is
     *     {@link NextType }
     *
     */
    public NextType getNext()
    {
        return next;
    }


    /**
     * Sets the value of the next property.
     *
     * @param value
     *     allowed object is
     *     {@link NextType }
     *
     */
    public void setNext(NextType value)
    {
        this.next = value;
    }


    /**
     * Gets the value of the done property.
     *
     * @return
     *     possible object is
     *     {@link DoneType }
     *
     */
    public DoneType getDone()
    {
        return done;
    }


    /**
     * Sets the value of the done property.
     *
     * @param value
     *     allowed object is
     *     {@link DoneType }
     *
     */
    public void setDone(DoneType value)
    {
        this.done = value;
    }


    /**
     * Gets the value of the custom property.
     *
     * @return
     *     possible object is
     *     {@link CustomType }
     *
     */
    public CustomType getCustom()
    {
        return custom;
    }


    /**
     * Sets the value of the custom property.
     *
     * @param value
     *     allowed object is
     *     {@link CustomType }
     *
     */
    public void setCustom(CustomType value)
    {
        this.custom = value;
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
}
