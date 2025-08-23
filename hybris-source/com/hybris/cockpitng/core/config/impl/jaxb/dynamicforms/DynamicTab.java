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
 * Java class for dynamicTab complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="dynamicTab"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.hybris.com/cockpitng/component/dynamicForms}abstractDynamicElement"&gt;
 *       &lt;attribute name="gotoTabId" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="gotoTabIf" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dynamicTab", namespace = "http://www.hybris.com/cockpitng/component/dynamicForms")
public class DynamicTab extends AbstractDynamicElement
{
    @XmlAttribute(name = "gotoTabId")
    protected String gotoTabId;
    @XmlAttribute(name = "gotoTabIf")
    protected String gotoTabIf;
    @XmlAttribute(name = "fallbackGotoTabId")
    protected String fallbackGotoTabId;


    /**
     * Gets the value of the gotoTabId property.
     *
     * @return possible object is {@link String }
     */
    public String getGotoTabId()
    {
        return gotoTabId;
    }


    /**
     * Sets the value of the gotoTabId property.
     *
     * @param gotoTabId
     *           allowed object is {@link String }
     */
    public void setGotoTabId(final String gotoTabId)
    {
        this.gotoTabId = gotoTabId;
    }


    /**
     * Gets the value of the gotoTabIf property.
     *
     * @return possible object is {@link String }
     */
    public String getGotoTabIf()
    {
        if(gotoTabIf == null)
        {
            return Boolean.TRUE.toString();
        }
        return gotoTabIf;
    }


    /**
     * Sets the value of the gotoTabIf property.
     *
     * @param gotoTabIf
     *           allowed object is {@link String }
     */
    public void setGotoTabIf(final String gotoTabIf)
    {
        this.gotoTabIf = gotoTabIf;
    }


    /**
     * Gets the value of the fallbackGotoTabId property.
     *
     * @return possible object is {@link String }
     */
    public String getFallbackGotoTabId()
    {
        return fallbackGotoTabId;
    }


    /**
     * Sets the value of the fallbackGotoTabId property.
     *
     * @param fallbackGotoTabId
     *           allowed object is {@link String }
     */
    public void setFallbackGotoTabId(final String fallbackGotoTabId)
    {
        this.fallbackGotoTabId = fallbackGotoTabId;
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
        final DynamicTab that = (DynamicTab)o;
        if(gotoTabId != null ? !gotoTabId.equals(that.gotoTabId) : (that.gotoTabId != null))
        {
            return false;
        }
        if(gotoTabIf != null ? !gotoTabIf.equals(that.gotoTabIf) : (that.gotoTabIf != null))
        {
            return false;
        }
        return !(fallbackGotoTabId != null ? !fallbackGotoTabId.equals(that.fallbackGotoTabId) : (that.fallbackGotoTabId != null));
    }


    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + (gotoTabId != null ? gotoTabId.hashCode() : 0);
        result = 31 * result + (gotoTabIf != null ? gotoTabIf.hashCode() : 0);
        result = 31 * result + (fallbackGotoTabId != null ? fallbackGotoTabId.hashCode() : 0);
        return result;
    }


    @Override
    public String toString()
    {
        return String.format("<tab gotoTabId=\"%s\" gotoTabIf=\"%s\" fallbackGotoTabId=\"%s\" %s</tab>", getGotoTabId(),
                        getGotoTabIf(), getFallbackGotoTabId(), super.toString());
    }
}
