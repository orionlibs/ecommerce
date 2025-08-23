/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for dynamicVisitor complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="dynamicVisitor"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.hybris.com/cockpitng/config/common}positioned"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="scriptingConfig" type="{http://www.hybris.com/cockpitng/component/dynamicForms}scriptingConfig" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="merge-mode" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="beanId" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dynamicVisitor", namespace = "http://www.hybris.com/cockpitng/component/dynamicForms", propOrder = {"scriptingConfig"})
public class DynamicVisitor extends Positioned
{
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/component/dynamicForms")
    protected ScriptingConfig scriptingConfig;
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "merge-mode")
    protected String mergeMode;
    @XmlAttribute(name = "beanId", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String beanId;


    /**
     * Gets the value of the scriptingConfig property.
     *
     * @return
     *         possible object is {@link ScriptingConfig }
     *
     */
    public ScriptingConfig getScriptingConfig()
    {
        return scriptingConfig;
    }


    /**
     * Sets the value of the scriptingConfig property.
     *
     * @param value
     *           allowed object is {@link ScriptingConfig }
     *
     */
    public void setScriptingConfig(final ScriptingConfig value)
    {
        this.scriptingConfig = value;
    }


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
    public void setId(final String value)
    {
        this.id = value;
    }


    /**
     * Gets the value of the mergeMode property.
     *
     * @return
     *         possible object is {@link String }
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
     *           allowed object is {@link String }
     *
     */
    public void setMergeMode(final String value)
    {
        this.mergeMode = value;
    }


    /**
     * Gets the value of the beanId property.
     *
     * @return
     *         possible object is {@link String }
     *
     */
    public String getBeanId()
    {
        return beanId;
    }


    /**
     * Sets the value of the beanId property.
     *
     * @param value
     *           allowed object is {@link String }
     *
     */
    public void setBeanId(final String value)
    {
        this.beanId = value;
    }
}
