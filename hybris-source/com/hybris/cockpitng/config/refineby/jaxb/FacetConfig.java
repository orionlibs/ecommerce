/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.refineby.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="facets" type="{http://www.hybris.com/cockpitng/config/refineBy}facets" minOccurs="0"/&gt;
 *         &lt;element name="blacklist" type="{http://www.hybris.com/cockpitng/config/refineBy}blacklist" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", namespace = "http://www.hybris.com/cockpitng/config/refineBy", propOrder = {
                "facets",
                "blacklist"
})
@XmlRootElement(name = "facet-config")
public class FacetConfig
{
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/config/refineBy")
    protected Facets facets;
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/config/refineBy")
    protected Blacklist blacklist;


    /**
     * Gets the value of the facets property.
     *
     * @return
     *     possible object is
     *     {@link Facets }
     *
     */
    public Facets getFacets()
    {
        return facets;
    }


    /**
     * Sets the value of the facets property.
     *
     * @param value
     *     allowed object is
     *     {@link Facets }
     *
     */
    public void setFacets(Facets value)
    {
        this.facets = value;
    }


    /**
     * Gets the value of the blacklist property.
     *
     * @return
     *     possible object is
     *     {@link Blacklist }
     *
     */
    public Blacklist getBlacklist()
    {
        return blacklist;
    }


    /**
     * Sets the value of the blacklist property.
     *
     * @param value
     *     allowed object is
     *     {@link Blacklist }
     *
     */
    public void setBlacklist(Blacklist value)
    {
        this.blacklist = value;
    }
}
