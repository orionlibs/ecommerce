/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.editorarea;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for abstractTab complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="abstractTab"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.hybris.com/cockpitng/component/editorArea}abstractPositioned"&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="merge-mode" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="displayEssentialSectionIfPresent" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *       &lt;attribute name="initiallyOpened" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractTab", namespace = "http://www.hybris.com/cockpitng/component/editorArea")
@XmlSeeAlso({CustomTab.class, Tab.class})
public abstract class AbstractTab extends AbstractPositioned
{
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "merge-mode")
    protected String mergeMode;
    @XmlAttribute(name = "displayEssentialSectionIfPresent")
    protected Boolean displayEssentialSectionIfPresent;
    @XmlAttribute(name = "tooltipText")
    protected String tooltipText;
    @XmlAttribute(name = "initiallyOpened")
    protected Boolean initiallyOpened;
    @XmlTransient
    protected Essentials essentials;


    /**
     * Gets the value of the name property.
     *
     * @return possible object is {@link String }
     */
    public String getName()
    {
        return name;
    }


    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is {@link String }
     */
    public void setName(String value)
    {
        this.name = value;
    }


    /**
     * Gets the value of the mergeMode property.
     *
     * @return possible object is {@link String }
     */
    public String getMergeMode()
    {
        return mergeMode;
    }


    /**
     * Sets the value of the mergeMode property.
     *
     * @param value allowed object is {@link String }
     */
    public void setMergeMode(String value)
    {
        this.mergeMode = value;
    }


    /**
     * Gets the value of the displayEssentialSectionIfPresent property.
     *
     * @return possible object is {@link Boolean }
     */
    public boolean isDisplayEssentialSectionIfPresent()
    {
        if(displayEssentialSectionIfPresent == null)
        {
            return true;
        }
        else
        {
            return displayEssentialSectionIfPresent;
        }
    }


    /**
     * Sets the value of the displayEssentialSectionIfPresent property.
     *
     * @param value allowed object is {@link Boolean }
     */
    public void setDisplayEssentialSectionIfPresent(Boolean value)
    {
        this.displayEssentialSectionIfPresent = value;
    }


    /**
     * Gets the value of the tooltipText property.
     *
     * @return possible object is {@link String }
     */
    public String getTooltipText()
    {
        return tooltipText;
    }


    /**
     * Sets the value of the tooltipText property.
     *
     * @param value allowed object is {@link String }
     */
    public void setTooltipText(String value)
    {
        this.tooltipText = value;
    }


    /**
     * Gets the value of the initiallyOpened property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public boolean isInitiallyOpened()
    {
        if(initiallyOpened == null)
        {
            return false;
        }
        else
        {
            return initiallyOpened;
        }
    }


    /**
     * Sets the value of the initiallyOpened property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setInitiallyOpened(Boolean value)
    {
        this.initiallyOpened = value;
    }


    public Essentials getEssentials()
    {
        return essentials;
    }


    public void setEssentials(final Essentials essentials)
    {
        this.essentials = essentials;
    }
}
