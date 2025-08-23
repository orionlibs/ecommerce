/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.notification.jaxb;

import com.hybris.cockpitng.core.config.annotations.Mergeable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for NotificationRenderer complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="NotificationRenderingInfo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="parameter" type="{http://www.hybris.com/cockpitng/config/notifications}NotificationParameter" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="renderer" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NotificationRenderingInfo", propOrder = {
                "parameter"
})
@XmlSeeAlso({
                Notification.class
})
public class NotificationRenderingInfo
{
    @Mergeable(key = "name")
    protected List<NotificationParameter> parameter;
    @XmlAttribute(name = "renderer")
    protected String renderer;


    /**
     * Gets the value of the parameter property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameter property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameter().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NotificationParameter }
     *
     *
     */
    public List<NotificationParameter> getParameter()
    {
        if(parameter == null)
        {
            parameter = new ArrayList<NotificationParameter>();
        }
        return this.parameter;
    }


    /**
     * Gets the value of the renderer property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRenderer()
    {
        return renderer;
    }


    /**
     * Sets the value of the renderer property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRenderer(String value)
    {
        this.renderer = value;
    }
}
