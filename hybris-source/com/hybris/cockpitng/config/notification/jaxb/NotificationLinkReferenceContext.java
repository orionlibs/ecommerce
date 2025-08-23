/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.notification.jaxb;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.MergeMode;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for NotificationLinkReferenceContext complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="NotificationLinkReferenceContext"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="parameter" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="evaluate" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *       &lt;attribute name="merge-mode" type="{http://www.hybris.com/cockpitng/config/common}MergeMode" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NotificationLinkReferenceContext")
public class NotificationLinkReferenceContext
{
    @XmlAttribute(name = "parameter", required = true)
    protected String parameter;
    @XmlAttribute(name = "value")
    protected String value;
    @XmlAttribute(name = "evaluate")
    protected Boolean evaluate;
    @XmlAttribute(name = "merge-mode")
    protected MergeMode mergeMode;


    /**
     * Gets the value of the parameter property.
     *
     * @return
     *            possible object is
     *         {@link String }
     *
     */
    public String getParameter()
    {
        return parameter;
    }


    /**
     * Sets the value of the parameter property.
     *
     * @param value
     *           allowed object is
     *           {@link String }
     *
     */
    public void setParameter(String value)
    {
        this.parameter = value;
    }


    /**
     * Gets the value of the value property.
     *
     * @return
     *            possible object is
     *         {@link String }
     *
     */
    public String getValue()
    {
        return value;
    }


    /**
     * Sets the value of the value property.
     *
     * @param value
     *           allowed object is
     *           {@link String }
     *
     */
    public void setValue(String value)
    {
        this.value = value;
    }


    /**
     * Gets the value of the evaluate property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isEvaluate()
    {
        if(evaluate == null)
        {
            return false;
        }
        else
        {
            return evaluate;
        }
    }


    /**
     * Sets the value of the evaluate property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setEvaluate(Boolean value)
    {
        this.evaluate = value;
    }


    /**
     * Gets the value of the mergeMode property.
     *
     * @return
     *            possible object is
     *         {@link MergeMode }
     *
     */
    public MergeMode getMergeMode()
    {
        return mergeMode;
    }


    /**
     * Sets the value of the mergeMode property.
     *
     * @param value
     *           allowed object is
     *           {@link MergeMode }
     *
     */
    public void setMergeMode(MergeMode value)
    {
        this.mergeMode = value;
    }
}
