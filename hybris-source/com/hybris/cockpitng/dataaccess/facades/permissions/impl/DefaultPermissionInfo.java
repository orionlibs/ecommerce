/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.permissions.impl;

import com.hybris.cockpitng.dataaccess.facades.permissions.Permission;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class DefaultPermissionInfo implements PermissionInfo
{
    private final transient Map<String, Permission> permissionMap = new HashMap<String, Permission>();
    private transient PermissionInfoType permissionInfoType;
    private transient String principal;
    private transient String type;
    private transient String field;
    private transient String label;
    private transient boolean persisted;


    public DefaultPermissionInfo(final PermissionInfoType permissionInfoType, final String principal, final String type,
                    final Map<String, Permission> permissionsMap)
    {
        this.permissionInfoType = permissionInfoType;
        this.principal = principal;
        this.type = type;
        this.persisted = true;
        if(permissionsMap != null)
        {
            this.permissionMap.putAll(permissionsMap);
        }
    }


    public DefaultPermissionInfo(final PermissionInfoType permissionInfoType, final String principal, final String type,
                    final String field, final Map<String, Permission> permissionsMap)
    {
        this(permissionInfoType, principal, type, permissionsMap);
        this.field = field;
    }


    @Override
    public List<Permission> getPermissions()
    {
        return new ArrayList<Permission>(permissionMap.values());
    }


    @Override
    public Permission getPermission(final String permissionName)
    {
        for(final Permission permission : getPermissions())
        {
            if(permissionName.equals(permission.getName()))
            {
                return permission;
            }
        }
        return null;
    }


    @Override
    public String getLabel()
    {
        if(label != null)
        {
            return label;
        }
        switch(permissionInfoType)
        {
            case PRINCIPAL:
                return principal;
            case FIELD:
                return field;
            case TYPE:
                return type;
            default:
                return StringUtils.EMPTY;
        }
    }


    @Override
    public void setLabel(final String label)
    {
        this.label = label;
    }


    @Override
    public PermissionInfoType getPermissionInfoType()
    {
        return permissionInfoType;
    }


    @Override
    public boolean isPersisted()
    {
        return persisted;
    }


    public void setPersisted(final boolean persisted)
    {
        this.persisted = persisted;
    }


    public void setPermissionInfoType(final PermissionInfoType permissionInfoType)
    {
        this.permissionInfoType = permissionInfoType;
    }


    public String getPrincipal()
    {
        return principal;
    }


    public void setPrincipal(final String principal)
    {
        this.principal = principal;
    }


    public String getTypeCode()
    {
        return type;
    }


    public void setType(final String type)
    {
        this.type = type;
    }
}
