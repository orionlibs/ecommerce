/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.refineby.jaxb;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.hybris.cockpitng.config.refineby.jaxb package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory
{
    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.hybris.cockpitng.config.refineby.jaxb
     *
     */
    public ObjectFactory()
    {
        // default constructor
    }


    /**
     * Create an instance of {@link FacetConfig }
     *
     */
    public FacetConfig createFacetConfig()
    {
        return new FacetConfig();
    }


    /**
     * Create an instance of {@link Facets }
     *
     */
    public Facets createFacets()
    {
        return new Facets();
    }


    /**
     * Create an instance of {@link Blacklist }
     *
     */
    public Blacklist createBlacklist()
    {
        return new Blacklist();
    }


    /**
     * Create an instance of {@link Parameter }
     *
     */
    public Parameter createParameter()
    {
        return new Parameter();
    }


    /**
     * Create an instance of {@link Facet }
     *
     */
    public Facet createFacet()
    {
        return new Facet();
    }
}
