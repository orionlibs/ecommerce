/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hybris.cockpitng.components.InvisibleWidgetchildren;
import com.hybris.cockpitng.components.Widgetchildren;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.AbstractCockpitComponentDefinition;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.RuleEngineResult;
import com.hybris.cockpitng.core.SocketConnectionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetConnection;
import com.hybris.cockpitng.core.WidgetController;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.WidgetService;
import com.hybris.cockpitng.core.WidgetSocket;
import com.hybris.cockpitng.core.WidgetTemplateRulesEngine;
import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.core.config.WidgetConfigurationContextDecorator;
import com.hybris.cockpitng.core.impl.DefaultCockpitComponentDefinitionService;
import com.hybris.cockpitng.core.impl.DefaultWidgetModel;
import com.hybris.cockpitng.core.impl.ModelValueHandlerFactory;
import com.hybris.cockpitng.core.impl.NotificationStack;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.core.persistence.WidgetPersistenceService;
import com.hybris.cockpitng.core.persistence.packaging.CockpitResourceLoader;
import com.hybris.cockpitng.core.persistence.packaging.WidgetClassLoader;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibUtils;
import com.hybris.cockpitng.core.persistence.packaging.impl.DefaultWidgetLibUtils;
import com.hybris.cockpitng.core.persistence.packaging.impl.DefaultWidgetResourceReader;
import com.hybris.cockpitng.core.security.WidgetAuthorizationService;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.ui.WidgetInstanceFacade;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.core.util.CockpitTypeUtils;
import com.hybris.cockpitng.core.util.impl.ReflectionUtils;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.core.util.impl.WidgetSocketUtils;
import com.hybris.cockpitng.core.util.impl.WidgetTreeUtils;
import com.hybris.cockpitng.engine.CockpitWidgetEngine;
import com.hybris.cockpitng.engine.ComponentWidgetAdapterAware;
import com.hybris.cockpitng.engine.SessionWidgetInstanceRegistry;
import com.hybris.cockpitng.engine.WidgetChildrenContainerRenderer;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.engine.WidgetInstanceManagerAware;
import com.hybris.cockpitng.events.SocketEvent;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.BindWidgetController;
import com.hybris.cockpitng.util.CockpitThreadContextCreator;
import com.hybris.cockpitng.util.CockpitUIFactory;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.HtmlBasedWidgetController;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.WidgetControllers;
import com.hybris.cockpitng.util.WidgetUtils;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.util.labels.CockpitComponentDefinitionLabelLocator;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.widgets.controller.DefaultFlowWidgetController;
import groovy.lang.GroovyClassLoader;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.zkoss.idom.Attribute;
import org.zkoss.idom.Document;
import org.zkoss.idom.Element;
import org.zkoss.util.Locales;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Vlayout;

/**
 * Default widget engine. Used in "normal" (not admin) rendering mode. For internal use only.
 */
