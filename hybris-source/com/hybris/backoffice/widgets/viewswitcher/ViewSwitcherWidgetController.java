/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.viewswitcher;

import com.hybris.backoffice.widgets.viewswitcher.permissions.ViewSwitcherUtils;
import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.components.Widgetchildren;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.expression.ExpressionResolver;
import com.hybris.cockpitng.core.expression.ExpressionResolverFactory;
import com.hybris.cockpitng.core.security.WidgetAuthorizationService;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.ui.WidgetInstanceFacade;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.WidgetControllers;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;

public class ViewSwitcherWidgetController extends DefaultWidgetController
{
    public static final String SOCKET_OUTPUT_SELECTED_VIEWS = "selectedViews";
    public static final String SOCKET_OUTPUT_REQUESTED_VIEWS = "requestedViews";
    public static final String SOCKET_OUTPUT_VIEWS_SWITCHED = "viewsSwitched";
    public static final String SOCKET_INPUT_SELECT_VIEW = "selectViews";
    public static final String EVENT_NAME = "onAuthorityGroupChangeEvent";
    public static final String MODEL_VIEWS = "modelViewsIds";
    public static final String MODEL_DESELECTED_VIEWS = "modelDeselectedViewsIds";
    protected static final String MODEL_PARENT = "parent";
    protected static final String WIDGETS_SLOT_VIEWS = "views";
    protected static final String SETTING_CONFIG_CONTEXT = "viewSwitcherConfigCtx";
    protected static final String DEFAULT_CONFIG_CONTEXT = "view-switcher";
    protected static final String SETTING_DEFAULT_VIEW = "defaultView";
    protected static final String SETTING_INITIAL_VIEW = "initialView";
    protected static final String SETTING_AUTO_CLOSE = "autoCloseInactive";
    protected static final String SETTING_EVALUATE_EXPRESSION_PATTERN = "%s_eval";
    private static final Pattern PATTERN_VIEWS = Pattern.compile(",");
    @WireVariable
    private transient WidgetAuthorizationService widgetAuthorizationService;
    @WireVariable
    private transient WidgetInstanceFacade widgetInstanceFacade;
    @WireVariable("expressionResolverFactory")
    private transient ExpressionResolverFactory resolverFactory;
    @WireVariable
    private transient ViewSwitcherUtils viewSwitcherUtils;
    @Wire
    private Widgetchildren viewsContainer;
    private transient ExpressionResolver resolver;


    @Override
    public void initialize(final Component comp)
    {
        setValue(MODEL_PARENT, comp);
        initializeVirtualSockets(comp);
        initialize();
    }


    protected void initializeVirtualSockets(final Component component)
    {
        final Widget widget = getWidgetslot().getWidgetInstance().getWidget();
        widget.getVirtualInputs().forEach(socket -> {
            final String socketId = socket.getId();
            final EventListener listener = event -> selectViews(socketId, event.getData());
            WidgetControllers.addWidgetSocketListeners(component, socketId, listener);
        });
    }


    protected void initialize()
    {
        resolver = resolverFactory.createResolver();
        final WidgetInstanceManager manager = getWidgetInstanceManager();
        final String initialView = manager.getWidgetSettings().getString(SETTING_INITIAL_VIEW);
        final Collection<String> restoredViews = manager.getModel().getValue(MODEL_VIEWS, Collection.class);
        UITools.postponeExecution(getValue(MODEL_PARENT, Component.class), () -> {
            initializeViewSelection(initialView, restoredViews);
            getViewsContainer().addEventListener(Events.ON_FOCUS, createOnFocusEventListener());
        });
    }


    public void initializeViewSelection(final String initialView, final Collection<String> restoredViews)
    {
        final boolean hasInitialView = StringUtils.isNotBlank(initialView);
        final boolean hasViewsToRestore = CollectionUtils.isNotEmpty(restoredViews);
        if(hasViewsToRestore)
        {
            selectViews(restoredViews);
        }
        else if(hasInitialView)
        {
            selectViews(initialView);
        }
        else
        {
            selectDefaultView();
        }
    }


    protected String getConfigurationContextCode()
    {
        return StringUtils.defaultIfBlank(getWidgetSettings().getString(SETTING_CONFIG_CONTEXT), DEFAULT_CONFIG_CONTEXT);
    }


    @GlobalCockpitEvent(eventName = EVENT_NAME, scope = CockpitEvent.SESSION)
    public void reset(final CockpitEvent event)
    {
        initialize();
    }


    protected String getViewsSlotId()
    {
        return WIDGETS_SLOT_VIEWS;
    }


    protected Collection<String> selectViewsImmediately(final String viewId)
    {
        return selectViewsImmediately(extractViews(viewId));
    }


