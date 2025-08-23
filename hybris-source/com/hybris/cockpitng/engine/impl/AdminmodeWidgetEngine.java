/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.admin.CockpitAdminService;
import com.hybris.cockpitng.components.Widgetchildren;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetConnection;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.persistence.WidgetPersistenceService;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.engine.ConnectButtonRenderer;
import com.hybris.cockpitng.engine.SymbolicModeRenderer;
import com.hybris.cockpitng.engine.WidgetChildrenContainerRenderer;
import com.hybris.cockpitng.engine.WidgetToolbarRenderer;
import com.hybris.cockpitng.engine.WidgetWizardCreationDelegate;
import com.hybris.cockpitng.util.WidgetTreeUIUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

/**
 * Adds additional admin mode functionality to the {@link DefaultCockpitWidgetEngine}.
 */
public class AdminmodeWidgetEngine extends DefaultCockpitWidgetEngine
{
    public static final String NO_SLOT_ID = "[none]";
    public static final String DRAGGED_WIDGET = "draggedWidget";
    public static final String TOOLBAR_COMPONENT = "toolbarComponent";
    public static final String ADMIN_FULLSCREEN = "cp_admin_fullscreen";
    public static final String WIDGET_TOKEN = "widget";
    public static final String CP_INVISIBLE_CONTAINER_VISIBLE = "#cp_invisibleContainer_visible";
    private static final char ASTERISK = '*';
    private static final Logger LOG = LoggerFactory.getLogger(AdminmodeWidgetEngine.class);
    private CockpitAdminService cockpitAdminService;
    private WidgetPersistenceService widgetPersistenceService;
    private CockpitComponentDefinitionService cockpitComponentDefinitionService;
    private SymbolicModeRenderer symbolicModeRenderer;
    private ConnectButtonRenderer connectButtonRenderer;
    private WidgetToolbarRenderer widgetToolbarRenderer;
    private WidgetWizardCreationDelegate widgetWizardCreationDelegate;


    static boolean isEqualOrParentOf(final Widget parent, final Widget child)
    {
        if(parent == null || child == null)
        {
            return false;
        }
        Widget widget = child;
        while(widget != null && !widget.equals(parent))
        {
            widget = widget.getParent();
        }
        return parent.equals(widget);
    }


    @Override
    public void createWidgetView(final Widgetslot widgetslot)
    {
        super.createWidgetView(widgetslot);
        if(!cockpitAdminService.isAdminMode())
        {
            return;
        }
        if(cockpitAdminService.isSymbolicAdminMode())
        {
            if(isEmptyRootSlot(widgetslot))
            {
                createAddWidgetButtonInSlot(widgetslot);
            }
        }
        else
        {
            final Widget currentWidget = getWidget(widgetslot);
            if(checkVisibility(currentWidget))
            {
                final WidgetDefinition widgetDefinition = getWidgetDefinition(currentWidget);
                getWidgetToolbarRenderer().appendWidgetToolbar(widgetslot, widgetDefinition, widgetslot);
                appendSingleSlotToolbar(widgetslot, currentWidget);
            }
            else
            {
                appendSingleSlotToolbar(widgetslot, null);
            }
            if(isFullScreen(currentWidget))
            {
                widgetslot.appendChild(createWindow(widgetslot, currentWidget));
            }
        }
    }


    protected boolean isEmptyRootSlot(final Widgetslot widgetslot)
    {
        final boolean widgetInstanceIsNull = widgetslot.getWidgetInstance() == null;
        final boolean parentWidgetInstanceIsNull = widgetslot.getParentWidgetInstance() == null;
        final boolean widgetIsNotVisible = !checkVisibility(getWidget(widgetslot));
        return (widgetInstanceIsNull || widgetIsNotVisible) && parentWidgetInstanceIsNull;
    }


    protected void createAddWidgetButtonInSlot(final Widgetslot widgetslot)
    {
        final Toolbarbutton addButton = createAddWidgetButton(widgetslot);
        widgetslot.appendChild(addButton);
        widgetslot.setDroppable(WIDGET_TOKEN);
        widgetslot.addEventListener(Events.ON_DROP, createDropListener(widgetslot));
    }


