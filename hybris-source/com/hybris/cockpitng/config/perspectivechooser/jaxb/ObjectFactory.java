/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.perspectivechooser.jaxb;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.hybris.cockpitng.config.perspectivechooser.jaxb package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java representation for XML content.
 * The Java representation of XML content can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory methods for each of these are provided in
 * this class.
 *
 */
@XmlRegistry
public class ObjectFactory
{
    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package:
     * com.hybris.cockpitng.config.perspectivechooser.jaxb
     *
     */
    public ObjectFactory()
    {
        // default constructor
    }


    /**
     * Create an instance of {@link PerspectiveChooser }
     *
     */
    public PerspectiveChooser createPerspectiveChooser()
    {
        return new PerspectiveChooser();
    }


    /**
     * Create an instance of {@link DefaultPerspective }
     *
     */
    public DefaultPerspective createDefaultPerspective()
    {
        return new DefaultPerspective();
    }


    /**
     * Create an instance of {@link Authority }
     *
     */
    public Authority createAuthority()
    {
        return new Authority();
    }


    /**
     * Create an instance of {@link Format }
     *
     */
    public Format createFormat()
    {
        return new Format();
    }


    /**
     * Create an instance of {@link Perspective }
     *
     */
    public Perspective createPerspective()
    {
        return new Perspective();
    }
}
