/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for scriptingConfig complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="scriptingConfig"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="visibleIfLanguage" type="{http://www.hybris.com/cockpitng/component/dynamicForms}scriptingLanguage" default="SpEL" /&gt;
 *       &lt;attribute name="visibleIfScriptType" type="{http://www.hybris.com/cockpitng/component/dynamicForms}scriptingType" default="inline" /&gt;
 *       &lt;attribute name="disabledIfLanguage" type="{http://www.hybris.com/cockpitng/component/dynamicForms}scriptingLanguage" default="SpEL" /&gt;
 *       &lt;attribute name="disabledIfScriptType" type="{http://www.hybris.com/cockpitng/component/dynamicForms}scriptingType" default="inline" /&gt;
 *       &lt;attribute name="gotoTabIfLanguage" type="{http://www.hybris.com/cockpitng/component/dynamicForms}scriptingLanguage" default="SpEL" /&gt;
 *       &lt;attribute name="gotoTabIfScriptType" type="{http://www.hybris.com/cockpitng/component/dynamicForms}scriptingType" default="inline" /&gt;
 *       &lt;attribute name="computedValueLanguage" type="{http://www.hybris.com/cockpitng/component/dynamicForms}scriptingLanguage" default="SpEL" /&gt;
 *       &lt;attribute name="computedValueScriptType" type="{http://www.hybris.com/cockpitng/component/dynamicForms}scriptingType" default="inline" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "scriptingConfig", namespace = "http://www.hybris.com/cockpitng/component/dynamicForms")
public class ScriptingConfig
{
    @XmlAttribute(name = "visibleIfLanguage")
    protected ScriptingLanguage visibleIfLanguage;
    @XmlAttribute(name = "visibleIfScriptType")
    protected ScriptingType visibleIfScriptType;
    @XmlAttribute(name = "disabledIfLanguage")
    protected ScriptingLanguage disabledIfLanguage;
    @XmlAttribute(name = "disabledIfScriptType")
    protected ScriptingType disabledIfScriptType;
    @XmlAttribute(name = "gotoTabIfLanguage")
    protected ScriptingLanguage gotoTabIfLanguage;
    @XmlAttribute(name = "gotoTabIfScriptType")
    protected ScriptingType gotoTabIfScriptType;
    @XmlAttribute(name = "computedValueLanguage")
    protected ScriptingLanguage computedValueLanguage;
    @XmlAttribute(name = "computedValueScriptType")
    protected ScriptingType computedValueScriptType;


    /**
     * Gets the value of the visibleIfLanguage property.
     *
     * @return
     *         possible object is {@link ScriptingLanguage }
     *
     */
    public ScriptingLanguage getVisibleIfLanguage()
    {
        if(visibleIfLanguage == null)
        {
            return ScriptingLanguage.SP_EL;
        }
        else
        {
            return visibleIfLanguage;
        }
    }


    /**
     * Sets the value of the visibleIfLanguage property.
     *
     * @param value
     *           allowed object is {@link ScriptingLanguage }
     *
     */
    public void setVisibleIfLanguage(final ScriptingLanguage value)
    {
        this.visibleIfLanguage = value;
    }


    /**
     * Gets the value of the visibleIfScriptType property.
     *
     * @return
     *         possible object is {@link ScriptingType }
     *
     */
    public ScriptingType getVisibleIfScriptType()
    {
        if(visibleIfScriptType == null)
        {
            return ScriptingType.INLINE;
        }
        else
        {
            return visibleIfScriptType;
        }
    }


    /**
     * Sets the value of the visibleIfScriptType property.
     *
     * @param value
     *           allowed object is {@link ScriptingType }
     *
     */
    public void setVisibleIfScriptType(final ScriptingType value)
    {
        this.visibleIfScriptType = value;
    }


    /**
     * Gets the value of the disabledIfLanguage property.
     *
     * @return
     *         possible object is {@link ScriptingLanguage }
     *
     */
    public ScriptingLanguage getDisabledIfLanguage()
    {
        if(disabledIfLanguage == null)
        {
            return ScriptingLanguage.SP_EL;
        }
        else
        {
            return disabledIfLanguage;
        }
    }


