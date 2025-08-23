/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.model.impl;

import com.google.common.collect.Lists;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.expression.spel.support.ReflectiveMethodResolver;

public class SecureReflectiveMethodResolver extends ReflectiveMethodResolver
{
    private static final Logger LOG = LoggerFactory.getLogger(SecureReflectiveMethodResolver.class);
    private Collection<Class> deprecatedClasses;
    private Collection<String> deprecatedPackages;
    private Collection<String> allowedStaticClasses;


    @Override
    protected Method[] getMethods(final Class<?> type)
    {
        final List<Method> result = Lists.newArrayList();
        for(final Class deprecated : deprecatedClasses)
        {
            if(deprecated.isAssignableFrom(type))
            {
                LOG.warn("Rejecting class access: {}", type);
                return new Method[0];
            }
        }
        final Package typePackage = type.getPackage();
        final String currentPackage = typePackage == null ? StringUtils.EMPTY : typePackage.getName();
        for(final String deprecatedPackage : deprecatedPackages)
        {
            if(currentPackage.startsWith(deprecatedPackage))
            {
                final int currentLength = currentPackage.length();
                final int deprecatedLength = deprecatedPackage.length();
                if(currentLength > deprecatedLength && currentPackage.charAt(currentLength) == '.')
                {
                    LOG.warn("Rejecting package access: {}", currentPackage);
                    return new Method[0];
                }
                else if(currentLength == deprecatedLength)
                {
                    LOG.warn("Rejecting package access: {}", currentPackage);
                    return new Method[0];
                }
            }
        }
        final boolean allowStatic = isStaticAllowed(type);
        for(final Method method : super.getMethods(type))
        {
            if(!Modifier.isStatic(method.getModifiers()) || allowStatic || String.class.equals(method.getDeclaringClass()))
            {
                result.add(method);
            }
            else
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Rejecting method access: " + method.toGenericString());
                }
            }
        }
        return result.toArray(new Method[result.size()]);
    }


    protected boolean isStaticAllowed(final Class<?> type)
    {
        if(type != null && CollectionUtils.isNotEmpty(getAllowedStaticClasses()))
        {
            return getAllowedStaticClasses().contains(type.getName());
        }
        return false;
    }


    public Collection<Class> getDeprecatedClasses()
    {
        return Collections.unmodifiableCollection(deprecatedClasses);
    }


    @Required
    public void setDeprecatedClasses(final Collection<Class> deprecatedClasses)
    {
        this.deprecatedClasses = deprecatedClasses;
    }


    public Collection<String> getDeprecatedPackages()
    {
        return Collections.unmodifiableCollection(deprecatedPackages);
    }


    @Required
    public void setDeprecatedPackages(final Collection<String> deprecatedPackages)
    {
        this.deprecatedPackages = deprecatedPackages;
    }


    public Collection<String> getAllowedStaticClasses()
    {
        return allowedStaticClasses;
    }


    public void setAllowedStaticClasses(final Collection<String> allowedStaticClasses)
    {
        this.allowedStaticClasses = allowedStaticClasses;
    }
}