    protected Toolbarbutton createAddWidgetButton(final Widgetslot widgetslot)
    {
        final Toolbarbutton addButton = new Toolbarbutton();
        addButton.setLabel("Add main widget...");
        addButton.setSclass("addMainSlotWidgetBtn");
        final EventListener<Event> listener = getWidgetWizardCreationDelegate().createAddWidgetWizardSelectListener(widgetslot,
                        widgetslot.getSlotID());
        addButton.addEventListener(Events.ON_CLICK, event -> getCockpitAdminService().showAddWidgetWizard(widgetslot, listener));
        return addButton;
    }


    protected boolean isFullScreen(final Widget currentWidget)
    {
        if(currentWidget == null)
        {
            return false;
        }
        return cockpitAdminService.isAdminMode() && currentWidget.getWidgetSettings().getBoolean(ADMIN_FULLSCREEN);
    }


    protected Window createWindow(final Widgetslot widgetslot, final Widget currentWidget)
    {
        final Window window = new Window(String.valueOf(currentWidget), "normal", true);
        window.addEventListener(Events.ON_DOUBLE_CLICK, STOP_PROPAGATION_LISTENER);
        window.setShadow(true);
        window.setSclass(CockpitAdminService.ADMINMODE_SCLASS);
        window.setWidth("90%");
        window.setHeight("90%");
        final Div widgetCnt = new Div();
        widgetCnt.setHeight("100%");
        widgetCnt.setStyle("position: relative;");
        window.appendChild(widgetCnt);
        window.addEventListener(Events.ON_CLOSE, event -> onWindowClosed(widgetslot, currentWidget));
        window.doHighlighted();
        final List<?> arrayList = new ArrayList<>(widgetslot.getChildren());
        arrayList.stream() //
                        .filter(child -> child instanceof Component) //
                        .forEach(child -> widgetCnt.appendChild((Component)child));
        return window;
    }


    private void onWindowClosed(final Widgetslot widgetslot, final Widget currentWidget)
    {
        currentWidget.getWidgetSettings().put(ADMIN_FULLSCREEN, String.valueOf(false));
        final Widgetslot parentWidgetslot = WidgetTreeUIUtils.getParentWidgetslot(widgetslot);
        if(parentWidgetslot == null)
        {
            widgetslot.updateView();
        }
        else
        {
            parentWidgetslot.updateView();
        }
    }


    @Override
    protected String getWidgetBodySclass(final WidgetInstance widgetInstance)
    {
        String result = super.getWidgetBodySclass(widgetInstance);
        if(widgetInstance.getWidget().isTemplate())
        {
            result += " widgetTemplate";
        }
        return result;
    }


    @Override
    protected void createComponents(final Widgetslot widgetslot)
    {
        if(cockpitAdminService.isSymbolicAdminMode())
        {
            getSymbolicModeRenderer().render(widgetslot, this);
        }
        else
        {
            super.createComponents(widgetslot);
        }
    }


    boolean isComposed(final Widget widget)
    {
        final WidgetDefinition componentDefinition = (WidgetDefinition)cockpitComponentDefinitionService
                        .getComponentDefinitionForCode(widget.getWidgetDefinitionId());
        return componentDefinition.getComposedWidgetRoot() == null;
    }


    boolean isStub(final Widget widget)
    {
        final WidgetDefinition componentDefinition = (WidgetDefinition)cockpitComponentDefinitionService
                        .getComponentDefinitionForCode(widget.getWidgetDefinitionId());
        return componentDefinition.isStubWidget();
    }


    @Override
    protected void renderUnattachedChildren(final Widgetslot widgetslot, final List<Widget> additionalWidgets)
    {
        super.renderUnattachedChildren(widgetslot, additionalWidgets);
        final Component invisibleContainer = widgetslot.getInvisibleContainer();
        if(invisibleContainer != null && cockpitAdminService.isAdminMode() && widgetslot.getDesktop() != null)
        {
            final boolean visible = Boolean.TRUE.equals(
                            widgetslot.getDesktop().getAttribute(CP_INVISIBLE_CONTAINER_VISIBLE + widgetslot.getWidgetInstance().getId()));
            invisibleContainer.setVisible(visible);
            getWidgetToolbarRenderer().setInvisibleContainerVisible(widgetslot, invisibleContainer.isVisible());
        }
    }


