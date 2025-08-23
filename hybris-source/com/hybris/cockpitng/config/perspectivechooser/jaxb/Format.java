/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.perspectivechooser.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for format complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="format"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="title-key" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="title" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="description-key" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="icon-key" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="icon" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "format", namespace = "http://www.hybris.com/cockpitng/config/perspectiveChooser")
public class Format
{
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "title-key")
    protected String titleKey;
    @XmlAttribute(name = "title")
    protected String title;
    @XmlAttribute(name = "description-key")
    protected String descriptionKey;
    @XmlAttribute(name = "description")
    protected String description;
    @XmlAttribute(name = "icon-key")
    protected String iconKey;
    @XmlAttribute(name = "icon")
    protected String icon;


    /**
     * Gets the value of the id property.
     *
     * @return
     *         possible object is {@link String }
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
     *           allowed object is {@link String }
     *
     */
    public void setId(String value)
    {
        this.id = value;
    }


    /**
     * Gets the value of the titleKey property.
     *
     * @return
     *         possible object is {@link String }
     *
     */
    public String getTitleKey()
    {
        return titleKey;
    }


    /**
     * Sets the value of the titleKey property.
     *
     * @param value
     *           allowed object is {@link String }
     *
     */
    public void setTitleKey(String value)
    {
        this.titleKey = value;
    }


    /**
     * Gets the value of the title property.
     *
     * @return
     *         possible object is {@link String }
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
     *           allowed object is {@link String }
     *
     */
    public void setTitle(String value)
    {
        this.title = value;
    }


    /**
     * Gets the value of the descriptionKey property.
     *
     * @return
     *         possible object is {@link String }
     *
     */
    public String getDescriptionKey()
    {
        return descriptionKey;
    }


    /**
     * Sets the value of the descriptionKey property.
     *
     * @param value
     *           allowed object is {@link String }
     *
     */
    public void setDescriptionKey(String value)
    {
        this.descriptionKey = value;
    }


    /**
     * Gets the value of the description property.
     *
     * @return
     *         possible object is {@link String }
     *
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Sets the value of the description property.
     *
     * @param value
     *           allowed object is {@link String }
     *
     */
    public void setDescription(String value)
    {
        this.description = value;
    }


    /**
     * Gets the value of the iconKey property.
     *
     * @return
     *         possible object is {@link String }
     *
     */
    public String getIconKey()
    {
        return iconKey;
    }


    /**
     * Sets the value of the iconKey property.
     *
     * @param value
     *           allowed object is {@link String }
     *
     */
    public void setIconKey(String value)
    {
        this.iconKey = value;
    }


    /**
     * Gets the value of the icon property.
     *
     * @return
     *         possible object is {@link String }
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
     *           allowed object is {@link String }
     *
     */
    public void setIcon(String value)
    {
        this.icon = value;
    }
}
