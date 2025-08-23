/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Positioned;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for abstractDynamicElement complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="abstractDynamicElement"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.hybris.com/cockpitng/config/common}positioned"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="scriptingConfig" type="{http://www.hybris.com/cockpitng/component/dynamicForms}scriptingConfig" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="merge-mode" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="visibleIf" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="disabledIf" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="modelProperty" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="triggeredOn" type="{http://www.w3.org/2001/XMLSchema}string" default="*" /&gt;
 *       &lt;attribute name="qualifier" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractDynamicElement", namespace = "http://www.hybris.com/cockpitng/component/dynamicForms", propOrder =
                {"scriptingConfig"})
@XmlSeeAlso(
                {DynamicAttribute.class, DynamicSection.class, DynamicTab.class})
public abstract class AbstractDynamicElement extends Positioned
{
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/component/dynamicForms")
    protected ScriptingConfig scriptingConfig;
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "merge-mode")
    protected String mergeMode;
    @XmlAttribute(name = "visibleIf")
    protected String visibleIf;
    @XmlAttribute(name = "disabledIf")
    protected String disabledIf;
    @XmlAttribute(name = "modelProperty")
    protected String modelProperty;
    @XmlAttribute(name = "triggeredOn")
    protected String triggeredOn;
    @XmlAttribute(name = "qualifier", required = true)
    protected String qualifier;


    /**
     * Gets the value of the scriptingConfig property.
     *
     * @return possible object is {@link ScriptingConfig }
     */
    public ScriptingConfig getScriptingConfig()
    {
        if(scriptingConfig == null)
        {
            scriptingConfig = new ObjectFactory().createScriptingConfig();
        }
        return scriptingConfig;
    }


    /**
     * Sets the value of the scriptingConfig property.
     *
     * @param value
     *           allowed object is {@link ScriptingConfig }
     */
    public void setScriptingConfig(final ScriptingConfig value)
    {
        this.scriptingConfig = value;
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
    public void setId(final String value)
    {
        this.id = value;
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
    public void setMergeMode(final String value)
    {
        this.mergeMode = value;
    }


    /**
     * Gets the value of the visibleIf property.
     *
     * @return possible object is {@link String }
     */
    public String getVisibleIf()
    {
        return visibleIf;
    }


    /**
     * Sets the value of the visibleIf property.
     *
     * @param value
     *           allowed object is {@link String }
     */
    public void setVisibleIf(final String value)
    {
        this.visibleIf = value;
    }


    /**
     * Gets the value of the disabledIf property.
     *
     * @return possible object is {@link String }
     */
    public String getDisabledIf()
    {
        return disabledIf;
    }


    /**
     * Sets the value of the disabledIf property.
     *
     * @param value
     *           allowed object is {@link String }
     */
    public void setDisabledIf(final String value)
    {
        this.disabledIf = value;
    }


    /**
     * Gets the value of the modelProperty property.
     *
     * @return possible object is {@link String }
     */
    public String getModelProperty()
    {
        return modelProperty;
    }


    /**
     * Sets the value of the modelProperty property.
     *
     * @param value
     *           allowed object is {@link String }
     */
    public void setModelProperty(final String value)
    {
        this.modelProperty = value;
    }


    /**
     * Gets the value of the triggeredOn property.
     *
     * @return possible object is {@link String }
     */
    public String getTriggeredOn()
    {
        if(triggeredOn == null)
        {
            return "*";
        }
        else
        {
            return triggeredOn;
        }
    }


    /**
     * Sets the value of the triggeredOn property.
     *
     * @param value
     *           allowed object is {@link String }
     */
    public void setTriggeredOn(final String value)
    {
        this.triggeredOn = value;
    }


    /**
     * Gets the value of the qualifier property.
     *
     * @return possible object is {@link String }
     */
    public String getQualifier()
    {
        return qualifier;
    }


    /**
     * Sets the value of the qualifier property.
     *
     * @param value
     *           allowed object is {@link String }
     */
    public void setQualifier(final String value)
    {
        this.qualifier = value;
    }


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null)
        {
            return false;
        }
        if(o.getClass() != this.getClass())
        {
            return false;
        }
        final AbstractDynamicElement that = (AbstractDynamicElement)o;
        if(scriptingConfig != null ? !scriptingConfig.equals(that.scriptingConfig) : (that.scriptingConfig != null))
        {
            return false;
        }
        if(id != null ? !id.equals(that.id) : (that.id != null))
        {
            return false;
        }
        if(mergeMode != null ? !mergeMode.equals(that.mergeMode) : (that.mergeMode != null))
        {
            return false;
        }
        if(visibleIf != null ? !visibleIf.equals(that.visibleIf) : (that.visibleIf != null))
        {
            return false;
        }
        if(disabledIf != null ? !disabledIf.equals(that.disabledIf) : (that.disabledIf != null))
        {
            return false;
        }
        if(modelProperty != null ? !modelProperty.equals(that.modelProperty) : (that.modelProperty != null))
        {
            return false;
        }
        if(triggeredOn != null ? !triggeredOn.equals(that.triggeredOn) : (that.triggeredOn != null))
        {
            return false;
        }
        return !(qualifier != null ? !qualifier.equals(that.qualifier) : (that.qualifier != null));
    }


    @Override
    public int hashCode()
    {
        int result = scriptingConfig != null ? scriptingConfig.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (mergeMode != null ? mergeMode.hashCode() : 0);
        result = 31 * result + (visibleIf != null ? visibleIf.hashCode() : 0);
        result = 31 * result + (disabledIf != null ? disabledIf.hashCode() : 0);
        result = 31 * result + (modelProperty != null ? modelProperty.hashCode() : 0);
        result = 31 * result + (triggeredOn != null ? triggeredOn.hashCode() : 0);
        result = 31 * result + (qualifier != null ? qualifier.hashCode() : 0);
        return result;
    }


    @Override
    public String toString()
    {
        return String.format(
                        "id=\"%s\" qualifier=\"%s\" visibleIf=\"%s\" disabledIf=\"%s\" triggeredOn=\"%s\" modelProperty=\"%s\">\n%s\n",
                        getId(), getQualifier(), getVisibleIf(), getDisabledIf(), getTriggeredOn(), getModelProperty(),
                        getScriptingConfig().toString());
    }
}