    Widget getWidget(final Widgetslot widgetslot)
    {
        final WidgetInstance widgetInstance = widgetslot.getWidgetInstance();
        return widgetInstance == null ? null : widgetInstance.getWidget();
    }


    protected EventListener<Event> createDropListener(final Widgetslot widgetslot)
    {
        return createDropListener(widgetslot, null);
    }


    protected EventListener<Event> createDropListener(final Widgetslot widgetslot, final Widget currentWidget)
    {
        return event -> {
            if(!(event instanceof DropEvent))
            {
                return;
            }
            event.stopPropagation();
            final Object attribute = ((DropEvent)event).getDragged().getAttribute(DRAGGED_WIDGET);
            final boolean canProcess = attribute instanceof Widget
                            && !AdminmodeWidgetEngine.this.getCockpitAdminService().isSymbolicAdminMode();
            if(!canProcess)
            {
                return;
            }
            final Widget widget = (Widget)attribute;
            if(isStub(widget))
            {
                return;
            }
            if(currentWidget == null)
            {
                moveWidget(widgetslot, widget);
            }
            else if(isEqualOrParentOf(currentWidget, widget))
            {
                showMessageBox(widgetslot, widget);
            }
            else
            {
                if(isEqualOrParentOf(widget, currentWidget))
                {
                    Messagebox.show("You can not move widget to one of its children.");
                    return;
                }
                swapWidgets(widgetslot, widget);
            }
        };
    }


    private void showMessageBox(final Widgetslot widgetslot, final Widget widget)
    {
        Messagebox.show("Target slot is not empty. Do you want to replace the widget?", "Moving widget",
                        Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, messageBoxEvent -> {
                            if(Integer.valueOf(Messagebox.YES).equals(messageBoxEvent.getData()))
                            {
                                moveWidget(widgetslot, widget);
                            }
                        });
    }


    protected void appendSingleSlotToolbar(final Widgetslot widgetslot, final Widget currentWidget)
    {
        final String slotId = widgetslot.getSlotID();
        if(!canAppendSingleSlotToolbar(currentWidget, slotId))
        {
            return;
        }
        final Div widgetToolbar = new Div();
        widgetToolbar.setSclass("widget_toolbar");
        final Label label = new Label(slotId);
        label.setSclass("widgetSlot_label");
        widgetToolbar.appendChild(label);
        final Div toolbarDiv = new Div();
        toolbarDiv.setSclass(TOOLBAR_COMPONENT);
        final Toolbarbutton addBtn = new Toolbarbutton();
        addBtn.setSclass("addWidgetBtn");
        addBtn.addEventListener(Events.ON_CLICK, event -> getCockpitAdminService().showAddWidgetWizard(widgetslot,
                        getWidgetWizardCreationDelegate().createAddWidgetWizardSelectListener(widgetslot, slotId)));
        if(currentWidget == null)
        {
            toolbarDiv.appendChild(addBtn);
        }
        if(!cockpitAdminService.isSymbolicAdminMode())
        {
            final EventListener<Event> dropListener = createDropListener(widgetslot, currentWidget);
            widgetslot.setDroppable(WIDGET_TOKEN);
            widgetslot.addEventListener(Events.ON_DROP, dropListener);
            widgetToolbar.setDroppable(WIDGET_TOKEN);
            widgetToolbar.addEventListener(Events.ON_DROP, dropListener);
        }
        widgetToolbar.appendChild(toolbarDiv);
        widgetslot.insertBefore(widgetToolbar, widgetslot.getFirstChild());
    }


    private boolean canAppendSingleSlotToolbar(final Widget currentWidget, final String slotId)
    {
        return slotId != null && cockpitAdminService.isAdminMode() && (currentWidget == null || !currentWidget.isPartOfGroup());
    }


