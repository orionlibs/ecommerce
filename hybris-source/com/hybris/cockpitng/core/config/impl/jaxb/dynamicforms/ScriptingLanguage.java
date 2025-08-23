/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for scriptingLanguage.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;simpleType name="scriptingLanguage"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Groovy"/&gt;
 *     &lt;enumeration value="SpEL"/&gt;
 *     &lt;enumeration value="BeanShell"/&gt;
 *     &lt;enumeration value="JavaScript"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "scriptingLanguage", namespace = "http://www.hybris.com/cockpitng/component/dynamicForms")
@XmlEnum
public enum ScriptingLanguage
{
    @XmlEnumValue("Groovy")
    GROOVY("Groovy"), @XmlEnumValue("SpEL")
SP_EL("SpEL"), @XmlEnumValue("BeanShell")
BEAN_SHELL("BeanShell"), @XmlEnumValue("JavaScript")
JAVA_SCRIPT("JavaScript");
    private final String value;


    ScriptingLanguage(final String v)
    {
        value = v;
    }


    public String value()
    {
        return value;
    }


    public static ScriptingLanguage fromValue(final String v)
    {
        for(final ScriptingLanguage c : ScriptingLanguage.values())
        {
            if(c.value.equals(v))
            {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
