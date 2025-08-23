/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for scriptingType.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;simpleType name="scriptingType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="inline"/&gt;
 *     &lt;enumeration value="uri"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "scriptingType", namespace = "http://www.hybris.com/cockpitng/component/dynamicForms")
@XmlEnum
public enum ScriptingType
{
    @XmlEnumValue("inline")
    INLINE("inline"), @XmlEnumValue("uri")
URI("uri");
    private final String value;


    ScriptingType(final String v)
    {
        value = v;
    }


    public String value()
    {
        return value;
    }


    public static ScriptingType fromValue(final String v)
    {
        for(final ScriptingType c : ScriptingType.values())
        {
            if(c.value.equals(v))
            {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
