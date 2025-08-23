/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.permissions.impl;

import com.google.common.collect.Sets;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacadeStrategy;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

/**
 * Implementation that returns true for all method calls.
 */
public class DefaultPermissionFacade implements PermissionFacade
{
    private PermissionFacadeStrategyRegistry registry;
    private boolean defaultAccess = true;


    @Override
    public boolean canReadType(final String typeCode)
    {
        final PermissionFacadeStrategy strategy = registry.getStrategy(typeCode);
        if(strategy != null)
        {
            return strategy.canReadType(typeCode);
        }
        return defaultAccess;
    }


    @Override
    public boolean canReadProperty(final String typeCode, final String property)
    {
        final PermissionFacadeStrategy strategy = registry.getStrategy(typeCode);
        if(strategy != null)
        {
            return strategy.canReadProperty(typeCode, property);
        }
        return defaultAccess;
    }


    @Override
    public boolean canChangeType(final String typeCode)
    {
        final PermissionFacadeStrategy strategy = registry.getStrategy(typeCode);
        if(strategy != null)
        {
            return strategy.canChangeType(typeCode);
        }
        return defaultAccess;
    }


    @Override
    public boolean canChangeProperty(final String typeCode, final String property)
    {
        final PermissionFacadeStrategy strategy = registry.getStrategy(typeCode);
        if(strategy != null)
        {
            return strategy.canChangeProperty(typeCode, property);
        }
        return defaultAccess;
    }


    @Override
    public boolean canCreateTypeInstance(final String typeCode)
    {
        final PermissionFacadeStrategy strategy = registry.getStrategy(typeCode);
        if(strategy != null)
        {
            return strategy.canCreateTypeInstance(typeCode);
        }
        return defaultAccess;
    }


    @Override
    public boolean canRemoveTypeInstance(final String typeCode)
    {
        final PermissionFacadeStrategy strategy = registry.getStrategy(typeCode);
        if(strategy != null)
        {
            return strategy.canRemoveTypeInstance(typeCode);
        }
        return defaultAccess;
    }


    @Override
    public boolean canChangeTypePermission(final String typeCode)
    {
        final PermissionFacadeStrategy strategy = registry.getStrategy(typeCode);
        if(strategy != null)
        {
            return strategy.canChangeTypePermission(typeCode);
        }
        return defaultAccess;
    }


    @Override
    public boolean canChangePropertyPermission(final String typeCode, final String property)
    {
        final PermissionFacadeStrategy strategy = registry.getStrategy(typeCode);
        if(strategy != null)
        {
            return strategy.canChangePropertyPermission(typeCode, property);
        }
        return defaultAccess;
    }


    @Override
    public Set<Locale> getReadableLocalesForInstance(final Object instance)
    {
        final PermissionFacadeStrategy strategy = registry.getStrategyByInstance(instance);
        if(strategy != null)
        {
            return strategy.getReadableLocalesForInstance(instance);
        }
        return null;
    }


    @Override
    public Set<Locale> getWritableLocalesForInstance(final Object instance)
    {
        final PermissionFacadeStrategy strategy = registry.getStrategyByInstance(instance);
        if(strategy != null)
        {
            return strategy.getWritableLocalesForInstance(instance);
        }
        return null;
    }


    @Override
    public boolean canReadInstanceProperty(final Object instance, final String property)
    {
        final PermissionFacadeStrategy strategy = registry.getStrategyByInstance(instance);
        if(strategy != null)
        {
            return strategy.canReadInstanceProperty(instance, property);
        }
        return defaultAccess;
    }


    @Override
    public boolean canChangeInstanceProperty(final Object instance, final String property)
    {
        final PermissionFacadeStrategy strategy = registry.getStrategyByInstance(instance);
        if(strategy != null)
        {
            return strategy.canChangeInstanceProperty(instance, property);
        }
        return defaultAccess;
    }


    @Override
    public boolean canChangeInstancesProperty(final Collection<Object> instances, final String qualifier)
    {
        final PermissionFacadeStrategy strategy = registry.getStrategyByInstances(instances);
        if(strategy != null)
        {
            return strategy.canChangeInstancesProperty(instances, qualifier);
        }
        return defaultAccess;
    }


    @Override
    public boolean canRemoveInstance(final Object instance)
    {
        final PermissionFacadeStrategy strategy = registry.getStrategyByInstance(instance);
        if(strategy != null)
        {
            return strategy.canRemoveInstance(instance);
        }
        return defaultAccess;
    }


    @Override
    public boolean canReadInstance(final Object instance)
    {
        final PermissionFacadeStrategy strategy = registry.getStrategyByInstance(instance);
        if(strategy != null)
        {
            return strategy.canReadInstance(instance);
        }
        return defaultAccess;
    }


    @Override
    public boolean canChangeInstance(final Object instance)
    {
        final PermissionFacadeStrategy strategy = registry.getStrategyByInstance(instance);
        if(strategy != null)
        {
            return strategy.canChangeInstance(instance);
        }
        return defaultAccess;
    }


    @Override
    public boolean canChangeInstances(final Collection<Object> instances)
    {
        final PermissionFacadeStrategy strategy = registry.getStrategyByInstances(instances);
        if(strategy != null)
        {
            return strategy.canChangeInstances(instances);
        }
        return defaultAccess;
    }


    @Override
    public Set<Locale> getAllReadableLocalesForCurrentUser()
    {
        final Set<Locale> ret = Sets.<Locale>newHashSet();
        final PermissionFacadeStrategy strategy = registry.getDefaultStrategy();
        if(strategy != null)
        {
            ret.addAll(strategy.getAllReadableLocalesForCurrentUser());
        }
        return ret;
    }


    @Override
    public Set<Locale> getAllWritableLocalesForCurrentUser()
    {
        final Set<Locale> ret = Sets.<Locale>newHashSet();
        final PermissionFacadeStrategy strategy = registry.getDefaultStrategy();
        if(strategy != null)
        {
            ret.addAll(strategy.getAllWritableLocalesForCurrentUser());
        }
        return ret;
    }


    @Override
    public Set<Locale> getEnabledReadableLocalesForCurrentUser()
    {
        final PermissionFacadeStrategy strategy = registry.getDefaultStrategy();
        if(strategy != null)
        {
            return Set.copyOf(strategy.getEnabledReadableLocalesForCurrentUser());
        }
        return Set.of();
    }


    @Override
    public Set<Locale> getEnabledWritableLocalesForCurrentUser()
    {
        final PermissionFacadeStrategy strategy = registry.getDefaultStrategy();
        if(strategy != null)
        {
            return Set.copyOf(strategy.getEnabledWritableLocalesForCurrentUser());
        }
        return Set.of();
    }


    @Required
    public void setPermissionFacadeStrategyRegistry(final PermissionFacadeStrategyRegistry permissionFacadeStrategyRegistry)
    {
        this.registry = permissionFacadeStrategyRegistry;
    }


    public void setDefaultAccess(final boolean defaultAccess)
    {
        this.defaultAccess = defaultAccess;
    }
}
