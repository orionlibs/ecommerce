/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.perspectivechooser.jaxb;

import com.hybris.cockpitng.config.viewswitcher.jaxb.ViewSwitcher;
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
 *       &lt;sequence&gt;
 *         &lt;element name="defaultPerspective" type="{http://www.hybris.com/cockpitng/config/perspectiveChooser}defaultPerspective" minOccurs="0"/&gt;
 *         &lt;element name="authority" type="{http://www.hybris.com/cockpitng/config/perspectiveChooser}authority" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="format" type="{http://www.hybris.com/cockpitng/config/perspectiveChooser}format" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"defaultPerspective", "authority", "format"})
@XmlRootElement(name = "perspective-chooser", namespace = "http://www.hybris.com/cockpitng/config/perspectiveChooser")
public class PerspectiveChooser
{
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/config/perspectiveChooser")
    protected DefaultPerspective defaultPerspective;
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/config/perspectiveChooser")
    protected List<Authority> authority;
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/config/perspectiveChooser")
    protected List<Format> format;


    /**
     * Gets the value of the defaultPerspective property.
     *
     * @return
     *         possible object is {@link DefaultPerspective }
     *
     */
    public DefaultPerspective getDefaultPerspective()
    {
        return defaultPerspective;
    }


    /**
     * Sets the value of the defaultPerspective property.
     *
     * @param value
     *           allowed object is {@link DefaultPerspective }
     *
     */
    public void setDefaultPerspective(DefaultPerspective value)
    {
        this.defaultPerspective = value;
    }


    /**
     * @deprecated since 6.5, use {@link ViewSwitcher#getAuthority()} instead
     */
    @Deprecated(since = "6.5", forRemoval = true)
    public List<Authority> getAuthority()
    {
        if(authority == null)
        {
            authority = new ArrayList<Authority>();
        }
        return this.authority;
    }


    /**
     * Gets the value of the format property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the format property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getFormat().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Format }
     *
     *
     */
    public List<Format> getFormat()
    {
        if(format == null)
        {
            format = new ArrayList<Format>();
        }
        return this.format;
    }
}
