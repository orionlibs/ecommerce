/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java element interface generated in the
 * com.hybris.cockpitng.config.jaxb.dynamicforms package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java representation for XML content.
 * The Java representation of XML content can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory methods for each of these are provided in
 * this class.
 */
@XmlRegistry
public class ObjectFactory
{
    /**
     * Create an instance of {@link DynamicForms }
     */
    public DynamicForms createDynamicForms()
    {
        return new DynamicForms();
    }


    /**
     * Create an instance of {@link DynamicAttribute }
     */
    public DynamicAttribute createDynamicAttribute()
    {
        return new DynamicAttribute();
    }


    /**
     * Create an instance of {@link DynamicSection }
     */
    public DynamicSection createDynamicSection()
    {
        return new DynamicSection();
    }


    /**
     * Create an instance of {@link DynamicTab }
     */
    public DynamicTab createDynamicTab()
    {
        return new DynamicTab();
    }


    /**
     * Create an instance of {@link ScriptingConfig }
     */
    public ScriptingConfig createScriptingConfig()
    {
        return new ScriptingConfig();
    }
}