public class DefaultCockpitWidgetEngine implements CockpitWidgetEngine
{
    /**
     * @deprecated since 1808, use {@link DefaultCockpitComponentDefinitionService#DEFAULT_VIEW_ZUL} instead
     */
    @Deprecated(since = "1808", forRemoval = true)
    public static final String VIEW_ZUL = DefaultCockpitComponentDefinitionService.DEFAULT_VIEW_ZUL;
    public static final String MONITOR_WIDGET_COUNTER_ATTRIB = "_cng_monitor_widget_counter";
    public static final String ON_CP_FWD_EL = "onCockpitLaterEvent";
    public static final EventListener<Event> STOP_PROPAGATION_LISTENER = event -> event.stopPropagation();
    public static final String WIDGET = "widget";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String SLOT_ID = "slotID";
    public static final String WIDGETSLOT = "widgetslot";
    public static final String FULL_WIDTH = "100%";
    public static final String SCLASS_WIDGET_BODY = "widget_body";
    public static final String CP_FWD_EL = "cp_fwd_el";
    public static final String SETTING_WIDGET_MOLD = "widgetMold";
    public static final String Y_TESTID_PATH = "y-testid-path";
    private static final String COCKPITNG_WIDGETENGINE_MONITOR_CONSOLE_ENABLED = "cockpitng.widgetengine.monitor.console.enabled";
    private static final String COCKPITNG_WIDGETENGINE_MONITOR_CLIENT_ENABLED = "cockpitng.widgetengine.monitor.client.enabled";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCockpitWidgetEngine.class);
    private static final Logger SOCLOG = LoggerFactory.getLogger("CNGSocketLogger");
    private static final String COCKPIT_WIDGET_CHILDREN_INVISIBLE_SLOT_ID = "cockpitWidgetChildrenInvisible";
    private static final int LOW_EVENT_PRIORITY = -9999;
    protected Document documentWithWidgetSlot;
    protected Document documentNoView;
    private Map<String, WidgetChildrenContainerRenderer> childrenContainerRenderer;
    private WidgetService widgetService;
    private WidgetInstanceFacade widgetInstanceFacade;
    private CockpitComponentDefinitionService componentDefinitionService;
    private SocketConnectionService socketConnectionService;
    private WidgetUtils widgetUtils;
    private CockpitTypeUtils cockpitTypeUtils;
    private WidgetTemplateRulesEngine rulesEngine;
    private WidgetPersistenceService widgetPersistenceService;
    private SessionWidgetInstanceRegistry sessionWidgetInstanceRegistry;
    private CockpitThreadContextCreator cockpitThreadContextCreator;
    private WidgetLibUtils widgetLibUtils;
    private CockpitConfigurationService cockpitConfigurationService;
    private WidgetAuthorizationService widgetAuthorizationService;
    private ModelValueHandlerFactory modelValueHandlerFactory;
    private CockpitProperties cockpitProperties;
    private CockpitResourceLoader cockpitResourceLoader;
    private List<WidgetConfigurationContextDecorator> widgetConfigurationContextDecoratorList;
    private Boolean renderingMonitorEnabled;
    private NotificationStack notificationStack;
    private LabelService labelService;
    private NotificationService notificationService;


    public static String getLabel(final String key, final Map<String, Object> labels)
    {
        if(key.contains("."))
        {
            final String[] tokens = key.split("\\.");
            if(tokens != null && tokens.length > 0)
            {
                final Object object = labels.get(tokens[0]);
                if(object instanceof Map)
                {
                    return getLabel(key.substring(tokens[0].length() + 1), (Map<String, Object>)object);
                }
            }
            return null;
        }
        else
        {
            final Object object = labels.get(key);
            return object instanceof String ? (String)object : null;
        }
    }


    public void setNotificationStack(final NotificationStack notificationStack)
    {
        this.notificationStack = notificationStack;
    }


    @Override
    public void createWidgetView(final Widgetslot widgetslot)
    {
        final WidgetInstance currentWidgetInstance = resolveWidgetInstance(widgetslot);
        if(currentWidgetInstance == null)
        {
            LOG.debug("No widget with slotId '" + widgetslot.getSlotID() + "' was found");
        }
        else
        {
            if(currentWidgetInstance.getParent() == null)
            {
                removeAllStubInstances(currentWidgetInstance.getWidget());
            }
            if(checkVisibility(currentWidgetInstance.getWidget()))
            {
                widgetUtils.registerWidgetslot(widgetslot);
                createComponents(widgetslot);
            }
        }
    }


    /**
     * Decides if the given widget should be displayed or not.
     *
     * @param widget
     *           The widget to check, can be null.
     * @return true, if the widget should be displayed, false otherwise or if widget is null.
     */
    protected boolean checkVisibility(final Widget widget)
    {
        if(widget == null)
        {
            return false;
        }
        return getWidgetAuthorizationService().isAccessAllowed(widget);
    }


    private void removeAllStubInstances(final Widget widget)
    {
        final WidgetDefinition definition = getWidgetDefinition(widget);
        if(definition != null && definition.isStubWidget())
        {
            final List<WidgetInstance> widgetInstances = new ArrayList<>(widget.getWidgetInstances());
            for(final WidgetInstance widgetInstance : widgetInstances)
            {
                if(widgetInstance.getCreator() instanceof ComponentWidgetAdapterAware)
                {
                    widgetInstanceFacade.removeWidgetInstance(widgetInstance);
                }
            }
        }
        for(final Widget child : widget.getChildren())
        {
            removeAllStubInstances(child);
        }
    }


    private WidgetInstance resolveWidgetInstance(final Widgetslot widgetslot)
    {
        WidgetInstance currentWidgetInstance = widgetslot.getWidgetInstance();
        if(currentWidgetInstance == null)
        {
            // resolve by parent
            final List<WidgetInstance> instances = (widgetslot.getParentWidgetInstance() == null
                            ? Collections.<WidgetInstance>emptyList()
                            : getWidgetInstanceFacade().getWidgetInstances(widgetslot.getParentWidgetInstance(), widgetslot.getSlotID(),
                                            true));
            if(!instances.isEmpty())
            {
                for(final WidgetInstance widgetInstance : instances)
                {
                    if(widgetInstance != null && checkVisibility(widgetInstance.getWidget()))
                    {
                        currentWidgetInstance = widgetInstance;
                        break;
                    }
                }
            }
            if(currentWidgetInstance == null)
            {
                // resolve from session
                currentWidgetInstance = getOrCreateInstance(widgetslot);
            }
            widgetslot.setWidgetInstance(currentWidgetInstance);
        }
        return currentWidgetInstance;
    }


    private WidgetInstance getOrCreateInstance(final Widgetslot widgetslot)
    {
        WidgetInstance currentWidgetInstance = getSessionWidgetInstance(widgetslot.getSlotID());
        if(currentWidgetInstance == null //
                        && widgetslot.getParentWidgetInstance() == null) // only allow injection of root widget for root slot
        {
            // load the tree and create new instance
            final Widget rootWidget = getWidgetPersistenceService().loadWidgetTree(widgetslot.getSlotID());
            if(rootWidget != null)
            {
                if(rootWidget.getParent() == null)
                {
                    currentWidgetInstance = getWidgetInstanceFacade().getRootWidgetInstance(rootWidget);
                    setSessionWidgetInstance(currentWidgetInstance, widgetslot.getSlotID());
                }
                else
                {
                    LOG.error("root widget expected but got a widget with parent (widget id='" + rootWidget.getId() + "')");
                }
            }
        }
        return currentWidgetInstance;
    }


    protected String getWidgetBodySclass(final WidgetInstance widgetInstance)
    {
        final StringBuilder builder = new StringBuilder();
        WidgetDefinition definition = getWidgetDefinition(widgetInstance.getWidget());
        if(definition == null)
        {
            LOG.warn("Could not find widget definition for widget instance: {}", widgetInstance);
        }
        else
        {
            final List<String> inheritanceHierarchy = new LinkedList<>();
            inheritanceHierarchy.add(definition.getCode());
            while(StringUtils.isNotBlank(definition.getParentCode()))
            {
                definition = (WidgetDefinition)getWidgetDefinitionService()
                                .getComponentDefinitionForCode(definition.getParentCode());
                inheritanceHierarchy.add(0, definition.getCode());
            }
            inheritanceHierarchy.stream().forEach(code -> {
                builder.append("yw-");
                builder.append(StringUtils.replace(code, ".", "_"));
                builder.append(" ");
            });
        }
        builder.append(SCLASS_WIDGET_BODY);
        return builder.toString();
    }


    protected String getWidgetMoldSclass(final WidgetInstance widgetInstance, final WidgetDefinition definition)
    {
        if(definition.getDefaultSettings() != null && definition.getDefaultSettings().containsKey(SETTING_WIDGET_MOLD))
        {
            final String widgetMold = widgetInstance.getWidget().getWidgetSettings().getString(SETTING_WIDGET_MOLD);
            if(StringUtils.isNotBlank(widgetMold))
            {
                return String.format("yw-widget-mold-%s", widgetMold);
            }
        }
        return StringUtils.EMPTY;
    }


    protected void injectWidgetVariables(final Object widgetController, final WidgetInstanceManager widgetInstanceManager)
    {
        if(widgetController instanceof WidgetInstanceManagerAware)
        {
            ((WidgetInstanceManagerAware)widgetController).setWidgetInstanceManager(widgetInstanceManager);
        }
        else
        {
            for(final Field field : ReflectionUtils.getAllDeclaredFields(widgetController.getClass()))
            {
                if(field.getType().isAssignableFrom(widgetInstanceManager.getClass()))
                {
                    ReflectionUtils.setField(field, widgetController, widgetInstanceManager, WireVariable.class);
                }
            }
        }
    }


    private boolean isRenderingConsoleMonitoringEnabled()
    {
        if(renderingMonitorEnabled == null)
        {
            renderingMonitorEnabled = Boolean
                            .valueOf(Boolean.parseBoolean(getCockpitProperties().getProperty(COCKPITNG_WIDGETENGINE_MONITOR_CONSOLE_ENABLED)));
        }
        return renderingMonitorEnabled.booleanValue();
    }


    protected void createComponents(final Widgetslot widgetslot)
    {
        final WidgetInstance widgetInstance = widgetslot.getWidgetInstance();
        final Widget widget = widgetInstance.getWidget();
        final boolean isRoot = widget.getParent() == null;
        Date startRender = null;
        if(isRenderingConsoleMonitoringEnabled())
        {
            if(isRoot)
            {
                Executions.getCurrent().setAttribute(MONITOR_WIDGET_COUNTER_ATTRIB, Integer.valueOf(1));
                startRender = new Date();
                LOG.info("Start rendering widget tree...");
            }
            else
            {
                final Object attribute = Executions.getCurrent().getAttribute(MONITOR_WIDGET_COUNTER_ATTRIB);
                if(attribute instanceof Integer)
                {
                    Executions.getCurrent().setAttribute(MONITOR_WIDGET_COUNTER_ATTRIB, Integer.valueOf((Integer)attribute + 1));
                }
            }
        }
        final WidgetDefinition definition = getWidgetDefinition(widget);
        final Map<String, Object> labelMap = createLabelMap(definition, widget);
        final DefaultWidgetInstanceManager defaultWidgetInstanceManager = createWidgetInstanceManager(widgetslot, labelMap);
        widgetslot.setAttribute(WIDGET, widgetslot);
        Object widgetController = createWidgetController(widget, definition);
        final String viewURI = definition.getViewURI();
        boolean htmlBased = false;
        if(widgetController == null)
        {
            if(isIndependentView(definition, viewURI))
            {
                widgetController = new HtmlBasedWidgetController(viewURI, definition);
                htmlBased = true;
            }
            else
            {
                widgetController = new DefaultWidgetController();
            }
        }
        injectWidgetVariables(widgetController, defaultWidgetInstanceManager);
        final WidgetModel widgetModel = new DefaultWidgetModel((Map<String, Object>)widgetInstance.getModel(),
                        this.modelValueHandlerFactory.createModelValueHandler());
        widgetslot.setAttribute(Widgetslot.ATTRIBUTE_WIDGET_MODEL, widgetModel);
        if(!(widgetController instanceof Composer<?>))
        // TODO explicit check for mvvm viewmodel?
        {
            final Object viewModel = widgetController;
            final BindWidgetController bindController = new BindWidgetController();
            bindController.setWidgetInstanceManager(defaultWidgetInstanceManager);
            bindController.setViewModel(viewModel);
            bindController.setWidgetslot(widgetslot);
            bindController.setViewModelId(definition.getControllerID());
            bindController.setWidgetDefinition(definition);
            widgetController = bindController;
        }
        if(StringUtils.isNotBlank(definition.getControllerID()))
        {
            widgetslot.setAttribute(definition.getControllerID(), widgetController);
        }
        widgetslot.setAttribute(Widgetslot.ATTRIBUTE_WIDGET_CONTROLLER, widgetController);
        widgetslot.setAttribute("widgetInstanceManager", defaultWidgetInstanceManager);
        final ApplicationContext applicationContext = componentDefinitionService.getApplicationContext(definition);
        widgetslot.setAttribute("moduleAppCtx", applicationContext);
        widgetslot.setAttribute("widgetSettings", widget.getWidgetSettings());
        forwardGroupWidgetSettings(widget);
        final String rootPath = definition.getLocationPath();
        widgetslot.setAttribute(WR_PARAM, rootPath);
        widgetslot.setAttribute(WIDGET_ROOT_PARAM, rootPath);
        final String resourcePath = definition.getResourcePath();
        widgetslot.setAttribute(WIDGET_RESOURCE_PATH_PARAM, resourcePath);
        widgetslot.setAttribute(LABELS_PARAM, labelMap);
        if(StringUtils.isNotEmpty(widget.getId()))
        {
            widgetslot.setAttribute(Y_TESTID_PATH, YTestTools.replaceUUIDIllegalChars(widget.getId()));
        }
        Component component;
        try
        {
            final String zulView = widget.getWidgetSettings().getString(DefaultCockpitComponentDefinitionService.DEFAULT_VIEW_ZUL);
            if(StringUtils.isNotBlank(zulView) && definition.getComposedWidgetRoot() == null)
            {
                component = Executions.createComponentsDirectly(zulView, null, widgetslot, Collections.emptyMap());
            }
            else if(definition.getComposedWidgetRoot() != null)
            {
                component = Executions.createComponentsDirectly(getDocumentWithWidgetSlot(), null, widgetslot,
                                Collections.emptyMap());
            }
            else if(!definition.hasView())
            {
                component = Executions.createComponentsDirectly(getDocumentNoViewWidget(), null, widgetslot, Collections.emptyMap());
            }
            else
            {
                if(htmlBased)
                {
                    component = Executions.createComponentsDirectly(getDocumentNoViewWidget(), null, widgetslot,
                                    Collections.emptyMap());
                }
                else
                {
                    component = Executions.createComponents(viewURI, widgetslot, Collections.emptyMap());
                }
            }
            if(component == null)
            {
                throw new UiException("Widget view component was null");
            }
            if(!WIDGET.equals(component.getDefinition().getName()))
            {
                LOG.warn("root tag of '{}' should be <widget>", viewURI);
            }
            if(StringUtils.isNotEmpty(widget.getId()))
            {
                YTestTools.modifyYTestId(component, widget.getId());
            }
        }
        catch(final RuntimeException unexpected)
        {
            LOG.error("Could not create widget view", unexpected);
            widgetslot.getChildren().clear();
            component = prepareWidgetCreationExceptionInfo(unexpected, widget);
            widgetslot.appendChild(component);
        }
        component.applyProperties();
        adjustWidgetSclass(widgetInstance, definition, component);
        widgetslot.applyProperties();
        // Check if all children have been attached to the desktop
        final List<Widget> unattachedChildren = new ArrayList<>();
        for(final WidgetInstance child : getWidgetInstanceFacade().getWidgetInstances(widgetInstance))
        {
            if(widgetUtils.getRegisteredWidgetslot(child) == null && child != null && !child.getWidget().isTemplate())
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(getWidgetDefinition(child.getWidget()).getName() + " has not been attached");
                }
                if(!COCKPIT_WIDGET_CHILDREN_INVISIBLE_SLOT_ID.equals(child.getWidget().getSlotId()))
                {
                    unattachedChildren.add(child.getWidget());
                }
            }
        }
        renderUnattachedChildren(widgetslot, unattachedChildren);
        if(isRoot && startRender != null)
        {
            final Date endRender = new Date();
            if(isRenderingConsoleMonitoringEnabled())
            {
                final long timeMS = endRender.getTime() - startRender.getTime();
                final Object counter = Executions.getCurrent().getAttribute(MONITOR_WIDGET_COUNTER_ATTRIB);
                final String info = "Finished rendering widget tree (" + counter + " widgets) in " + timeMS + "ms.";
                LOG.info(info);
                if(Boolean.parseBoolean(getCockpitProperties().getProperty(COCKPITNG_WIDGETENGINE_MONITOR_CLIENT_ENABLED)))
                {
                    final Popup monitorPopup = new Popup();
                    monitorPopup.setSclass("cng-widgetengine-monitor-popup");
                    final Vlayout vlayout = new Vlayout();
                    vlayout.appendChild(new Label(info));
                    vlayout.appendChild(
                                    new Label(createPropertyString(DefaultWidgetLibUtils.COCKPITNG_WIDGETCLASSLOADER_RESOURCECACHE_ENABLED)));
                    vlayout.appendChild(
                                    new Label(createPropertyString(DefaultWidgetResourceReader.COCKPITNG_RESOURCELOADER_RESOURCECACHE_ENABLED)));
                    vlayout.appendChild(new Label(createPropertyString(CockpitUIFactory.COCKPITNG_UIFACTORY_CACHE_ENABLED)));
                    monitorPopup.appendChild(vlayout);
                    widgetslot.appendChild(monitorPopup);
                    monitorPopup.open(0, 0);
                }
            }
        }
    }


    protected DefaultWidgetInstanceManager createWidgetInstanceManager(final Widgetslot widgetslot,
                    final Map<String, Object> labelMap)
    {
        return new DefaultWidgetInstanceManager(widgetslot, this, widgetUtils, cockpitThreadContextCreator, labelMap,
                        getWidgetConfigurationContextDecoratorList(), cockpitConfigurationService, getLabelService(),
                        getNotificationService());
    }


    protected void adjustWidgetSclass(final WidgetInstance widgetInstance, final WidgetDefinition definition,
                    final Component component)
    {
        if(component instanceof HtmlBasedComponent)
        {
            UITools.modifySClass((HtmlBasedComponent)component, getWidgetBodySclass(widgetInstance), true);
            UITools.modifySClass((HtmlBasedComponent)component, getWidgetMoldSclass(widgetInstance, definition), true);
        }
    }


    protected Document getDocumentNoViewWidget()
    {
        if(documentNoView == null)
        {
            documentNoView = new Document(new Element(WIDGET));
        }
        return documentNoView;
    }


    protected Document getDocumentWithWidgetSlot()
    {
        if(documentWithWidgetSlot == null)
        {
            final Element widgetRoot = new Element(WIDGET);
            widgetRoot.setAttribute(new Attribute(WIDTH, FULL_WIDTH));
            widgetRoot.setAttribute(new Attribute(HEIGHT, FULL_WIDTH));
            final Element widgetSlotElement = new Element(WIDGETSLOT);
            widgetSlotElement.setAttribute(new Attribute(WIDTH, FULL_WIDTH));
            widgetSlotElement.setAttribute(new Attribute(HEIGHT, FULL_WIDTH));
            widgetSlotElement.setAttribute(new Attribute(SLOT_ID, WidgetService.COMPOSED_ROOT_SLOT_ID));
            widgetRoot.appendChild(widgetSlotElement);
            documentWithWidgetSlot = new Document(widgetRoot);
        }
        return documentWithWidgetSlot;
    }


    private static Component prepareWidgetCreationExceptionInfo(final RuntimeException unexpected, final Widget widget)
    {
        final Div container = new Div();
        container.appendChild(new Label(String.format("Failed to render widget [id='%s', definitionId='%s', message='%s']",
                        widget.getId(), widget.getWidgetDefinitionId(), unexpected.getMessage())));
        container.setSclass("yw-widget-rendering-error-container");
        return container;
    }


    private String createPropertyString(final String propertyKey)
    {
        return " * " + propertyKey + ": " + getCockpitProperties().getProperty(propertyKey);
    }


    public Map<String, Object> createLabelMap(final WidgetDefinition definition, final Widget widget)
    {
        final List<WidgetDefinition> hierarchyList = getWidgetsInheritanceHierarchy(definition);
        final Map<String, Object> labelMap = new HashMap<>();
        for(final WidgetDefinition widgetDefinition : hierarchyList)
        {
            final Map<String, Object> labelsForWidget = CockpitComponentDefinitionLabelLocator.getLabelMap(widgetDefinition);
            if(MapUtils.isNotEmpty(labelsForWidget))
            {
                labelMap.putAll(labelsForWidget);
            }
        }
        processLabelSettings(labelMap, widget.getWidgetSettings());
        return labelMap;
    }


    private List<WidgetDefinition> getWidgetsInheritanceHierarchy(final WidgetDefinition definition)
    {
        final List<WidgetDefinition> hierarchyList = new ArrayList<>();
        if(definition == null)
        {
            LOG.error("Cannot resolve inheritance hierarchy for null definition");
        }
        else
        {
            final String parentCode = definition.getParentCode();
            if(StringUtils.isNotBlank(parentCode))
            {
                final WidgetDefinition parent = getComponentDefinition(parentCode);
                if(parent != null)
                {
                    hierarchyList.addAll(getWidgetsInheritanceHierarchy(parent));
                }
            }
            hierarchyList.add(definition);
        }
        return hierarchyList;
    }


    private static void processLabelSettings(final Map<String, Object> labelMap, final TypedSettingsMap typedSettingsMap)
    {
        final Locale locale = Locales.getCurrent();
        final String language = locale.getLanguage();
        final String localizedKey = "labels_" + language + ".";
        final String fallbackKey = "labels.";
        final Map<String, Object> overriddenLabels = new HashMap<>();
        for(final String key : typedSettingsMap.keySet())
        {
            if(key.startsWith(fallbackKey))
            {
                final String realKey = key.substring(fallbackKey.length());
                if(StringUtils.isBlank(getLabel(realKey, overriddenLabels)))
                {
                    final String value = typedSettingsMap.getString(key);
                    setLabel(realKey, value, overriddenLabels);
                    setLabel(realKey, value, labelMap);
                    LOG.debug("Overriding label '{}' with {} ", key, value);
                }
            }
            else if(key.startsWith(localizedKey))
            {
                final String realKey = key.substring(localizedKey.length());
                final String value = typedSettingsMap.getString(key);
                setLabel(realKey, value, overriddenLabels);
                setLabel(realKey, value, labelMap);
                LOG.debug("Overriding label '{}' with value {}", key, value);
            }
        }
    }


    private static void setLabel(final String key, final String value, final Map<String, Object> labels)
    {
        if(key.contains("."))
        {
            final String[] tokens = key.split("\\.");
            if(tokens != null && tokens.length > 0)
            {
                final Object object = labels.get(tokens[0]);
                if(object instanceof Map)
                {
                    setLabel(key.substring(tokens[0].length() + 1), value, (Map<String, Object>)object);
                }
            }
        }
        else
        {
            labels.put(key, value);
        }
    }


    private static void forwardGroupWidgetSettings(final Widget widget)
    {
        if(widget.isPartOfGroup() && widget.getGroupContainer() != null)
        {
            final TypedSettingsMap widgetSettings = widget.getGroupContainer().getWidgetSettings();
            final Map<String, Map<String, Pair<Class, Object>>> groupedSettings = new HashMap<>();
            for(final Entry<String, Object> entry : widgetSettings.entrySet())
            {
                final String key = entry.getKey();
                if(key.charAt(0) == WidgetControllers.GROUP_CHILD_SETTINGS_SEPARATOR)
                {
                    final String[] split = key.substring(1).split(String.valueOf(WidgetControllers.GROUP_CHILD_SETTINGS_SEPARATOR));
                    if(split.length == 2)
                    {
                        final String widgetID = split[0];
                        final String subkey = split[1];
                        Map<String, Pair<Class, Object>> map = groupedSettings.get(widgetID);
                        if(map == null)
                        {
                            map = new HashMap<>();
                            groupedSettings.put(widgetID, map);
                        }
                        final Object typedValue = widgetSettings.getTyped(key);
                        final Class settingClass = widgetSettings.getSettingClass(key);
                        map.put(subkey, new ImmutablePair<>(settingClass, typedValue));
                    }
                }
            }
            if(!groupedSettings.isEmpty())
            {
                final Set<Widget> allChildWidgets = Sets.newHashSet(WidgetTreeUtils.getAllChildWidgetsRecursively(widget));
                allChildWidgets.add(widget);
                for(final Widget child : allChildWidgets)
                {
                    final Map<String, Pair<Class, Object>> map = groupedSettings.get(child.getOriginalID());
                    if(map != null)
                    {
                        for(final Entry<String, Pair<Class, Object>> entry : map.entrySet())
                        {
                            child.getWidgetSettings().put(entry.getKey(), entry.getValue().getRight(), entry.getValue().getLeft());
                        }
                    }
                }
            }
        }
    }


    protected void renderUnattachedChildren(final Widgetslot widgetslot, final List<Widget> unattachedChildren)
    {
        final Div invisibleContainer = new Div();
        widgetslot.setInvisibleContainer(invisibleContainer);
        invisibleContainer.setSclass("invisibleWidgetChildren");
        widgetslot.insertBefore(invisibleContainer, widgetslot.getFirstChild());
        final Widgetchildren invChldCmp = new InvisibleWidgetchildren(unattachedChildren);
        invisibleContainer.appendChild(invChldCmp);
        invChldCmp.setHeight("100%");
        invChldCmp.setType(Widgetchildren.INVISIBLE);
        invChldCmp.setSlotID(COCKPIT_WIDGET_CHILDREN_INVISIBLE_SLOT_ID);
        invChldCmp.afterCompose();
    }


    protected WidgetDefinition getWidgetDefinition(final Widget widget)
    {
        return (WidgetDefinition)componentDefinitionService.getComponentDefinitionForCode(widget.getWidgetDefinitionId());
    }


    protected <T> T getComponentDefinition(final String widgetId)
    {
        return (T)componentDefinitionService.getComponentDefinitionForCode(widgetId);
    }


    private Object createWidgetController(final Widget widget, final WidgetDefinition definition)
    {
        final String controllerClassName = StringUtils.defaultIfBlank(definition.getController(), StringUtils.EMPTY);
        GroovyClassLoader groovyLoader = null;
        try
        {
            final String groovyCtrl = widget.getWidgetSettings().getString("controller.groovy");
            if(!StringUtils.isBlank(groovyCtrl))
            {
                groovyLoader = new GroovyClassLoader(getClassLoaderForGroovy(definition));
                final Class<?> parseClass = groovyLoader.parseClass(groovyCtrl);
                return parseClass.newInstance();
            }
            else if(controllerClassName.endsWith(".groovy"))
            {
                final ClassLoader classLoader = getWidgetDefinitionService().getClassLoader(definition);
                groovyLoader = new GroovyClassLoader(getClassLoaderForGroovy(definition));
                try(final InputStream resourceAsStream = getResourceAsStream(controllerClassName, classLoader))
                {
                    if(resourceAsStream == null)
                    {
                        throw new ClassNotFoundException();
                    }
                    final Class<?> parseClass = groovyLoader.parseClass(IOUtils.toString(resourceAsStream));
                    return parseClass.newInstance();
                }
            }
            else if(StringUtils.isNotBlank(controllerClassName))
            {
                return getWidgetDefinitionService().createAutowiredComponent(definition, controllerClassName, null, false);
            }
        }
        catch(final ClassNotFoundException e)
        {
            LOG.error("Could not find controller class '" + controllerClassName + "'.", e);
        }
        catch(final Exception e)
        {
            LOG.error("Could not instantiate controller class '" + controllerClassName + "'.", e);
        }
        finally
        {
            IOUtils.closeQuietly(groovyLoader);
        }
        return null;
    }


    // TODO provide better and more performant solution
    private ClassLoader getClassLoaderForGroovy(final WidgetDefinition widgetDefinition)
    {
        final ClassLoader groovyClassLoader = this.componentDefinitionService.getClassLoader(widgetDefinition);
        if(groovyClassLoader instanceof WidgetClassLoader)
        {
            return groovyClassLoader;
        }
        final List<AbstractCockpitComponentDefinition> definitions = this.componentDefinitionService.getAllComponentDefinitions();
        for(final AbstractCockpitComponentDefinition definition : definitions)
        {
            final ClassLoader classLoader = this.componentDefinitionService.getClassLoader(definition);
            if(classLoader instanceof WidgetClassLoader)
            {
                final ClassLoader parent = classLoader.getParent();
                if(parent instanceof WidgetClassLoader)
                {
                    return parent;
                }
            }
        }
        return groovyClassLoader;
    }


    private InputStream getResourceAsStream(final String name, final ClassLoader classLoader)
    {
        if(cockpitResourceLoader != null)
        {
            InputStream resourceAsStream = null;
            if(classLoader instanceof WidgetClassLoader)
            {
                resourceAsStream = cockpitResourceLoader.getResourceAsStream(((WidgetClassLoader)classLoader).getWidgetLibInfo(),
                                name);
            }
            else
            {
                resourceAsStream = cockpitResourceLoader.getResourceAsStream(name);
            }
            if(resourceAsStream != null)
            {
                return resourceAsStream;
            }
        }
        return classLoader.getResourceAsStream(name);
    }


    protected WidgetInstance getSessionWidgetInstance(final String slotId)
    {
        return sessionWidgetInstanceRegistry.getRegisteredWidgetInstance(slotId);
    }


    protected void setSessionWidgetInstance(final WidgetInstance instance, final String slotId)
    {
        if(instance == null)
        {
            sessionWidgetInstanceRegistry.unregisterWidgetInstance(slotId);
        }
        else
        {
            sessionWidgetInstanceRegistry.registerWidgetInstance(instance, slotId);
        }
    }


    private void updateChildSelection(final WidgetInstance parent, final String slotId)
    {
        final List<WidgetInstance> children = getWidgetInstanceFacade().getWidgetInstances(parent, slotId, false);
        final int lastFocusedChildIndex = parent.getSelectedChildIndex();
        if(lastFocusedChildIndex < children.size())
        {
            final WidgetInstance widgetInstance = children.get(lastFocusedChildIndex);
            final Widgetslot wiComp = widgetUtils.getRegisteredWidgetslot(widgetInstance);
            if(wiComp != null)
            {
                final Widgetchildren parentChildrenContainer = wiComp.getParentChildrenContainer();
                if(parentChildrenContainer != null)
                {
                    parentChildrenContainer.selectChildWidget(lastFocusedChildIndex);
                }
            }
        }
    }


    @Override
    public void sendOutput(final Widgetslot widgetslot, final String outputID)
    {
        sendOutput(widgetslot, outputID, null);
    }


    @Override
    public void sendOutput(final Widgetslot widgetslot, final String outputID, final Object data)
    {
        sendOutput(widgetslot, outputID, data, false);
    }


    @Override
    public void sendOutput(final Widgetslot widgetslot, final String outputId, final Object data, final boolean ignoreSockets)
    {
        Objects.requireNonNull(widgetslot, "Widgetslot reference might not be null");
        final WidgetInstance currentWidgetInstance = widgetslot.getWidgetInstance();
        final Widget currentWidget = currentWidgetInstance.getWidget();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Send to output: " + outputId + " - " + data);
        }
        if(!ignoreSockets)
        {
            final WidgetSocket outputWidgetSocket = WidgetSocketUtils.getOutputSocketWithId(currentWidget,
                            getWidgetDefinition(currentWidget), outputId);
            final boolean socketIdMatch = outputWidgetSocket != null;
            if(!socketIdMatch)
            {
                final Widget groupContainer = currentWidget.getGroupContainer();
                if(groupContainer == null || !currentWidget.equals(groupContainer.getComposedRootInstance()))
                {
                    LOG.error("No output socket with id '{}' found for widget {}", outputId, currentWidget.getWidgetDefinitionId());
                }
                return;
            }
            isSocketDataTypeValid(data, currentWidget, outputWidgetSocket);
        }
        final List<WidgetConnection> connectors = getWidgetService().getWidgetConnectionsForOutputWidgetAndSocketID(currentWidget,
                        outputId);
        for(final WidgetConnection cockpitWidgetConnector : connectors)
        {
            final String inputId = cockpitWidgetConnector.getInputId();
            final Widget target = cockpitWidgetConnector.getTarget();
            if(LOG.isDebugEnabled())
            {
                LOG.debug(
                                "Found connected widget, send event from widget " + currentWidgetInstance.getId() + " to " + target.getId());
            }
            sendEventToTargetWidget(inputId, target, data, outputId, currentWidgetInstance);
            // composed widget handling
            if(target.getComposedRootInstance() != null)
            {
                sendEventToTargetWidget(inputId, target.getComposedRootInstance(), data, outputId, currentWidgetInstance);
                final String fwdStr = getWidgetDefinition(target).getForwardMap().get(inputId);
                if(StringUtils.isNotBlank(fwdStr))
                {
                    final List<WidgetInstance> widgetInstances = target.getComposedRootInstance().getWidgetInstances();
                    if(widgetInstances.isEmpty())
                    {
                        LOG.error("Could not get instance for composed widget " + target.getComposedRootInstance());
                    }
                    else
                    {
                        final List<WidgetInstance> targetWidgetInstances = Lists.newArrayList(widgetInstances);
                        final boolean isStubWidget = getWidgetDefinition(currentWidget).isStubWidget();
                        if(isStubWidget)
                        {
                            for(final WidgetInstance singleWidgetInstance : widgetInstances)
                            {
                                if(singleWidgetInstance.getTemplateRoot() != null
                                                && currentWidgetInstance.equals(singleWidgetInstance.getTemplateRoot().getCreator()))
                                {
                                    targetWidgetInstances.clear();
                                    targetWidgetInstances.add(singleWidgetInstance);
                                    break;
                                }
                            }
                        }
                        for(final WidgetInstance singleWidgetInstance : targetWidgetInstances)
                        {
                            final Widgetslot registeredWidgetslot = widgetUtils.getRegisteredWidgetslot(singleWidgetInstance);
                            if(registeredWidgetslot != null)
                            {
                                sendOutput(registeredWidgetslot, fwdStr, data, true);
                            }
                        }
                    }
                }
            }
            else if(target.getGroupContainer() != null)
            {
                if(target.getGroupContainer().isTemplate())
                {
                    final WidgetInstance groupTemplateInstance = lookupGroupTemplateInstance(target.getGroupContainer(),
                                    currentWidgetInstance);
                    if(groupTemplateInstance != null)
                    {
                        final String forwardOutput = getWidgetDefinition(target.getGroupContainer()).getForwardMap().get(inputId);
                        if(StringUtils.isNotBlank(forwardOutput))
                        {
                            final Widgetslot registeredWidgetslot = widgetUtils.getRegisteredWidgetslot(groupTemplateInstance);
                            if(registeredWidgetslot != null)
                            {
                                sendOutput(registeredWidgetslot, forwardOutput, data);
                            }
                        }
                    }
                }
                sendEventToTargetWidget(inputId, target.getGroupContainer(), data, outputId, currentWidgetInstance);
            }
        }
        if(currentWidget.isTemplate() || (currentWidget.getParent() == null && currentWidget.getGroupContainer() != null
                        && currentWidget.getGroupContainer().isTemplate()))
        {
            WidgetInstance templateInstance = currentWidgetInstance;
            if(!currentWidget.isTemplate()
                            && (currentWidget.getGroupContainer() == null || !currentWidget.getGroupContainer().isTemplate()))
            {
                templateInstance = currentWidgetInstance.getParent();
            }
            if(templateInstance != null)
            {
                Events.postEvent(LOW_EVENT_PRIORITY, new Event("onSocketOutput", widgetslot, outputId));
            }
        }
    }


    protected boolean isSocketDataTypeValid(final Object object, final Widget currentWidget, final WidgetSocket outputWidgetSocket)
    {
        String outputSocketType = outputWidgetSocket.getDataType();
        final String resolvedInputGenericType = socketConnectionService.resolveGenericType(outputWidgetSocket, currentWidget);
        if(resolvedInputGenericType != null)
        {
            outputSocketType = resolvedInputGenericType;
        }
        if(socketConnectionService.hasGenericType(outputWidgetSocket) && resolvedInputGenericType == null)
        {
            outputSocketType = Object.class.getName();
        }
        if(!isSocketTypeMatching(outputSocketType, outputWidgetSocket.getDataMultiplicity(), object))
        {
            if(!getSocketConnectionService().canResolveGenericType(outputWidgetSocket, currentWidget))
            {
                final String sentObjectDataTypeMsg = (object == null ? "null" : object.getClass().getName());
                final String logMessage = String.format(
                                "Generic type %s of output socket '%s' of widget '%s' cannot be resolved. Please make sure that widget defines %s setting - if not, set it to %s.",
                                outputSocketType, outputWidgetSocket.getId(), currentWidget.getId(),
                                SocketConnectionService.SOCKET_DATA_TYPE_PREFIX + "*", sentObjectDataTypeMsg);
                LOG.warn(logMessage);
            }
            else
            {
                final String sentObjectDataTypeMsg = (object == null ? "null" : object.getClass().getName());
                final String outputSocketTypeMsg = getOutputSocketTypeMessage(outputWidgetSocket, outputSocketType);
                final String logMessage = String.format(
                                "Widget '%s' output socket '%s' sent object of type %s which is NOT compatible with defined socket type %s",
                                currentWidget.getId(), outputWidgetSocket.getId(), sentObjectDataTypeMsg, outputSocketTypeMsg);
                LOG.warn(logMessage);
            }
            return false;
        }
        return true;
    }


    protected boolean isSocketTypeMatching(final String socketDataType, final WidgetSocket.Multiplicity socketMultiplicity,
                    final Object object)
    {
        if(object == null)
        {
            return true;
        }
        if(socketDataType == null)
        {
            return false;
        }
        if(socketMultiplicity != null)
        {
            if(object instanceof Collection)
            {
                return socketConnectionService.isCollectionTypeAssignable(socketMultiplicity, resolveObjectMultiplicity(object));
            }
            else
            {
                return false;
            }
        }
        return cockpitTypeUtils.isAssignableFrom(socketDataType, object.getClass().getName());
    }


    protected WidgetSocket.Multiplicity resolveObjectMultiplicity(final Object object)
    {
        if(object instanceof List)
        {
            return WidgetSocket.Multiplicity.LIST;
        }
        else if(object instanceof Set)
        {
            return WidgetSocket.Multiplicity.SET;
        }
        else if(object instanceof Collection)
        {
            return WidgetSocket.Multiplicity.COLLECTION;
        }
        return null;
    }


    protected String getOutputSocketTypeMessage(final WidgetSocket outputWidgetSocket, final String outputSocketType)
    {
        if(outputSocketType == null)
        {
            return "null";
        }
        else if(outputWidgetSocket.getDataMultiplicity() == null)
        {
            return outputSocketType;
        }
        return outputWidgetSocket.getDataMultiplicity() + " of " + outputSocketType;
    }


    protected WidgetInstance lookupGroupTemplateInstance(final Widget groupContainerWidget,
                    final WidgetInstance currentWidgetInstance)
    {
        if(currentWidgetInstance != null && groupContainerWidget.isTemplate())
        {
            if(currentWidgetInstance.getTemplateRoot() != null
                            && groupContainerWidget.equals(currentWidgetInstance.getTemplateRoot().getWidget())
                            && currentWidgetInstance.getTemplateRoot().getWidget().isTemplate())
            {
                return currentWidgetInstance.getTemplateRoot();
            }
            else if(groupContainerWidget.equals(currentWidgetInstance.getWidget())
                            && currentWidgetInstance.getWidget().isTemplate())
            {
                return currentWidgetInstance;
            }
            else
            {
                return lookupGroupTemplateInstance(groupContainerWidget, currentWidgetInstance.getParent());
            }
        }
        return null;
    }


    protected void sendEventToTargetWidget(final String eventName, final Widget target, final Object data,
                    final String sourceSocketId, final WidgetInstance source)
    {
        if(target == null)
        {
            LOG.warn("Target widget removed, connector invalid.");
            return;
        }
        if(!checkVisibility(target))
        {
            return;
        }
        if(LOG.isDebugEnabled())
        {
            final WidgetDefinition widgetDefinition = getWidgetDefinition(target);
            final String widgetName = widgetDefinition == null ? "" : widgetDefinition.getName();
            LOG.debug("Received socket event on '" + widgetName + " (id:" + target.getId() + ")': " + eventName + ", data: " + data);
        }
        List<WidgetInstance> widgetInstances = new ArrayList<>();
        if(target.isTemplate())
        {
            final RuleEngineResult result = getRulesEngine().forwardSocketEvent(target, eventName, source, sourceSocketId);
            if(result.getInstances().isEmpty())
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("No template instance resolvable for configured rules, ignoring event.");
                }
                return;
            }
            else
            {
                widgetInstances = result.getInstances();
                final WidgetInstance first = result.getInstances().iterator().next();
                final WidgetInstance parent = result.getParent();
                final Widget widget = first.getWidget();
                final String slotId = widget.getSlotId();
                final Widgetslot parentContainer = widgetUtils.getRegisteredWidgetslot(parent);
                if(parentContainer != null)
                {
                    if(result.isUpdateNeeded())
                    {
                        parentContainer.updateChildren(slotId);
                        if(result.isWidgetAboutToShow())
                        {
                            notificationStack.onNewTemplateDisplayed(first);
                        }
                        if(result.isWidgetAboutToClose())
                        {
                            notificationStack.onTemplateClosed(first);
                        }
                    }
                    else
                    {
                        updateChildSelection(parentContainer.getWidgetInstance(), slotId);
                    }
                }
            }
        }
        else
        {
            final WidgetInstance singleInstance = resolveSingleTargetInstance(source, target);
            if(singleInstance == null)
            {
                final WidgetDefinition widgetDefinition = getWidgetDefinition(target);
                if(widgetDefinition == null)
                {
                    LOG.warn(String.format(
                                    "Could not resolve widget definition for target: [%s]. Are you referring to a widget from external module?",
                                    Objects.toString(target)));
                    return;
                }
                if(widgetDefinition.isStubWidget())
                {
                    return;
                }
                for(final WidgetInstance potentialWidgetInstance : target.getWidgetInstances())
                {
                    if(potentialWidgetInstance.getCreator() instanceof ComponentWidgetAdapterAware)
                    {
                        widgetInstances.add(potentialWidgetInstance);
                    }
                }
                if(CollectionUtils.isEmpty(widgetInstances))
                {
                    widgetInstances.addAll(target.getWidgetInstances());
                }
            }
            else
            {
                widgetInstances.add(singleInstance);
            }
        }
        sendEventToTargetWidgetInternal(eventName, data, sourceSocketId, source, widgetInstances);
    }


    protected void sendEventToTargetWidgetInternal(final String eventName, final Object data, final String sourceSocketId,
                    final WidgetInstance source, final List<WidgetInstance> widgetInstances)
    {
        for(final WidgetInstance instance : widgetInstances)
        {
            final Widgetslot widgetContainer = widgetUtils.getRegisteredWidgetslot(instance);
            if(instance.getCreator() instanceof ComponentWidgetAdapterAware)
            {
                final SocketEvent event = new SocketEvent(eventName, null, data, source == null ? null : source.getId(),
                                sourceSocketId);
                ((ComponentWidgetAdapterAware)instance.getCreator()).handleSocketInputEvent(event);
                continue;
            }
            if(widgetContainer == null)
            {
                final EventListener<Event> listener = event -> {
                    final Object attribute = event.getTarget().getAttribute(CP_FWD_EL);
                    if(attribute instanceof EventListener)
                    {
                        getDesktop().removeAttribute(CP_FWD_EL);
                        event.getTarget().removeEventListener(ON_CP_FWD_EL, (EventListener<?>)attribute);
                        final Widgetslot widgetslot = widgetUtils.getRegisteredWidgetslot(instance);
                        if(widgetslot == null)
                        {
                            LOG.error("No component for widget id '" + instance.getId() + "' found.");
                        }
                        else
                        {
                            sendSocketEvent(widgetslot, eventName, data, sourceSocketId, source);
                        }
                    }
                };
                final Desktop desktop = getDesktop();
                if(desktop != null)
                {
                    desktop.setAttribute(CP_FWD_EL, listener);
                    final Component cockpitRoot = widgetUtils.getCockpitRoot();
                    cockpitRoot.addEventListener(ON_CP_FWD_EL, listener);
                    Events.echoEvent(ON_CP_FWD_EL, cockpitRoot, null);
                }
                else
                {
                    LOG.debug("Could not register {} listener and echo event on null Desktop, skipping", CP_FWD_EL);
                }
            }
            else
            {
                sendSocketEvent(widgetContainer, eventName, data, sourceSocketId, source);
            }
        }
    }


    /**
     * Checks, if an event should be routed to one single instance of the target widget.
     *
     * @param source
     *           The {@link WidgetInstance} which has sent the event.
     * @param target
     *           The target {@link Widget}.
     * @return The target {@link WidgetInstance} that should receive the event or null, if none could be resolved.
     */
    protected WidgetInstance resolveSingleTargetInstance(final WidgetInstance source, final Widget target)
    {
        WidgetInstance ret = null;
        if(getWidgetDefinition(source.getWidget()).isStubWidget())
        {
            for(final WidgetInstance widgetInstance : target.getWidgetInstances())
            {
                if(widgetInstance.getTemplateRoot() != null && source.equals(widgetInstance.getTemplateRoot().getCreator()))
                {
                    ret = widgetInstance;
                    break;
                }
            }
        }
        else if(source.getTemplateRoot() != null)
        {
            if(source.getCreator() instanceof WidgetInstance && target.equals(((WidgetInstance)source.getCreator()).getWidget()))
            {
                final List<WidgetInstance> targetInstances = new ArrayList<>(target.getWidgetInstances());
                if(targetInstances.contains(source.getCreator()))
                {
                    final WidgetInstance widgetInstance = (WidgetInstance)source.getCreator();
                    final int instancePos = targetInstances.indexOf(widgetInstance);
                    return targetInstances.get(instancePos);
                }
            }
            for(final WidgetInstance widgetInstance : target.getWidgetInstances())
            {
                if(source.getTemplateRoot().equals(widgetInstance.getTemplateRoot()))
                {
                    ret = widgetInstance;
                    break;
                }
            }
        }
        return ret;
    }


    protected void sendSocketEvent(final Widgetslot widgetContainer, final String eventName, final Object data,
                    final String sourceSocketId, final WidgetInstance sourceWidgetInstance)
    {
        if(widgetContainer != null && widgetContainer.getDesktop() != null && widgetContainer.getDesktop().equals(getDesktop()))
        {
            if(SOCLOG.isDebugEnabled())
            {
                logSocketEvent(widgetContainer, eventName, data, sourceSocketId, sourceWidgetInstance);
            }
            // notify parent widget about the socket
            // XXX refactor or remove
            final WidgetInstance parentInstance = widgetContainer.getWidgetInstance().getParent();
            if(parentInstance != null)
            {
                final Widgetslot parentContainer = getWidgetUtils().getRegisteredWidgetslot(parentInstance);
                if(parentContainer != null)
                {
                    final WidgetController parentController = (WidgetController)parentContainer
                                    .getAttribute(Widgetslot.ATTRIBUTE_WIDGET_CONTROLLER);
                    if(parentController instanceof DefaultFlowWidgetController)
                    {
                        ((DefaultFlowWidgetController)parentController).onSocketEvent(widgetContainer.getWidgetInstance(), eventName,
                                        data, sourceWidgetInstance, sourceSocketId);
                    }
                }
            }
            final WidgetModel model = (WidgetModel)widgetContainer.getAttribute("widgetModel");
            if(model != null)
            {
                model.setValue("$_" + eventName, data);
            }
            final String eventStr = "onSocketInput_" + eventName;
            final List<Component> children = new ArrayList<>(widgetContainer.getChildren());
            if(CollectionUtils.isNotEmpty(children))
            {
                for(final Component component : children)
                {
                    final Event event = new SocketEvent(eventStr, component, data,
                                    sourceWidgetInstance == null ? null : sourceWidgetInstance.getId(), sourceSocketId);
                    Events.sendEvent(event);
                    Events.sendEvent(new ForwardEvent("onSocketInput", component, event, data));
                }
            }
            else
            {
                widgetContainer.setAttribute("lastEventData", data);
                widgetContainer.updateView();
            }
            final WidgetDefinition widgetDefinition = getWidgetDefinition(widgetContainer.getWidgetInstance().getWidget());
            final Widget widget = widgetContainer.getWidgetInstance().getWidget();
            final String forward = widgetDefinition.getForwardMap().get(eventName);
            if(StringUtils.isNotBlank(forward))
            {
                sendOutput(widgetContainer, forward, data);
            }
            else if(widget.getGroupContainer() != null)
            {
                final String composedForward = getWidgetDefinition(widget.getGroupContainer()).getForwardMap().get(eventName);
                if(StringUtils.isNotBlank(composedForward))
                {
                    sendOutput(widgetContainer, composedForward, data);
                }
            }
        }
    }


    protected void logSocketEvent(final Widgetslot widgetContainer, final String targetSocketId, final Object data,
                    final String sourceSocketId, final WidgetInstance sourceWidgetInstance)
    {
        final String sourceWidgetId = sourceWidgetInstance.getId();
        final String sourceWidgetControllerClass = widgetContainer.getWidgetDefinition(sourceWidgetInstance.getWidget())
                        .getController();
        final String sourceWidgetDefinitionId = sourceWidgetInstance.getWidget().getWidgetDefinitionId();
        final String targetWidgetId = widgetContainer.getWidgetInstance().getId();
        final String targetWidgetControllerClass = widgetContainer
                        .getWidgetDefinition(widgetContainer.getWidgetInstance().getWidget()).getController();
        final String targetWidgetDefinitionId = widgetContainer.getWidgetInstance().getWidget().getWidgetDefinitionId();
        SOCLOG.debug("");
        SOCLOG.debug("sourceSocket- {} (widget: {}) (widgetDef: {}) (controller: {})", sourceSocketId, sourceWidgetId,
                        sourceWidgetDefinitionId, sourceWidgetControllerClass);
        SOCLOG.debug("targetSocket- {} (widget: {}) (widgetDef: {}) (controller: {})", targetSocketId, targetWidgetId,
                        targetWidgetDefinitionId, targetWidgetControllerClass);
        SOCLOG.debug("");
    }


    protected Desktop getDesktop()
    {
        final Execution execution = Executions.getCurrent();
        return execution == null ? null : execution.getDesktop();
    }


    @Override
    public void createWidgetView(final Widgetchildren widgetChildrenComponent)
    {
        createWidgetView(widgetChildrenComponent, Collections.<String, Object>emptyMap());
    }


    @Override
    public void createWidgetView(final Widgetchildren widgetChildrenComponent, final Map<String, Object> ctx)
    {
        String type = widgetChildrenComponent.getType();
        if(type == null)
        {
            type = Widgetchildren.LIST;
        }
        List<WidgetInstance> children = getWidgetInstanceFacade()
                        .getWidgetInstances(widgetChildrenComponent.getParentWidgetInstance(), widgetChildrenComponent.getSlotID(), false);
        if(widgetChildrenComponent instanceof InvisibleWidgetchildren)
        {
            final List<Widget> additionalChildren = ((InvisibleWidgetchildren)widgetChildrenComponent).getAdditionalChildren();
            if(CollectionUtils.isNotEmpty(additionalChildren))
            {
                children = new ArrayList<>(children);
                for(final Widget widget : additionalChildren)
                {
                    children.addAll(widget.getWidgetInstances());
                }
            }
        }
        final WidgetChildrenContainerRenderer chldCntRenderer = getChildrenContainerRenderer().get(type);
        if(chldCntRenderer == null)
        {
            LOG.error("No renderer found for type '{}'", type);
        }
        else
        {
            chldCntRenderer.render(widgetChildrenComponent, children, ctx);
        }
    }


    public Map<String, WidgetChildrenContainerRenderer> getChildrenContainerRenderer()
    {
        return childrenContainerRenderer == null ? Collections.<String, WidgetChildrenContainerRenderer>emptyMap()
                        : childrenContainerRenderer;
    }


    public void setChildrenContainerRenderer(final Map<String, WidgetChildrenContainerRenderer> childrenContainerRenderer)
    {
        this.childrenContainerRenderer = childrenContainerRenderer;
    }


    @Override
    public boolean isIndependentView(final WidgetDefinition widget, final String url)
    {
        final String viewURI = widget.getViewURI();
        return viewURI != null && viewURI.equals(url)
                        && (StringUtils.endsWith(viewURI, ".html") || StringUtils.contains(StringUtils.deleteWhitespace(viewURI), ".html@"));
    }


    public WidgetService getWidgetService()
    {
        return widgetService;
    }


    @Required
    public void setWidgetService(final WidgetService widgetService)
    {
        this.widgetService = widgetService;
    }


    protected CockpitComponentDefinitionService getWidgetDefinitionService()
    {
        return componentDefinitionService;
    }


    @Required
    public void setComponentDefinitionService(final CockpitComponentDefinitionService componentDefinitionService)
    {
        this.componentDefinitionService = componentDefinitionService;
    }


    protected WidgetUtils getWidgetUtils()
    {
        return widgetUtils;
    }


    @Required
    public void setWidgetUtils(final WidgetUtils widgetUtils)
    {
        this.widgetUtils = widgetUtils;
    }


    protected WidgetTemplateRulesEngine getRulesEngine()
    {
        return rulesEngine;
    }


    @Required
    public void setRulesEngine(final WidgetTemplateRulesEngine rulesEngine)
    {
        this.rulesEngine = rulesEngine;
    }


    protected WidgetPersistenceService getWidgetPersistenceService()
    {
        return widgetPersistenceService;
    }


    @Required
    public void setWidgetPersistenceService(final WidgetPersistenceService widgetPersistenceService)
    {
        this.widgetPersistenceService = widgetPersistenceService;
    }


    @Required
    public void setSessionWidgetInstanceRegistry(final SessionWidgetInstanceRegistry sessionWidgetInstanceRegistry)
    {
        this.sessionWidgetInstanceRegistry = sessionWidgetInstanceRegistry;
    }


    protected WidgetInstanceFacade getWidgetInstanceFacade()
    {
        return widgetInstanceFacade;
    }


    @Required
    public void setWidgetInstanceFacade(final WidgetInstanceFacade widgetInstanceFacade)
    {
        this.widgetInstanceFacade = widgetInstanceFacade;
    }


    protected CockpitThreadContextCreator getCockpitThreadContextCreator()
    {
        return cockpitThreadContextCreator;
    }


    public void setCockpitThreadContextCreator(final CockpitThreadContextCreator cockpitThreadContextCreator)
    {
        this.cockpitThreadContextCreator = cockpitThreadContextCreator;
    }


    protected WidgetLibUtils getWidgetLibUtils()
    {
        return widgetLibUtils;
    }


    @Required
    public void setWidgetLibUtils(final WidgetLibUtils widgetLibUtils)
    {
        this.widgetLibUtils = widgetLibUtils;
    }


    @Required
    public void setModelValueHandlerFactory(final ModelValueHandlerFactory modelValueHandlerFactory)
    {
        this.modelValueHandlerFactory = modelValueHandlerFactory;
    }


    public List<WidgetConfigurationContextDecorator> getWidgetConfigurationContextDecoratorList()
    {
        return widgetConfigurationContextDecoratorList;
    }


    public void setWidgetConfigurationContextDecoratorList(final List<WidgetConfigurationContextDecorator> contextDecoratorList)
    {
        this.widgetConfigurationContextDecoratorList = contextDecoratorList;
    }


    public CockpitConfigurationService getCockpitConfigurationService()
    {
        return cockpitConfigurationService;
    }


    @Required
    public void setCockpitConfigurationService(final CockpitConfigurationService cockpitConfigurationService)
    {
        this.cockpitConfigurationService = cockpitConfigurationService;
    }


    protected WidgetAuthorizationService getWidgetAuthorizationService()
    {
        return widgetAuthorizationService;
    }


    @Required
    public void setWidgetAuthorizationService(final WidgetAuthorizationService widgetAuthorizationService)
    {
        this.widgetAuthorizationService = widgetAuthorizationService;
    }


    protected CockpitProperties getCockpitProperties()
    {
        return cockpitProperties;
    }


    @Required
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }


    public void setCockpitResourceLoader(final CockpitResourceLoader cockpitResourceLoader)
    {
        this.cockpitResourceLoader = cockpitResourceLoader;
    }


    public CockpitTypeUtils getCockpitTypeUtils()
    {
        return cockpitTypeUtils;
    }


    @Required
    public void setCockpitTypeUtils(final CockpitTypeUtils cockpitTypeUtils)
    {
        this.cockpitTypeUtils = cockpitTypeUtils;
    }


    public SocketConnectionService getSocketConnectionService()
    {
        return socketConnectionService;
    }


    @Required
    public void setSocketConnectionService(final SocketConnectionService socketConnectionService)
    {
        this.socketConnectionService = socketConnectionService;
    }


    public LabelService getLabelService()
    {
        return labelService;
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    public NotificationService getNotificationService()
    {
        return notificationService;
    }


    @Required
    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }
}