    void moveWidget(final Widgetslot targetWidgetslot, final String targetSlotID, final Widget draggedWidget)
    {
        final Widget oldParentWidget = draggedWidget.getParent();
        final Widget targetWidget = targetWidgetslot == null ? null : getWidget(targetWidgetslot);
        final Widget currentWidgetInTargetSlot = getVisibleWidget(targetWidgetslot, targetSlotID);
        final Widget widgetToRemove = getEmptySlotIds(targetWidgetslot).contains(targetSlotID) ? null : currentWidgetInTargetSlot;
        doMoveWidget(targetWidgetslot, targetWidget, targetSlotID, draggedWidget, oldParentWidget, widgetToRemove);
    }


    void moveWidget(final Widgetslot dropTargetWidgetslot, final Widget draggedWidget)
    {
        final Widget oldParentWidget = draggedWidget.getParent();
        final String dropTargetSlotID = dropTargetWidgetslot.getSlotID();
        final Widgetslot targetParentWidgetslot = getParentWidgetslot(dropTargetWidgetslot);
        final Widget targetWidget = targetParentWidgetslot == null ? null : getWidget(targetParentWidgetslot);
        final Widget currentWidgetInTargetSlot = getWidget(dropTargetWidgetslot);
        doMoveWidget(dropTargetWidgetslot, targetWidget, dropTargetSlotID, draggedWidget, oldParentWidget,
                        currentWidgetInTargetSlot);
    }


    protected void doMoveWidget(final Widgetslot targetWidgetslot, final Widget targetWidget, final String targetSlotID,
                    final Widget draggedWidget, final Widget oldParentWidget, final Widget widgetToRemove)
    {
        if(targetWidgetslot == null || draggedWidget == null)
        {
            return;
        }
        getWidgetService().moveWidget(draggedWidget, targetWidget);
        draggedWidget.setSlotId(targetSlotID);
        if(oldParentWidget == null)
        {
            getWidgetPersistenceService().deleteWidgetTree(draggedWidget);
        }
        else
        {
            getWidgetPersistenceService().storeWidgetTree(oldParentWidget);
        }
        if(targetWidget == null)
        {
            // root widgetslot
            if(widgetToRemove != null)
            {
                getWidgetPersistenceService().deleteWidgetTree(widgetToRemove);
                getWidgetService().removeWidget(widgetToRemove);
            }
            getWidgetPersistenceService().deleteWidgetTree(draggedWidget);
            draggedWidget.setId(targetSlotID);
            getWidgetPersistenceService().storeWidgetTree(draggedWidget);
            targetWidgetslot.setWidgetInstance(null);
        }
        else
        {
            if(widgetToRemove != null)
            {
                getWidgetService().removeWidget(widgetToRemove);
            }
            getWidgetPersistenceService().storeWidgetTree(targetWidget);
        }
        cockpitAdminService.removeWidgetFromClipboard(draggedWidget);
        WidgetTreeUIUtils.updateRootWidget(targetWidgetslot, false);
    }


    protected Widget getVisibleWidget(final Widgetslot targetWidgetslot, final String targetSlotID)
    {
        return WidgetTreeUIUtils.getVisibleWidget(targetWidgetslot, targetSlotID, this::checkVisibility);
    }


    protected Widgetslot getParentWidgetslot(final Widgetslot widgetslot)
    {
        return WidgetTreeUIUtils.getParentWidgetslot(widgetslot);
    }


    private void swapWidgets(final Widgetslot dropTargetWidgetslot, final Widget draggedWidget)
    {
        final Widget oldParent = draggedWidget.getParent();
        final String oldSlotId = draggedWidget.getSlotId();
        final String slotID = dropTargetWidgetslot.getSlotID();
        final Widget currentWidget = getWidget(dropTargetWidgetslot);
        final Widget currentParentWidget = currentWidget == null ? null : currentWidget.getParent();
        getWidgetService().moveWidget(draggedWidget, currentParentWidget);
        draggedWidget.setSlotId(slotID);
        if(oldParent != null && currentWidget != null)
        {
            currentWidget.setSlotId(oldSlotId);
            oldParent.addChild(currentWidget);
            currentWidget.setParent(oldParent);
            getWidgetPersistenceService().storeWidgetTree(oldParent);
        }
        if(currentParentWidget == null)
        {
            // root widgetslot
            getWidgetPersistenceService().storeWidgetTree(draggedWidget);
            dropTargetWidgetslot.setWidgetInstance(null);
        }
        else
        {
            currentParentWidget.removeChild(currentWidget);
            getWidgetPersistenceService().storeWidgetTree(currentParentWidget);
        }
        WidgetTreeUIUtils.updateRootWidget(dropTargetWidgetslot, false);
    }


