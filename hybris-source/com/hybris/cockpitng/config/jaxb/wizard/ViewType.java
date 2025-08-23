/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.jaxb.wizard;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ViewType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ViewType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="renderer" type="{http://www.hybris.com/cockpitng/config/wizard-config}Renderer" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *       &lt;attribute name="src" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="lang" use="required"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;enumeration value="zul"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
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
@XmlType(name = "ViewType", namespace = "http://www.hybris.com/cockpitng/config/wizard-config", propOrder = {"content"})
public class ViewType
{
    @XmlElementRef(name = "renderer", namespace = "http://www.hybris.com/cockpitng/config/wizard-config", type = JAXBElement.class, required = false)
    @XmlMixed
    protected List<Serializable> content;
    @XmlAttribute(name = "src")
    protected String src;
    @XmlAttribute(name = "lang", required = true)
    protected String lang;
    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "merge-mode")
    protected String mergeMode;
    @XmlAttribute(name = "position")
    protected BigInteger position;
    @XmlTransient
    protected String generatedId;


    /**
     * Gets the value of the content property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link Renderer }{@code >}
     * {@link String }
     *
     *
     */
    public List<Serializable> getContent()
    {
        if(content == null)
        {
            content = new ArrayList<Serializable>();
        }
        return this.content;
    }


    /**
     * Gets the value of the src property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSrc()
    {
        return src;
    }


    /**
     * Sets the value of the src property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSrc(String value)
    {
        this.src = value;
    }


    /**
     * Gets the value of the lang property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLang()
    {
        return lang;
    }


    /**
     * Sets the value of the lang property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLang(String value)
    {
        this.lang = value;
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
    public void setPosition(BigInteger value)
    {
        this.position = value;
    }
}
