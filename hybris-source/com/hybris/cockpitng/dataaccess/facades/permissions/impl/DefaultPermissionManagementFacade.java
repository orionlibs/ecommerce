/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.permissions.impl;

import com.hybris.cockpitng.dataaccess.facades.permissions.Permission;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionInfo;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionManagementFacade;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionManagementFacadeStrategy;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of facade for managing principal permissions.
 */
public class DefaultPermissionManagementFacade implements PermissionManagementFacade
{
    private PermissionManagementFacadeStrategyRegistry registry;


    @Override
    public Permission getTypePermission(final String principal, final String type, final String permissionName)
    {
        final PermissionManagementFacadeStrategy strategy = registry.getStrategy(type);
        if(strategy != null)
        {
            return strategy.getTypePermission(principal, type, permissionName);
        }
        return null;
    }


    @Override
    public PermissionInfo getTypePermissionInfo(final String principal, final String type)
    {
        final PermissionManagementFacadeStrategy strategy = registry.getStrategy(type);
        if(strategy != null)
        {
            return strategy.getTypePermissionInfo(principal, type);
        }
        return null;
    }


    @Override
    public PermissionInfo getPrincipalPermissionInfo(final String principal, final String type)
    {
        final PermissionManagementFacadeStrategy strategy = registry.getStrategy(type);
        if(strategy != null)
        {
            return strategy.getPrincipalPermissionInfo(principal, type);
        }
        return null;
    }


    @Override
    public Permission getFieldPermission(final String principal, final String type, final String field, final String permissionName)
    {
        final PermissionManagementFacadeStrategy strategy = registry.getStrategy(type);
        if(strategy != null)
        {
            return strategy.getFieldPermission(principal, type, field, permissionName);
        }
        return null;
    }


    @Override
    public PermissionInfo getFieldPermissionInfo(final String principal, final String type, final String field)
    {
        final PermissionManagementFacadeStrategy strategy = registry.getStrategy(type);
        if(strategy != null)
        {
            return strategy.getFieldPermissionInfo(principal, type, field);
        }
        return null;
    }


    @Override
    public Collection<PermissionInfo> getPrincipalsWithPermissionAssignment(final String type)
    {
        final PermissionManagementFacadeStrategy strategy = registry.getStrategy(type);
        if(strategy != null)
        {
            return strategy.getPrincipalsWithPermissionAssignment(type);
        }
        return null;
    }


    @Override
    public Collection<PermissionInfo> getTypePermissionInfosForPrincipal(final String principal)
    {
        final PermissionManagementFacadeStrategy strategy = registry.getStrategy(principal);
        if(strategy != null)
        {
            return strategy.getTypePermissionInfosForPrincipal(principal);
        }
        // TODO throw new TypeNotManagableException() checked Expetion
        return null;
    }


    @Override
    public void setPermission(final Permission permission)
    {
        final PermissionManagementFacadeStrategy strategy = registry.getStrategy(permission.getType());
        if(strategy != null)
        {
            strategy.setPermission(permission);
        }
    }


    @Override
    public PermissionInfo updatePermissionInfo(final Permission permission)
    {
        final PermissionManagementFacadeStrategy strategy = registry.getStrategy(permission.getType());
        if(strategy != null)
        {
            if(permission.getField() != null && !permission.getField().isEmpty())
            {
                return strategy.getFieldPermissionInfo(permission.getPrincipal(), permission.getType(), permission.getField());
            }
            else
            {
                return strategy.getTypePermissionInfo(permission.getPrincipal(), permission.getType());
            }
        }
        return null;
    }


    @Override
    public void deletePermission(final Permission permission)
    {
        final PermissionManagementFacadeStrategy strategy = registry.getStrategy(permission.getType());
        if(strategy != null)
        {
            strategy.deletePermission(permission);
        }
    }


    @Required
    public void setPermissionManagementFacadeStrategyRegistry(
                    final PermissionManagementFacadeStrategyRegistry permissionManagementFacadeStrategyRegistry)
    {
        this.registry = permissionManagementFacadeStrategyRegistry;
    }
}
