/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.refineby.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for facetsOrder.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;simpleType name="facetsOrder"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="listed"/&gt;
 *     &lt;enumeration value="received"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "facetsOrder", namespace = "http://www.hybris.com/cockpitng/config/refineBy")
@XmlEnum
public enum FacetsOrder
{
    @XmlEnumValue("listed")
    LISTED("listed"),
    @XmlEnumValue("received")
    RECEIVED("received");
    private final String value;


    FacetsOrder(String v)
    {
        value = v;
    }


    public String value()
    {
        return value;
    }


    public static FacetsOrder fromValue(String v)
    {
        for(FacetsOrder c : FacetsOrder.values())
        {
            if(c.value.equals(v))
            {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
