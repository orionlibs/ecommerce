/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.permissions;

import java.util.Collection;

/**
 * Allows managing principal permissions.
 */
public interface PermissionManagementFacade
{
    /**
     * Returns permission by given name for given type and principal.
     *
     * @param principal - principal for which permission is fetched.
     * @param typeCode - type for which permission is fetched.
     * @param permissionName - permission name
     * @return permission for given inputs
     */
    Permission getTypePermission(String principal, String typeCode, String permissionName);


    /**
     * Returns all permissions for the given type
     *
     * @param principal - principal for which permissions are fetched.
     * @param typeCode - type for which permissions are fetched.
     * @return permission info for the given type.
     */
    PermissionInfo getTypePermissionInfo(String principal, String typeCode);


    /**
     * Returns all permissions for the given principal
     *
     * @param principal - principal for which permissions are fetched.
     * @param typeCode - type for which permissions are fetched.
     * @return permission info for the given principal.
     */
    PermissionInfo getPrincipalPermissionInfo(String principal, String typeCode);


    /**
     * Returns a Permission object for the given permissionName, type's field and principal.
     *
     * @param principal - principal for which permission is fetched.
     * @param typeCode - type for which permission is fetched.
     * @param permissionName - permission name
     * @param field - field for which permission is fetched.
     * @return permission for the given field.
     */
    Permission getFieldPermission(String principal, String typeCode, String field, String permissionName);


    /**
     * Returns a PermissionInfo object representing the permission values of a field on a type for a given principal.
     *
     * @param principal - principal for which permission is fetched.
     * @param typeCode - type for which permission is fetched.
     * @param field - field or attribute for which permission is fetched.
     * @return permission info for the given field.
     */
    PermissionInfo getFieldPermissionInfo(String principal, String typeCode, String field);


    /**
     * Returns permission infos related to all principals who have any permission assignment for the given type.
     *
     * @param typeCode - principals with permission assignments to this type will be fetched.
     * @return Collection of PermissionInfos
     */
    Collection<PermissionInfo> getPrincipalsWithPermissionAssignment(String typeCode);


    /**
     * Returns all PermissionInfos related to all types for which a given principal has any permission assignments.
     *
     * @param principal - principal for which permission is fetched.
     * @return Collection of PermissionInfos.
     */
    Collection<PermissionInfo> getTypePermissionInfosForPrincipal(String principal);


    /**
     * Store new permission assignment.
     *
     * @param permission to set.
     */
    void setPermission(Permission permission);


    /**
     * Delete permission assignment.
     *
     * @param permission to delete.
     */
    void deletePermission(Permission permission);


    /**
     * Reload of the PermissionInfo.
     *
     * @param permission to update
     * @return updated permission info.
     */
    PermissionInfo updatePermissionInfo(Permission permission);
}
