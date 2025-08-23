/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.async.Operation;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.WidgetConfigurationContextDecorator;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.core.spring.RequestOperationContextHolder;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.DefaultCallbackChain;
import com.hybris.cockpitng.engine.CockpitWidgetEngine;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.engine.operations.CockpitNGBackgroundOperation;
import com.hybris.cockpitng.engine.operations.LongOperationNotifier;
import com.hybris.cockpitng.events.callback.CallbackOperation;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.CockpitThreadContextCreator;
import com.hybris.cockpitng.util.WidgetControllers;
import com.hybris.cockpitng.util.WidgetTreeUIUtils;
import com.hybris.cockpitng.util.WidgetUtils;
import com.hybris.cockpitng.util.labels.CockpitComponentDefinitionLabelLocator;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Box;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Vbox;

/**
 * Provides access to the widget instance from within a controller.
 */
public class DefaultWidgetInstanceManager implements WidgetInstanceManager
{
    public static final String CNG_CURRENT_TITLE = "_cng_current_title";
    public static final String WIM_FAILURE_NOTIFICATION_TYPE = "WIMFailure";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWidgetInstanceManager.class);
    private static final String ATTRIBUTE_VERTICAL_LAYOUT = "verticalLayout";
    private final Widgetslot widgetslot;
    private final CockpitWidgetEngine cockpitWidgetEngine;
    private final CockpitConfigurationService cockpitConfigurationService;
    private final WidgetUtils widgetUtils;
    private final CockpitThreadContextCreator cockpitThreadContextCreator;
    private final Map<String, Object> labels;
    private final List<WidgetConfigurationContextDecorator> contextDecorators;
    private final Collection<CallbackOperation> editorBeforeCallbacks;
    private final Collection<CallbackOperation> editorAfterCallbacks;
    private final LabelService labelService;
    private final NotificationService notificationService;


    public DefaultWidgetInstanceManager(final Widgetslot widgetslot, final CockpitWidgetEngine cockpitWidgetEngine,
                    final WidgetUtils widgetUtils, final CockpitThreadContextCreator ctxCreator, final Map<String, Object> labels,
                    final List<WidgetConfigurationContextDecorator> contextDecorators,
                    final CockpitConfigurationService cockpitConfigurationService, final LabelService labelService,
                    final NotificationService notificationService)
    {
        this.widgetslot = widgetslot;
        this.cockpitWidgetEngine = cockpitWidgetEngine;
        this.widgetUtils = widgetUtils;
        this.cockpitThreadContextCreator = ctxCreator;
        this.labels = (labels == null ? Collections.emptyMap() : labels);
        this.contextDecorators = contextDecorators;
        this.cockpitConfigurationService = cockpitConfigurationService;
        this.notificationService = notificationService;
        this.editorBeforeCallbacks = Lists.newArrayList();
        this.editorAfterCallbacks = Lists.newArrayList();
        this.labelService = labelService;
    }


    @Override
    public void sendOutput(final String socketId, final Object data)
    {
        try
        {
            cockpitWidgetEngine.sendOutput(getWidgetslot(), socketId, data);
        }
        catch(final Exception e)
        {
            LOG.error("Error during send output", e);
            notificationService.notifyUser(this, WIM_FAILURE_NOTIFICATION_TYPE, NotificationEvent.Level.WARNING);
        }
    }


    @Override
    public TypedSettingsMap getWidgetSettings()
    {
        return widgetslot.getWidgetInstance().getWidget().getWidgetSettings();
    }


    @Override
    public WidgetModel getModel()
    {
        return widgetslot.getViewModel();
    }


    @Override
    public Widgetslot getWidgetslot()
    {
        return widgetslot;
    }


    public CockpitComponentDefinitionService getComponentDefinitionService()
    {
        return cockpitWidgetEngine instanceof DefaultCockpitWidgetEngine
                        ? ((DefaultCockpitWidgetEngine)cockpitWidgetEngine).getWidgetDefinitionService()
                        : null;
    }


    /**
     * Initializes a widget setting and sets an initial value if there is none, i.e. this method does nothing if there is
     * already a value set for the given key.
     */
    @Override
    public void initWidgetSetting(final String key, final Object initialValue)
    {
        WidgetControllers.initWidgetSetting(getWidgetSettings(), key, initialValue);
    }


    @Override
    public final void sendOutputAfterOperation(final String socketId, final Operation operation,
                    final EventListener<Event> callbackEvent, final String busyMessage)
    {
        executeOperationInternal(socketId, operation, callbackEvent, busyMessage, false);
    }


    @Override
    public void executeOperation(final Operation operation, final EventListener<Event> callbackEvent, final String busyMessage)
    {
        executeOperationInternal(null, operation, callbackEvent, busyMessage, false);
    }


    @Override
    public void executeOperationInParallel(final Operation operation, final EventListener<Event> callbackEvent)
    {
        executeOperationInternal(null, operation, callbackEvent, null, true);
    }


    @Override
    public String getLabel(final String key)
    {
        return CockpitComponentDefinitionLabelLocator.getLabel(this.labels, key);
    }


    @Override
    public String getLabel(final String key, final Object[] args)
    {
        return CockpitComponentDefinitionLabelLocator.getLabel(this.labels, key, args);
    }


    /**
     * For internal use only
     */
    private void executeOperationInternal(final String socketId, final Operation operation,
                    final EventListener<Event> callbackEvent, final String busyMessage, final boolean parallel)
    {
        final String operationId = String.format("%s_%s", getWidgetslot().getWidgetInstance().getId(),
                        ObjectUtils.defaultIfNull(socketId, StringUtils.EMPTY));
        if(!parallel && !lockOperationIfPossible(operationId, operation.getId()))
        {
            if(StringUtils.isNotBlank(busyMessage))
            {
                Messagebox.show(busyMessage);
            }
            LOG.warn("Another thread is currently running for operation:{}, ignoring operation request.", operationId);
            return;
        }
        final LongOperationNotifier longOperationNotifier = new LongOperationNotifier(operation);
        final CockpitNGBackgroundOperation longOperation = prepareLongOperation(operation, callbackEvent, longOperationNotifier, parallel,
                        operationId, socketId);
        longOperationNotifier.showNotification(createNotificationComponent(), longOperation);
        this.cockpitThreadContextCreator.execute(longOperation);
    }


    protected CockpitNGBackgroundOperation prepareLongOperation(final Operation operation,
                    final EventListener<Event> callbackEvent, final LongOperationNotifier longOperationNotifier, final boolean parallel, final String operationId,
                    final String socketId)
    {
        final Consumer<Exception> onError = this::onBackgroundOperationError;
        final Consumer<Object> onFinish = result -> onBackgroundOperationFinished(operation, parallel, operationId,
                        longOperationNotifier);
        final Consumer<Object> onSuccess = result -> onBackgroundOperationSuccess(socketId, callbackEvent, operationId, result);
        return new CockpitNGBackgroundOperation(Executions.getCurrent().getDesktop(),
                        RequestOperationContextHolder.instance().prepareForOperation(),
                        () -> operation.execute(longOperationNotifier.getProgress()), result -> {
            try
            {
                onSuccess.accept(result);
            }
            finally
            {
                onFinish.accept(result);
            }
        }, result -> {
            try
            {
                onError.accept(result);
            }
            finally
            {
                onFinish.accept(result);
            }
        });
    }


    protected void onBackgroundOperationFinished(final Operation operation, final boolean parallel, final String operationId,
                    final LongOperationNotifier longOperationNotifier)
    {
        longOperationNotifier.removeNotification();
        if(!parallel)
        {
            unlockOperation(operationId, operation.getId());
        }
    }


    protected void onBackgroundOperationError(final Exception e)
    {
        LOG.warn("Background operation failed", e);
        notificationService.notifyUser(this, WIM_FAILURE_NOTIFICATION_TYPE, NotificationEvent.Level.WARNING);
    }


    protected void onBackgroundOperationSuccess(final String socketId, final EventListener<Event> callbackEvent,
                    final String operationId, final Object result)
    {
        try
        {
            if(StringUtils.isNotBlank(socketId))
            {
                sendOutput(socketId, result);
            }
            if(callbackEvent != null)
            {
                callbackEvent.onEvent(new Event(operationId, widgetslot, result));
            }
        }
        catch(final Exception e)
        {
            LOG.warn("Updating UI after background operation failed", e);
            notificationService.notifyUser(this, WIM_FAILURE_NOTIFICATION_TYPE, NotificationEvent.Level.WARNING);
        }
    }


    /**
     * Creates component on which long operation notification will be displayed. The component will be detached after long
     * operation.
     *
     * @return a component to render a notification.
     */
    protected Component createNotificationComponent()
    {
        final Component localNotifierStack = getWidgetslot().getFellowIfAny(WidgetUtils.NOTIFIER_STACK);
        boolean isVertical = false;
        if(localNotifierStack != null)
        {
            final Object isVerticalAttribute = localNotifierStack.getAttribute(ATTRIBUTE_VERTICAL_LAYOUT);
            isVertical = isVerticalAttribute instanceof String && Boolean.valueOf((String)isVerticalAttribute);
        }
        final Box box = isVertical ? new Vbox() : new Hbox();
        if(localNotifierStack != null)
        {
            widgetUtils.addNotifierToStack(box, localNotifierStack);
        }
        else
        {
            widgetUtils.addNotifierToStack(box);
        }
        return box;
    }


    /**
     * Checks whether widgetSlot has an attribute with given operationId. If there is not such attribute it will be added so
     * other operation with the same id won't be executed at the same time.
     *
     * @param operationIdentityValue
     *           operation identity value.
     * @param operationId
     *           result of {@link Operation#getId()}
     * @return true if there was not attribute with given id.
     */
    protected boolean lockOperationIfPossible(final String operationIdentityValue, final Object operationId)
    {
        Set<Object> ids = (Set<Object>)getWidgetslot().getAttribute(operationIdentityValue);
        if(ids == null)
        {
            ids = new HashSet<>();
            getWidgetslot().setAttribute(operationIdentityValue, ids);
        }
        return ids.add(operationId);
    }


    /**
     * Removed widget slot's information for given operation id.
     *
     * @param operationIdentityValue
     *           operation identity value.
     * @param operationId
     *           result of {@link Operation#getId()}
     */
    protected void unlockOperation(final String operationIdentityValue, final Object operationId)
    {
        final Set<Object> ids = (Set<Object>)getWidgetslot().getAttribute(operationIdentityValue);
        if(ids != null && !ids.isEmpty())
        {
            ids.remove(operationId);
        }
    }


    @Override
    public String getTitle()
    {
        final Object title = widgetslot.getAttribute(CNG_CURRENT_TITLE);
        if(title instanceof String)
        {
            return (String)title;
        }
        return widgetslot.getWidgetInstance().getWidget().getTitle();
    }


    @Override
    public void setTitle(final String title)
    {
        Widgetslot targetWidgetslot = widgetslot;
        final Widget widget = widgetslot.getWidgetInstance().getWidget();
        if(widget != null && widget.isPartOfGroup() && widget.getGroupContainer() != null
                        && widget.equals(widget.getGroupContainer().getComposedRootInstance()))
        {
            targetWidgetslot = WidgetTreeUIUtils.getParentWidgetslot(widgetslot);
        }
        targetWidgetslot.setAttribute(CNG_CURRENT_TITLE, title);
        final Object titleListener = targetWidgetslot.getAttribute(CNG_TITLE_CHANGE_LISTENER);
        if(titleListener instanceof EventListener<?>)
        {
            try
            {
                ((EventListener<Event>)titleListener).onEvent(new Event("onTitleChanged", null, title));
            }
            catch(final Exception e)
            {
                LOG.error("Could not update title, reason: ", e);
            }
        }
    }


    @Override
    public <CONFIG> ConfigContext buildConfigurationContext(final ConfigContext additionalContext,
                    final Class<CONFIG> configurationType)
    {
        ConfigContext decoratedContext = additionalContext;
        if(CollectionUtils.isNotEmpty(this.contextDecorators))
        {
            for(final WidgetConfigurationContextDecorator decorator : this.contextDecorators)
            {
                decoratedContext = decorator.decorateContext(decoratedContext, configurationType, widgetslot.getWidgetInstance());
            }
        }
        return decoratedContext;
    }


    @Override
    public <CONFIG> CONFIG loadConfiguration(final ConfigContext context, final Class<CONFIG> configurationType)
                    throws CockpitConfigurationException
    {
        return cockpitConfigurationService.loadConfiguration(buildConfigurationContext(context, configurationType),
                        configurationType, getWidgetslot().getWidgetInstance());
    }


    @Override
    public <CONFIG> void storeConfiguration(final ConfigContext context, final CONFIG configuration)
                    throws CockpitConfigurationException
    {
        cockpitConfigurationService.storeConfiguration(buildConfigurationContext(context, configuration.getClass()), configuration);
    }


    @Override
    public EditorListener registerEditorListener(final EditorContext context, final EditorListener listener)
    {
        return new EditorListener()
        {
            @Override
            public void onValueChanged(final Object value)
            {
                final DefaultCallbackChain chain = new DefaultCallbackChain();
                chain.addAll(editorBeforeCallbacks);
                chain.add((innerChain, ctx, val) -> {
                    listener.onValueChanged(val);
                    innerChain.doChain(ctx, val);
                });
                chain.addAll(editorAfterCallbacks);
                chain.doChain(context, value);
            }


            @Override
            public void onEditorEvent(final String eventCode)
            {
                listener.onEditorEvent(eventCode);
            }


            @Override
            public void sendSocketOutput(final String outputId, final Object data)
            {
                listener.sendSocketOutput(outputId, data);
            }
        };
    }


    @Override
    public void registerBeforeEditorCallback(final CallbackOperation callback)
    {
        editorBeforeCallbacks.add(callback);
    }


    @Override
    public void registerAfterEditorCallback(final CallbackOperation callback)
    {
        editorAfterCallbacks.add(callback);
    }


    public LabelService getLabelService()
    {
        return labelService;
    }
}
