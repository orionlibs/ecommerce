/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.impl.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Single js library definition
 *
 * <p>Java class for Library complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Library"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence minOccurs="0"&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="url" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="protocol" type="{}DependencyProtocol" /&gt;
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" default="text/javascript" /&gt;
 *       &lt;attribute name="point" type="{}DependencyInjectionPoint" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Library", propOrder = {
                "name",
                "version"
})
public class Library
{
    protected String name;
    protected String version;
    @XmlAttribute(name = "url", required = true)
    protected String url;
    @XmlAttribute(name = "protocol")
    protected DependencyProtocol protocol;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "point")
    protected DependencyInjectionPoint point;


    /**
     * Gets the value of the name property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName()
    {
        return name;
    }


    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value)
    {
        this.name = value;
    }


    /**
     * Gets the value of the version property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getVersion()
    {
        return version;
    }


    /**
     * Sets the value of the version property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setVersion(String value)
    {
        this.version = value;
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
     * Gets the value of the protocol property.
     *
     * @return
     *     possible object is
     *     {@link DependencyProtocol }
     *
     */
    public DependencyProtocol getProtocol()
    {
        return protocol;
    }


    /**
     * Sets the value of the protocol property.
     *
     * @param value
     *     allowed object is
     *     {@link DependencyProtocol }
     *
     */
    public void setProtocol(DependencyProtocol value)
    {
        this.protocol = value;
    }


    /**
     * Gets the value of the type property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getType()
    {
        if(type == null)
        {
            return "text/javascript";
        }
        else
        {
            return type;
        }
    }


    /**
     * Sets the value of the type property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setType(String value)
    {
        this.type = value;
    }


    /**
     * Gets the value of the point property.
     *
     * @return
     *     possible object is
     *     {@link DependencyInjectionPoint }
     *
     */
    public DependencyInjectionPoint getPoint()
    {
        return point;
    }


    /**
     * Sets the value of the point property.
     *
     * @param value
     *     allowed object is
     *     {@link DependencyInjectionPoint }
     *
     */
    public void setPoint(DependencyInjectionPoint value)
    {
        this.point = value;
    }
}
