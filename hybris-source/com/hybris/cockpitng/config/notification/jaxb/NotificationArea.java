/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.notification.jaxb;

import com.hybris.cockpitng.core.config.annotations.Mergeable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;sequence&gt;
 *           &lt;element name="notifications" type="{http://www.hybris.com/cockpitng/config/notifications}Notification" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;/sequence&gt;
 *         &lt;element name="defaults" type="{http://www.hybris.com/cockpitng/config/notifications}NotificationDefaults" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
                "notifications",
                "defaults"
})
@XmlRootElement(name = "notification-area")
public class NotificationArea
{
    @Mergeable(key = {"eventType", "level", "referencesType"})
    protected List<Notification> notifications;
    protected NotificationDefaults defaults;


    /**
     * Gets the value of the notifications property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the notifications property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNotifications().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Notification }
     *
     *
     */
    public List<Notification> getNotifications()
    {
        if(notifications == null)
        {
            notifications = new ArrayList<Notification>();
        }
        return this.notifications;
    }


    /**
     * Gets the value of the defaults property.
     *
     * @return
     *     possible object is
     *     {@link NotificationDefaults }
     *
     */
    public NotificationDefaults getDefaults()
    {
        return defaults;
    }


    /**
     * Sets the value of the defaults property.
     *
     * @param value
     *     allowed object is
     *     {@link NotificationDefaults }
     *
     */
    public void setDefaults(NotificationDefaults value)
    {
        this.defaults = value;
    }
}
