/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.extendedsplitlayout.jaxb;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.MergeMode;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for LayoutMapping complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="LayoutMapping"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="parentLayout" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="selfLayout" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LayoutMapping", namespace = "http://www.hybris.com/cockpitng/config/extendedsplitlayout")
public class LayoutMapping
{
    @XmlAttribute(name = "parentLayout", required = true)
    protected String parentLayout;
    @XmlAttribute(name = "selfLayout", required = true)
    protected String selfLayout;
    @XmlAttribute(name = "merge-mode")
    protected MergeMode mergeMode;


    /**
     * Gets the value of the parentLayout property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getParentLayout()
    {
        return parentLayout;
    }


    /**
     * Sets the value of the parentLayout property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setParentLayout(String value)
    {
        this.parentLayout = value;
    }


    /**
     * Gets the value of the selfLayout property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSelfLayout()
    {
        return selfLayout;
    }


    /**
     * Sets the value of the selfLayout property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSelfLayout(String value)
    {
        this.selfLayout = value;
    }


    /**
     * Gets the value of the mergeMode property.
     *
     * @return possible object is {@link MergeMode }
     */
    public MergeMode getMergeMode()
    {
        return mergeMode;
    }


    /**
     * Sets the value of the mergeMode property.
     *
     * @param value
     *           allowed object is {@link MergeMode }
     */
    public void setMergeMode(MergeMode value)
    {
        this.mergeMode = value;
    }
}
