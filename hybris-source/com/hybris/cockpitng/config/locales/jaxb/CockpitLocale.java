/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.locales.jaxb;

import com.google.common.base.Objects;
import java.io.Serializable;
import java.util.Locale;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for cockpit-locale complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="cockpit-locale"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="locale" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="enabled" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cockpit-locale")
public class CockpitLocale implements Serializable
{
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "locale", required = true)
    @XmlJavaTypeAdapter(LocaleAdapter.class)
    protected Locale locale;
    @XmlAttribute(name = "enabled", required = true)
    protected boolean enabled;
    @XmlAttribute(name = "default", required = false)
    protected boolean defaultLocale;


    /**
     * Gets the value of the name property.
     *
     * @return possible object is {@link String }
     */
    public String getName()
    {
        return name;
    }


    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is {@link String }
     */
    public void setName(final String value)
    {
        this.name = value;
    }


    /**
     * Gets the value of the locale property.
     *
     * @return possible object is {@link String }
     */
    public Locale getLocale()
    {
        return locale;
    }


    /**
     * Sets the value of the locale property.
     *
     * @param value allowed object is {@link String }
     */
    public void setLocale(final Locale value)
    {
        this.locale = value;
    }


    /**
     * Gets the value of the enabled property.
     */
    public boolean isEnabled()
    {
        return enabled;
    }


    /**
     * Sets the value of the enabled property.
     */
    public void setEnabled(final boolean value)
    {
        this.enabled = value;
    }


    /**
     * @return the defaultLocale
     */
    public boolean isDefaultLocale()
    {
        return defaultLocale;
    }


    /**
     * @param defaultLocale the defaultLocale to set
     */
    public void setDefaultLocale(final boolean defaultLocale)
    {
        this.defaultLocale = defaultLocale;
    }


    @Override
    public int hashCode()
    {
        return Objects.hashCode(name, locale, enabled);
    }


    @Override
    public boolean equals(final Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        final CockpitLocale other = (CockpitLocale)obj;
        return Objects.equal(this.name, other.name) && Objects.equal(this.locale, other.locale)
                        && Objects.equal(this.enabled, other.enabled);
    }
}
