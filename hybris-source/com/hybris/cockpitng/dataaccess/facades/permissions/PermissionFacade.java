/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dataaccess.facades.permissions;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Provides methods for checking CRUD permissions on type and property level.
 */
public interface PermissionFacade
{
    /**
     * Returns true if the current user is granted read permission to the given type. Returns false otherwise.
     *
     * @param typeCode
     *           type
     * @return true if user has read permission, false otherwise
     */
    boolean canReadType(String typeCode);


    /**
     * Returns true if the current user is granted change permission to the given type. Returns false otherwise.
     *
     * @param typeCode
     *           type
     * @return true if user has change permission, false otherwise
     */
    boolean canChangeType(String typeCode);


    /**
     * Returns true if the current user is allowed to read the specified property at the given instance. This usually
     * includes a type check, see {@link #canChangeProperty(String, String)}.
     *
     * @param instance
     *           object which property is read
     * @param property
     *           property of object
     * @return true if user has read permission, false otherwise
     */
    boolean canReadInstanceProperty(Object instance, String property);


    /**
     * Returns true if the current user is granted read permission to the given property. Returns false otherwise. You
     * should always prefer using {@link #canReadInstanceProperty(Object, String)} if there is an instance available.
     *
     * @param typeCode
     *           type of object which property is read
     * @param property
     *           property of object
     * @return true if user has read permission, false otherwise
     */
    boolean canReadProperty(String typeCode, String property);


    /**
     * Returns true if the current user is allowed to change the specified property at the given instance. This usually
     * includes a type check, see {@link #canChangeProperty(String, String)}.
     *
     * @param instance
     *           object which property is change
     * @param property
     *           property of object
     * @return true if user has change permission, false otherwise
     */
    boolean canChangeInstanceProperty(Object instance, String property);


    /**
     * Returns true if the current user is granted change permission to the given property. Returns false otherwise. You
     * should always prefer using {@link #canChangeInstanceProperty(Object, String)} if there is an instance available.
     *
     * @param typeCode
     *           type of object which property is change
     * @param property
     *           property of object
     * @return true if user has change permission, false otherwise
     */
    boolean canChangeProperty(String typeCode, String property);


    /**
     * Returns true if the current user is granted create permission to instances of the given type. Returns false
     * otherwise.
     *
     * @param typeCode
     *           type
     * @return true if user has create permission, false otherwise
     */
    boolean canCreateTypeInstance(String typeCode);


    /**
     * Returns true if the current user is allowed to delete this instance. This usually includes a type check, see
     * {@link #canRemoveTypeInstance(String)}.
     *
     * @param instance
     *           object to delete
     * @return true if user has delete permission, false otherwise
     */
    boolean canRemoveInstance(Object instance);


    /**
     * Returns true if the current user is allowed to read this instance. Returns false otherwise.
     *
     * @param instance
     *           object to read
     * @return true if user has read permission, false otherwise
     */
    boolean canReadInstance(Object instance);


    /**
     * Returns true if the current user is allowed to change this instance. Returns false otherwise.
     *
     * @param instance
     *           object to change
     * @return true if user has change permission, false otherwise
     */
    boolean canChangeInstance(Object instance);


    /**
     * Returns true if the current user is allowed to change all instances. Returns false otherwise.
     *
     * @param instances
     *           objects to change
     * @return true if user has change permission, false otherwise
     */
    default boolean canChangeInstances(final Collection<Object> instances)
    {
        return !CollectionUtils.emptyIfNull(instances).stream().distinct().anyMatch(this::canChangeInstance);
    }


    /**
     * Returns true if the current user is granted change permission to the given property. Returns false otherwise.
     *
     * @param instances
     *           objects to change
     * @param property
     *           property of objects
     * @return true if user has change permission, false otherwise
     */
    default boolean canChangeInstancesProperty(final Collection<Object> instances, final String property)
    {
        return !CollectionUtils.emptyIfNull(instances).stream().distinct()
                        .anyMatch(instance -> !canChangeInstanceProperty(instance, property));
    }


    /**
     * Returns true if the current user is granted delete permission to an instance of the given type. Returns false
     * otherwise. You should always prefer using {@link #canRemoveInstance(Object)} if there is an instance available.
     *
     * @param typeCode
     *           type of object
     * @return true if user has delete permission, false otherwise
     */
    boolean canRemoveTypeInstance(String typeCode);


    /**
     * Returns true if the current user is granted "change permission" permission to the given type. Returns false
     * otherwise.
     *
     * @param typeCode
     *           type of object
     * @return true if user has change permission, false otherwise
     */
    boolean canChangeTypePermission(String typeCode);


    /**
     * Returns true if the current user is granted "change permission" permission to the given property. Returns false
     * otherwise.
     *
     * @param typeCode
     *           type of object
     * @param property
     *           property to change
     * @return true if user has change permission, false otherwise
     */
    boolean canChangePropertyPermission(String typeCode, String property);


    /**
     * Returns a set of locales for which the current user has permission to read localized property values of given
     * instance. Returns null, if no restrictions are set.
     *
     * @param instance
     *           object to read localized property
     * @return set o locales on which user has read permission
     */
    Set<Locale> getReadableLocalesForInstance(Object instance);


    /**
     * Returns a set of locales for which the current user has permission to write localized property values of given
     * instance. Returns null, if no restrictions are set.
     *
     * @param instance
     *           object to write localized property
     * @return set o locales on which user has write permission
     */
    Set<Locale> getWritableLocalesForInstance(Object instance);


    /**
     * Returns a set of locales for which the current user has permission to read localized property values. Returns
     * null, if no restrictions are set.
     *
     * @return set o locales on which user has read permission
     */
    Set<Locale> getAllReadableLocalesForCurrentUser();


    /**
     * Returns a set of locales for which the current user has permission to write localized property values of given
     * instance. Returns null, if no restrictions are set.
     *
     * @return set o locales on which user has write permission
     */
    Set<Locale> getAllWritableLocalesForCurrentUser();


    /**
     * Returns a set of enabled locales for which the current user has permission to read localized property values.
     * Returns empty set, if no restrictions are set.
     *
     * @return set o enabled locales on which user has read permission
     */
    default Set<Locale> getEnabledReadableLocalesForCurrentUser()
    {
        return Set.of();
    }


    /**
     * Returns a set of enabled locales for which the current user has permission to write localized property values of
     * given instance. Returns empty set, if no restrictions are set.
     *
     * @return set o locales on which user has write permission
     */
    default Set<Locale> getEnabledWritableLocalesForCurrentUser()
    {
        return Set.of();
    }
}