    protected Collection<String> selectViewsImmediately(final Collection<String> views)
    {
        final List<WidgetInstance> widgetInstances = getPossibleWidgetInstances();
        final List<WidgetInstance> selectedWidgets = getSelectedViewsWidgets(views, widgetInstances);
        if(selectedWidgets.size() < views.size())
        {
            addSelectedViews(views, widgetInstances, selectedWidgets);
        }
        if(CollectionUtils.isNotEmpty(selectedWidgets))
        {
            removeSelectedViews(views, widgetInstances);
            setSelectedViews(widgetInstances, selectedWidgets);
            getViewsContainer().updateChildren();
        }
        return selectedWidgets.stream().map(WidgetInstance::getId).collect(Collectors.toSet());
    }


    protected List<WidgetInstance> getSelectedViewsWidgets(final Collection<String> views, final List<WidgetInstance> instances)
    {
        final Predicate<WidgetInstance> containsInstanceOfWidget = instance -> views.contains(instance.getWidget().getId());
        return instances.stream().filter(containsInstanceOfWidget).collect(Collectors.toList());
    }


    protected void addSelectedViews(final Collection<String> views, final List<WidgetInstance> instances,
                    final List<WidgetInstance> selected)
    {
        final Set<String> availableWidgetIds = instances.stream().map(WidgetInstance::getId).collect(Collectors.toSet());
        getPossibleWidgets().stream().filter(widget -> views.contains(widget.getId()))
                        .filter(widget -> !availableWidgetIds.contains(widget.getId()))
                        .map(widget -> getWidgetInstanceFacade().createWidgetInstance(widget, getWidgetslot().getWidgetInstance()))
                        .forEach(selected::add);
    }


    protected void removeSelectedViews(final Collection<String> views, final List<WidgetInstance> instances)
    {
        if(getWidgetSettings().getBoolean(SETTING_AUTO_CLOSE))
        {
            instances.stream().filter(instance -> instance.getWidget().isTemplate())
                            .filter(instance -> !views.contains(instance.getWidget().getId()))
                            .forEach(getWidgetInstanceFacade()::removeWidgetInstance);
        }
    }


    protected void setSelectedViews(final List<WidgetInstance> instances, final List<WidgetInstance> selected)
    {
        selected.forEach(widgetInstance -> {
            final int childIndexToSelect = instances.indexOf(widgetInstance);
            widgetInstance.getWidget().setLastFocusedTemplateInstance(widgetInstance);
            getWidgetslot().getWidgetInstance().setSelectedChildIndex(childIndexToSelect);
        });
    }


    protected void notifyViewsRequested(final Collection<String> viewsId)
    {
        sendOutput(SOCKET_OUTPUT_REQUESTED_VIEWS, viewsId);
    }


    protected void notifyViewsSelected(final Collection<String> viewsId)
    {
        sendOutput(SOCKET_OUTPUT_SELECTED_VIEWS, viewsId);
    }


    protected void notifyViewsSwitched(final Collection<String> requested, final Collection<String> selected,
                    final Collection<String> deselected)
    {
        sendOutput(SOCKET_OUTPUT_VIEWS_SWITCHED, new ViewsSwitchedData(requested, selected, deselected));
    }


    protected void notifyViewsFocused(final String viewId)
    {
        notifyViewsFocused(extractViews(viewId));
    }


    protected void notifyViewsFocused(final Collection<String> viewsId)
    {
        Events.sendEvent(Events.ON_FOCUS, getViewsContainer(), viewsId);
    }


    @SocketEvent(socketId = SOCKET_INPUT_SELECT_VIEW)
    public void selectViews(final Object views)
    {
        selectViews(SOCKET_INPUT_SELECT_VIEW, views);
    }


    protected void selectViews(final String socket, final Object views)
    {
        final Collection<String> viewsIds = evaluateViews(socket, views);
        notifyViewsRequested(viewsIds);
        selectViews(viewsIds);
    }


    protected void selectViews(final Collection<String> viewsIds)
    {
        final Collection<String> selectedViews = selectViewsImmediately(viewsIds);
        if(CollectionUtils.isEmpty(selectedViews))
        {
            selectDefaultView();
        }
        else
        {
            final Collection<String> deselectedViews = updateSelectedViewsInModel(selectedViews);
            notifyViewsSwitched(viewsIds, selectedViews, deselectedViews);
            notifyViewsFocused(selectedViews);
        }
    }


    /**
     * Updates selected views in model
     *
     * @param newSelection
     *           list of selected view ids.
     * @return deselected views if some were selected.
     */
    protected Collection<String> updateSelectedViewsInModel(final Collection<String> newSelection)
    {
        final Collection<String> currentSelection = getModel().getValue(MODEL_VIEWS, Collection.class);
        final boolean viewsChange = currentSelection == null || !CollectionUtils.isEqualCollection(newSelection, currentSelection);
        if(viewsChange)
        {
            getModel().setValue(MODEL_VIEWS, newSelection);
            getModel().setValue(MODEL_DESELECTED_VIEWS, currentSelection);
            return currentSelection;
        }
        else
        {
            final Collection<String> loadedDeselection = getModel().getValue(MODEL_DESELECTED_VIEWS, Collection.class);
            return loadedDeselection != null ? loadedDeselection : Collections.emptyList();
        }
    }


