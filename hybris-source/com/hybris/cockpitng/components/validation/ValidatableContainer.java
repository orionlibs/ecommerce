/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.validation;

import com.hybris.cockpitng.core.model.ValueObserver;
import com.hybris.cockpitng.validation.model.ValidationResult;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;

/**
 * Container with fields that may be validated
 */
public interface ValidatableContainer
{
    /**
     * Container component
     *
     * @return component containing fields
     */
    Component getContainer();


    /**
     * Gets a full object that was validated for specified violation path
     *
     * @param path
     *           path of violation
     * @return object that was validated or <code>null</code> if no object matches provided path
     */
    Object getCurrentObject(String path);


    /**
     * Alters provided path (that comes from violation) so that is relative to current object
     *
     * @param path
     *           violation path
     * @return path relative to validated object, empty string if path points directly to validated object or
     *         <code>null</code> if path does not match any validated objects
     */
    String getCurrentObjectPath(String path);


    /**
     * Checks whether provided path should be treated as root path.
     * <P>
     * Any validation path that matches containers restrictions, yet does not specify any particular attribute/qualifier
     * (i.e. points exactly to one of validated objects) should be considered as root path. In general, if a path is
     * validated by container or is a parent of validated path, but does not point any particular attribute, then it is a
     * root path. Empty string should always be considered as root path, cause they are always a kind of parent of
     * validated path.
     *
     * @return <code>true</code> if path is validated by container (or is a parent path of such) and does not point any
     *         attribute
     */
    boolean isRootPath(String path);


    /**
     * Gets an object that is able to transfer focus between fields on basis of their paths.
     *
     * @return focus transfer
     */
    ValidationFocusTransferHandler getFocusTransfer();


    /**
     * Registers a new observer for validation results of container
     *
     * @param observer
     *           observer to be notified
     */
    void addValidationObserver(ValueObserver observer);


    /**
     * Registers a new observer for validation results of container for specified path and all its children
     *
     * @param key
     *           validation path to be observed
     * @param observer
     *           observer to be notified
     */
    void addValidationObserver(String key, ValueObserver observer);


    /**
     * Unregisters a new observer for validation results of container
     *
     * @param observer
     *           observer not to be notified
     */
    void removeValidationObserver(ValueObserver observer);


    /**
     * Gets validation results of container related to provided path (relative to container).
     * <P>
     * Returned container may be editable and notify listeners about changes. If a returned container is a wrapper of
     * other container, then any changes made to the wrapper should be propagated to wrapped one and all listeners should
     * be notified. It is not sure that changes made to wrapped one will be propagated to the wrapper. Also notifications
     * of wrapped container may not be transferred to listeners of a wrapper.
     *
     * @return validation results found or {@link ValidationResult#EMPTY} if no violations found for specified path
     */
    ValidationResult getCurrentValidationResult(final String path);


    /**
     * Gets current validation results of container (relative to container).
     * <P>
     * Returned container may be editable and notify listeners about changes. If a returned container is a wrapper of
     * other container, then any changes made to the wrapper should be propagated to wrapped one and all listeners should
     * be notified. It is not sure that changes made to wrapped one will be propagated to the wrapper. Also notifications
     * of wrapped container may not be transferred to listeners of a wrapper.
     *
     * @return validation results found or {@link ValidationResult#EMPTY} if no violations found for specified path
     */
    default ValidationResult getCurrentValidationResult()
    {
        return getCurrentValidationResult(StringUtils.EMPTY);
    }


    /**
     * Checks whether this validatable container is able to process validation result change for given path.
     *
     * @return true if this validatable container can process validation result change
     */
    boolean reactOnValidationChange(final String path);


    /**
     * Checks whether this validatable container is able to process validation result change for root path.
     *
     * @return true if this validatable container can process validation result change
     */
    default boolean reactOnValidationChange()
    {
        return reactOnValidationChange(StringUtils.EMPTY);
    }


    /**
     * Prevents validation container to not delegate validation change further.
     *
     * @param preventBroadcastValidationChange
     */
    void setPreventBroadcastValidationChange(final boolean preventBroadcastValidationChange);
}
