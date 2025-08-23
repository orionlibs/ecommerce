/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.permissions;

/**
 * Strategy interface allows you to extend permission engine.
 */
public interface PermissionFacadeStrategy extends PermissionFacade
{
    /**
     * Returns true, if this strategy can provide permission information for the given type.
     */
    boolean canHandle(String type);
}
