/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for MergeMode.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;simpleType name="MergeMode"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="MERGE"/&gt;
 *     &lt;enumeration value="REPLACE"/&gt;
 *     &lt;enumeration value="REMOVE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "MergeMode", namespace = "http://www.hybris.com/cockpitng/config/common")
@XmlEnum
public enum MergeMode
{
    MERGE,
    REPLACE,
    REMOVE;


    public String value()
    {
        return name();
    }


    public static MergeMode fromValue(String v)
    {
        return valueOf(v.toUpperCase());
    }
}
