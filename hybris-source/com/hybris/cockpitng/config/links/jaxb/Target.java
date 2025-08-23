/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.links.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for target.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;simpleType name="target"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="_blank"/&gt;
 *     &lt;enumeration value="_self"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "target", namespace = "http://www.hybris.com/cockpitng/config/links")
@XmlEnum
public enum Target
{
    @XmlEnumValue("_blank")
    BLANK("_blank"),
    @XmlEnumValue("_self")
    SELF("_self");
    private final String value;


    Target(String v)
    {
        value = v;
    }


    public String value()
    {
        return value;
    }


    public static Target fromValue(String v)
    {
        for(Target c : Target.values())
        {
            if(c.value.equals(v))
            {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
