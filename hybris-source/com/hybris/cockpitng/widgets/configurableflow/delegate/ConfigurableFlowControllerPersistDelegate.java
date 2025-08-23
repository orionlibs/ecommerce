/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow.delegate;

import com.hybris.cockpitng.config.jaxb.wizard.AbstractActionType;
import com.hybris.cockpitng.config.jaxb.wizard.CancelType;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;

/**
 * Provides methods for performing persistence and notifying operations on ConfigurableFlowController.
 * Implementation of this interface allows persisting/revoking properties and manages success/failure notifications.
 */
public interface ConfigurableFlowControllerPersistDelegate extends ConfigurableFlowControllerDelegate
{
    /**
     * Persists properties of the action
     * @param actionType action type
     * @return true if operation succeeded, otherwise false
     */
    boolean persistProperties(final AbstractActionType actionType);


    /**
     * Removes properties which where saved during the operations on the Configurable Flow
     * @param cancel cancel type
     */
    void revertProperties(final CancelType cancel);


    /**
     * Persist property from values of the controller
     * @param property property to save
     * @return saved object
     * @throws ObjectSavingException when saving fails
     */
    Object persistWidgetProperty(final String property) throws ObjectSavingException;


    /**
     *	Shows objects creation success notification
     * @param persistedObject object which was persisted
     */
    void showSuccessNotification(final Object persistedObject);


    /**
     * Shows object creation failure notification
     * @param failureException object saving exception
     */
    void showFailureNotification(final ObjectSavingException failureException);


    /**
     * Shows object creation failure notification
     * @param failureException throwable
     */
    void showFailureNotification(final Throwable failureException);
}