    @Override
    public void createWidgetView(final Widgetchildren widgetChildrenComponent, final Map<String, Object> ctx)
    {
        if(cockpitAdminService.isAdminMode())
        {
            final Map<String, Object> adminCtx = new HashMap<>(ctx);
            adminCtx.put("adminMode", Boolean.TRUE);
            final List<WidgetInstance> children = getWidgetInstanceFacade()
                            .getWidgetInstances(widgetChildrenComponent.getParentWidgetInstance());
            final List<WidgetInstance> adminChildren = getFilteredWidgetInstances(children);
            if(cockpitAdminService.isSymbolicAdminMode())
            {
                final WidgetChildrenContainerRenderer containerRenderer = getChildrenContainerRenderer().get(Widgetchildren.SYMBOLIC);
                if(containerRenderer == null)
                {
                    LOG.error("No renderer found for type '{}'", Widgetchildren.SYMBOLIC);
                }
                else
                {
                    containerRenderer.render(widgetChildrenComponent, adminChildren, adminCtx);
                }
            }
            else
            {
                super.createWidgetView(widgetChildrenComponent, adminCtx);
            }
        }
        else
        {
            super.createWidgetView(widgetChildrenComponent, ctx);
        }
        final Widget parentWidget = widgetChildrenComponent.getParentWidgetContainer().getWidgetInstance().getWidget();
        if(canAppendChildrenSlotToolbar(parentWidget))
        {
            appendChildrenSlotToolbar(widgetChildrenComponent);
        }
    }


    protected boolean canAppendChildrenSlotToolbar(final Widget parentWidget)
    {
        if(cockpitAdminService.isAdminMode())
        {
            if(parentWidget == null)
            {
                return true;
            }
            if(isComposed(parentWidget) && !parentWidget.isPartOfGroup() && !getWidgetUtils().isPartOfComposedWidget(parentWidget))
            {
                return true;
            }
        }
        return false;
    }


    /**
     * filter out widget instance that shouldn't be displayed in admin mode
     *
     * @param children
     *           widget instances
     * @return filtered widget instances
     */
    protected List<WidgetInstance> getFilteredWidgetInstances(final List<WidgetInstance> children)
    {
        final Set<Widget> widgets = new HashSet<>();
        final List<WidgetInstance> adminChildren = new ArrayList<>();
        for(final WidgetInstance widgetInstance : children)
        {
            if(widgetInstance != null && !widgets.contains(widgetInstance.getWidget()))
            {
                widgets.add(widgetInstance.getWidget());
                adminChildren.add(widgetInstance);
            }
        }
        return adminChildren;
    }


    protected void appendChildrenSlotToolbar(final Widgetchildren widgetChildrenComponent)
    {
        final String slotID = widgetChildrenComponent.getSlotID();
        final Widget parentWidget = widgetChildrenComponent.getParentWidgetContainer().getWidgetInstance().getWidget();
        final Div widgetToolbar = new Div();
        widgetToolbar.setSclass("widget_toolbar");
        final Label label = new Label("[" + (slotID == null ? "unnamed" : slotID) + "]");
        label.setSclass("widgetSlot_label");
        widgetToolbar.appendChild(label);
        final Div toolbarDiv = new Div();
        toolbarDiv.setSclass(TOOLBAR_COMPONENT);
        final Toolbarbutton addBtn = new Toolbarbutton();
        addBtn.setSclass("addWidgetBtn");
        addBtn.addEventListener(Events.ON_CLICK, event -> {
            final EventListener<Event> eventListener = getWidgetWizardCreationDelegate() //
                            .createAddWidgetWizardEventListener(slotID, parentWidget, () -> { //
                                widgetChildrenComponent.getChildren().clear();
                                createWidgetView(widgetChildrenComponent);
                            });
            cockpitAdminService.showAddWidgetWizard(widgetChildrenComponent, eventListener);
        });
        toolbarDiv.appendChild(addBtn);
        widgetToolbar.appendChild(toolbarDiv);
        final EventListener<Event> eventListener = createOnWidgetDropEventListener(widgetChildrenComponent, slotID, parentWidget);
        if(!cockpitAdminService.isSymbolicAdminMode())
        {
            widgetChildrenComponent.setDroppable(WIDGET_TOKEN);
            widgetChildrenComponent.addEventListener(Events.ON_DROP, eventListener);
            widgetToolbar.setDroppable(WIDGET_TOKEN);
            widgetToolbar.addEventListener(Events.ON_DROP, eventListener);
        }
        widgetChildrenComponent.insertBefore(widgetToolbar, widgetChildrenComponent.getFirstChild());
    }


