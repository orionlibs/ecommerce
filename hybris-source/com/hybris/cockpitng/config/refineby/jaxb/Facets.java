/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.refineby.jaxb;

import com.hybris.cockpitng.core.config.annotations.Mergeable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for facets complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="facets"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="facet" type="{http://www.hybris.com/cockpitng/config/refineBy}facet" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="not-listed" type="{http://www.hybris.com/cockpitng/config/refineBy}notListedFacets" /&gt;
 *       &lt;attribute name="order" type="{http://www.hybris.com/cockpitng/config/refineBy}facetsOrder" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "facets", namespace = "http://www.hybris.com/cockpitng/config/refineBy", propOrder = {
                "facet"
})
public class Facets
{
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/config/refineBy")
    @Mergeable(key = "name")
    protected List<Facet> facet;
    @XmlAttribute(name = "not-listed")
    protected NotListedFacets notListed;
    @XmlAttribute(name = "order")
    protected FacetsOrder order;


    /**
     * Gets the value of the facet property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the facet property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFacet().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Facet }
     *
     *
     */
    public List<Facet> getFacet()
    {
        if(facet == null)
        {
            facet = new ArrayList<Facet>();
        }
        return this.facet;
    }


    /**
     * Gets the value of the notListed property.
     *
     * @return
     *     possible object is
     *     {@link NotListedFacets }
     *
     */
    public NotListedFacets getNotListed()
    {
        return notListed;
    }


    /**
     * Sets the value of the notListed property.
     *
     * @param value
     *     allowed object is
     *     {@link NotListedFacets }
     *
     */
    public void setNotListed(NotListedFacets value)
    {
        this.notListed = value;
    }


    /**
     * Gets the value of the order property.
     *
     * @return
     *     possible object is
     *     {@link FacetsOrder }
     *
     */
    public FacetsOrder getOrder()
    {
        return order;
    }


    /**
     * Sets the value of the order property.
     *
     * @param value
     *     allowed object is
     *     {@link FacetsOrder }
     *
     */
    public void setOrder(FacetsOrder value)
    {
        this.order = value;
    }
}
