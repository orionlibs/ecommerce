/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.jaxb.impl;

import com.hybris.cockpitng.core.util.jaxb.JAXBContextFactory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation. Caches the JAXBContext per Class.
 */
public class DefaultJAXBContextFactory implements JAXBContextFactory
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultJAXBContextFactory.class);
    private final ConcurrentMap<Set<Class<?>>, JAXBContext> contextCache = new ConcurrentHashMap<Set<Class<?>>, JAXBContext>();


    @Override
    public JAXBContext createContext(final Class<?>... jaxbClasses) throws JAXBException
    {
        if(jaxbClasses == null || jaxbClasses.length == 0)
        {
            throw new IllegalArgumentException("JAXB class list for context must not be null");
        }
        final Set<Class<?>> key = new HashSet<>(Arrays.asList(jaxbClasses));
        try
        {
            return contextCache.computeIfAbsent(key, k -> {
                try
                {
                    return JAXBContext.newInstance(jaxbClasses);
                }
                catch(final JAXBException e)
                {
                    throw new JAXBRuntimeException(e);
                }
            });
        }
        catch(final JAXBRuntimeException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Cannot create JAXB Context", e);
            }
            throw e.getTypedCause();
        }
    }


    private static class JAXBRuntimeException extends RuntimeException
    {
        public JAXBRuntimeException(final JAXBException cause)
        {
            super(cause);
        }


        public JAXBException getTypedCause()
        {
            return (JAXBException)getCause();
        }
    }
}
