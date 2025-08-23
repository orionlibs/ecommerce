/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.impl.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for DependencyInjectionPoint.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;simpleType name="DependencyInjectionPoint"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="HEADER"/&gt;
 *     &lt;enumeration value="BEFORE_BODY"/&gt;
 *     &lt;enumeration value="AFTER_BODY"/&gt;
 *     &lt;enumeration value="BEFORE_WIDGET"/&gt;
 *     &lt;enumeration value="AFTER_WIDGET"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "DependencyInjectionPoint")
@XmlEnum
public enum DependencyInjectionPoint
{
    HEADER,
    BEFORE_BODY,
    AFTER_BODY,
    BEFORE_WIDGET,
    AFTER_WIDGET;


    public String value()
    {
        return name();
    }


    public static DependencyInjectionPoint fromValue(String v)
    {
        return valueOf(v);
    }
}
