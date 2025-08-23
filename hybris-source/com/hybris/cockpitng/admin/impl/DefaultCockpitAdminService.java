/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin.impl;

import com.google.common.collect.Maps;
import com.hybris.cockpitng.admin.CockpitAdminService;
import com.hybris.cockpitng.admin.ImpersonationPreviewHelper;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.AbstractCockpitComponentDefinition;
import com.hybris.cockpitng.core.CNG;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.SocketConnectionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetConnection;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.WidgetService;
import com.hybris.cockpitng.core.WidgetSocket;
import com.hybris.cockpitng.core.WidgetSocket.SocketVisibility;
import com.hybris.cockpitng.core.impl.NotificationStack;
import com.hybris.cockpitng.core.persistence.WidgetPersistenceService;
import com.hybris.cockpitng.core.persistence.impl.XMLWidgetPersistenceService;
import com.hybris.cockpitng.core.persistence.packaging.SimpleHybrisWidgetResourceLoader;
import com.hybris.cockpitng.core.persistence.packaging.SocketWrapper;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibUtils;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.ui.WidgetInstanceService;
import com.hybris.cockpitng.core.user.AuthorityGroupService;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.core.util.impl.WidgetSocketUtils;
import com.hybris.cockpitng.core.util.impl.WidgetTreeUtils;
import com.hybris.cockpitng.engine.ModalWindowStack;
import com.hybris.cockpitng.engine.SessionWidgetInstanceRegistry;
import com.hybris.cockpitng.renderers.common.TypedSettingsRenderer;
import com.hybris.cockpitng.util.CockpitEventUtils;
import com.hybris.cockpitng.util.CockpitSessionService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.WidgetUtils;
import com.hybris.cockpitng.util.YTestTools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

/**
 * Provides functionality for cockpitNG admin mode.
 */
