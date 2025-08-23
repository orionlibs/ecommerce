/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.editorarea;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for customElement complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="customElement"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.hybris.com/cockpitng/component/editorArea}abstractPositioned"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="default" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="locale" type="{http://www.hybris.com/cockpitng/component/editorArea}localeCustomElement" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customElement", namespace = "http://www.hybris.com/cockpitng/component/editorArea", propOrder = {
                "_default",
                "locale"
})
public class CustomElement
                extends AbstractPositioned
{
    @XmlElement(name = "default", namespace = "http://www.hybris.com/cockpitng/component/editorArea", required = true)
    protected String _default;
    @XmlElement(namespace = "http://www.hybris.com/cockpitng/component/editorArea")
    protected List<LocaleCustomElement> locale;


    /**
     * Gets the value of the default property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDefault()
    {
        return _default;
    }


    /**
     * Sets the value of the default property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDefault(String value)
    {
        this._default = value;
    }


    /**
     * Gets the value of the locale property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the locale property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocale().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LocaleCustomElement }
     *
     *
     */
    public List<LocaleCustomElement> getLocale()
    {
        if(locale == null)
        {
            locale = new ArrayList<LocaleCustomElement>();
        }
        return this.locale;
    }
}
