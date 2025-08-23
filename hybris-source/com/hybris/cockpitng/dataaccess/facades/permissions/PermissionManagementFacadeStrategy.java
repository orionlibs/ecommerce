/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.permissions;

public interface PermissionManagementFacadeStrategy extends PermissionManagementFacade
{
    /**
     * Returns true, if this strategy can provide permission information for the given type.
     */
    boolean canHandle(String type);
}
