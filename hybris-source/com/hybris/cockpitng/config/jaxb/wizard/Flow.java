/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.jaxb.wizard;

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
 *     &lt;extension base="{http://www.hybris.com/cockpitng/config/wizard-config}AbstractFlowType"&gt;
 *       &lt;attribute name="title" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="size" type="{http://www.hybris.com/cockpitng/config/wizard-config}Size" default="medium" /&gt;
 *       &lt;attribute name="show-breadcrumb" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "flow", namespace = "http://www.hybris.com/cockpitng/config/wizard-config")
public class Flow
                extends AbstractFlowType
{
    @XmlAttribute(name = "title", required = true)
    protected String title;
    @XmlAttribute(name = "size")
    protected Size size;
    @XmlAttribute(name = "show-breadcrumb")
    protected Boolean showBreadcrumb;


    /**
     * Gets the value of the title property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTitle()
    {
        return title;
    }


    /**
     * Sets the value of the title property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTitle(String value)
    {
        this.title = value;
    }


    /**
     * Gets the value of the size property.
     *
     * @return
     *     possible object is
     *     {@link Size }
     *
     */
    public Size getSize()
    {
        if(size == null)
        {
            return Size.MEDIUM;
        }
        else
        {
            return size;
        }
    }


    /**
     * Sets the value of the size property.
     *
     * @param value
     *     allowed object is
     *     {@link Size }
     *
     */
    public void setSize(Size value)
    {
        this.size = value;
    }


    /**
     * Gets the value of the showBreadcrumb property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isShowBreadcrumb()
    {
        if(showBreadcrumb == null)
        {
            return true;
        }
        else
        {
            return showBreadcrumb;
        }
    }


    /**
     * Sets the value of the showBreadcrumb property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setShowBreadcrumb(Boolean value)
    {
        this.showBreadcrumb = value;
    }
}
