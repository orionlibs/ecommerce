/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.create;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.common.model.ObjectWithComponentContext;
import com.hybris.cockpitng.core.context.CockpitContext;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectCreationException;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.search.FieldSearchFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.search.data.SimpleSearchQueryData;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Resource;
import javax.naming.NoPermissionException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Object, Object>
{
    public static final String SOCKET_OUTPUT_CREATE_CONTEXT = "createContext";
    public static final String SOCKET_OUTPUT_CREATED_OBJECT = "createdObject";
    public static final String SOCKET_INPUT_CREATED_OBJECT = "createdObject";
    public static final String SETTING_INITIALIZE_ENTITY = "initializeEntity";
    public static final String SETTING_CONTEXT_COMPONENT_NAME = "contextComponentName";
    /**
     * @deprecated since 6.6
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public static final String VALUE_SETTING_TARGET_COMPONENT_CONFIGURABLE_FLOW = "configurableFlow";
    /**
     * @deprecated since 6.6
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public static final String VALUE_SETTING_TARGET_COMPONENT_EDITOR_AREA = "editorArea";
    public static final String PARAM_ACTION_CONTEXT = "actionContext";
    private static final Logger LOG = LoggerFactory.getLogger(CreateAction.class);
    @Resource
    private PermissionFacade permissionFacade;
    @Resource
    private TypeFacade typeFacade;
    @Resource
    private ObjectFacade objectFacade;
    @Resource
    private FieldSearchFacade fieldSearchFacade;
    @Resource
    private NotificationService notificationService;


    @Override
    public ActionResult<Object> perform(final ActionContext<Object> ctx)
    {
        final String typeCode = findTypeCode(ctx);
        try
        {
            if(StringUtils.isNotBlank(typeCode))
            {
                if(canCreateInstance(getTypeFacade().load(typeCode)))
                {
                    sendConfiguredOutput(ctx, typeCode);
                    return new ActionResult<>(ActionResult.SUCCESS);
                }
                else
                {
                    LOG.warn("Skipped invocation of create action due to changed access rights on entity creation.");
                    getNotificationService().notifyUser(getNotificationSource(ctx), NotificationEventTypes.EVENT_TYPE_OBJECT_CREATION,
                                    NotificationEvent.Level.FAILURE, new NoPermissionException());
                }
            }
        }
        catch(final ObjectCreationException | TypeNotFoundException e)
        {
            LOG.error(e.getLocalizedMessage(), e);
            getNotificationService().notifyUser(getNotificationSource(ctx), NotificationEventTypes.EVENT_TYPE_OBJECT_CREATION,
                            NotificationEvent.Level.FAILURE, e);
        }
        return new ActionResult<>(ActionResult.ERROR);
    }


    protected String findTypeCode(final ActionContext<Object> ctx)
    {
        String typeCode = null;
        final WidgetModel model = (WidgetModel)ctx.getParameter(ActionContext.PARENT_WIDGET_MODEL);
        if(model != null)
        {
            final DataType userChosenType = model.getValue(CreateActionRenderer.CREATE_ACTION_USER_CHOSEN_TYPE, DataType.class);
            final DataType rootType = model.getValue(CreateActionRenderer.CREATE_ACTION_ROOT_TYPE, DataType.class);
            typeCode = userChosenType != null ? userChosenType.getCode() : rootType != null ? rootType.getCode() : null;
        }
        if(typeCode == null && ctx.getData() instanceof String)
        {
            typeCode = StringUtils.isNotBlank((String)ctx.getData()) ? (String)ctx.getData() : null;
        }
        return typeCode;
    }


    /**
     * @deprecated since 6.7, use {@link NotificationService#getWidgetNotificationSource(ActionContext)} instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected String getNotificationSource(final ActionContext<Object> ctx)
    {
        return getNotificationService().getWidgetNotificationSource(ctx);
    }


    protected void sendConfiguredOutput(final ActionContext<Object> ctx, final String typeCode) throws ObjectCreationException
    {
        final CockpitContext cockpitContext;
        if(shouldInitializeEntity(ctx))
        {
            final Object newInstance = objectFacade.create(typeCode);
            final Object component = ctx.getParameter(SETTING_CONTEXT_COMPONENT_NAME);
            final String componentName = Objects.toString(component, null);
            final String configCtx = StringUtils.defaultIfBlank(componentName, StringUtils.EMPTY);
            cockpitContext = new ObjectWithComponentContext(newInstance, configCtx, typeCode);
        }
        else
        {
            cockpitContext = new CreateContext(typeCode);
        }
        ctx.getParameterKeys().forEach(key -> cockpitContext.setParameter(key, ctx.getParameter(key)));
        if(ctx.getData() instanceof Map)
        {
            cockpitContext.setParameter(PARAM_ACTION_CONTEXT, ctx.getData());
        }
        sendOutput(SOCKET_OUTPUT_CREATE_CONTEXT, cockpitContext);
    }


    protected boolean shouldInitializeEntity(final ActionContext<Object> ctx)
    {
        final Object parameter = ctx.getParameter(SETTING_INITIALIZE_ENTITY);
        return parameter != null && Boolean.parseBoolean(parameter.toString());
    }


    @Override
    public void initializeDefaultEventListeners()
    {
        addSocketInputEventListener(SOCKET_INPUT_CREATED_OBJECT,
                        event -> sendOutput(SOCKET_OUTPUT_CREATED_OBJECT, event.getData()));
    }


    @Override
    public boolean canPerform(final ActionContext<Object> ctx)
    {
        String typeCode = null;
        if(ctx != null)
        {
            typeCode = findTypeCode(ctx);
        }
        return isAllowed(typeCode);
    }


    protected boolean isAllowed(final String typeCode)
    {
        boolean allowed = false;
        if(StringUtils.isNotBlank(typeCode))
        {
            try
            {
                final DataType type = typeFacade.load(typeCode);
                allowed = canCreateInstance(type);
            }
            catch(final TypeNotFoundException typeNotFoundException)
            {
                LOG.warn("Type not found: ".concat(typeCode), typeNotFoundException);
            }
        }
        return allowed;
    }


    protected boolean canCreateInstance(final DataType type)
    {
        boolean instanceCreationAllowed = getPermissionFacade().canCreateTypeInstance(type.getCode());
        if(instanceCreationAllowed && type.isSingleton())
        {
            final SimpleSearchQueryData query = new SimpleSearchQueryData(type.getCode());
            query.setPageSize(10);
            final Pageable pageable = getFieldSearchFacade().search(query);
            instanceCreationAllowed = pageable.getTotalCount() == 0;
            if(!instanceCreationAllowed)
            {
                LOG.debug("Entity creation disallowed due to existing singleton instances.");
            }
        }
        return instanceCreationAllowed;
    }


    @Override
    public boolean needsConfirmation(final ActionContext<Object> ctx)
    {
        return false;
    }


    @Override
    public String getConfirmationMessage(final ActionContext<Object> ctx)
    {
        throw new UnsupportedOperationException();
    }


    protected PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    public void setPermissionFacade(final PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    public ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    public void setObjectFacade(final ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    public FieldSearchFacade getFieldSearchFacade()
    {
        return fieldSearchFacade;
    }


    public void setFieldSearchFacade(final FieldSearchFacade fieldSearchFacade)
    {
        this.fieldSearchFacade = fieldSearchFacade;
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
