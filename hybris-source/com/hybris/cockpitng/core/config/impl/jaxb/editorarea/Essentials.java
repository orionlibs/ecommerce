/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.editorarea;

import com.hybris.cockpitng.core.config.annotations.Mergeable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for essentials complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="essentials"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice&gt;
 *           &lt;element name="essentialCustomSection" type="{http://www.hybris.com/cockpitng/component/editorArea}essentialCustomSection"/&gt;
 *           &lt;element name="essentialSection" type="{http://www.hybris.com/cockpitng/component/editorArea}essentialSection"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="initiallyOpened" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "essentials", namespace = "http://www.hybris.com/cockpitng/component/editorArea", propOrder = {
                "essentialCustomSection",
                "essentialSection"
})
public class Essentials
{
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/component/editorArea")
    protected EssentialCustomSection essentialCustomSection;
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/component/editorArea")
    @Mergeable(key = "name")
    protected EssentialSection essentialSection;
    @XmlAttribute(name = "initiallyOpened")
    protected Boolean initiallyOpened;


    /**
     * Gets the value of the essentialCustomSection property.
     *
     * @return
     *     possible object is
     *     {@link EssentialCustomSection }
     *
     */
    public EssentialCustomSection getEssentialCustomSection()
    {
        return essentialCustomSection;
    }


    /**
     * Sets the value of the essentialCustomSection property.
     *
     * @param value
     *     allowed object is
     *     {@link EssentialCustomSection }
     *
     */
    public void setEssentialCustomSection(EssentialCustomSection value)
    {
        this.essentialCustomSection = value;
    }


    /**
     * Gets the value of the essentialSection property.
     *
     * @return
     *     possible object is
     *     {@link EssentialSection }
     *
     */
    public EssentialSection getEssentialSection()
    {
        return essentialSection;
    }


    /**
     * Sets the value of the essentialSection property.
     *
     * @param value
     *     allowed object is
     *     {@link EssentialSection }
     *
     */
    public void setEssentialSection(EssentialSection value)
    {
        this.essentialSection = value;
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
}