    protected String getDefaultView()
    {
        return getWidgetSettings().getString(SETTING_DEFAULT_VIEW);
    }


    public void selectDefaultView()
    {
        if(getDefaultView() != null)
        {
            final Collection<String> selectedViews = selectViewsImmediately(extractViews(getDefaultView()));
            if(CollectionUtils.isNotEmpty(selectedViews))
            {
                final Collection<String> deselectedViews = updateSelectedViewsInModel(selectedViews);
                notifyViewsSwitched(Collections.emptyList(), selectedViews, deselectedViews);
                notifyViewsFocused(getDefaultView());
            }
        }
    }


    protected Collection<String> extractViews(final String setting)
    {
        return setting != null ? Arrays.asList(PATTERN_VIEWS.split(setting)) : Collections.emptyList();
    }


    protected Collection<String> evaluateViews(final String socket, final Object views)
    {
        if(views instanceof Collection)
        {
            final Stream<?> stream = ((Collection<?>)views).stream();
            final Stream<Collection<String>> mappedStream = stream.map(view -> evaluateView(socket, view));
            return mappedStream.distinct().flatMap(Collection::stream).distinct().collect(Collectors.toList());
        }
        else
        {
            return evaluateView(socket, views).stream().distinct().collect(Collectors.toList());
        }
    }


    protected Collection<String> evaluateView(final String socket, final Object view)
    {
        final String expressionSetting = String.format(SETTING_EVALUATE_EXPRESSION_PATTERN, socket);
        final String expression = getWidgetInstanceManager().getWidgetSettings().getString(expressionSetting);
        final Object value = StringUtils.isNotBlank(expression) ? getResolver().getValue(view, expression) : view;
        return extractViews(ObjectUtils.toString(value));
    }


    protected List<WidgetInstance> getPossibleWidgetInstances()
    {
        final WidgetInstance widgetSlotInstance = getWidgetslot().getWidgetInstance();
        final List<WidgetInstance> allWidgetInstances = getWidgetInstanceFacade().getWidgetInstances(widgetSlotInstance,
                        getViewsSlotId(), false);
        final Map<Widget, WidgetInstance> allWidgetInstancesByWidgets = allWidgetInstances.stream()
                        .collect(Collectors.toMap(WidgetInstance::getWidget, Function.identity()));
        return filterPossibleWidgets(allWidgetInstances.stream().map(WidgetInstance::getWidget).collect(Collectors.toList()))
                        .stream().map(allWidgetInstancesByWidgets::get).collect(Collectors.toList());
    }


    protected List<Widget> getPossibleWidgets()
    {
        final WidgetInstance widgetInstance = getWidgetslot().getWidgetInstance();
        final List<Widget> possibleWidgets = getWidgetInstanceFacade().getPossibleWidgets(widgetInstance, getViewsSlotId());
        return filterPossibleWidgets(possibleWidgets);
    }


    protected List<Widget> filterPossibleWidgets(final List<Widget> widgets)
    {
        final List<Widget> accessibleWidgets = getViewSwitcherUtils().getAccessibleWidgets(getConfigurationContextCode(),
                        getWidgetslot().getWidgetInstance(), widgets);
        if(getWidgetAuthorizationService() == null || CollectionUtils.isEmpty(widgets))
        {
            return accessibleWidgets;
        }
        return accessibleWidgets.stream().filter(getWidgetAuthorizationService()::isAccessAllowed).collect(Collectors.toList());
    }


    public WidgetAuthorizationService getWidgetAuthorizationService()
    {
        return widgetAuthorizationService;
    }


    public Widgetchildren getViewsContainer()
    {
        return viewsContainer;
    }


    public WidgetInstanceFacade getWidgetInstanceFacade()
    {
        return widgetInstanceFacade;
    }


    public ExpressionResolverFactory getResolverFactory()
    {
        return resolverFactory;
    }


    protected ExpressionResolver getResolver()
    {
        return resolver;
    }


    protected ViewSwitcherUtils getViewSwitcherUtils()
    {
        return viewSwitcherUtils;
    }


    protected EventListener<Event> createOnFocusEventListener()
    {
        return event -> {
            if(event.getTarget().equals(getViewsContainer()))
            {
                if(event.getData() instanceof Collection)
                {
                    notifyViewsSelected((Collection<String>)event.getData());
                }
                else
                {
                    notifyViewsSelected(Collections.singleton(ObjectUtils.toString(event.getData())));
                }
            }
        };
    }
}
