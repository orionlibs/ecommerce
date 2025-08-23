/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.refineby.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for notListedFacets.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;simpleType name="notListedFacets"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="visible"/&gt;
 *     &lt;enumeration value="skip"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "notListedFacets", namespace = "http://www.hybris.com/cockpitng/config/refineBy")
@XmlEnum
public enum NotListedFacets
{
    @XmlEnumValue("visible")
    VISIBLE("visible"),
    @XmlEnumValue("skip")
    SKIP("skip");
    private final String value;


    NotListedFacets(String v)
    {
        value = v;
    }


    public String value()
    {
        return value;
    }


    public static NotListedFacets fromValue(String v)
    {
        for(NotListedFacets c : NotListedFacets.values())
        {
            if(c.value.equals(v))
            {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