    private EventListener<Event> createOnWidgetDropEventListener(final Widgetchildren widgetChildrenComponent, final String slotID,
                    final Widget parentWidget)
    {
        return event -> handleDropEvent(widgetChildrenComponent, slotID, parentWidget, event);
    }


    private void handleDropEvent(final Widgetchildren widgetChildrenComponent, final String slotID, final Widget parentWidget,
                    final Event event)
    {
        if(!(event instanceof DropEvent))
        {
            return;
        }
        event.stopPropagation();
        final Object attribute = ((DropEvent)event).getDragged().getAttribute(DRAGGED_WIDGET);
        if(!(attribute instanceof Widget))
        {
            return;
        }
        final Widget draggedNode = (Widget)attribute;
        final Widget oldParent = draggedNode.getParent();
        if(isEqualOrParentOf(draggedNode, parentWidget))
        {
            Messagebox.show("You can not move widget to one of its children.");
            return;
        }
        getCockpitAdminService().removeWidgetFromClipboard(draggedNode);
        getWidgetPersistenceService().deleteWidgetTree(draggedNode);
        getWidgetService().moveWidget(draggedNode, parentWidget);
        draggedNode.setSlotId(slotID);
        if(oldParent != null)
        {
            getWidgetPersistenceService().storeWidgetTree(oldParent);
        }
        if(parentWidget != null)
        {
            getWidgetPersistenceService().storeWidgetTree(parentWidget);
        }
        if(WidgetTreeUIUtils.getParentWidgetslot(widgetChildrenComponent) != null)
        {
            WidgetTreeUIUtils.updateRootWidget(WidgetTreeUIUtils.getParentWidgetslot(widgetChildrenComponent), false);
        }
    }


    /**
     * Returns IDs of widget slots that are available for new widgets.
     *
     * @param widgetslot
     * @return List of widget slot IDs.
     */
    public List<String> getEmptySlotIds(final Widgetslot widgetslot)
    {
        final List<Widget> visibleChildren = getVisibleWidgets(widgetslot);
        final Set<String> filledSlotIds = new HashSet<>();
        for(final Widget child : visibleChildren)
        {
            filledSlotIds.add(child.getSlotId());
        }
        final List<String> allSlotIds = getAllSlotIds(widgetslot);
        final List<String> ret = new ArrayList<>();
        for(final String string : allSlotIds)
        {
            if(string.charAt(0) == ASTERISK)
            {
                ret.add(string.substring(1));
            }
            else if(!filledSlotIds.contains(string))
            {
                ret.add(string);
            }
        }
        return ret;
    }


    /**
     * Returns list of widget, children of the given widget slot, that are visible according to
     * {@link #checkVisibility(Widget)} method. By default - visible for the current authority group.
     *
     * @param widgetslot
     * @return {@link List} of {@link Widget}
     */
    public List<Widget> getVisibleWidgets(final Widgetslot widgetslot)
    {
        final List<Widget> children = widgetslot.getWidgetInstance().getWidget().getChildren();
        final List<Widget> visibleChildren = new ArrayList<>(children.size());
        if(CollectionUtils.isNotEmpty(children))
        {
            for(final Widget child : children)
            {
                if(checkVisibility(child))
                {
                    visibleChildren.add(child);
                }
            }
        }
        return visibleChildren;
    }


