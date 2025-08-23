/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionDefinition;
import com.hybris.cockpitng.actions.ActionListener;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.BackgroundOperationDefinition;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.actions.CockpitActionRenderer;
import com.hybris.cockpitng.actions.impl.DefaultActionRenderer;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.model.ModelObserver;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.core.util.impl.WidgetSocketUtils;
import com.hybris.cockpitng.dependencies.impl.SpringApplicationContextDependencyResolver;
import com.hybris.cockpitng.engine.CockpitWidgetEngine;
import com.hybris.cockpitng.engine.ComponentWidgetAdapterAware;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.keyboard.KeyboardSupportService;
import com.hybris.cockpitng.util.labels.CockpitComponentDefinitionLabelLocator;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Span;

public class Action extends AbstractCockpitComponent implements AfterCompose, IdSpace
{
    public static final String ON_ACTION_PERFORMED = "onActionPerformed";
    public static final DefaultActionRenderer DEFAULT_ACTION_RENDERER = new DefaultActionRenderer();
    public static final String KEYBOARD_SUPPORT_SERVICE_BEAN_ID = "keyboardSupportService";
    private static final long serialVersionUID = -1156760773943370792L;
    private static final Logger LOG = LoggerFactory.getLogger(Action.class);
    private static final int MINIMUM_PRIORITY = -9999;
    private static final String COCKPIT_ACTION_CLASS_INSTANCE_PARAM = "cockpitActionClassInstance";
    private final transient Map<String, Object> actionParameters = new HashMap<>();
    private String actionUID;
    private transient Object inputValue;
    private String property;
    private String outputProperty;
    private String actionId;
    private String viewMode;
    private boolean composed;
    private String triggerOnKeys;
    private transient KeyboardSupportService keyboardSupportService;
    private ActionContext actionContext;


    @Override
    public void destroy()
    {
        if(widgetInstanceManager.getWidgetslot() != null)
        {
            widgetInstanceManager.getWidgetslot().removeForward(Events.ON_CTRL_KEY, this, Events.ON_CTRL_KEY);
        }
        super.destroy();
    }


    public void reload()
    {
        initialized = false;
        this.initialize();
    }


    protected void reloadIfNecessary()
    {
        if(initialized)
        {
            reload();
        }
    }


