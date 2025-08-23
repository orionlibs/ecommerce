/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.service.impl;

import com.hybris.cockpitng.service.ExceptionLinkIgnoreResolver;
import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link ExceptionLinkIgnoreResolver}, the resolver to decide if the notification message link
 * is needed to be ignored for the exception.
 */
public class DefaultExceptionLinkIgnoreResolver implements ExceptionLinkIgnoreResolver
{
    private List<Class<? extends Throwable>> exceptionClasses = new ArrayList<>();


    @Override
    public boolean isMatch(final Throwable exception)
    {
        if(exception == null)
        {
            return false;
        }
        if(exceptionClasses.stream().anyMatch(e -> e.isInstance(exception)))
        {
            return true;
        }
        return isMatch(exception.getCause());
    }


    public void setExceptionClasses(List<Class<? extends Throwable>> exceptionClasses)
    {
        this.exceptionClasses = exceptionClasses;
    }
}