public class DefaultCockpitAdminService implements CockpitAdminService
{
    public static final String COCKPIT_SYMBOLIC_ADMIN_MODE = "cockpitSymbolicAdminMode";
    public static final String COCKPIT_SHOW_CONNECTIONS_FLAG = "cockpitShowConnectionsFlag";
    private static final int ENTRY_INFO_SIZE = 3;
    private static final String CNG_CSS_IMAGES_OUTPUT_SOCKET_PNG = "/cng/css/images/outputSocket.png";
    private static final String CNG_CSS_IMAGES_INPUT_SOCKET_PNG = "/cng/css/images/inputSocket.png";
    private static final String COCKPIT_WIDGET_CLIPBOARD = "cockpitWidgetClipboard";
    private static final String SELECTED_SOCKETS_MODEL = "selectedSocketsModel";
    private static final String CNG_ADVANCED_INFO_ROW_SCLASS = "cng-advancedInfo-row";
    private static final String SETTINGS_CONTENT_SCLASS = "settingsContent";
    private static final String DELETE_BUTTON_SCLASS = "yo-delete-btn";
    private static final String INLINE_CSS = "table-layout: fixed; padding: 0 0 10px 0;";
    private static final String WIDTH_100_PERCENT = "100%";
    private static final String WIDTH_45_AUTO = "45%, auto";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCockpitAdminService.class);
    private CockpitSessionService sessionService;
    private NotificationStack notificationStack;
    private ModalWindowStack modalWindowStack;
    private WidgetUtils widgetUtils;
    private WidgetService widgetService;
    private CockpitComponentDefinitionService widgetDefinitionService;
    private WidgetPersistenceService widgetPersistenceService;
    private XMLWidgetPersistenceService xmlBasedWidgetPersistenceService;
    private SessionWidgetInstanceRegistry sessionWidgetInstanceRegistry;
    private WidgetInstanceService widgetInstanceService;
    private WidgetLibUtils widgetLibUtils;
    private SocketConnectionService socketConnectionService;
    private CockpitProperties cockpitProperties;
    private AuthorityGroupService adminModeAuthorityGroupService;
    private CockpitUserService cockpitUserService;
    private TypedSettingsRenderer typedSettingsRenderer;
    private ImpersonationPreviewHelper impersonationPreviewHelper;


    @Override
    public void refreshCockpit()
    {
        sessionWidgetInstanceRegistry.clear();
        SimpleHybrisWidgetResourceLoader.clearCssCache();
        if(Executions.getCurrent() != null)
        {
            Executions.sendRedirect(null);
        }
    }


    private WidgetDefinition getWidgetDefinition(final Widget widget)
    {
        return getWidgetDefinitionService().getComponentDefinitionForCode(widget.getWidgetDefinitionId(), WidgetDefinition.class);
    }


    @Override
    public boolean isSymbolicAdminMode()
    {
        return isAdminMode() && isSymbolicAdminFlagEnabled();
    }


    @Override
    public void setSymbolicAdminFlag(final boolean enabled)
    {
        getSessionService().setAttribute(COCKPIT_SYMBOLIC_ADMIN_MODE, Boolean.valueOf(enabled));
    }


    @Override
    public boolean isSymbolicAdminFlagEnabled()
    {
        Object attribute = getSessionService().getAttribute(COCKPIT_SYMBOLIC_ADMIN_MODE);
        if(attribute == null)
        {
            attribute = Boolean.valueOf(cockpitProperties.getProperty("cockpitng.admin.ao.symbolic.default"));
            getSessionService().setAttribute(COCKPIT_SYMBOLIC_ADMIN_MODE, attribute);
        }
        return Boolean.TRUE.equals(attribute);
    }


    @Override
    public boolean isAdminMode()
    {
        return Boolean.TRUE.equals(getSessionService().getAttribute("cockpitAdminMode"));
    }


    @Override
    public boolean isAdminModePermitted()
    {
        return isCurrentUserAdminOrNotAvailable()
                        && !BooleanUtils.toBoolean(this.cockpitProperties.getProperty("cockpit.adminmode.disabled"));
    }


    private boolean isCurrentUserAdminOrNotAvailable()
    {
        return cockpitUserService == null || cockpitUserService.getCurrentUser() == null
                        || cockpitUserService.isAdmin(cockpitUserService.getCurrentUser());
    }


    @Override
    public void setAdminMode(final boolean adminMode, final Component ref)
    {
        CockpitEventUtils.disableGlobalEvents(adminMode);
        if(!adminMode
                        && (getAdminModeAuthorityGroupService() != null && !impersonationPreviewHelper.isImpersonatedPreviewEnabled()))
        {
            getAdminModeAuthorityGroupService().setActiveAuthorityGroupForUser(null);
        }
        if(isAdminModePermitted() && (adminMode || adminMode != isAdminMode()))
        {
            notificationStack.resetNotifierStack();
            modalWindowStack.resetModalWindowStack();
            getSessionService().setAttribute("cockpitAdminMode", Boolean.valueOf(adminMode));
            if(ref != null)
            {
                Component root = ref;
                Widgetslot rootWidget = (Widgetslot)(root instanceof Widgetslot ? root : null);
                while(root.getParent() != null)
                {
                    root = root.getParent();
                    if(root instanceof Widgetslot)
                    {
                        rootWidget = (Widgetslot)root;
                    }
                }
                if(root instanceof HtmlBasedComponent)
                {
                    ((HtmlBasedComponent)root).setSclass(adminMode
                                    ? (isSymbolicAdminFlagEnabled() ? (ADMINMODE_SCLASS + " " + ADMINMODE_SYMBOLIC_SCLASS) : ADMINMODE_SCLASS)
                                    : "");
                    if(rootWidget != null && rootWidget.getWidgetInstance() != null)
                    {
                        final Widget widget = rootWidget.getWidgetInstance().getWidget();
                        removeAllTemplateInstances(widget);
                    }
                }
                root.invalidate();
                if(rootWidget != null)
                {
                    rootWidget.updateView();
                }
            }
        }
    }


    private void removeAllTemplateInstances(final Widget widget)
    {
        if(widget.isTemplate())
        {
            final List<WidgetInstance> widgetInstances = new ArrayList<>(widget.getWidgetInstances());
            for(final WidgetInstance widgetInstance : widgetInstances)
            {
                widgetInstanceService.removeWidgetInstance(widgetInstance);
            }
        }
        for(final Widget child : widget.getChildren())
        {
            removeAllTemplateInstances(child);
        }
    }


    @Override
    public String getWidgetToolbarColor()
    {
        final Object attribute = getSessionService().getAttribute("widgetToolbarColor");
        return attribute instanceof String ? (String)attribute : null;
    }


    @Override
    public void removeWidgetFromClipboard(final Widget node)
    {
        final Object attribute = getSessionService().getAttribute(COCKPIT_WIDGET_CLIPBOARD);
        if(attribute instanceof List)
        {
            final List<Object> newClipboardWidgets = new ArrayList<>((List<?>)attribute);
            newClipboardWidgets.remove(node);
            getSessionService().setAttribute(COCKPIT_WIDGET_CLIPBOARD, newClipboardWidgets);
            updateClipboard();
        }
    }


    @Override
    public boolean isInClipboard(final Widget widget)
    {
        final Object attribute = getSessionService().getAttribute(COCKPIT_WIDGET_CLIPBOARD);
        return attribute instanceof List && ((List<?>)attribute).contains(widget);
    }


    @Override
    public void moveWidgetToClipboard(final Widget widget)
    {
        if(isInClipboard(widget))
        {
            return;
        }
        Optional<WidgetInstance> oldParentInstance = Optional.empty();
        Optional<Widgetslot> registeredWidgetslot = Optional.empty();
        logWarningForManyInstancesIfNecessary(widget.getWidgetInstances().size());
        final Optional<WidgetInstance> widgetInstance = widget.getWidgetInstances().stream().findFirst();
        final boolean isTemplateWidget = !widgetInstance.isPresent();
        if(!isTemplateWidget)
        {
            oldParentInstance = Optional.of(widgetInstance.get().getParent());
            registeredWidgetslot = Optional.of(widgetUtils.getRegisteredWidgetslot(widgetInstance.get()));
            widgetInstanceService.removeWidgetInstance(widgetInstance.get());
        }
        else if(!widget.getParent().getWidgetInstances().isEmpty())
        {
            oldParentInstance = widget.getParent().getWidgetInstances().stream().findFirst();
        }
        pasteWidgetToClipboard(widget);
        registeredWidgetslot.ifPresent(this::removeWidgetSlotFromView);
        oldParentInstance.ifPresent(this::storeWidgetInWidgetTree);
    }


    protected void logWarningForManyInstancesIfNecessary(final int widgetInstancesSize)
    {
        if(widgetInstancesSize > 1)
        {
            LOG.warn("More than one instance found");
        }
    }


    protected void removeWidgetSlotFromView(final Widgetslot slot)
    {
        slot.setWidgetInstance(null);
        slot.updateView();
    }


    protected void storeWidgetInWidgetTree(final WidgetInstance instance)
    {
        getWidgetPersistenceService().storeWidgetTree(instance.getWidget());
        widgetUtils.getRegisteredWidgetslot(instance).updateView();
    }


    protected void pasteWidgetToClipboard(final Widget widget)
    {
        getWidgetService().moveWidget(widget, null);
        sessionWidgetInstanceRegistry.unregisterWidgetInstance(widget.getSlotId());
        widget.setSlotId(null);
        getWidgetPersistenceService().deleteWidgetTree(widget);
        widget.setId(UUID.randomUUID().toString());
        getWidgetPersistenceService().storeWidgetTree(widget);
        final Object widgetClipboardAttribute = getSessionService().getAttribute(COCKPIT_WIDGET_CLIPBOARD);
        final List<Object> clipboardWidgets = getClipboardWidgets(widgetClipboardAttribute);
        clipboardWidgets.add(widget);
        getSessionService().setAttribute(COCKPIT_WIDGET_CLIPBOARD, clipboardWidgets);
        updateClipboard();
    }


    protected List<Object> getClipboardWidgets(final Object widgetClipboardAttribute)
    {
        if(widgetClipboardAttribute instanceof List)
        {
            return new ArrayList<>((List<?>)widgetClipboardAttribute);
        }
        return new ArrayList<>();
    }


    protected void updateClipboard()
    {
        try
        {
            final Component fellowIfAny = getRoot().getFellowIfAny("widgetClipboard");
            fellowIfAny.getChildren().clear();
            renderWidgetClipboard(fellowIfAny, (Caption)getRoot().getFellowIfAny("widgetClipboardCaption"));
        }
        catch(final Exception e)
        {
            LOG.error("Could not update clipboard, reason: ", e);
        }
    }


    @Override
    public void renderWidgetClipboard(final Component clipboardComponent, final Caption caption)
    {
        final Object attribute = getSessionService().getAttribute(COCKPIT_WIDGET_CLIPBOARD);
        if(attribute instanceof List)
        {
            caption.setLabel("Widget clipboard (" + ((List<?>)attribute).size() + ")");
            ((List<?>)attribute).stream().filter(widget -> widget instanceof Widget).forEach(widget -> {
                final Div container = new Div();
                container.setSclass("z-panel-header clipboardEntry");
                container.setDraggable("widget");
                container.setAttribute("draggedWidget", widget);
                container.appendChild(new Label(getWidgetDefinition((Widget)widget).getName()));
                final Toolbarbutton clearBtn = new Toolbarbutton();
                clearBtn.setSclass("removeWidgetBtn");
                clearBtn.addEventListener(Events.ON_CLICK, event -> {
                    getWidgetPersistenceService().deleteWidgetTree((Widget)widget);
                    removeWidgetFromClipboard((Widget)widget);
                    getWidgetService().removeWidget((Widget)widget);
                    updateClipboard();
                });
                container.appendChild(clearBtn);
                clipboardComponent.appendChild(container);
            });
        }
    }


    @Override
    public void showGroupWidgetWizard(final Widget widget, final EventListener<Event> onCloseListener)
    {
        final Window window = new Window("Create widget group", "normal", true);
        window.setWidth("70%");
        window.setTop("100px");
        window.setSclass("yo-create-widget-group-wizard");
        window.setParent(getRoot());
        window.setVisible(true);
        window.doHighlighted();
        final WidgetDefinition definition = createGroupDefinitionTemplate(widget);
        window.setAttribute("groupDefinition", definition);
        window.setAttribute("mainWizardWindow", window);
        window.setAttribute("childSocketsTreeModel", prepareTreeModel(definition));
        final TreeitemRenderer<?> childSocketsTreeRenderer = (item, data, index) -> {
            if(data instanceof Widget)
            {
                item.setLabel(((Widget)data).getWidgetDefinitionId());
            }
            else if(data instanceof SocketWrapper)
            {
                final SocketWrapper socketWrapper = (SocketWrapper)data;
                if(socketWrapper.isInput())
                {
                    item.setImage(CNG_CSS_IMAGES_INPUT_SOCKET_PNG);
                }
                else
                {
                    item.setImage(CNG_CSS_IMAGES_OUTPUT_SOCKET_PNG);
                }
                item.setLabel(socketWrapper.getSocket().getId());
                item.setTooltiptext((socketWrapper.isInput() ? "Input: " : "Output: ") + socketWrapper.getWidget() + "["
                                + socketWrapper.getSocket() + "]");
                item.setDraggable("socketWrapper");
            }
            item.setValue(data);
        };
        window.setAttribute("childSocketsTreeRenderer", childSocketsTreeRenderer);
        final ListModelList<?> selectedSocketsModel = new ListModelList<>();
        window.setAttribute(SELECTED_SOCKETS_MODEL, selectedSocketsModel);
        window.setAttribute("selectedSocketsRenderer", (ListitemRenderer<Object>)(item, data, index) -> {
            if(data instanceof SocketWrapper)
            {
                final SocketWrapper socketWrapper = (SocketWrapper)data;
                final Image iconImg = new Image();
                if(socketWrapper.isInput())
                {
                    iconImg.setSrc(CNG_CSS_IMAGES_INPUT_SOCKET_PNG);
                }
                else
                {
                    iconImg.setSrc(CNG_CSS_IMAGES_OUTPUT_SOCKET_PNG);
                }
                final Listcell iconCell = new Listcell();
                iconCell.setSclass("y-iconcell");
                iconCell.appendChild(iconImg);
                item.appendChild(iconCell);
                final Listcell listcell = new Listcell();
                item.appendChild(listcell);
                final Textbox textbox = new Textbox(socketWrapper.getId());
                textbox.addEventListener(Events.ON_CHANGE, event -> socketWrapper.setId(textbox.getText()));
                textbox.setInplace(true);
                listcell.appendChild(textbox);
                item.setTooltiptext((socketWrapper.isInput() ? "Input: " : "Output: ") + socketWrapper.getWidget() + "["
                                + socketWrapper.getSocket() + "]");
                item.setCtrlKeys("#del");
                item.addEventListener(Events.ON_CTRL_KEY, event -> {
                    final ListModelList<?> listModel = (ListModelList<?>)window.getAttribute(SELECTED_SOCKETS_MODEL);
                    final List<Object> newList = new ArrayList<>(listModel.getInnerList());
                    final Set<Object> toRemove = new HashSet<>();
                    final Set<?> selectedItems = item.getListbox().getSelectedItems();
                    for(final Object object : selectedItems)
                    {
                        toRemove.add(((Listitem)object).getValue());
                    }
                    newList.removeAll(toRemove);
                    final ListModelList<?> listModelList = new ListModelList<>(newList);
                    window.setAttribute(SELECTED_SOCKETS_MODEL, listModelList);
                    item.getListbox().setModel(listModelList);
                });
                item.setValue(data);
                final Listcell removeCell = new Listcell();
                removeCell.setSclass("y-removeBtn");
                item.appendChild(removeCell);
                final Button removeBtn = new Button();
                removeBtn.setSclass(DELETE_BUTTON_SCLASS);
                removeBtn.addEventListener(Events.ON_CLICK, event -> {
                    final ListModelList<?> listModel = (ListModelList<?>)window.getAttribute(SELECTED_SOCKETS_MODEL);
                    final List<Object> newList = new ArrayList<>(listModel.getInnerList());
                    newList.remove(socketWrapper);
                    final ListModelList<?> listModelList = new ListModelList<>(newList);
                    window.setAttribute(SELECTED_SOCKETS_MODEL, listModelList);
                    item.getListbox().setModel(listModelList);
                });
                removeCell.appendChild(removeBtn);
            }
        });
        window.setAttribute("dropListener", (EventListener<Event>)event -> {
            if(event instanceof DropEvent)
            {
                final DropEvent evt = (DropEvent)event;
                final Component dragged = evt.getDragged();
                final Component target = evt.getTarget();
                if(dragged instanceof Treeitem)
                {
                    final Object value = ((Treeitem)dragged).getValue();
                    if(value instanceof SocketWrapper)
                    {
                        final ListModelList<?> listmodel = (ListModelList<?>)window.getAttribute(SELECTED_SOCKETS_MODEL);
                        final List<Object> newList = new ArrayList<>(listmodel.getInnerList());
                        if(!newList.contains(value))
                        {
                            newList.add(value);
                            final ListModelList<?> listModelList = new ListModelList<>(newList);
                            window.setAttribute(SELECTED_SOCKETS_MODEL, listModelList);
                            if(target instanceof Listbox)
                            {
                                ((Listbox)target).setModel(listModelList);
                            }
                        }
                    }
                }
            }
        });
        window.setAttribute("finishListener", (EventListener<Event>)event -> {
            final List<?> innerList = ((ListModelList<?>)window.getAttribute(SELECTED_SOCKETS_MODEL)).getInnerList();
            final List<SocketWrapper> socketWrapperList = innerList.stream().filter(object -> object instanceof SocketWrapper)
                            .map(object -> (SocketWrapper)object).collect(Collectors.toList());
            widgetLibUtils.createComposedWidgetJarArchive(definition, socketWrapperList, getXmlBasedWidgetPersistenceService());
            widgetUtils.refreshWidgetLibrary();
            window.detach();
            // replace current widget with composed one
            final AbstractCockpitComponentDefinition componentDefinitionForCode = widgetDefinitionService
                            .getComponentDefinitionForCode(definition.getCode());
            if(componentDefinitionForCode != null)
            {
                final Widget composedRoot = definition.getComposedWidgetRoot();
                if(composedRoot != null)
                {
                    final Widget parent = widget.getParent();
                    getWidgetService().removeWidget(widget);
                    final Widget composedWidget = getWidgetService().createComposedWidget(parent, null, widget.getSlotId(),
                                    definition.getCode(), composedRoot);
                    if(parent == null)
                    {
                        composedWidget.setId(widget.getSlotId());
                    }
                    getWidgetPersistenceService().deleteWidgetTree(widget);
                    getWidgetPersistenceService().storeWidgetTree(parent == null ? composedWidget : parent);
                    onCloseListener.onEvent(event);
                }
            }
        });
        Executions.createComponents("/groupWizard.zul", window, Collections.emptyMap());
    }


    protected TreeModel<Object> prepareTreeModel(final WidgetDefinition definition)
    {
        final List<Widget> descendants = getWidgetService().getAllDescendants(definition.getComposedWidgetRoot()).stream()
                        .filter(wdg -> !getWidgetDefinition(wdg).isStubWidget()).collect(Collectors.toList());
        final Object root = new Object();
        return new AbstractTreeModel<Object>(root)
        {
            private static final long serialVersionUID = 1L;
            private final transient Map<Object, List<SocketWrapper>> socketMap = new HashMap<>();


            @Override
            public boolean isLeaf(final Object node)
            {
                return getChildCount(node) <= 0;
            }


            @Override
            public int getChildCount(final Object parent)
            {
                if(parent.equals(root))
                {
                    return descendants.size();
                }
                else if(parent instanceof Widget)
                {
                    final List<SocketWrapper> sockets = getSockets((Widget)parent);
                    socketMap.put(parent, sockets);
                    return sockets.size();
                }
                return 0;
            }


            @Override
            public Object getChild(final Object parent, final int index)
            {
                final List<SocketWrapper> list = socketMap.get(parent);
                if(list != null && index < list.size())
                {
                    return list.get(index);
                }
                else
                {
                    return descendants.get(index);
                }
            }
        };
    }


    private List<SocketWrapper> getSockets(final Widget widget)
    {
        final List<SocketWrapper> ret = new ArrayList<>();
        final WidgetDefinition definition = getWidgetDefinition(widget);
        final List<WidgetSocket> inputs = WidgetSocketUtils.getAllInputs(widget, definition);
        ret.addAll(inputs.stream().map(input -> new SocketWrapper(widget, input, true)).collect(Collectors.toList()));
        final List<WidgetSocket> outputs = WidgetSocketUtils.getAllOutputs(widget, definition);
        ret.addAll(outputs.stream().map(output -> new SocketWrapper(widget, output, false)).collect(Collectors.toList()));
        return ret;
    }


    protected WidgetDefinition createGroupDefinitionTemplate(final Widget widget)
    {
        final WidgetDefinition ret = new WidgetDefinition();
        final WidgetDefinition rootDefinition = getWidgetDefinition(widget);
        ret.setCode(rootDefinition.getCode() + "Group");
        ret.setCategoryTag("Groups");
        ret.setDefaultTitle(rootDefinition.getDefaultTitle());
        ret.setDescription(rootDefinition.getDescription());
        ret.setName(rootDefinition.getName() + " (grouped)");
        final Widget tempWidget = getWidgetService().createWidget(null, rootDefinition.getCode(), null, rootDefinition.getCode());
        getWidgetService().loadComposedChildren(tempWidget, widget);
        ret.setComposedWidgetRoot(tempWidget.getComposedRootInstance());
        return ret;
    }


    @Override
    public void showWidgetConnectionWizard(final Widget source, final Widget target, final Component ref)
    {
        final HashMap<Object, Object> attributes = Maps.newHashMap();
        attributes.put("sourceWidget", source);
        attributes.put("targetWidget", target);
        final Window window = (Window)Executions.createComponents("/connectionWizard.zul", ref.getRoot(), attributes);
        window.setVisible(true);
        window.doHighlighted();
    }


    @Override
    public void showWidgetMultiConnectionWizard(final Widget widget, final Component ref)
    {
        final HashMap<Object, Object> attributes = Maps.newHashMap();
        attributes.put("widget", widget);
        final Window window = (Window)Executions.createComponents("/connectionMultiWizard.zul", ref.getRoot(), attributes);
        window.setVisible(true);
        window.doHighlighted();
    }


    /**
     * @deprecated since 6.5
     * @param widget
     */
    @Deprecated(since = "6.5", forRemoval = true)
    public void createVirtualSocket(final Widget widget)
    {
        LOG.debug("Create virtual socket for {}", widget);
    }


    protected boolean hasConnections(final Widget widget)
    {
        boolean connected = false;
        if(widget != null)
        {
            final WidgetDefinition definition = getWidgetDefinition(widget);
            connected = CollectionUtils.isNotEmpty(WidgetSocketUtils.getAllInputs(widget, definition))
                            || CollectionUtils.isNotEmpty(WidgetSocketUtils.getAllOutputs(widget, definition));
        }
        return connected;
    }


    private Map<String, Object> getLabelMap(final Widgetslot widgetslot)
    {
        try
        {
            return (Map<String, Object>)widgetslot.getAttribute("labels");
        }
        catch(final ClassCastException e)
        {
            LOG.error("Could not get label attribute for " + widgetslot, e);
            return null;
        }
    }


    @Override
    public Component renderWidgetDefinitionInfo(final Component parent, final WidgetDefinition widgetDefinition)
    {
        final Div infoCnt = new Div();
        infoCnt.setSclass("infoCnt");
        parent.appendChild(infoCnt);
        final Div nameDiv = new Div();
        infoCnt.appendChild(nameDiv);
        nameDiv.setSclass("widgetName");
        nameDiv.appendChild(new Label(widgetDefinition.getName()));
        final Div infoDiv = new Div();
        infoCnt.appendChild(infoDiv);
        infoDiv.setSclass("widgetDescr");
        final Label label2 = new Label(widgetDefinition.getCode());
        label2.setSclass("code");
        infoDiv.appendChild(label2);
        final Div descTitle = new Div();
        descTitle.setSclass("descTitle");
        infoCnt.appendChild(descTitle);
        descTitle.appendChild(new Label("Description:"));
        final Div descrDiv = new Div();
        infoCnt.appendChild(descrDiv);
        descrDiv.setSclass("widgetDescr");
        final Label descrLabel = new Label();
        descrLabel.setMultiline(true);
        descrLabel.setValue(widgetDefinition.getDescription());
        descrDiv.appendChild(descrLabel);
        final Div inputsDiv = new Div();
        inputsDiv.setSclass("sockets");
        infoCnt.appendChild(inputsDiv);
        inputsDiv.appendChild(new Label("Inputs:"));
        widgetDefinition.getInputs().stream().filter(input -> !SocketVisibility.HIDDEN.equals(input.getVisibility()))
                        .forEach(input -> {
                            final Label label = new Label(input.getId() + " (" + input.getDataType()
                                            + (input.getDataMultiplicity() == null ? "" : ":" + input.getDataMultiplicity()) + ")");
                            label.setSclass("socketLabel");
                            inputsDiv.appendChild(label);
                        });
        final Div outputsDiv = new Div();
        outputsDiv.setSclass("sockets");
        infoCnt.appendChild(outputsDiv);
        outputsDiv.appendChild(new Label("Outputs:"));
        widgetDefinition.getOutputs().stream().filter(output -> !SocketVisibility.HIDDEN.equals(output.getVisibility()))
                        .forEach(output -> {
                            final Label label = new Label(output.getId() + " (" + output.getDataType()
                                            + (output.getDataMultiplicity() == null ? "" : ":" + output.getDataMultiplicity()) + ")");
                            label.setSclass("socketLabel");
                            outputsDiv.appendChild(label);
                        });
        final Groupbox advancedGroupbox = new Groupbox();
        advancedGroupbox.setTitle("Advanced");
        advancedGroupbox.setSclass("cng-advancedInfo");
        advancedGroupbox.setMold("3d");
        advancedGroupbox.setOpen(false);
        final Div viewRow = new Div();
        viewRow.setSclass(CNG_ADVANCED_INFO_ROW_SCLASS);
        viewRow.appendChild(new Label("view URI: "));
        viewRow.appendChild(new Label(widgetDefinition.getViewURI()));
        advancedGroupbox.appendChild(viewRow);
        final Div ctrlRow = new Div();
        ctrlRow.setSclass(CNG_ADVANCED_INFO_ROW_SCLASS);
        ctrlRow.appendChild(new Label("controller class: "));
        ctrlRow.appendChild(new Label(widgetDefinition.getController()));
        advancedGroupbox.appendChild(ctrlRow);
        final Div declRow = new Div();
        declRow.setSclass(CNG_ADVANCED_INFO_ROW_SCLASS);
        declRow.appendChild(new Label("declaring module: "));
        final String declaringModule = widgetDefinition.getDeclaringModule();
        declRow.appendChild(new Label(StringUtils.isBlank(declaringModule) ? "---" : declaringModule));
        advancedGroupbox.appendChild(declRow);
        final Div resRow = new Div();
        resRow.setSclass(CNG_ADVANCED_INFO_ROW_SCLASS);
        resRow.appendChild(new Label("resource path: "));
        resRow.appendChild(new Label(widgetDefinition.getResourcePath()));
        advancedGroupbox.appendChild(resRow);
        final Div locRow = new Div();
        locRow.setSclass(CNG_ADVANCED_INFO_ROW_SCLASS);
        locRow.appendChild(new Label("location path: "));
        locRow.appendChild(new Label(widgetDefinition.getLocationPath()));
        advancedGroupbox.appendChild(locRow);
        infoCnt.appendChild(advancedGroupbox);
        return infoCnt;
    }


    @Override
    public Window createSettingsWizard(final Component parent, final Widgetslot widgetslot, final Widget widget,
                    final EventListener<Event> closeCallback)
    {
        final Window window = new Window();
        window.setSclass("widget-settings-wizard");
        window.setBorder(false);
        window.setTitle("Settings for " + widget);
        window.setClosable(true);
        window.setWidth("70%");
        final Map<String, Object> labelMap = getLabelMap(widgetslot);
        final Tabbox tabbox = new Tabbox();
        final Tabs tabs = new Tabs();
        tabbox.appendChild(tabs);
        final Tab generalTab = new Tab("General");
        final Tab localizationTab = new Tab("Localization");
        final Tab virtualSocketsTab = new Tab("Virtual sockets");
        final Tab accessRestrictionsTab = new Tab("Access Restrictions");
        final Tab aboutTab = new Tab("About");
        localizationTab.setDisabled(labelMap == null || labelMap.isEmpty());
        accessRestrictionsTab.setDisabled(widget.getParent() == null);
        tabs.appendChild(generalTab);
        tabs.appendChild(localizationTab);
        tabs.appendChild(virtualSocketsTab);
        tabs.appendChild(accessRestrictionsTab);
        tabs.appendChild(aboutTab);
        final Tabpanels tabpanels = new Tabpanels();
        tabbox.appendChild(tabpanels);
        final Tabpanel generalTabpanel = new Tabpanel();
        tabpanels.appendChild(generalTabpanel);
        final Tabpanel localizationTabpanel = new Tabpanel();
        tabpanels.appendChild(localizationTabpanel);
        final Tabpanel virtualSocketsTabpanel = new Tabpanel();
        tabpanels.appendChild(virtualSocketsTabpanel);
        final Tabpanel accessRestrictionTabpanel = new Tabpanel();
        tabpanels.appendChild(accessRestrictionTabpanel);
        final Tabpanel aboutTabpanel = new Tabpanel();
        tabpanels.appendChild(aboutTabpanel);
        window.appendChild(tabbox);
        final Div content = new Div();
        generalTabpanel.appendChild(content);
        content.setSclass(SETTINGS_CONTENT_SCLASS);
        final Div locContent = new Div();
        localizationTabpanel.appendChild(locContent);
        locContent.setSclass(SETTINGS_CONTENT_SCLASS);
        renderTabContent(widget, content, locContent);
        final Div vsContent = new Div();
        virtualSocketsTabpanel.appendChild(vsContent);
        vsContent.setSclass(SETTINGS_CONTENT_SCLASS);
        renderVirtualSocketsTabContent(widget, vsContent);
        final Div accessRestrictionContent = new Div();
        accessRestrictionTabpanel.appendChild(accessRestrictionContent);
        accessRestrictionContent.setSclass(SETTINGS_CONTENT_SCLASS);
        renderAccessRestrictionTabContent(widget, accessRestrictionContent);
        final Button addGeneralSettingButton = new Button("Add Setting");
        YTestTools.modifyYTestId(addGeneralSettingButton, "add_widget_setting_" + widget.getId());
        generalTabpanel.appendChild(addGeneralSettingButton);
        final Popup popup = new Popup();
        UITools.addSClass(popup, "add_setting_popup");
        generalTabpanel.appendChild(popup);
        addGeneralSettingButton.addEventListener(Events.ON_CLICK, event -> {
            popup.getChildren().clear();
            final Vlayout vlayout = new Vlayout();
            popup.appendChild(vlayout);
            vlayout.appendChild(new Label("Key"));
            final Textbox keyTextbox = new Textbox();
            vlayout.appendChild(keyTextbox);
            keyTextbox.setFocus(true);
            vlayout.appendChild(new Label("Type"));
            final Combobox typeBox = new Combobox();
            typeBox.setWidth("220px");
            vlayout.appendChild(typeBox);
            typeBox.appendChild(new Comboitem(String.class.getName()));
            typeBox.appendChild(new Comboitem(Boolean.class.getName()));
            typeBox.appendChild(new Comboitem(Integer.class.getName()));
            typeBox.appendChild(new Comboitem(Double.class.getName()));
            final Button addButton = new Button("Add");
            vlayout.appendChild(addButton);
            addButton.addEventListener(Events.ON_CLICK, event2 -> {
                final String key = keyTextbox.getText();
                if(StringUtils.isNotBlank(key))
                {
                    if(widget.getWidgetSettings().getRaw(key) != null)
                    {
                        final String msg = Labels.getLabel("widget.settings.duplicate.message", new Object[]
                                        {key});
                        final String title = Labels.getLabel("widget.settings.duplicate.title");
                        Messagebox.show(msg, title, Messagebox.OK, Messagebox.EXCLAMATION);
                        return;
                    }
                    if(typeBox.getSelectedItem() == null)
                    {
                        widget.getWidgetSettings().put(key, "");
                    }
                    else
                    {
                        final Class<?> clazz = Class.forName(typeBox.getSelectedItem().getLabel());
                        Object value = "";
                        if(Boolean.class.equals(clazz))
                        {
                            value = Boolean.FALSE;
                        }
                        else if(Integer.class.equals(clazz))
                        {
                            value = Integer.valueOf(0);
                        }
                        else if(Double.class.equals(clazz))
                        {
                            value = Double.valueOf(0d);
                        }
                        widget.getWidgetSettings().put(key, value);
                    }
                    renderTabContent(widget, content, locContent);
                    popup.close();
                }
            });
            popup.open(addGeneralSettingButton);
        });
        final Button addLocalizationSettingButton = new Button("Add Label");
        localizationTabpanel.appendChild(addLocalizationSettingButton);
        final Popup locPopup = new Popup();
        UITools.addSClass(locPopup, "add_label_popup");
        addLocalizationSettingButton.addEventListener(Events.ON_CLICK, event -> {
            locPopup.getChildren().clear();
            final Vlayout vlayout = new Vlayout();
            locPopup.appendChild(vlayout);
            vlayout.appendChild(new Label("Key"));
            final Combobox keyCombobox = new Combobox();
            keyCombobox.setWidth("220px");
            addLabelComboItems(keyCombobox, labelMap);
            vlayout.appendChild(keyCombobox);
            vlayout.appendChild(new Label("Language"));
            final Textbox langTextbox = new Textbox();
            vlayout.appendChild(langTextbox);
            final Button addButton = new Button("Add");
            vlayout.appendChild(addButton);
            addButton.addEventListener(Events.ON_CLICK, event2 -> {
                if(StringUtils.isNotBlank(keyCombobox.getText()))
                {
                    String lang = "en";
                    if(StringUtils.isNotBlank(langTextbox.getText()))
                    {
                        lang = langTextbox.getText();
                    }
                    String key = keyCombobox.getText();
                    if("en".equals(lang))
                    {
                        key = "labels." + key;
                    }
                    else
                    {
                        key = "labels_" + lang + "." + key;
                    }
                    if(widget.getWidgetSettings().getRaw(key) != null)
                    {
                        throw new IllegalArgumentException("Label '" + key + "' already exists.");
                    }
                    widget.getWidgetSettings().put(key, "");
                    renderTabContent(widget, content, locContent);
                }
                locPopup.close();
            });
            locPopup.open(addLocalizationSettingButton);
        });
        localizationTabpanel.appendChild(locPopup);
        final Button addVirtualSocketButton = new Button("Add Virtual Socket");
        virtualSocketsTabpanel.appendChild(addVirtualSocketButton);
        final Popup vsPopup = new Popup();
        UITools.addSClass(vsPopup, "add_virtual_socket_popup");
        addVirtualSocketButton.addEventListener(Events.ON_CLICK, event -> {
            vsPopup.getChildren().clear();
            final Vlayout vlayout = new Vlayout();
            vsPopup.appendChild(vlayout);
            vlayout.appendChild(new Label("Kind"));
            final Combobox inoutBox = new Combobox();
            inoutBox.setWidth("220px");
            vlayout.appendChild(inoutBox);
            inoutBox.setReadonly(true);
            inoutBox.appendChild(new Comboitem("input"));
            inoutBox.appendChild(new Comboitem("output"));
            inoutBox.setSelectedIndex(0);
            inoutBox.setFocus(true);
            vlayout.appendChild(new Label("Key"));
            final Textbox keyTextbox = new Textbox();
            vlayout.appendChild(keyTextbox);
            vlayout.appendChild(new Label("Type"));
            final Combobox typeBox = new Combobox();
            typeBox.setWidth("220px");
            vlayout.appendChild(typeBox);
            typeBox.appendChild(new Comboitem(String.class.getName()));
            typeBox.appendChild(new Comboitem(Boolean.class.getName()));
            typeBox.appendChild(new Comboitem(Integer.class.getName()));
            typeBox.appendChild(new Comboitem(Double.class.getName()));
            vlayout.appendChild(new Label("Multiplicity"));
            final Listbox mtpBox = new Listbox();
            mtpBox.setMold("select");
            mtpBox.setWidth("220px");
            vlayout.appendChild(mtpBox);
            mtpBox.appendChild(new Listitem("<single>", null));
            mtpBox.appendChild(new Listitem("Collection", WidgetSocket.Multiplicity.COLLECTION));
            mtpBox.appendChild(new Listitem("List", WidgetSocket.Multiplicity.LIST));
            mtpBox.appendChild(new Listitem("Set", WidgetSocket.Multiplicity.SET));
            mtpBox.setSelectedIndex(0);
            final Button addButton = new Button("Add");
            vlayout.appendChild(addButton);
            addButton.addEventListener(Events.ON_CLICK, event2 -> {
                final String key = keyTextbox.getText();
                if(StringUtils.isNotBlank(key))
                {
                    final boolean input = (inoutBox.getSelectedIndex() <= 0);
                    if(hasSocket(widget, key, input))
                    {
                        throw new IllegalArgumentException("Socket '" + key + "' already exists.");
                    }
                    final WidgetSocket socket = new WidgetSocket(widget,
                                    (input ? CNG.SOCKET_IN : CNG.SOCKET_OUT) | CNG.SOCKET_VIRTUAL);
                    socket.setId(key);
                    if(StringUtils.isBlank(typeBox.getValue()))
                    {
                        socket.setDataType(Object.class.getName());
                    }
                    else
                    {
                        socket.setDataType(typeBox.getValue());
                    }
                    final Listitem mtpSelection = mtpBox.getSelectedItem();
                    if(mtpSelection != null && (mtpSelection.getValue() instanceof WidgetSocket.Multiplicity))
                    {
                        socket.setDataMultiplicity(mtpSelection.getValue());
                    }
                    if(input)
                    {
                        widget.addVirtualInput(socket);
                    }
                    else
                    {
                        widget.addVirtualOutput(socket);
                    }
                    renderVirtualSocketsTabContent(widget, vsContent);
                    vsPopup.close();
                }
            });
            vsPopup.open(addVirtualSocketButton);
        });
        virtualSocketsTabpanel.appendChild(vsPopup);
        parent.appendChild(window);
        final EventListener<Event> closeListener = event -> {
            if(closeCallback != null)
            {
                closeCallback.onEvent(event);
            }
            if(event.isPropagatable())
            {
                window.detach();
            }
        };
        final Div bottomCnt = new Div();
        bottomCnt.setSclass("settingsBottomCnt");
        final Button closeBtn = new Button("Close");
        YTestTools.modifyYTestId(closeBtn, "close_widget_setting_" + widget.getId());
        bottomCnt.appendChild(closeBtn);
        window.appendChild(bottomCnt);
        closeBtn.addEventListener(Events.ON_CLICK, closeListener);
        window.addEventListener(Events.ON_CLOSE, closeListener);
        window.setVisible(false);
        final Div aboutContent = new Div();
        aboutContent.setSclass("settingsContent addWidgetWizardDetails");
        aboutTabpanel.appendChild(aboutContent);
        renderWidgetDefinitionInfo(aboutContent, getWidgetDefinition(widget));
        return window;
    }


    private static void addLabelComboItems(final Combobox keyCombobox, final Map<String, Object> labelMap)
    {
        final Map<String, String> labelKeys = getLabelKeys(labelMap);
        for(final Entry<String, String> entry : labelKeys.entrySet())
        {
            final Comboitem item = new Comboitem(entry.getKey());
            item.setTooltiptext("default: '" + entry.getValue() + "'");
            keyCombobox.appendChild(item);
        }
    }


    private static Map<String, String> getLabelKeys(final Map<String, Object> labelMap)
    {
        final Map<String, String> ret = new HashMap<>();
        final Set<Entry<String, Object>> entrySet = labelMap.entrySet();
        for(final Entry<String, Object> entry : entrySet)
        {
            if(entry.getValue() instanceof Map)
            {
                final Map<String, String> labelKeys = getLabelKeys((Map<String, Object>)entry.getValue());
                for(final Entry<String, String> subkey : labelKeys.entrySet())
                {
                    ret.put(entry.getKey() + "." + subkey.getKey(), String.valueOf(subkey.getValue()));
                }
            }
            else
            {
                ret.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        return ret;
    }


    private void renderTabContent(final Widget widget, final Component content, final Component locContent)
    {
        content.getChildren().clear();
        locContent.getChildren().clear();
        final Div locCntInner = new Div();
        locContent.appendChild(locCntInner);
        final Hbox titleLine = new Hbox();
        titleLine.setStyle(INLINE_CSS);
        titleLine.setWidth(WIDTH_100_PERCENT);
        titleLine.setWidths(WIDTH_45_AUTO);
        titleLine.appendChild(new Label("Title: "));
        final Textbox titlebox = new Textbox(widget.getTitle());
        YTestTools.modifyYTestId(titlebox, "Title");
        titleLine.appendChild(titlebox);
        titlebox.addEventListener(Events.ON_CHANGE, event2 -> {
            widget.setTitle(titlebox.getValue());
            getWidgetPersistenceService().storeWidgetTree(widget);
        });
        content.appendChild(titleLine);
        // change the slot it
        final Hbox idLine = new Hbox();
        idLine.setStyle(INLINE_CSS);
        idLine.setWidth(WIDTH_100_PERCENT);
        idLine.setWidths(WIDTH_45_AUTO);
        idLine.appendChild(new Label("Widget ID: "));
        final Widget rootWidget = WidgetTreeUtils.getRootWidget(widget);
        final Textbox widgetIdbox = new Textbox(widget.getId());
        YTestTools.modifyYTestId(widgetIdbox, "Widget_ID");
        idLine.appendChild(widgetIdbox);
        if(rootWidget.equals(widget))
        {
            widgetIdbox.setDisabled(true);
        }
        else
        {
            widgetIdbox.addEventListener(Events.ON_CHANGE, event -> {
                final Set<Widget> widgetTree = WidgetTreeUtils.getAllChildWidgetsRecursively(rootWidget);
                boolean isWidgetIdAvailable = false;
                if(!widgetTree.isEmpty())
                {
                    for(final Widget treeWidget : widgetTree)
                    {
                        if(widgetIdbox.getValue().equals(treeWidget.getId()))
                        {
                            isWidgetIdAvailable = false;
                            break;
                        }
                        else
                        {
                            isWidgetIdAvailable = true;
                        }
                    }
                }
                if(isWidgetIdAvailable)
                {
                    widget.setId(widgetIdbox.getValue());
                    widgetPersistenceService.storeWidgetTree(rootWidget);
                }
                else
                {
                    Messagebox.show("Please choose another id");
                }
            });
        }
        content.appendChild(idLine);
        final Set<String> keySet = widget.getWidgetSettings().keySet();
        final String[] keyList = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyList);
        for(final String setting : keyList)
        {
            final boolean locLabel = setting.startsWith("labels") && setting.contains(".");
            final Component line = createSettingsLine(setting, locLabel, widget);
            if(locLabel)
            {
                appendLocalizationLine(line, locCntInner);
            }
            else
            {
                content.appendChild(line);
            }
        }
    }


    private static void renderVirtualSocketsTabContent(final Widget widget, final Component content)
    {
        content.getChildren().clear();
        final Vlayout cntInner = new Vlayout();
        UITools.addSClass(cntInner, "virtual-sockets");
        content.appendChild(cntInner);
        List<WidgetSocket> sockets = widget.getVirtualInputs();
        if(sockets.isEmpty())
        {
            final Label viLabel = new Label("No virtual inputs");
            viLabel.setSclass("no-virtual-sockets");
            cntInner.appendChild(viLabel);
        }
        else
        {
            cntInner.appendChild(new Label("Virtual inputs:"));
            for(final WidgetSocket socket : sockets)
            {
                appendVirtualSocketLine(widget, socket, true, cntInner);
            }
        }
        sockets = widget.getVirtualOutputs();
        if(sockets.isEmpty())
        {
            final Label voLabel = new Label("No virtual outputs");
            voLabel.setSclass("no-virtual-sockets");
            cntInner.appendChild(voLabel);
        }
        else
        {
            cntInner.appendChild(new Label("Virtual outputs:"));
            for(final WidgetSocket socket : sockets)
            {
                appendVirtualSocketLine(widget, socket, false, cntInner);
            }
        }
    }


    private static void renderAccessRestrictionTabContent(final Widget widget, final Component content)
    {
        final Hbox accessRestrictionLine = new Hbox();
        accessRestrictionLine.setStyle(INLINE_CSS);
        accessRestrictionLine.setWidth(WIDTH_100_PERCENT);
        accessRestrictionLine.setWidths(WIDTH_45_AUTO);
        final Label label = new Label("Access Restrictions: ");
        label.setTooltiptext("Comma separated list of authority groups eligible to use the widget...");
        accessRestrictionLine.appendChild(label);
        final Textbox accessRestrictionbox = new Textbox();
        if(CollectionUtils.isNotEmpty(widget.getAccessRestrictions()))
        {
            accessRestrictionbox.setValue(StringUtils.join(widget.getAccessRestrictions().toArray(), ", "));
        }
        accessRestrictionLine.appendChild(accessRestrictionbox);
        accessRestrictionbox.addEventListener(Events.ON_CHANGE, (final InputEvent inputEvent) -> {
            final String restrictions = inputEvent.getValue();
            final List<String> restrictionList = new ArrayList<>();
            if(StringUtils.isNotBlank(restrictions))
            {
                restrictionList.addAll(Arrays.asList(StringUtils.split(restrictions, ',')));
            }
            widget.setAccessRestrictions(restrictionList);
        });
        content.appendChild(accessRestrictionLine);
    }


    private static void appendVirtualSocketLine(final Widget widget, final WidgetSocket socket, final boolean input,
                    final Component parent)
    {
        final Hbox line = new Hbox();
        line.setSclass("settingsRow");
        line.setStyle(INLINE_CSS);
        line.setWidth(WIDTH_100_PERCENT);
        line.setWidths("45%, auto, 40px");
        final String keyLabel = socket.getId();
        line.appendChild(new Label(keyLabel));
        line.setTooltiptext(socket.toString());
        final StringBuilder valueLabel = new StringBuilder(socket.getDataType());
        if(socket.getDataMultiplicity() != null)
        {
            valueLabel.append(" (").append(socket.getDataMultiplicity()).append(")");
        }
        if(socket.getVisibility() != null)
        {
            valueLabel.append(" ").append(socket.getVisibility());
        }
        line.appendChild(new Label(valueLabel.toString()));
        final Button removeButton = new Button();
        removeButton.setSclass(DELETE_BUTTON_SCLASS);
        line.appendChild(removeButton);
        removeButton.setTooltiptext("Remove");
        removeButton.addEventListener(Events.ON_CLICK, event -> {
            removeVirtualSocket(widget, socket, input);
            line.detach();
        });
        parent.appendChild(line);
    }


    private static void removeVirtualSocket(final Widget widget, final WidgetSocket socket, final boolean input)
    {
        if(input)
        {
            widget.removeVirtualInput(socket);
        }
        else
        {
            widget.removeVirtualOutput(socket);
        }
        final List<WidgetConnection> connections = new ArrayList<>(input ? widget.getInConnections() : widget.getOutConnections());
        connections.stream().filter(connection -> socket.getId().equals(input ? connection.getInputId() : connection.getOutputId()))
                        .forEach(connection -> {
                            if(input)
                            {
                                widget.removeInConnection(connection);
                            }
                            else
                            {
                                widget.removeOutConnection(connection);
                            }
                            final Widget other = (input ? connection.getSource() : connection.getTarget());
                            if(other != null)
                            {
                                if(input)
                                {
                                    other.removeOutConnection(connection);
                                }
                                else
                                {
                                    other.removeInConnection(connection);
                                }
                            }
                        });
    }


    private boolean hasSocket(final Widget widget, final String id, final boolean input)
    {
        final WidgetDefinition definition = getWidgetDefinition(widget);
        final List<WidgetSocket> sockets = (input ? WidgetSocketUtils.getAllInputs(widget, definition)
                        : WidgetSocketUtils.getAllOutputs(widget, definition));
        for(final WidgetSocket socket : sockets)
        {
            if(id.equalsIgnoreCase(socket.getId()))
            {
                return true;
            }
        }
        return false;
    }


    private static void appendLocalizationLine(final Component line, final Component parent)
    {
        final Object attribute = line.getAttribute("keylabel");
        if(attribute instanceof String)
        {
            Object compContainer = parent.getAttribute(String.valueOf(attribute));
            if(!(compContainer instanceof Component))
            {
                compContainer = new Div();
                parent.appendChild((Component)compContainer);
                parent.setAttribute(String.valueOf(attribute), compContainer);
            }
            ((Component)compContainer).appendChild(line);
        }
    }


    private Component createSettingsLine(final String setting, final boolean locLabel, final Widget widget)
    {
        final TypedSettingsMap widgetSettings = widget.getWidgetSettings();
        final Hbox line = new Hbox();
        line.setSclass("settingsRow");
        line.setStyle(INLINE_CSS);
        line.setWidth(WIDTH_100_PERCENT);
        line.setWidths("45%, auto, 40px");
        String keyLabel = setting;
        if(locLabel)
        {
            final String lang;
            if(keyLabel.startsWith("labels_"))
            {
                final String[] split = keyLabel.split("\\.");
                lang = split[0].substring("labels_".length());
                keyLabel = setting.substring(split[0].length() + 1);
            }
            else
            {
                lang = "en";
                keyLabel = setting.substring("labels.".length());
            }
            line.setAttribute("keylabel", keyLabel);
            keyLabel += " (" + lang + ")";
        }
        line.appendChild(new Label(keyLabel + ": "));
        line.setTooltiptext(setting);
        final Component viewComp = typedSettingsRenderer.renderSetting(widgetSettings, setting, hasConnections(widget));
        if(viewComp != null)
        {
            line.appendChild(viewComp);
        }
        final Button removeButton = new Button();
        removeButton.setSclass(DELETE_BUTTON_SCLASS);
        line.appendChild(removeButton);
        removeButton.setTooltiptext("Remove / Reset to default");
        removeButton.addEventListener(Events.ON_CLICK, event -> {
            widgetSettings.remove(setting);
            line.detach();
        });
        return line;
    }


    @Override
    public void showAddWidgetWizard(final Component ref, final EventListener<Event> selectListener)
    {
        final Window window = new Window("Choose widget", "normal", true);
        window.setSclass("chooseWidgetWizard");
        window.setTop("100px");
        window.setParent(getRoot());
        window.setVisible(true);
        window.setAttribute("selectListener", selectListener);
        Executions.createComponents("/addWidgetWizard.zul", window, Collections.emptyMap());
        window.doHighlighted();
    }


    protected List<String[]> parseSocketInfo(final String propsStr)
    {
        final List<String[]> ret = new ArrayList<>();
        final String[] split = StringUtils.split(propsStr, ";");
        if(split != null)
        {
            for(final String string : split)
            {
                String singleSocketInfo = string.trim();
                singleSocketInfo = StringUtils.replaceEach(singleSocketInfo, new String[]
                                {"[", "]"}, new String[]
                                {"", ""});
                final String[] split2 = StringUtils.split(singleSocketInfo, ",");
                final String[] entry = new String[ENTRY_INFO_SIZE];
                entry[1] = "java.lang.Object";
                entry[0] = split2[0];
                if(split2.length > 1 && StringUtils.isNotBlank(split2[1]))
                {
                    entry[1] = split2[1];
                }
                if(split2.length > 2)
                {
                    entry[2] = split2[2];
                }
                ret.add(entry);
            }
        }
        return ret;
    }


    private static Widgetslot getRootWidget(final Component ref)
    {
        Component root = ref;
        Widgetslot rootWidget = (Widgetslot)(root instanceof Widgetslot ? root : null);
        while(root.getParent() != null)
        {
            root = root.getParent();
            if(root instanceof Widgetslot)
            {
                rootWidget = (Widgetslot)root;
            }
        }
        return rootWidget;
    }


    private Component getRoot()
    {
        return widgetUtils.getRoot();
    }


    @Override
    public void setWidgetToolbarColor(final String color, final Component ref)
    {
        getSessionService().setAttribute("widgetToolbarColor", color);
        final Widgetslot rootWidget = getRootWidget(ref);
        if(rootWidget != null)
        {
            rootWidget.updateView();
        }
    }


    @Override
    public boolean canReceiveFrom(final WidgetSocket inputSocket, final Widget targetWidget, final WidgetSocket outputSocket,
                    final Widget srcWidget)
    {
        return socketConnectionService.canReceiveFrom(inputSocket, targetWidget, outputSocket, srcWidget);
    }


    @Override
    public boolean canReceiveFrom(final WidgetSocket inputSocket, final WidgetSocket outputSocket)
    {
        return canReceiveFrom(inputSocket, inputSocket.getDeclaringWidget(), outputSocket, outputSocket.getDeclaringWidget());
    }


    protected CockpitSessionService getSessionService()
    {
        return sessionService;
    }


    @Required
    public void setSessionService(final CockpitSessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    private WidgetService getWidgetService()
    {
        return widgetService;
    }


    @Required
    public void setWidgetService(final WidgetService widgetService)
    {
        this.widgetService = widgetService;
    }


    private CockpitComponentDefinitionService getWidgetDefinitionService()
    {
        return widgetDefinitionService;
    }


    @Required
    public void setWidgetDefinitionService(final CockpitComponentDefinitionService widgetDefinitionService)
    {
        this.widgetDefinitionService = widgetDefinitionService;
    }


    public NotificationStack getNotificationStack()
    {
        return notificationStack;
    }


    @Required
    public void setNotificationStack(final NotificationStack notificationStack)
    {
        this.notificationStack = notificationStack;
    }


    public ModalWindowStack getModalWindowStack()
    {
        return modalWindowStack;
    }


    @Required
    public void setModalWindowStack(final ModalWindowStack modalWindowStack)
    {
        this.modalWindowStack = modalWindowStack;
    }


    private WidgetPersistenceService getWidgetPersistenceService()
    {
        return widgetPersistenceService;
    }


    @Required
    public void setWidgetPersistenceService(final WidgetPersistenceService widgetPersistenceService)
    {
        this.widgetPersistenceService = widgetPersistenceService;
    }


    private XMLWidgetPersistenceService getXmlBasedWidgetPersistenceService()
    {
        return xmlBasedWidgetPersistenceService;
    }


    @Required
    public void setXmlBasedWidgetPersistenceService(final XMLWidgetPersistenceService xmlBasedWidgetPersistenceService)
    {
        this.xmlBasedWidgetPersistenceService = xmlBasedWidgetPersistenceService;
    }


    @Required
    public void setWidgetUtils(final WidgetUtils widgetUtils)
    {
        this.widgetUtils = widgetUtils;
    }


    @Required
    public void setWidgetInstanceService(final WidgetInstanceService widgetInstanceService)
    {
        this.widgetInstanceService = widgetInstanceService;
    }


    @Required
    public void setSessionWidgetInstanceRegistry(final SessionWidgetInstanceRegistry sessionWidgetInstanceRegistry)
    {
        this.sessionWidgetInstanceRegistry = sessionWidgetInstanceRegistry;
    }


    @Required
    public void setWidgetLibUtils(final WidgetLibUtils widgetLibUtils)
    {
        this.widgetLibUtils = widgetLibUtils;
    }


    @Required
    public void setSocketConnectionService(final SocketConnectionService socketConnectionService)
    {
        this.socketConnectionService = socketConnectionService;
    }


    @Required
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }


    @Required
    public void setTypedSettingsRenderer(final TypedSettingsRenderer typedSettingsRenderer)
    {
        this.typedSettingsRenderer = typedSettingsRenderer;
    }


    /**
     * @return the adminModeAuthorityGroupService
     */
    protected AuthorityGroupService getAdminModeAuthorityGroupService()
    {
        return adminModeAuthorityGroupService;
    }


    /**
     * @param adminModeAuthorityGroupService
     *           the adminModeAuthorityGroupService to set
     */
    public void setAdminModeAuthorityGroupService(final AuthorityGroupService adminModeAuthorityGroupService)
    {
        this.adminModeAuthorityGroupService = adminModeAuthorityGroupService;
    }


    /**
     * @param impersonationPreviewHelper
     *           the impersonationPreviewHelper to set
     */
    public void setImpersonationPreviewHelper(final ImpersonationPreviewHelper impersonationPreviewHelper)
    {
        this.impersonationPreviewHelper = impersonationPreviewHelper;
    }


    public void setCockpitUserService(final CockpitUserService cockpitUserService)
    {
        this.cockpitUserService = cockpitUserService;
    }


    @Override
    public boolean isShowConnectionsFlagEnabled()
    {
        final Object attribute = getSessionService().getAttribute(COCKPIT_SHOW_CONNECTIONS_FLAG);
        if(attribute == null)
        {
            getSessionService().setAttribute(COCKPIT_SHOW_CONNECTIONS_FLAG, Boolean.FALSE);
        }
        return Boolean.TRUE.equals(attribute);
    }


    @Override
    public void setShowConnectionsFlagEnabled(final boolean enabled)
    {
        getSessionService().setAttribute(COCKPIT_SHOW_CONNECTIONS_FLAG, Boolean.valueOf(enabled));
    }


    @Override
    public void toggleAdminMode(final Component slot)
    {
        if(isAdminModePermitted())
        {
            Clients.evalJavaScript("cngResetConnections();");
            setAdminMode(!isAdminMode(), slot);
            Events.echoEvent("onInvalidateLater", slot.getParent(), null);
            final Component roleIndicatorOverlay = slot.getParent().getFellowIfAny("roleIndicatorOverlay");
            if(roleIndicatorOverlay != null)
            {
                Events.postEvent("onUpdate", roleIndicatorOverlay, null);
            }
            final Component roleSelector = slot.getParent().getFellowIfAny("roleSelector");
            if(roleSelector != null)
            {
                Events.postEvent("onUpdate", roleSelector, null);
            }
        }
    }
}
