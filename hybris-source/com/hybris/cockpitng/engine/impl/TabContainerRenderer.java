/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.components.Widgetchildren;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.security.WidgetAuthorizationService;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.util.CockpitSessionService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

/**
 * Renders a {@link Widgetchildren} component as {@link Tabbox} and its children as {@link Tab}s
 */
public class TabContainerRenderer extends AbstractChildrenContainerRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(TabContainerRenderer.class);
    private static final String WIDGET_NODE = "widgetNode";
    private static final String IS_NEW_TAB = "is_new_tab";
    public static final String ATTR_CHILDREN_LISTENER_ADDED = "childrenListenerAdded";
    private WidgetAuthorizationService widgetAuthorizationService;
    private CockpitSessionService sessionService;
    private boolean showNewTab = true;
    private boolean closable = true;
    private boolean showTabs = true;


    @Override
    public void render(final Widgetchildren childrenComponent, final List<WidgetInstance> children, final Map<String, Object> ctx)
    {
        final WidgetInstance parentWidgetInstance = childrenComponent.getParentWidgetInstance();
        final Tabbox tabbox = getOrCreateTabbox(childrenComponent);
        final Tabs tabs = tabbox.getTabs();
        final List<Widgetslot> newWidgetslots = new ArrayList<>();
        final List<Component> components = new ArrayList<>();
        tabs.setVisible(isShowTabs() || isAdminMode());
        getAccessibleWidgets(parentWidgetInstance, children).forEach(child -> {
            final Optional<Component> alreadyPlacedComponent = lookupAlreadyPlacedTabForWidget(tabbox, child);
            if(alreadyPlacedComponent.isPresent())
            {
                components.add(alreadyPlacedComponent.get());
            }
            else
            {
                final Widgetslot widgetContainer = createWidgetSlot(childrenComponent, child);
                final Tab tab = addToTab(child, widgetContainer, -1, tabbox);
                components.add(tab);
                newWidgetslots.add(widgetContainer);
            }
        });
        removeTabsForRemovedWidgetInstances(tabs, components);
        syncComponentOrder(tabs.getChildren(), tabbox.getTabpanels().getChildren(), components);
        selectTabWithLastFocusedWidgetInstance(parentWidgetInstance, tabbox, tabs);
        showNewTabButton(childrenComponent, parentWidgetInstance, tabs);
        addWidgetSelectedListener(childrenComponent, tabbox);
        newWidgetslotsAfterCompose(newWidgetslots);
    }


    protected List<WidgetInstance> getAccessibleWidgets(final WidgetInstance parentWidgetInstance,
                    final List<WidgetInstance> widgets)
    {
        if(isAdminMode())
        {
            return widgets;
        }
        return widgets.stream()
                        .filter(widgetInstance -> getWidgetAuthorizationService().isAccessAllowed(widgetInstance.getWidget()))
                        .collect(Collectors.toList());
    }


    protected void removeTabsForRemovedWidgetInstances(final Tabs tabs, final List<Component> componentList)
    {
        final List<Component> childComponents = new ArrayList<>(tabs.getChildren());
        for(final Component component : childComponents)
        {
            if((component instanceof Tab) && !componentList.contains(component) && !isNewTab((Tab)component))
            {
                removeTab((Tab)component);
            }
        }
    }


    protected void selectTabWithLastFocusedWidgetInstance(final WidgetInstance parentWidgetInstance, final Tabbox tabbox,
                    final Tabs tabs)
    {
        final int lastFocusedChildIndex = parentWidgetInstance.getSelectedChildIndex();
        if(tabs.getChildren().size() > lastFocusedChildIndex && lastFocusedChildIndex >= 0)
        {
            tabbox.setSelectedIndex(lastFocusedChildIndex);
        }
    }


    protected void showNewTabButton(final Widgetchildren childrenComponent, final WidgetInstance parentWidgetInstance,
                    final Tabs tabs)
    {
        if(isShowNewTab())
        {
            final List<Widget> templates = getTemplates(parentWidgetInstance.getWidget(), childrenComponent.getSlotID());
            if(CollectionUtils.isNotEmpty(templates))
            {
                final Tab newTab = getNewTab(tabs);
                if(newTab == null)
                {
                    createNewTab(tabs, templates, parentWidgetInstance, childrenComponent);
                }
            }
        }
    }


    protected void addWidgetSelectedListener(final Widgetchildren childrenComponent, final Tabbox tabbox)
    {
        if(!Boolean.TRUE.equals(tabbox.getAttribute(ATTR_CHILDREN_LISTENER_ADDED)))
        {
            childrenComponent.addEventListener(Widgetchildren.ON_CHILD_WIDGET_SELECTED, new EventListener<Event>()
            {
                @Override
                public void onEvent(final Event event) throws Exception
                {
                    final Object data = event.getData();
                    if(data instanceof Integer)
                    {
                        selectWidgetInstanceByIndex(childrenComponent, tabbox, ((Integer)data).intValue());
                    }
                    else
                    {
                        LOG.warn(Widgetchildren.ON_CHILD_WIDGET_SELECTED + " was fired without index but " + data);
                    }
                }
            });
            tabbox.setAttribute(ATTR_CHILDREN_LISTENER_ADDED, Boolean.TRUE);
        }
    }


    protected Optional<Component> lookupAlreadyPlacedTabForWidget(final Tabbox tabbox, final WidgetInstance child)
    {
        Component component = getComponentFor(child, tabbox.getTabpanels());
        if(component instanceof Tabpanel)
        {
            component = ((Tabpanel)component).getLinkedTab();
            if(component != null)
            {
                return Optional.of(component);
            }
        }
        return Optional.empty();
    }


    protected Widgetslot createWidgetSlot(final Widgetchildren childrenComponent, final WidgetInstance child)
    {
        final Widgetslot widgetContainer = new Widgetslot();
        widgetContainer.setWidgetInstance(child);
        widgetContainer.setParentChildrenContainer(childrenComponent);
        widgetContainer.setHeight("100%");
        return widgetContainer;
    }


    private void selectWidgetInstanceByIndex(final Widgetchildren childrenComponent, final Tabbox tabbox, final int index)
    {
        final WidgetInstance parentWidgetInstance = childrenComponent.getParentWidgetInstance();
        if(parentWidgetInstance != null)
        {
            final List<WidgetInstance> instances = getWidgetInstanceFacade().getWidgetInstances(parentWidgetInstance,
                            childrenComponent.getSlotID(), false);
            if((!instances.isEmpty()) && (!tabbox.getTabs().getChildren().isEmpty()))
            {
                final int computedIndex = Math.max(0, Math.min(index, instances.size() - 1));
                final WidgetInstance instance = instances.get(computedIndex);
                final int tabIndex = instance != null ? getTabIndexForWidgetInstance(tabbox, instance) : -1;
                if(tabIndex >= 0)
                {
                    final int computedTabIndex = Math.min(tabIndex, tabbox.getTabs().getChildren().size() - 1);
                    tabbox.setSelectedIndex(computedTabIndex);
                }
            }
        }
    }


    private int getTabIndexForWidgetInstance(final Tabbox tabbox, final WidgetInstance instance)
    {
        int index = 0;
        for(final Component comp : tabbox.getTabs().getChildren())
        {
            final WidgetInstance tabInstance = (WidgetInstance)comp.getAttribute(WIDGET_NODE);
            if(instance.equals(tabInstance))
            {
                return index;
            }
            index++;
        }
        return -1;
    }


    private void createTemplate(final Widget template, final WidgetInstance parentWidgetInstance,
                    final Widgetchildren childrenComponent)
    {
        final WidgetInstance templateInstance = getWidgetInstanceFacade().createWidgetInstance(template, parentWidgetInstance);
        template.setLastFocusedTemplateInstance(templateInstance);
        final List<WidgetInstance> instances = getWidgetInstanceFacade().getWidgetInstances(parentWidgetInstance,
                        childrenComponent.getSlotID(), false);
        final int selectedIndex = (instances.isEmpty() ? 0 : instances.size() - 1);
        parentWidgetInstance.setSelectedChildIndex(selectedIndex);
        childrenComponent.updateChildren();
    }


    private List<Widget> getTemplates(final Widget widget, final String slotId)
    {
        final List<Widget> result = new ArrayList<>();
        for(final Widget child : widget.getChildren())
        {
            if(child.isTemplate() && Objects.equals(slotId, child.getSlotId()))
            {
                result.add(child);
            }
        }
        return result;
    }


    protected Tab addToTab(final WidgetInstance widgetInstance, final Widgetslot widgetContainer, final int index,
                    final Tabbox tabbox)
    {
        final Tab tab = new Tab(getLocalizedTitle(widgetInstance));
        tab.setAttribute(WIDGET_NODE, widgetInstance);
        addTitleChangeListener(widgetContainer, event -> tab.setLabel(String.valueOf(event.getData())));
        if(isClosable() && widgetInstance.getWidget().isTemplate())
        {
            tab.setClosable(true);
            tab.addEventListener(Events.ON_CLOSE, event -> {
                getWidgetInstanceFacade().removeWidgetInstance(widgetInstance);
                final int currentIndex = tabbox.getTabs().getChildren().indexOf(tab);
                Tab nextTab = null;
                if((currentIndex + 1) < tabbox.getTabs().getChildren().size())
                {
                    nextTab = (Tab)tabbox.getTabs().getChildren().get(currentIndex + 1);
                }
                if(nextTab == null || (tab.equals(tabbox.getSelectedTab())
                                && currentIndex < tabbox.getTabs().getChildren().size() - 1 && isNewTab(nextTab)))
                {
                    tabbox.setSelectedIndex(0);
                }
            });
        }
        final Tabs tabs = tabbox.getTabs();
        final List<Component> tabsChildren = tabs.getChildren();
        Component refTab = null;
        if(index >= 0 && index < tabs.getChildren().size())
        {
            refTab = tabsChildren.get(index);
        }
        else if(CollectionUtils.isNotEmpty(tabsChildren))
        {
            final Component lastTab = tabsChildren.get(tabsChildren.size() - 1);
            if((lastTab instanceof Tab) && isNewTab((Tab)lastTab))
            {
                refTab = lastTab;
            }
        }
        tabs.insertBefore(tab, refTab);
        final Tabpanel tabpanel = new Tabpanel();
        tabpanel.setWidgetAttribute("data-scrollable", Boolean.TRUE.toString());
        tabbox.getTabpanels().getChildren().add(tab.getIndex(), tabpanel);
        tabpanel.appendChild(widgetContainer);
        return tab;
    }


    private boolean isNewTab(final Tab tab)
    {
        return Boolean.TRUE.equals(tab.getAttribute(IS_NEW_TAB));
    }


    private Tab getNewTab(final Tabs tabs)
    {
        Tab newTab = null;
        final List<Component> children = tabs.getChildren();
        if(CollectionUtils.isNotEmpty(children))
        {
            final Component last = children.get(children.size() - 1);
            if((last instanceof Tab) && isNewTab((Tab)last))
            {
                newTab = (Tab)last;
            }
        }
        return newTab;
    }


    private void createNewTab(final Tabs tabs, final List<Widget> templates, final WidgetInstance parentWidgetInstance,
                    final Widgetchildren childrenComponent)
    {
        final Tab newTab = new Tab("+");
        newTab.setAttribute(IS_NEW_TAB, Boolean.TRUE);
        tabs.appendChild(newTab);
        newTab.setSclass("cng-newTab");
        newTab.addEventListener(Events.ON_CLICK, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event) throws Exception
            {
                if(CollectionUtils.isNotEmpty(templates))
                {
                    final Widget template = templates.iterator().next();
                    createTemplate(template, parentWidgetInstance, childrenComponent);
                }
            }
        });
        final String popupId = "templateSelectorPopup";
        final Component comp = childrenComponent.getFellowIfAny(popupId, false);
        if(comp != null)
        {
            childrenComponent.removeChild(comp);
        }
        final Popup popup = new Popup();
        popup.setId(popupId);
        childrenComponent.appendChild(popup);
        final Component component = Executions.createComponents("/templateSelector.zul", popup, Collections.emptyMap());
        component.addEventListener(TemplateSelectorController.ON_TEMPLATE_SELECTED, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event) throws Exception
            {
                if(event.getData() instanceof Widget)
                {
                    createTemplate((Widget)event.getData(), parentWidgetInstance, childrenComponent);
                    popup.close();
                }
            }
        });
        newTab.addEventListener(Events.ON_RIGHT_CLICK, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event) throws Exception
            {
                popup.open(newTab);
            }
        });
    }


    private Tabbox getOrCreateTabbox(final Widgetchildren childrenComponent)
    {
        final List<Component> children = childrenComponent.getChildren();
        for(final Component child : children)
        {
            if(child instanceof Tabbox)
            {
                return (Tabbox)child;
            }
        }
        final Tabbox tabbox = new Tabbox();
        tabbox.setHeight("100%");
        childrenComponent.appendChild(tabbox);
        final Tabs tabs = new Tabs();
        tabbox.appendChild(tabs);
        final Tabpanels tabpanels = new Tabpanels();
        tabpanels.setHeight("100%");
        tabbox.appendChild(tabpanels);
        final WidgetInstance parentWidgetInstance = childrenComponent.getParentWidgetInstance();
        tabbox.addEventListener(Events.ON_SELECT, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event) throws Exception
            {
                parentWidgetInstance.setSelectedChildIndex(tabbox.getSelectedIndex());
                final Tab selectedTab = tabbox.getSelectedTab();
                final Object tabNode = selectedTab.getAttribute(WIDGET_NODE);
                if(tabNode instanceof WidgetInstance)
                {
                    final WidgetInstance tabNodeInstance = ((WidgetInstance)tabNode);
                    tabNodeInstance.getWidget().setLastFocusedTemplateInstance(tabNodeInstance);
                    Events.sendEvent(Events.ON_FOCUS, childrenComponent, tabNodeInstance.getId());
                    Events.postEvent(new Event(Events.ON_SELECT, childrenComponent, tabNodeInstance.getId()));
                }
            }
        });
        return tabbox;
    }


    private void removeTab(final Tab tab)
    {
        final Tabbox tabbox = tab.getTabbox();
        final Tabs tabs = tabbox.getTabs();
        final Tabpanels tabpanels = tabbox.getTabpanels();
        final Tabpanel panel = tab.getLinkedPanel();
        tabs.removeChild(tab);
        if(panel != null)
        {
            tabpanels.removeChild(panel);
        }
    }


    protected void syncComponentOrder(final List<Component> tabLiveList, final List<Component> panelLiveList,
                    final List<Component> componentSortedList)
    {
        int index = 0;
        for(final Component component : componentSortedList)
        {
            final Component liveComponent = index < tabLiveList.size() ? tabLiveList.get(index) : null;
            if(!component.equals(liveComponent))
            {
                final Tab tab = (Tab)component;
                setComponentAtIndex(tabLiveList, tab, index);
                final Tabpanel panel = tab.getLinkedPanel();
                setComponentAtIndex(panelLiveList, panel, index);
            }
            index++;
        }
    }


    protected void setComponentAtIndex(final List<Component> components, final Component component, final int index)
    {
        if(component != null)
        {
            components.remove(component);
            if(components.size() >= index)
            {
                components.add(index, component);
            }
            else
            {
                components.add(component);
            }
        }
    }


    private boolean isAdminMode()
    {
        return Boolean.TRUE.equals(getSessionService().getAttribute("cockpitAdminMode"));
    }


    public boolean isShowNewTab()
    {
        return showNewTab;
    }


    public void setShowNewTab(final boolean showNewTab)
    {
        this.showNewTab = showNewTab;
    }


    public boolean isShowTabs()
    {
        return showTabs;
    }


    public void setShowTabs(final boolean showTabs)
    {
        this.showTabs = showTabs;
    }


    public boolean isClosable()
    {
        return closable;
    }


    public void setClosable(final boolean closable)
    {
        this.closable = closable;
    }


    @Required
    public void setSessionService(final CockpitSessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    protected CockpitSessionService getSessionService()
    {
        return sessionService;
    }


    public WidgetAuthorizationService getWidgetAuthorizationService()
    {
        return widgetAuthorizationService;
    }


    @Required
    public void setWidgetAuthorizationService(final WidgetAuthorizationService widgetAuthorizationService)
    {
        this.widgetAuthorizationService = widgetAuthorizationService;
    }
}
