/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.notification.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for Destination.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;simpleType name="Destination"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="TOPMOST"/&gt;
 *     &lt;enumeration value="PARENT"/&gt;
 *     &lt;enumeration value="GLOBAL"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "Destination")
@XmlEnum
public enum Destination
{
    TOPMOST,
    PREVIOUS,
    GLOBAL;


    public String value()
    {
        return name();
    }


    public static Destination fromValue(String v)
    {
        return valueOf(v);
    }
}
