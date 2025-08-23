/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.jaxb.wizard;

import com.hybris.cockpitng.core.config.annotations.Mergeable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for PrepareType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="PrepareType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="initialize" type="{http://www.hybris.com/cockpitng/config/wizard-config}InitializeType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="assign" type="{http://www.hybris.com/cockpitng/config/wizard-config}AssignType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="merge-mode" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PrepareType", namespace = "http://www.hybris.com/cockpitng/config/wizard-config", propOrder = {
                "initialize",
                "assign"
})
public class PrepareType
{
    @Mergeable(key = "property")
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/config/wizard-config")
    protected List<InitializeType> initialize;
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/config/wizard-config")
    @Mergeable(key = {"property", "lang"})
    protected List<AssignType> assign;
    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "merge-mode")
    protected String mergeMode;
    @XmlAttribute(name = "handler")
    protected String handler;


    /**
     * Gets the value of the initialize property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the initialize property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInitialize().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InitializeType }
     *
     *
     */
    public List<InitializeType> getInitialize()
    {
        if(initialize == null)
        {
            initialize = new ArrayList<InitializeType>();
        }
        return this.initialize;
    }


    /**
     * Gets the value of the assign property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the assign property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssign().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AssignType }
     *
     *
     */
    public List<AssignType> getAssign()
    {
        if(assign == null)
        {
            assign = new ArrayList<AssignType>();
        }
        return this.assign;
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
     * Gets the value of the handler property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getHandler()
    {
        return handler;
    }


    /**
     * Sets the value of the handler property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setHandler(String value)
    {
        this.handler = value;
    }
}
