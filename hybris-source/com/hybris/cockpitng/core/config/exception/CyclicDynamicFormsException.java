/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.exception;

import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.DynamicAttribute;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;

/**
 * Exception which is thrown when {@link DynamicAttribute} in
 * {@link com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.DynamicForms} have cyclic references.
 */
public class CyclicDynamicFormsException extends RuntimeException
{
    private final transient List<DynamicAttribute> cycle;


    public CyclicDynamicFormsException(final String message, final List<DynamicAttribute> cycle)
    {
        super(buildMessage(message, cycle));
        this.cycle = cycle;
    }


    private static String buildMessage(final String message, final List<DynamicAttribute> cycle)
    {
        final StringBuilder messageWithCycle = new StringBuilder(message.concat("\n"));
        if(CollectionUtils.isNotEmpty(cycle))
        {
            cycle.forEach(attribute -> messageWithCycle.append(attribute.toString().concat("\n")));
        }
        return messageWithCycle.toString();
    }


    public List<DynamicAttribute> getCycle()
    {
        return cycle;
    }
}
