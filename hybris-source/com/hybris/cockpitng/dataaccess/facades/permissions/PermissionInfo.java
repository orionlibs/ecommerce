/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.permissions;

import java.util.List;

/**
 * Getting information of permissions
 */
public interface PermissionInfo
{
    /**
     * Returns a list of permissions
     *
     * @return List
     */
    List<Permission> getPermissions();


    /**
     * Returns permission by given name.
     *
     * @param permissionName
     *           - permission name
     * @return Permission
     */
    Permission getPermission(String permissionName);


    /**
     * Returns the permissionInfoType which could be type, user or field.
     *
     * @return String
     */
    String getLabel();


    void setLabel(String label);


    String getTypeCode();


    PermissionInfoType getPermissionInfoType();


    String getPrincipal();


    boolean isPersisted();


    enum PermissionInfoType
    {
        PRINCIPAL, FIELD, TYPE
    }
}