    /**
     * Sets the value of the disabledIfLanguage property.
     *
     * @param value
     *           allowed object is {@link ScriptingLanguage }
     *
     */
    public void setDisabledIfLanguage(final ScriptingLanguage value)
    {
        this.disabledIfLanguage = value;
    }


    /**
     * Gets the value of the disabledIfScriptType property.
     *
     * @return
     *         possible object is {@link ScriptingType }
     *
     */
    public ScriptingType getDisabledIfScriptType()
    {
        if(disabledIfScriptType == null)
        {
            return ScriptingType.INLINE;
        }
        else
        {
            return disabledIfScriptType;
        }
    }


    /**
     * Sets the value of the disabledIfScriptType property.
     *
     * @param value
     *           allowed object is {@link ScriptingType }
     *
     */
    public void setDisabledIfScriptType(final ScriptingType value)
    {
        this.disabledIfScriptType = value;
    }


    /**
     * Gets the value of the gotoTabIfLanguage property.
     *
     * @return
     *         possible object is {@link ScriptingLanguage }
     *
     */
    public ScriptingLanguage getGotoTabIfLanguage()
    {
        if(gotoTabIfLanguage == null)
        {
            return ScriptingLanguage.SP_EL;
        }
        else
        {
            return gotoTabIfLanguage;
        }
    }


    /**
     * Sets the value of the gotoTabIfLanguage property.
     *
     * @param value
     *           allowed object is {@link ScriptingLanguage }
     *
     */
    public void setGotoTabIfLanguage(final ScriptingLanguage value)
    {
        this.gotoTabIfLanguage = value;
    }


    /**
     * Gets the value of the gotoTabIfScriptType property.
     *
     * @return
     *         possible object is {@link ScriptingType }
     *
     */
    public ScriptingType getGotoTabIfScriptType()
    {
        if(gotoTabIfScriptType == null)
        {
            return ScriptingType.INLINE;
        }
        else
        {
            return gotoTabIfScriptType;
        }
    }


    /**
     * Sets the value of the gotoTabIfScriptType property.
     *
     * @param value
     *           allowed object is {@link ScriptingType }
     *
     */
    public void setGotoTabIfScriptType(final ScriptingType value)
    {
        this.gotoTabIfScriptType = value;
    }


    /**
     * Gets the value of the computedValueLanguage property.
     *
     * @return
     *         possible object is {@link ScriptingLanguage }
     *
     */
    public ScriptingLanguage getComputedValueLanguage()
    {
        if(computedValueLanguage == null)
        {
            return ScriptingLanguage.SP_EL;
        }
        else
        {
            return computedValueLanguage;
        }
    }


    /**
     * Sets the value of the computedValueLanguage property.
     *
     * @param value
     *           allowed object is {@link ScriptingLanguage }
     *
     */
    public void setComputedValueLanguage(final ScriptingLanguage value)
    {
        this.computedValueLanguage = value;
    }


    /**
     * Gets the value of the computedValueScriptType property.
     *
     * @return
     *         possible object is {@link ScriptingType }
     *
     */
    public ScriptingType getComputedValueScriptType()
    {
        if(computedValueScriptType == null)
        {
            return ScriptingType.INLINE;
        }
        else
        {
            return computedValueScriptType;
        }
    }


    /**
     * Sets the value of the computedValueScriptType property.
     *
     * @param value
     *           allowed object is {@link ScriptingType }
     *
     */
    public void setComputedValueScriptType(final ScriptingType value)
    {
        this.computedValueScriptType = value;
    }


    @Override
    public String toString()
    {
        return String
                        .format(
                                        "<ScriptingConfig visibleIfLanguage=%s visibleIfScriptType=%s disabledIfLanguage=%s disabledIfScriptType=%s gotoTabIfLanguage=%s gotoTabIfScriptType=%s computedValueLanguage=%s computedValueScriptType=%s/>",
                                        visibleIfLanguage, visibleIfScriptType, disabledIfLanguage, disabledIfScriptType, gotoTabIfLanguage,
                                        gotoTabIfScriptType, computedValueLanguage, computedValueScriptType);
    }
}