    protected List<String> getAllSlotIds(final Widgetslot widgetslot)
    {
        final Widgetslot clone = new Widgetslot();
        clone.setWidgetInstance(widgetslot.getWidgetInstance());
        clone.setSlotID(widgetslot.getSlotID());
        super.createComponents(clone);
        return getAllChildSlotIds(clone);
    }


    protected List<String> getAllChildSlotIds(final Component component)
    {
        final List<String> ret = new ArrayList<>();
        for(final Component child : component.getChildren())
        {
            if(child instanceof Widgetslot)
            {
                final String slotID = ((Widgetslot)child).getSlotID();
                ret.add(slotID == null ? NO_SLOT_ID : slotID);
            }
            else if(child instanceof Widgetchildren)
            {
                final String slotID = ((Widgetchildren)child).getSlotID();
                ret.add(ASTERISK + (slotID == null ? NO_SLOT_ID : slotID));
            }
            else
            {
                ret.addAll(getAllChildSlotIds(child));
            }
        }
        return ret;
    }


    /**
     * @deprecated since 6.7 - please use
     *             {@link DefaultWidgetToolbarRenderer#renderRemoveBtn(Component, Widgetslot, WidgetDefinition)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void renderRemoveBtn(final Component parent, final Widgetslot widgetCompnent, final Widget widget)
    {
        final WidgetDefinition widgetDefinition = getWidgetDefinition(widget);
        getWidgetToolbarRenderer().renderRemoveBtn(parent, widgetCompnent, widgetDefinition);
    }


    /**
     * @deprecated since 6.7 - please use {@link DefaultWidgetToolbarRenderer#confirmNeeded(Widget)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected boolean confirmNeeded(final Widget widget)
    {
        return getWidgetToolbarRenderer().confirmNeeded(widget);
    }


    /**
     * @deprecated since 6.7 - please use {@link DefaultWidgetToolbarRenderer#removeWidget(Widgetslot, Widget)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void removeWidget(final Widgetslot widgetCompnent, final Widget widget)
    {
        getWidgetToolbarRenderer().removeWidget(widgetCompnent, widget);
    }


    /**
     * @deprecated since 6.7 - please use
     *             {@link DefaultWidgetToolbarRenderer#setInvisibleContainerVisible(Widgetslot, boolean)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void setInvisibleContainerVisible(final Widgetslot widgetslot, final boolean value)
    {
        getWidgetToolbarRenderer().setInvisibleContainerVisible(widgetslot, value);
    }


    /**
     * @deprecated since 6.7 - please use
     *             {@link DefaultWidgetToolbarRenderer#renderComposedGroupButton(Component, Widgetslot)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void renderComposedGroupButton(final Component parent, final Widgetslot widgetslot)
    {
        getWidgetToolbarRenderer().renderComposedGroupButton(parent, widgetslot);
    }


    /**
     * @deprecated since 6.7 - please use {@link DefaultWidgetToolbarRenderer#appendSettingsButton(Component, Widgetslot)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void appendSettingsButton(final Component parent, final Widgetslot widgetslot)
    {
        getWidgetToolbarRenderer().appendSettingsButton(parent, widgetslot);
    }


    /**
     * @deprecated since 6.7 - please use
     *             {@link DefaultWidgetToolbarRenderer#appendWidgetToolbar(Component, WidgetDefinition, Widgetslot)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void appendWidgetToolbar(final Widgetslot widgetslot)
    {
        appendWidgetToolbar(widgetslot, widgetslot);
    }


    /**
     * @deprecated since 6.7 - please use
     *             {@link DefaultWidgetToolbarRenderer#appendWidgetToolbar(Component, WidgetDefinition, Widgetslot)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void appendWidgetToolbar(final Widgetslot widgetslot, final Component parent)
    {
        getWidgetToolbarRenderer().appendWidgetToolbar(parent, getWidgetDefinition(getWidget(widgetslot)), widgetslot);
    }


    /**
     * @deprecated since 6.7 - please use {@link DefaultWidgetWizardCreationDelegate#applyRoleRestrictions}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void applyRoleRestrictions(final Widget widget)
    {
        throw new UnsupportedOperationException(
                        "this method is deprecated since v 6.7, please use DefaultWidgetWizardCreationDelegate#applyRoleRestrictions");
    }


    /**
     * @deprecated since 6.7 - please use
     *             {@link DefaultWidgetToolbarRenderer#renderShowInvisibleChildrenButton(Component, Widgetslot)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void renderShowInvisibleChildrenButton(final Component parent, final Widgetslot widgetslot,
                    final Widget currentWidget)
    {
        getWidgetToolbarRenderer().renderShowInvisibleChildrenButton(parent, widgetslot);
    }


    /**
     * @deprecated since 6.7 - please use {@link DefaultConnectButtonRenderer#hasDisplayedConnections(Widget)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected boolean hasDisplayedConnections(final Widget widget)
    {
        return false;
    }


    /**
     * @deprecated since 6.7 - please use
     *             {@link DefaultConnectButtonRenderer#renderConnectButton(Component, Widget, Widgetslot)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void renderConnectBtn(final Component parent, final Widgetslot widgetslot)
    {
        getConnectButtonRenderer().renderConnectButton(parent, getWidget(widgetslot), widgetslot);
    }


    /**
     * @deprecated since 6.7 - please use
     *             {@link DefaultConnectButtonRenderer#renderConnectorEntry(Component, WidgetConnection)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void renderConnectorEntry(final Component parent, final WidgetConnection widgetConnection)
    {
        throw new UnsupportedOperationException(
                        "this method is deprecated since v 6.7, please use DefaultConnectButtonRenderer#renderConnectorEntry(Component, WidgetConnection)");
    }


    /**
     * @deprecated since 6.7 - please use
     *             {@link DefaultTemplateButtonRenderer#renderTemplateButton(Component, Widget, WidgetDefinition, Widgetslot)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void renderTemplateButton(final Component parent, final Widgetslot widgetslot)
    {
        throw new UnsupportedOperationException(
                        "this method is deprecated since v 6.7, please use DefaultTemplateButtonRenderer#renderTemplateButton(Component, Widget, WidgetDefinition, Widgetslot)");
    }


    public CockpitAdminService getCockpitAdminService()
    {
        return cockpitAdminService;
    }


    @Required
    public void setCockpitAdminService(final CockpitAdminService cockpitAdminService)
    {
        this.cockpitAdminService = cockpitAdminService;
    }


    @Required
    public void setCockpitComponentDefinitionService(final CockpitComponentDefinitionService cockpitComponentDefinitionService)
    {
        this.cockpitComponentDefinitionService = cockpitComponentDefinitionService;
    }


    @Override
    protected WidgetPersistenceService getWidgetPersistenceService()
    {
        return widgetPersistenceService;
    }


    @Required
    @Override
    public void setWidgetPersistenceService(final WidgetPersistenceService widgetPersistenceService)
    {
        this.widgetPersistenceService = widgetPersistenceService;
    }


    protected SymbolicModeRenderer getSymbolicModeRenderer()
    {
        return symbolicModeRenderer;
    }


    @Required
    public void setSymbolicModeRenderer(final SymbolicModeRenderer symbolicModeRenderer)
    {
        this.symbolicModeRenderer = symbolicModeRenderer;
    }


    public ConnectButtonRenderer getConnectButtonRenderer()
    {
        return connectButtonRenderer;
    }


    @Required
    public void setConnectButtonRenderer(final ConnectButtonRenderer connectButtonRenderer)
    {
        this.connectButtonRenderer = connectButtonRenderer;
    }


    public WidgetWizardCreationDelegate getWidgetWizardCreationDelegate()
    {
        return widgetWizardCreationDelegate;
    }


    @Required
    public void setWidgetWizardCreationDelegate(final WidgetWizardCreationDelegate widgetWizardCreationDelegate)
    {
        this.widgetWizardCreationDelegate = widgetWizardCreationDelegate;
    }


    public WidgetToolbarRenderer getWidgetToolbarRenderer()
    {
        return widgetToolbarRenderer;
    }


    @Required
    public void setWidgetToolbarRenderer(final WidgetToolbarRenderer widgetToolbarRenderer)
    {
        this.widgetToolbarRenderer = widgetToolbarRenderer;
    }
}
