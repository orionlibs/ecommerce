/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.permissions.custom;

/**
 * Interface for providing custom strategies for instance permissions. Used by DefaultPlatformPermissionFacadeStrategy
 * and possible to use by any other implementation of PermissionFacadeStrategy. Should be used carefully while too many
 * implementations may be harmful to performance.
 *
 * @see com.hybris.backoffice.cockpitng.dataaccess.facades.permissions.DefaultPlatformPermissionFacadeStrategy
 * @see com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacadeStrategy
 * @param <T>
 *           desired type object to handle. Implementations of
 *           InstancePermissionAdvisor#isApplicableTo(java.lang.Object) should return true only if type T is assignable
 *           from the instance's type.
 */
public interface InstancePermissionAdvisor<T>
{
    /**
     * @param instance
     *           instance to be checked
     * @return true if the instance can be modified
     */
    boolean canModify(T instance);


    /**
     * @param instance
     *           instance to be checked
     * @return true if the instance can be deleted
     */
    boolean canDelete(T instance);


    /**
     * @param instance
     *           instance to be checked
     * @return true if the instance can be checked by the strategy
     */
    boolean isApplicableTo(Object instance);
}
