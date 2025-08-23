/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.jaxb.wizard;

import com.hybris.cockpitng.core.config.annotations.Mergeable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for AbstractFlowType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="AbstractFlowType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="prepareStream" type="{http://www.hybris.com/cockpitng/config/wizard-config}PrepareType" minOccurs="0"/&gt;
 *         &lt;choice maxOccurs="unbounded"&gt;
 *           &lt;element name="step" type="{http://www.hybris.com/cockpitng/config/wizard-config}StepType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *           &lt;element name="subflow" type="{http://www.hybris.com/cockpitng/config/wizard-config}SubflowType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="model" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="merge-mode" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractFlowType", namespace = "http://www.hybris.com/cockpitng/config/wizard-config", propOrder = {
                "prepare",
                "stepOrSubflow",
                "handler"
})
@XmlSeeAlso({
                SubflowType.class,
                Flow.class
})
public abstract class AbstractFlowType
{
    @Mergeable(key = "id")
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/config/wizard-config")
    protected PrepareType prepare;
    @XmlElements({
                    @XmlElement(name = "step", namespace = "http://www.hybris.com/cockpitng/config/wizard-config", type = StepType.class),
                    @XmlElement(name = "subflow", namespace = "http://www.hybris.com/cockpitng/config/wizard-config", type = SubflowType.class)
    })
    @Mergeable(key = "generatedId")
    protected List<Object> stepOrSubflow;
    protected List<ComposedHandlerType> handler;
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "model")
    protected String model;
    @XmlAttribute(name = "merge-mode")
    protected String mergeMode;
    @XmlTransient
    protected String generatedId;


    /**
     * Gets the value of the prepareStream property.
     *
     * @return
     *     possible object is
     *     {@link PrepareType }
     *
     */
    public PrepareType getPrepare()
    {
        return prepare;
    }


    /**
     * Sets the value of the prepareStream property.
     *
     * @param value
     *     allowed object is
     *     {@link PrepareType }
     *
     */
    public void setPrepare(PrepareType value)
    {
        this.prepare = value;
    }


    /**
     * Gets the value of the stepOrSubflow property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stepOrSubflow property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStepOrSubflow().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StepType }
     * {@link SubflowType }
     *
     *
     */
    public List<Object> getStepOrSubflow()
    {
        if(stepOrSubflow == null)
        {
            stepOrSubflow = new ArrayList<>();
        }
        return this.stepOrSubflow;
    }


    /**
     * Gets the value of the handlers property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the handlers property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHandlers().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ComposedHandlerType }
     *
     *
     */
    public List<ComposedHandlerType> getHandler()
    {
        if(handler == null)
        {
            handler = new ArrayList<>();
        }
        return this.handler;
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
     * Gets the value of the model property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getModel()
    {
        return model;
    }


    /**
     * Sets the value of the model property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setModel(String value)
    {
        this.model = value;
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
}
