/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util.impl.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for DependencyProtocol.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;simpleType name="DependencyProtocol"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="HTTP"/&gt;
 *     &lt;enumeration value="RESOURCE"/&gt;
 *     &lt;enumeration value="CLASSPATH"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "DependencyProtocol")
@XmlEnum
public enum DependencyProtocol
{
    HTTP,
    RESOURCE,
    CLASSPATH;


    public String value()
    {
        return name();
    }


    public static DependencyProtocol fromValue(String v)
    {
        return valueOf(v);
    }
}
