/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.delete;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacadeOperationResult;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectAccessException;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteAction implements CockpitAction<Object, Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(DeleteAction.class);
    @Resource
    public ObjectFacade objectFacade;
    @Resource
    private PermissionFacade permissionFacade;
    @Resource
    private NotificationService notificationService;


    @Override
    public ActionResult<Object> perform(final ActionContext<Object> ctx)
    {
        ObjectFacadeOperationResult<Object> result;
        final List<Object> ctxObjects = getDataAsCollection(ctx);
        try
        {
            result = objectFacade.delete(ctxObjects);
        }
        catch(final RuntimeException ex)
        {
            result = addItemsToFailedObjects(ctxObjects, ex);
            LOG.debug("Cannot delete item", ex);
        }
        if(result == null)
        {
            return new ActionResult<>(ActionResult.ERROR, ctxObjects);
        }
        if(result.hasError())
        {
            showFailureNotification(ctx,
                            result.getFailedObjects().stream().collect(Collectors.toMap(e -> e, result::getErrorForObject)));
        }
        if(result.countSuccessfulObjects() > 0)
        {
            showSuccessNotification(ctx, ctxObjects);
        }
        return new ActionResult<>(result.countSuccessfulObjects() < 1 ? ActionResult.ERROR : ActionResult.SUCCESS, ctxObjects);
    }


    private List<Object> getDataAsCollection(final ActionContext<Object> ctx)
    {
        final List<Object> ctxObjects = new ArrayList<>();
        if(ctx.getData() instanceof Collection)
        {
            ctxObjects.addAll((Collection<Object>)ctx.getData());
        }
        else
        {
            ctxObjects.add(ctx.getData());
        }
        return ctxObjects;
    }


    private ObjectFacadeOperationResult<Object> addItemsToFailedObjects(final List<Object> ctxObjects, final RuntimeException ex)
    {
        final ObjectFacadeOperationResult<Object> result = new ObjectFacadeOperationResult<>();
        ctxObjects.forEach(obj -> result.addFailedObject(obj, new ObjectAccessException(ex.getMessage(), ex)));
        return result;
    }


    /**
     * @deprecated since 6.7, use {@link NotificationService#getWidgetNotificationSource(ActionContext)} instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected String getNotificationSource(final ActionContext<Object> ctx)
    {
        return getNotificationService().getWidgetNotificationSource(ctx);
    }


    protected void showSuccessNotification(final ActionContext<Object> ctx, final List<Object> deletedObjects)
    {
        final String notificationSource = getNotificationSource(ctx);
        getNotificationService().notifyUser(notificationSource, NotificationEventTypes.EVENT_TYPE_OBJECT_REMOVAL,
                        NotificationEvent.Level.SUCCESS, deletedObjects);
    }


    protected void showFailureNotification(final ActionContext<Object> ctx, final Map<Object, ObjectAccessException> problems)
    {
        final String notificationSource = getNotificationSource(ctx);
        getNotificationService().notifyUser(notificationSource, NotificationEventTypes.EVENT_TYPE_OBJECT_REMOVAL,
                        NotificationEvent.Level.FAILURE, problems);
    }


    @Override
    public boolean canPerform(final ActionContext<Object> ctx)
    {
        if(ctx.getData() == null)
        {
            return false;
        }
        if(ctx.getData() instanceof Collection)
        {
            final Collection selectedItems = (Collection<?>)ctx.getData();
            final ObjectFacadeOperationResult reloadResult = getObjectFacade().reload(selectedItems);
            final Collection refreshedItems = reloadResult.getSuccessfulObjects();
            return isCollectionDeletable(refreshedItems);
        }
        return hasPermissionAndObjectIsPersisted(ctx.getData());
    }


    protected boolean isCollectionDeletable(final Collection<?> collection)
    {
        return CollectionUtils.isNotEmpty(collection) && collection.stream().allMatch(this::hasPermissionAndObjectIsPersisted);
    }


    protected boolean hasPermissionAndObjectIsPersisted(final Object object)
    {
        return permissionFacade.canRemoveInstance(object) && !objectFacade.isNew(object);
    }


    @Override
    public boolean needsConfirmation(final ActionContext<Object> ctx)
    {
        return true;
    }


    @Override
    public String getConfirmationMessage(final ActionContext<Object> ctx)
    {
        return ctx.getLabel("delete.confirm");
    }


    protected ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    public void setObjectFacade(final ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    protected PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    protected NotificationService getNotificationService()
    {
        return notificationService;
    }


    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }
}
