/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.links.jaxb;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.MergeMode;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for link complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="link"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.hybris.com/cockpitng/config/common}positioned"&gt;
 *       &lt;attribute name="label" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="icon" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="url" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="target" type="{http://www.hybris.com/cockpitng/config/links}target" /&gt;
 *       &lt;attribute name="merge-mode" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "link", namespace = "http://www.hybris.com/cockpitng/config/links")
public class Link
                extends Positioned
{
    @XmlAttribute(name = "label", required = true)
    protected String label;
    @XmlAttribute(name = "icon")
    protected String icon;
    @XmlAttribute(name = "url")
    protected String url;
    @XmlAttribute(name = "target")
    protected Target target;
    @XmlAttribute(name = "merge-mode")
    protected MergeMode mergeMode;


    /**
     * Gets the value of the label property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLabel()
    {
        return label;
    }


    /**
     * Sets the value of the label property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLabel(String value)
    {
        this.label = value;
    }


    /**
     * Gets the value of the icon property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIcon()
    {
        return icon;
    }


    /**
     * Sets the value of the icon property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIcon(String value)
    {
        this.icon = value;
    }


    /**
     * Gets the value of the url property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUrl()
    {
        return url;
    }


    /**
     * Sets the value of the url property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUrl(String value)
    {
        this.url = value;
    }


    /**
     * Gets the value of the target property.
     *
     * @return
     *     possible object is
     *     {@link Target }
     *
     */
    public Target getTarget()
    {
        return target;
    }


    /**
     * Sets the value of the target property.
     *
     * @param value
     *     allowed object is
     *     {@link Target }
     *
     */
    public void setTarget(Target value)
    {
        this.target = value;
    }


    /**
     * Gets the value of the mergeMode property.
     *
     * @return
     *     possible object is
     *     {@link String }
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
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMergeMode(MergeMode value)
    {
        this.mergeMode = value;
    }
}
