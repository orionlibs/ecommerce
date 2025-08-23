/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.perspectivechooser.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for authority complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="authority"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="perspective" type="{http://www.hybris.com/cockpitng/config/perspectiveChooser}perspective" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "authority", namespace = "http://www.hybris.com/cockpitng/config/perspectiveChooser", propOrder = {"perspective"})
public class Authority
{
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/config/perspectiveChooser")
    protected List<Perspective> perspective;
    @XmlAttribute(name = "name", required = true)
    protected String name;


    /**
     * Gets the value of the perspective property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the perspective property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getPerspective().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Perspective }
     *
     *
     */
    public List<Perspective> getPerspective()
    {
        if(perspective == null)
        {
            perspective = new ArrayList<Perspective>();
        }
        return this.perspective;
    }


    /**
     * Gets the value of the name property.
     *
     * @return
     *         possible object is {@link String }
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
     *           allowed object is {@link String }
     *
     */
    public void setName(String value)
    {
        this.name = value;
    }
}