    public void initialize()
    {
        if(initialized)
        {
            return;
        }
        else
        {
            initialized = true;
        }
        if(StringUtils.isBlank(getActionId()))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Aborting action initialization since action id is not set.");
            }
            return;
        }
        final ActionDefinition definition = (ActionDefinition)getComponentDefinitionService()
                        .getComponentDefinitionForCode(getActionId());
        if(definition == null)
        {
            LOG.warn("could not find action definition for code [" + getActionId() + "]");
            return;
        }
        final CockpitAction<?, ?> action = resolveCockpitActionClassInstance(definition);
        if(action != null)
        {
            final SpringApplicationContextDependencyResolver dependencyResolver = new SpringApplicationContextDependencyResolver<>(
                            SpringUtil.getApplicationContext());
            dependencyResolver.injectDependencies(action);
            if(action instanceof ComponentWidgetAdapterAware)
            {
                initializeComponentWidgetAdapter((ComponentWidgetAdapterAware)action);
            }
            this.setAttribute("componentRoot", definition.getLocationPath());
            addKeyboardSupport();
            renderAction(this, definition, action, false);
            unregisterObserver();
            if(widgetInstanceManager != null && !StringUtils.isBlank(getProperty()))
            {
                modelObserver = new ModelObserver()
                {
                    @Override
                    public void modelChanged()
                    {
                        widgetInstanceManager.getModel().removeObserver(getProperty(), this);
                        reload();
                    }
                };
                widgetInstanceManager.getModel().addObserver(getProperty(), modelObserver);
            }
        }
    }


    protected void renderAction(final HtmlBasedComponent parent, final ActionDefinition actionDefinition,
                    final CockpitAction action, final boolean updateOnly)
    {
        final CockpitActionRenderer renderer = getRenderer(actionDefinition);
        actionContext = createActionContext(actionDefinition);
        initializeActionContext(actionContext, parent, actionDefinition, action, updateOnly);
        final ActionListener actionListener = result -> {
            if(ActionResult.SUCCESS.equals(result.getResultCode()) && !StringUtils.isBlank(getOutputProperty())
                            && Action.this.widgetInstanceManager != null)
            {
                Action.this.widgetInstanceManager.getModel().setValue(getOutputProperty(), result.getData());
            }
            Events.postEvent(ON_ACTION_PERFORMED, Action.this, result);
        };
        if(!getChildren().isEmpty())
        {
            getChildren().clear();
        }
        renderer.render(parent, action, actionContext, updateOnly, actionListener);
    }


    protected void initializeActionContext(final ActionContext actionContext, final HtmlBasedComponent parent,
                    final ActionDefinition actionDefinition, final CockpitAction action, final boolean updateOnly)
    {
        // add component root and resource path
        if(actionDefinition != null)
        {
            actionContext.setParameter(CockpitWidgetEngine.COMPONENT_ROOT_PARAM, actionDefinition.getLocationPath());
            actionContext.setParameter(CockpitWidgetEngine.COMPONENT_RESOURCE_PATH_PARAM, actionDefinition.getResourcePath());
        }
        if(StringUtils.isNotBlank(getViewMode()))
        {
            actionContext.setParameter(ActionContext.VIEW_MODE_PARAM, getViewMode());
        }
        actionContext.setParameter(ActionContext.PARENT_WIDGET_MODEL, Action.this.getWidgetInstanceManager().getModel());
        actionContext.setParameter(ActionContext.ACTION_UID, getActionUID());
    }


    protected ActionContext createActionContext(final ActionDefinition actionDefinition)
    {
        final Object data;
        if(StringUtils.isBlank(getProperty()))
        {
            data = getInputValue();
        }
        else
        {
            data = this.widgetInstanceManager.getModel().getValue(getProperty(), Object.class);
        }
        // construct the parameter map
        final Map<String, Object> parameters = new HashMap<>();
        // add settings from the definition
        if(actionDefinition != null)
        {
            final TypedSettingsMap settings = actionDefinition.getDefaultSettings();
            if(MapUtils.isNotEmpty(settings))
            {
                parameters.putAll(settings.getAll());
            }
        }
        // add instance parameters
        parameters.putAll(getParameters());
        final Map<String, Object> labels = actionDefinition != null
                        ? CockpitComponentDefinitionLabelLocator.getLabelMap(actionDefinition)
                        : Collections.emptyMap();
        final ActionContext<Span> ctx = new ActionContext(data, actionDefinition, parameters, labels);
        ctx.setTriggerOnKeys(getTriggerOnKeys());
        return ctx;
    }


    protected void addKeyboardSupport()
    {
        if(StringUtils.isNotBlank(getTriggerOnKeys()) && widgetInstanceManager.getWidgetslot() != null)
        {
            final Widgetslot widgetslot = widgetInstanceManager.getWidgetslot();
            final String ctrlKeys = ObjectUtils.defaultIfNull(widgetslot.getCtrlKeys(), StringUtils.EMPTY);
            widgetslot.setCtrlKeys(getKeyboardSupportService().mergeCtrlKeys(ctrlKeys, getTriggerOnKeys()));
            widgetslot.addForward(Events.ON_CTRL_KEY, this, Events.ON_CTRL_KEY);
            this.getEventListeners(Events.ON_CTRL_KEY).forEach(el -> this.removeEventListener(Events.ON_CTRL_KEY, el));
        }
    }


    protected CockpitAction instantiateAction(final ActionDefinition actionDefinition)
    {
        final String classname = actionDefinition.getActionClassName();
        try
        {
            return getComponentDefinitionService().createAutowiredComponent(actionDefinition, classname, null);
        }
        catch(final ReflectiveOperationException e)
        {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }


    protected CockpitActionRenderer getRenderer(final ActionDefinition actionDefinition)
    {
        final String classname = actionDefinition.getCustomRendererClassName();
        if(StringUtils.isBlank(classname))
        {
            return DEFAULT_ACTION_RENDERER;
        }
        else
        {
            try
            {
                return getComponentDefinitionService().createAutowiredComponent(actionDefinition,
                                actionDefinition.getCustomRendererClassName(), SpringUtil.getApplicationContext());
            }
            catch(final ReflectiveOperationException e)
            {
                LOG.error(e.getMessage(), e);
            }
            return null;
        }
    }


    public Object getInputValue()
    {
        return inputValue;
    }


    public void setInputValue(final Object value)
    {
        if((!ClassUtils.isPrimitiveOrWrapper(value == null ? null : value.getClass()))
                        || (!Objects.equals(this.inputValue, value)))
        {
            this.inputValue = value;
            reloadIfNecessary();
        }
    }


    public String getProperty()
    {
        return property;
    }


    public void setProperty(final String property)
    {
        if(!Objects.equals(this.property, property))
        {
            this.property = property;
            reloadIfNecessary();
        }
    }


    public String getOutputProperty()
    {
        return outputProperty;
    }


    public void setOutputProperty(final String outputProperty)
    {
        if(!Objects.equals(this.outputProperty, outputProperty))
        {
            this.outputProperty = outputProperty;
            reloadIfNecessary();
        }
    }


    public String getTriggerOnKeys()
    {
        return triggerOnKeys;
    }


    public void setTriggerOnKeys(final String triggerOnKeys)
    {
        this.triggerOnKeys = triggerOnKeys;
    }


    public String getActionId()
    {
        return actionId;
    }


    public void setActionId(final String actionId)
    {
        if(!Objects.equals(this.actionId, actionId))
        {
            this.actionId = actionId;
            reloadIfNecessary();
        }
    }


    public String getViewMode()
    {
        return viewMode;
    }


    public void setViewMode(final String viewMode)
    {
        if(!Objects.equals(this.viewMode, viewMode))
        {
            this.viewMode = viewMode;
            reloadIfNecessary();
        }
    }


    public Map<String, Object> getParameters()
    {
        return actionParameters;
    }


    public void setParameters(final Map<String, Object> parameters)
    {
        this.actionParameters.clear();
        if(parameters != null)
        {
            this.actionParameters.putAll(parameters);
        }
    }


    public void addParameter(final String key, final Object val)
    {
        this.actionParameters.put(key, val);
    }


    public void removeParameter(final String key)
    {
        this.actionParameters.remove(key);
    }


    public String getActionUID()
    {
        return actionUID;
    }


    public void setActionUID(final String actionUID)
    {
        this.actionUID = actionUID;
    }


    @Override
    public void afterCompose()
    {
        if(this.composed)
        {
            return;
        }
        initialize();
        this.addEventListener(MINIMUM_PRIORITY, ON_ACTION_PERFORMED, event -> {
            final ActionResult<?> result = (ActionResult<?>)event.getData();
            final Map<String, Object> outputsToSend = new LinkedHashMap<>();
            if(StringUtils.isNotEmpty(Action.this.actionId))
            {
                outputsToSend.put(Action.this.actionId + "." + result.getResultCode(), result.getData());
            }
            for(final Map.Entry<String, Object> entry : result.getOutputsToSend().entrySet())
            {
                outputsToSend.put(entry.getKey(), entry.getValue());
            }
            for(final Map.Entry<String, Object> entry : outputsToSend.entrySet())
            {
                sendOutput(entry.getKey(), entry.getValue());
            }
            result.getSocketAfterOperation().forEach(this::sendOutputAfterOperation);
        });
        this.composed = true;
    }


    protected void sendOutput(final String outputId, final Object object)
    {
        final WidgetInstanceManager wim = getWidgetInstanceManager();
        if(wim != null)
        {
            final Widget widget = wim.getWidgetslot().getWidgetInstance().getWidget();
            if(WidgetSocketUtils.hasOutputSocketWithId(widget, getComponentDefinitionService()
                            .getComponentDefinitionForCode(widget.getWidgetDefinitionId(), WidgetDefinition.class), outputId))
            {
                wim.sendOutput(outputId, object);
            }
        }
    }


    protected void sendOutputAfterOperation(final BackgroundOperationDefinition definition)
    {
        final WidgetInstanceManager wim = getWidgetInstanceManager();
        if(wim != null)
        {
            final String id = StringUtils.isBlank(definition.getId()) ? getOutputProperty() : definition.getId();
            wim.sendOutputAfterOperation(id, definition.getOperation(), definition.getCallbackEvent(), definition.getBusyMessage());
        }
    }


    protected CockpitAction resolveCockpitActionClassInstance(final ActionDefinition definition, final boolean create)
    {
        CockpitAction<?, ?> ret = null;
        if(getAttribute(COCKPIT_ACTION_CLASS_INSTANCE_PARAM) != null)
        {
            ret = (CockpitAction<?, ?>)getAttribute(COCKPIT_ACTION_CLASS_INSTANCE_PARAM);
        }
        else if(create)
        {
            ret = instantiateAction(definition);
            setAttribute(COCKPIT_ACTION_CLASS_INSTANCE_PARAM, ret);
        }
        return ret;
    }


    protected CockpitAction resolveCockpitActionClassInstance(final ActionDefinition definition)
    {
        return resolveCockpitActionClassInstance(definition, true);
    }


    @Override
    public String getComponentID()
    {
        return getActionId();
    }


    @Override
    public List<ComponentWidgetAdapterAware> getWidgetAdaptersAwareIfPresent()
    {
        final CockpitAction<?, ?> actionInstance = resolveCockpitActionClassInstance();
        if(actionInstance instanceof ComponentWidgetAdapterAware)
        {
            return Collections.singletonList((ComponentWidgetAdapterAware)actionInstance);
        }
        return Collections.emptyList();
    }


    protected CockpitAction resolveCockpitActionClassInstance()
    {
        CockpitAction<?, ?> ret = null;
        if(getAttribute(COCKPIT_ACTION_CLASS_INSTANCE_PARAM) != null)
        {
            ret = (CockpitAction<?, ?>)getAttribute(COCKPIT_ACTION_CLASS_INSTANCE_PARAM);
        }
        return ret;
    }


    public KeyboardSupportService getKeyboardSupportService()
    {
        if(keyboardSupportService == null)
        {
            keyboardSupportService = (KeyboardSupportService)SpringUtil.getBean(KEYBOARD_SUPPORT_SERVICE_BEAN_ID,
                            KeyboardSupportService.class);
        }
        return keyboardSupportService;
    }


    public ActionContext getActionContext()
    {
        return actionContext;
    }
}
