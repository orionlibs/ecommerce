/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.jaxb.wizard;

import com.hybris.cockpitng.core.config.annotations.Mergeable;
import java.math.BigInteger;
import java.util.Objects;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for StepType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="StepType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="info" type="{http://www.hybris.com/cockpitng/config/wizard-config}InfoType" minOccurs="0"/&gt;
 *         &lt;element name="content" type="{http://www.hybris.com/cockpitng/config/wizard-config}ContentType"/&gt;
 *         &lt;element name="navigation" type="{http://www.hybris.com/cockpitng/config/wizard-config}NavigationType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="label" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="sublabel" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="merge-mode" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="position" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StepType", namespace = "http://www.hybris.com/cockpitng/config/wizard-config", propOrder = {
                "info",
                "content",
                "navigation"
})
public class StepType
{
    @Mergeable(key = "id")
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/config/wizard-config")
    protected InfoType info;
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/config/wizard-config", required = true)
    @Mergeable(key = "id")
    protected ContentType content;
    @Mergeable(key = "id")
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/config/wizard-config")
    protected NavigationType navigation;
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "label", required = true)
    protected String label;
    @XmlAttribute(name = "sublabel")
    protected String sublabel;
    @XmlAttribute(name = "merge-mode")
    protected String mergeMode;
    @XmlTransient
    protected String generatedId;
    @XmlAttribute(name = "position")
    protected BigInteger position;
    @XmlAttribute(name = "hide-breadcrumb")
    protected Boolean hideBreadcrumbs;


    /**
     * Gets the value of the info property.
     *
     * @return
     *     possible object is
     *     {@link InfoType }
     *
     */
    public InfoType getInfo()
    {
        return info;
    }


    /**
     * Sets the value of the info property.
     *
     * @param value
     *     allowed object is
     *     {@link InfoType }
     *
     */
    public void setInfo(InfoType value)
    {
        this.info = value;
    }


    /**
     * Gets the value of the content property.
     *
     * @return
     *     possible object is
     *     {@link ContentType }
     *
     */
    public ContentType getContent()
    {
        return content;
    }


    /**
     * Sets the value of the content property.
     *
     * @param value
     *     allowed object is
     *     {@link ContentType }
     *
     */
    public void setContent(ContentType value)
    {
        this.content = value;
    }


    /**
     * Gets the value of the navigation property.
     *
     * @return
     *     possible object is
     *     {@link NavigationType }
     *
     */
    public NavigationType getNavigation()
    {
        return navigation;
    }


    /**
     * Sets the value of the navigation property.
     *
     * @param value
     *     allowed object is
     *     {@link NavigationType }
     *
     */
    public void setNavigation(NavigationType value)
    {
        this.navigation = value;
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
     * Gets the value of the sublabel property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSublabel()
    {
        return sublabel;
    }


    /**
     * Sets the value of the sublabel property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSublabel(String value)
    {
        this.sublabel = value;
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


    public String getGeneratedId()
    {
        return generatedId;
    }


    public void afterUnmarshal(final Unmarshaller u, final Object parent)
    {
        generatedId = String.format("%s_%s", getClass().getCanonicalName(), id);
    }


    /**
     * Gets the value of the position property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getPosition()
    {
        return position;
    }


    /**
     * Sets the value of the position property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setPosition(final BigInteger value)
    {
        this.position = value;
    }


    public Boolean getHideBreadcrumbs()
    {
        return Objects.requireNonNullElse(hideBreadcrumbs, false);
    }


    public void setHideBreadcrumbs(final Boolean hideBreadcrumbs)
    {
        this.hideBreadcrumbs = hideBreadcrumbs;
    }
}
