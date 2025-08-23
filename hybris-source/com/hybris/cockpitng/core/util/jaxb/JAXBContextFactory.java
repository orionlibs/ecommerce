/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * Creates JAXBContext for a JAXB class or classes.
 * The point is that the implementation of this interface caches the JAXBContext objects per Class or list of Classes.
 */
public interface JAXBContextFactory
{
    /**
     * Provides JAXBContext for the given class or class list.
     *
     * @param jaxbClasses classes to get JAXBContext for
     * @return JAXBContext for the given class
     */
    JAXBContext createContext(Class<?>... jaxbClasses) throws JAXBException;
}
