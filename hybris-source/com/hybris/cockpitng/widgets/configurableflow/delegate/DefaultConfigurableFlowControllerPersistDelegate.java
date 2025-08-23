/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow.delegate;

import com.hybris.cockpitng.config.jaxb.wizard.AbstractActionType;
import com.hybris.cockpitng.config.jaxb.wizard.CancelType;
import com.hybris.cockpitng.config.jaxb.wizard.InitializeType;
import com.hybris.cockpitng.config.jaxb.wizard.SaveType;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacadeOperationResult;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowController;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultConfigurableFlowControllerPersistDelegate implements ConfigurableFlowControllerPersistDelegate
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultConfigurableFlowControllerPersistDelegate.class);
    private ConfigurableFlowController controller;
    private NotificationService notificationService;
    private ObjectFacade objectFacade;


    @Override
    public boolean persistProperties(final AbstractActionType actionType)
    {
        final Set<Object> objectsToPersist = getPersistableProperties(actionType).stream()
                        .map(property -> getController().getValue(property, Object.class)).collect(Collectors.toSet());
        final ObjectFacadeOperationResult<Object> savingResult = getObjectFacade().save(objectsToPersist);
        savingResult.getSuccessfulObjects().forEach(this::showSuccessNotification);
        savingResult.getFailedObjects().stream().map(savingResult::getErrorForObject).forEach(this::showFailureNotification);
        return savingResult.getFailedObjects().isEmpty();
    }


    @InextensibleMethod
    private Set<String> getPersistableProperties(final AbstractActionType action)
    {
        final Set<String> persistableProperties = new HashSet<>();
        if(action == null)
        {
            return persistableProperties;
        }
        final List<String> nonPersistable = getController().getNonPersistablePropertiesList();
        if(action.getSave() != null)
        {
            for(final SaveType save : action.getSave())
            {
                final String property = save.getProperty();
                if(property != null && !nonPersistable.contains(property))
                {
                    persistableProperties.add(property);
                }
            }
        }
        if(action.getSaveAll() != null)
        {
            for(final InitializeType init : getController().getInitializeList())
            {
                final String property = init.getProperty();
                if(property != null && !nonPersistable.contains(property))
                {
                    persistableProperties.add(property);
                }
            }
        }
        return persistableProperties;
    }


    @Override
    public void revertProperties(final CancelType cancel)
    {
        try
        {
            final Collection<Object> toRevertRAW = cancel.getRevert().stream()
                            .map(rev -> getController().getValue(rev.getProperty(), Object.class)).collect(Collectors.toList());
            final Collection<Object> toRevertExpanded = new LinkedList<>();
            for(final Object elem : toRevertRAW)
            {
                if(elem instanceof Collection)
                {
                    toRevertExpanded.addAll((Collection<?>)elem);
                }
                else
                {
                    toRevertExpanded.add(elem);
                }
            }
            final List<Object> objectsToRevert = toRevertExpanded.stream()//
                            .filter(entity -> Objects.nonNull(entity) && !getObjectFacade().isNew(entity)) //
                            .collect(Collectors.toList());
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Reverting entities on wizard cancellation: "
                                + objectsToRevert.stream().map(Object::toString).collect(Collectors.joining(", ")));
            }
            getObjectFacade().delete(objectsToRevert);
        }
        catch(final RuntimeException exc)
        {
            showFailureNotification(exc);
        }
    }


    @Override
    public Object persistWidgetProperty(final String property) throws ObjectSavingException
    {
        final Object propertyValue = getController().getValue(property, Object.class);
        return getObjectFacade().save(propertyValue);
    }


    @Override
    public void showSuccessNotification(final Object persistedObject)
    {
        if(persistedObject != null)
        {
            getNotificationService().notifyUser(
                            getNotificationService().getWidgetNotificationSource(getController().getWidgetInstanceManager()),
                            NotificationEventTypes.EVENT_TYPE_OBJECT_CREATION, NotificationEvent.Level.SUCCESS, persistedObject);
        }
    }


    @Override
    public void showFailureNotification(final ObjectSavingException failureException)
    {
        showFailureNotification((Throwable)failureException);
    }


    @Override
    public void showFailureNotification(final Throwable failureException)
    {
        Validate.notNull("The exception may not be null", failureException);
        LOG.info(failureException.getMessage(), failureException);
        getNotificationService().notifyUser(
                        getNotificationService().getWidgetNotificationSource(getController().getWidgetInstanceManager()),
                        NotificationEventTypes.EVENT_TYPE_OBJECT_CREATION, NotificationEvent.Level.FAILURE, failureException);
    }


    protected ConfigurableFlowController getController()
    {
        return controller;
    }


    @Override
    public void setController(final ConfigurableFlowController controller)
    {
        this.controller = controller;
    }


    protected NotificationService getNotificationService()
    {
        return notificationService;
    }


    @Required
    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    protected ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    @Required
    public void setObjectFacade(final ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }
}
