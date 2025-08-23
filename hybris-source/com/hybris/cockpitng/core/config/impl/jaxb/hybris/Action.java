/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.hybris;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for action complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="action"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="parameter" type="{http://www.hybris.com/cockpit/config/hybris}parameter" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="action-id" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="property" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="output-property" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="triggerOnKeys" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="merge-mode" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "action", propOrder =
                {"parameter"})
public class Action extends Positioned
{
    protected List<Parameter> parameter;
    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "action-id")
    protected String actionId;
    @XmlAttribute(name = "property")
    protected String property;
    @XmlAttribute(name = "output-property")
    protected String outputProperty;
    @XmlAttribute(name = "triggerOnKeys")
    protected String triggerOnKeys;
    @XmlAttribute(name = "merge-mode")
    protected String mergeMode;


    /**
     * Gets the value of the parameter property.
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the parameter property.
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getParameter().add(newItem);
     * </pre>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Parameter }
     */
    public List<Parameter> getParameter()
    {
        if(parameter == null)
        {
            parameter = new ArrayList<Parameter>();
        }
        return this.parameter;
    }


    /**
     * Gets the value of the id property.
     *
     * @return possible object is {@link String }
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
     */
    public void setId(String value)
    {
        this.id = value;
    }


    /**
     * Gets the value of the actionId property.
     *
     * @return possible object is {@link String }
     */
    public String getActionId()
    {
        return actionId;
    }


    /**
     * Sets the value of the actionId property.
     *
     * @param value
     *           allowed object is {@link String }
     */
    public void setActionId(String value)
    {
        this.actionId = value;
    }


    /**
     * Gets the value of the property property.
     *
     * @return possible object is {@link String }
     */
    public String getProperty()
    {
        return property;
    }


    /**
     * Sets the value of the property property.
     *
     * @param value
     *           allowed object is {@link String }
     */
    public void setProperty(String value)
    {
        this.property = value;
    }


    /**
     * Gets the value of the outputProperty property.
     *
     * @return possible object is {@link String }
     */
    public String getOutputProperty()
    {
        return outputProperty;
    }


    /**
     * Sets the value of the outputProperty property.
     *
     * @param value
     *           allowed object is {@link String }
     */
    public void setOutputProperty(String value)
    {
        this.outputProperty = value;
    }


    /**
     * Gets the value of the triggerOnKeys property.
     *
     * @return possible object is {@link String }
     */
    public String getTriggerOnKeys()
    {
        return triggerOnKeys;
    }


    /**
     * Sets the value of the triggerOnKeys property.
     *
     * @param value
     *           allowed object is {@link String }
     */
    public void setTriggerOnKeys(String value)
    {
        this.triggerOnKeys = value;
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
     * @param value
     *           allowed object is {@link String }
     */
    public void setMergeMode(String value)
    {
        this.mergeMode = value;
    }
}
