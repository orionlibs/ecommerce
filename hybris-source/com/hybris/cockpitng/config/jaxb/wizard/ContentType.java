/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.jaxb.wizard;

import com.hybris.cockpitng.core.config.annotations.Mergeable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ContentType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ContentType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice&gt;
 *           &lt;sequence&gt;
 *             &lt;element name="column" type="{http://www.hybris.com/cockpitng/config/wizard-config}ContentType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *           &lt;/sequence&gt;
 *           &lt;sequence&gt;
 *             &lt;choice maxOccurs="unbounded"&gt;
 *               &lt;element name="property" type="{http://www.hybris.com/cockpitng/config/wizard-config}PropertyType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *               &lt;element name="property-list" type="{http://www.hybris.com/cockpitng/config/wizard-config}PropertyListType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *               &lt;element name="custom-view" type="{http://www.hybris.com/cockpitng/config/wizard-config}ViewType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *             &lt;/choice&gt;
 *           &lt;/sequence&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
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
@XmlType(name = "ContentType", namespace = "http://www.hybris.com/cockpitng/config/wizard-config", propOrder = {
                "column",
                "propertyOrPropertyListOrCustomView"
})
public class ContentType
{
    @Mergeable(key = "id")
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/config/wizard-config")
    protected List<ContentType> column;
    @XmlElements({
                    @XmlElement(name = "property", namespace = "http://www.hybris.com/cockpitng/config/wizard-config", type = PropertyType.class),
                    @XmlElement(name = "property-list", namespace = "http://www.hybris.com/cockpitng/config/wizard-config", type = PropertyListType.class),
                    @XmlElement(name = "custom-view", namespace = "http://www.hybris.com/cockpitng/config/wizard-config", type = ViewType.class)
    })
    @Mergeable(key = "generatedId")
    protected List<Object> propertyOrPropertyListOrCustomView;
    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "merge-mode")
    protected String mergeMode;
    @XmlAttribute(name = "position")
    protected BigInteger position;


    /**
     * Gets the value of the column property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the column property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getColumn().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContentType }
     *
     *
     */
    public List<ContentType> getColumn()
    {
        if(column == null)
        {
            column = new ArrayList<ContentType>();
        }
        return this.column;
    }


    /**
     * Gets the value of the propertyOrPropertyListOrCustomView property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the propertyOrPropertyListOrCustomView property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPropertyOrPropertyListOrCustomView().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PropertyType }
     * {@link PropertyListType }
     * {@link ViewType }
     *
     *
     */
    public List<Object> getPropertyOrPropertyListOrCustomView()
    {
        if(propertyOrPropertyListOrCustomView == null)
        {
            propertyOrPropertyListOrCustomView = new ArrayList<Object>();
        }
        return this.propertyOrPropertyListOrCustomView;
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
