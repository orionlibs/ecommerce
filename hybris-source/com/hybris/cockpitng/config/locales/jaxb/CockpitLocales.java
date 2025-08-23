/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.locales.jaxb;

import com.google.common.base.Objects;
import com.hybris.cockpitng.core.config.annotations.Mergeable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0"&gt;
 *         &lt;element name="cockpit-locale" type="{http://www.hybris.com/cockpitng/config/availableLocales}cockpit-locale"
 *         maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"cockpitLocale"})
@XmlRootElement(name = "cockpit-locales")
public class CockpitLocales
{
    @XmlElement(name = "cockpit-locale")
    @Mergeable(key = "name")
    protected List<CockpitLocale> cockpitLocale;


    /**
     * <P>
     * Gets the value of the cockpitLocale property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the cockpitLocale property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getCockpitLocale().add(newItem);
     * </pre>
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link CockpitLocale }
     */
    @Mergeable(key = "locale")
    public List<CockpitLocale> getCockpitLocale()
    {
        if(cockpitLocale == null)
        {
            cockpitLocale = new ArrayList<CockpitLocale>();
        }
        return this.cockpitLocale;
    }


    @Override
    public int hashCode()
    {
        return Objects.hashCode(cockpitLocale);
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
        final CockpitLocales other = (CockpitLocales)obj;
        return Objects.equal(this.cockpitLocale, other.cockpitLocale);
    }
}
