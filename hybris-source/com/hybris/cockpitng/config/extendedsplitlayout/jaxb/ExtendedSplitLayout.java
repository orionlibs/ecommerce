/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.extendedsplitlayout.jaxb;

import com.hybris.cockpitng.core.config.annotations.Mergeable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="layout-mapping" type="{http://www.hybris.com/cockpitng/config/extendedsplitlayout}LayoutMapping" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="defaultLayout" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
                "layoutMapping"
})
@XmlRootElement(name = "extended-split-layout", namespace = "http://www.hybris.com/cockpitng/config/extendedsplitlayout")
public class ExtendedSplitLayout
{
    @XmlElement(name = "layout-mapping", namespace = "http://www.hybris.com/cockpitng/config/extendedsplitlayout")
    @Mergeable(key = "parentLayout")
    protected List<LayoutMapping> layoutMapping;
    @XmlAttribute(name = "defaultLayout")
    protected String defaultLayout;


    /**
     * Gets the value of the layoutMapping property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the layoutMapping property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLayoutMapping().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LayoutMapping }
     *
     *
     */
    public List<LayoutMapping> getLayoutMapping()
    {
        if(layoutMapping == null)
        {
            layoutMapping = new ArrayList<LayoutMapping>();
        }
        return this.layoutMapping;
    }


    /**
     * Gets the value of the defaultLayout property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDefaultLayout()
    {
        return defaultLayout;
    }


    /**
     * Sets the value of the defaultLayout property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDefaultLayout(String value)
    {
        this.defaultLayout = value;
    }
}
