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
 * Java class for dynamicAttribute complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="dynamicAttribute"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.hybris.com/cockpitng/component/dynamicForms}abstractDynamicElement"&gt;
 *       &lt;attribute name="computedValue" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="lang" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dynamicAttribute", namespace = "http://www.hybris.com/cockpitng/component/dynamicForms")
public class DynamicAttribute extends AbstractDynamicElement
{
    @XmlAttribute(name = "computedValue")
    protected String computedValue;
    @XmlAttribute(name = "lang")
    protected String lang;
    @XmlAttribute(name = "paramName")
    protected String paramName;


    /**
     * Gets the value of the computedValue property.
     *
     * @return possible object is {@link String }
     */
    public String getComputedValue()
    {
        return computedValue;
    }


    /**
     * Sets the value of the computedValue property.
     *
     * @param value
     *           allowed object is {@link String }
     */
    public void setComputedValue(final String value)
    {
        this.computedValue = value;
    }


    /**
     * Gets the value of the lang property.
     *
     * @return possible object is {@link String }
     */
    public String getLang()
    {
        return lang;
    }


    /**
     * Sets the value of the lang property.
     *
     * @param value
     *           allowed object is {@link String }
     */
    public void setLang(final String value)
    {
        this.lang = value;
    }


    /**
     * Sets the value of the paramName property.
     *
     * @param value
     *           allowed object is {@link String }
     */
    public void setParamName(final String value)
    {
        this.paramName = value;
    }


    /**
     * Gets the value of the paramName property.
     *
     * @return possible object is {@link String }
     */
    public String getParamName()
    {
        return paramName;
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
        if(!super.equals(o))
        {
            return false;
        }
        final DynamicAttribute that = (DynamicAttribute)o;
        if(computedValue != null ? !computedValue.equals(that.computedValue) : (that.computedValue != null))
        {
            return false;
        }
        if(lang != null ? !lang.equals(that.lang) : (that.lang != null))
        {
            return false;
        }
        return paramName != null ? paramName.equals(that.paramName) : (that.paramName == null);
    }


    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + (computedValue != null ? computedValue.hashCode() : 0);
        result = 31 * result + (lang != null ? lang.hashCode() : 0);
        result = 31 * result + (paramName != null ? paramName.hashCode() : 0);
        return result;
    }


    @Override
    public String toString()
    {
        return String.format("<attribute computedValue=\"%s\" lang=\"%s\" paramName=\"%s\" %s</attribute>", getComputedValue(),
                        getLang(), getParamName(), super.toString());
    }
}
