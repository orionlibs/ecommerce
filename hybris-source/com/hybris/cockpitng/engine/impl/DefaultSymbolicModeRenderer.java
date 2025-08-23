/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.components.Widgetchildren;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetConnection;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.WidgetSocket;
import com.hybris.cockpitng.core.persistence.packaging.SimpleHybrisWidgetResourceLoader;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.util.impl.WidgetSocketUtils;
import com.hybris.cockpitng.core.util.impl.WidgetTreeUtils;
import com.hybris.cockpitng.engine.SymbolicModeRenderer;
import com.hybris.cockpitng.engine.WidgetWizardCreationDelegate;
import com.hybris.cockpitng.util.CockpitSessionService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.WidgetControllers;
import com.hybris.cockpitng.util.WidgetUtils;
import com.hybris.cockpitng.util.YTestTools;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Window;

public class DefaultSymbolicModeRenderer implements SymbolicModeRenderer
{
    public static final String CNG_PLACEHOLDER_CONTENT = "cng-placeholderContent";
    public static final String CNG_CHILD_WIDGET_SYMBOLIC = "cng-addChildWidgetSymbolic";
    /**
     * @deprecated since 6.7 - no longer used
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public static final String STUB_CONTAINER = "stubContainerVlayout";
    public static final String WIDGET_PLACEHOLDER = "widgetPlaceholder";
    public static final String WIDGET_PLACEHOLDER_STUB_CLASS = "widgetPlaceholder widgetStub";
    public static final String WIDGET_PLACEHOLDER_WIDGET_TEMPLATE_CLASS = "widgetPlaceholder widgetTemplate";
    public static final String TREE_STRUCTURE_BAR_DIV_CLASS = "barDiv";
    protected static final String STUB_CNT_VISIBILITY_PARAM = "stubCntVisibilityParam";
    private static final String MAIN_SLOT_ID_PARAM = "mainSlotId";
    private static final String DEFAULT_MAIN_SLOT_ID = "mainSlot";
    private static final String STUB_CONTAINER_CONSTANT = "stubContainer";
    private static final String STUB_CONTAINER_SHOW_BTN = "stubContainerShowButton";
    private static final String STUB_CONTAINER_HIDE_BTN = "stubContainerHideButton";
    private static final String WIDGET_SLOT_WITH_STUBS = "stubs_presented";
    private static final String WIDGET_SLOT_WITHOUT_STUBS = "no_stubs";
    private static final String STUB_WIDGET_TOGGLE_BUTTON = "stubContainerButton";
    private static final String ALIGNED = "Aligned";
    private CockpitComponentDefinitionService widgetDefinitionService;
    private CockpitSessionService cockpitSessionService;
    private WidgetUtils widgetUtils;
    private WidgetWizardCreationDelegate widgetWizardCreationDelegate;


    @Override
    public void render(final Widgetslot widgetslot, final AdminmodeWidgetEngine engine)
    {
        final WidgetInstance widgetInstance = widgetslot.getWidgetInstance();
        final String mainSlotId = lookupMainSlotId();
        if(widgetInstance.getParent() == null && mainSlotId.equals(widgetslot.getSlotID()))
        {
            createWidgetStubContainer(widgetslot);
        }
        final Widget widget = widgetInstance.getWidget();
        final WidgetDefinition widgetDefinition = getWidgetDefinition(widget.getWidgetDefinitionId());
        if(widgetDefinition.isStubWidget())
        {
            WidgetControllers.initSettings(widgetInstance, widgetslot, widgetDefinitionService);
            widgetslot.setAttribute(AdminmodeWidgetEngine.LABELS_PARAM, engine.createLabelMap(getWidgetDefinition(widget), widget));
            final Div widgetPlaceholder = new Div();
            widgetPlaceholder.setSclass(WIDGET_PLACEHOLDER_STUB_CLASS);
            widgetslot.appendChild(widgetPlaceholder);
            engine.appendWidgetToolbar(widgetslot, widgetPlaceholder);
            final Widgetslot mainWidgetSlot = widgetUtils.getRegisteredWidgetslot(mainSlotId);
            if(mainWidgetSlot != null)
            {
                final Component stubWidgetContainer = mainWidgetSlot.getFellowIfAny(STUB_CONTAINER_CONSTANT);
                if(stubWidgetContainer != null)
                {
                    stubWidgetContainer.appendChild(widgetslot);
                }
                if(engine.getWidgetDefinition(widget) != null)
                {
                    renderOutputConnectionBars(widgetPlaceholder, widget, engine);
                }
            }
        }
        else
        {
            WidgetControllers.initSettings(widgetInstance, widgetslot, widgetDefinitionService);
            widgetslot.setAttribute(AdminmodeWidgetEngine.LABELS_PARAM, engine.createLabelMap(getWidgetDefinition(widget), widget));
            final Div widgetPlaceholder = new Div();
            if(widget.isTemplate())
            {
                widgetPlaceholder.setSclass(WIDGET_PLACEHOLDER_WIDGET_TEMPLATE_CLASS);
            }
            else
            {
                widgetPlaceholder.setSclass(WIDGET_PLACEHOLDER);
            }
            YTestTools.modifyYTestId(widgetPlaceholder, widget.getId());
            widgetslot.appendChild(widgetPlaceholder);
            final Widgetchildren children = new Widgetchildren();
            children.setType(Widgetchildren.SYMBOLIC);
            Events.echoEvent(Events.ON_CREATE, children, null);
            engine.appendWidgetToolbar(widgetslot, widgetPlaceholder);
            if(engine.getWidgetDefinition(widget) != null)
            {
                widgetPlaceholder.insertBefore(children, widgetPlaceholder.getFirstChild());
                final Div treeStructureBarDiv = new Div();
                treeStructureBarDiv.setSclass(TREE_STRUCTURE_BAR_DIV_CLASS);
                if(StringUtils.isNotBlank(widget.getSlotId()))
                {
                    treeStructureBarDiv.appendChild(new Label(widget.getSlotId()));
                }
                widgetPlaceholder.appendChild(treeStructureBarDiv);
                renderOutputConnectionBars(widgetPlaceholder, widget, engine);
                children.afterCompose();
            }
            final Div container = new Div();
            container.setSclass(CNG_PLACEHOLDER_CONTENT);
            final Div addChildWidgetBtnCnt = new Div();
            addChildWidgetBtnCnt.setSclass(CNG_CHILD_WIDGET_SYMBOLIC);
            addChildWidgetBtnCnt.addEventListener(Events.ON_CLICK, event -> openChooseSlotIdForChildWizard(widgetslot, engine));
            addChildWidgetBtnCnt.setDroppable(AdminmodeWidgetEngine.WIDGET_TOKEN);
            addChildWidgetBtnCnt.addEventListener(Events.ON_DROP, event -> {
                final Object dragged = ((DropEvent)event).getDragged().getAttribute("draggedWidget");
                if(dragged instanceof Widget && !engine.isStub((Widget)dragged))
                {
                    if(engine.isEqualOrParentOf((Widget)dragged, widgetslot.getWidgetInstance().getWidget()))
                    {
                        Messagebox.show("You can not move widget to one of its children.");
                        return;
                    }
                    openChooseSlotIdForChildWizard(widgetslot, engine, (Widget)dragged);
                }
            });
            container.appendChild(addChildWidgetBtnCnt);
            widgetPlaceholder.appendChild(container);
            widgetslot.setAttribute(CNG_PLACEHOLDER_CONTENT, container);
        }
    }


    protected String lookupMainSlotId()
    {
        String ret = DEFAULT_MAIN_SLOT_ID;
        if(Executions.getCurrent().getParameter(MAIN_SLOT_ID_PARAM) != null)
        {
            ret = Objects.toString(Executions.getCurrent().getParameter(MAIN_SLOT_ID_PARAM));
        }
        return ret;
    }


    private Window openChooseSlotIdForChildWizard(final Widgetslot widgetslot, final List<String> slots,
                    final EventListener<SelectEvent<Listitem, String>> slotListener)
    {
        final Window window = new Window("Choose slot id", "none", false);
        window.setWidth("400px");
        widgetslot.appendChild(window);
        window.doHighlighted();
        final Listbox slotbox = new Listbox();
        slotbox.setSclass("cng-chooseSlot-listbox");
        window.appendChild(slotbox);
        slotbox.setModel(new SimpleListModel<>(slots));
        final Div btnCnt = new Div();
        btnCnt.setSclass("cng-chooseSlot-btn-cnt");
        window.appendChild(btnCnt);
        final Button cancelBtn = new Button("Cancel");
        btnCnt.appendChild(cancelBtn);
        cancelBtn.addEventListener(Events.ON_CLICK, event -> window.detach());
        slotbox.addEventListener(Events.ON_SELECT, event -> window.detach());
        slotbox.addEventListener(Events.ON_SELECT, slotListener);
        return window;
    }


    protected void openChooseSlotIdForChildWizard(final Widgetslot widgetslot, final AdminmodeWidgetEngine engine,
                    final Widget data)
    {
        final EventListener<SelectEvent<Listitem, String>> slotListener = event -> {
            final String slotID = event.getSelectedObjects().iterator().next();
            if(slotID != null && !AdminmodeWidgetEngine.NO_SLOT_ID.equals(slotID))
            {
                engine.moveWidget(widgetslot, slotID, data);
            }
        };
        openChooseSlotIdForChildWizard(widgetslot, engine.getEmptySlotIds(widgetslot), slotListener);
    }


    /**
     * @param widgetslot
     * @param engine
     */
    protected void openChooseSlotIdForChildWizard(final Widgetslot widgetslot, final AdminmodeWidgetEngine engine)
    {
        final EventListener<SelectEvent<Listitem, String>> slotListener = event -> engine.getCockpitAdminService()
                        .showAddWidgetWizard(widgetslot, event2 -> {
                            final Widget parent = widgetslot.getWidgetInstance().getWidget();
                            final String id = null;
                            final Widget newWidget;
                            final WidgetDefinition definition = (WidgetDefinition)event2.getData();
                            String slotID = event.getSelectedObjects().iterator().next();
                            if(AdminmodeWidgetEngine.NO_SLOT_ID.equals(slotID))
                            {
                                slotID = null;
                            }
                            newWidget = getWidgetWizardCreationDelegate().createWidget(slotID, parent, id, definition);
                            Executions.getCurrent().setAttribute("lastCreatedWidget", newWidget);
                            widgetslot.updateView();
                            SimpleHybrisWidgetResourceLoader.clearCssCache();
                        });
        openChooseSlotIdForChildWizard(widgetslot, engine.getEmptySlotIds(widgetslot), slotListener);
    }


    private void addToConnectionPopups(final Window window, final Widget widget)
    {
        final Object attribute = getDesktop().getAttribute("connectionPopups");
        final Map popups;
        if(attribute instanceof Map)
        {
            popups = (Map)attribute;
        }
        else
        {
            popups = new HashMap();
            getDesktop().setAttribute("connectionPopups", popups);
        }
        popups.put(widget.getId(), window);
    }


    protected void createWidgetStubContainer(final Widgetslot mainWidgetslot)
    {
        if(mainWidgetslot == null)
        {
            return;
        }
        final boolean stubCntVisible = Boolean.TRUE.equals(cockpitSessionService.getAttribute(STUB_CNT_VISIBILITY_PARAM));
        final Div stubWidgetContainer = new Div();
        stubWidgetContainer.setId(STUB_CONTAINER_CONSTANT);
        stubWidgetContainer.setSclass(STUB_CONTAINER_CONSTANT);
        stubWidgetContainer.setVisible(stubCntVisible);
        final Button stubCntToggleButton = new Button();
        stubCntToggleButton.setId(STUB_CONTAINER_SHOW_BTN);
        stubCntToggleButton.setVisible(!stubCntVisible);
        final Div stubContainerHeader = new Div();
        stubContainerHeader.setSclass("stubWidgetContainerHeader");
        final Label headerLabel = new Label(Labels.getLabel("component_holder.label"));
        stubContainerHeader.appendChild(headerLabel);
        final var toggleStubWidgetContainer = new Button();
        toggleStubWidgetContainer.setId(STUB_CONTAINER_HIDE_BTN);
        toggleStubWidgetContainer.setSclass("stub_toggle_btn");
        UITools.modifySClass(mainWidgetslot, WIDGET_SLOT_WITHOUT_STUBS, !stubCntVisible);
        UITools.modifySClass(mainWidgetslot, WIDGET_SLOT_WITH_STUBS, stubCntVisible);
        toggleStubWidgetContainer.addEventListener(Events.ON_CLICK, event -> {
            UITools.modifySClass(mainWidgetslot, WIDGET_SLOT_WITHOUT_STUBS, stubWidgetContainer.isVisible());
            UITools.modifySClass(mainWidgetslot, WIDGET_SLOT_WITH_STUBS, !stubWidgetContainer.isVisible());
            final boolean currentVisibility = !stubWidgetContainer.isVisible();
            stubWidgetContainer.setVisible(currentVisibility);
            cockpitSessionService.setAttribute(STUB_CNT_VISIBILITY_PARAM, currentVisibility);
            stubCntToggleButton.setVisible(true);
        });
        stubContainerHeader.appendChild(toggleStubWidgetContainer);
        stubWidgetContainer.appendChild(stubContainerHeader);
        stubCntToggleButton.addEventListener(Events.ON_CLICK, event -> {
            UITools.modifySClass(mainWidgetslot, WIDGET_SLOT_WITHOUT_STUBS, stubWidgetContainer.isVisible());
            UITools.modifySClass(mainWidgetslot, WIDGET_SLOT_WITH_STUBS, !stubWidgetContainer.isVisible());
            final boolean currentVisibility = !stubWidgetContainer.isVisible();
            stubWidgetContainer.setVisible(currentVisibility);
            cockpitSessionService.setAttribute(STUB_CNT_VISIBILITY_PARAM, currentVisibility);
            stubCntToggleButton.setVisible(false);
        });
        stubCntToggleButton.setSclass(STUB_WIDGET_TOGGLE_BUTTON);
        mainWidgetslot.appendChild(stubCntToggleButton);
        mainWidgetslot.appendChild(stubWidgetContainer);
    }


    protected void renderOutputConnectionBars(final Component parent, final Widget currentWidget,
                    final AdminmodeWidgetEngine engine)
    {
        final Collection<WidgetSocket> outputs = WidgetSocketUtils.getAllOutputs(currentWidget,
                        engine.getWidgetDefinition(currentWidget));
        final Div popCntDiv = new Div();
        popCntDiv.setSclass("connectionPopupWindowCnt");
        parent.appendChild(popCntDiv);
        final Window pop = new Window();
        popCntDiv.appendChild(pop);
        pop.setSclass("connectionPopup");
        pop.setPosition("parent");
        pop.setWidth("150px");
        pop.setHeight("50px");
        pop.setShadow(false);
        pop.setVisible(false);
        addToConnectionPopups(pop, currentWidget);
        final Hbox hbox = new Hbox();
        pop.appendChild(hbox);
        for(final WidgetSocket widgetOutputSocket : outputs)
        {
            final List<WidgetConnection> connectors = engine.getWidgetService()
                            .getWidgetConnectionsForOutputWidgetAndSocketID(currentWidget, widgetOutputSocket.getId());
            for(final WidgetConnection cockpitWidgetConnector : connectors)
            {
                if(!engine.checkVisibility(cockpitWidgetConnector.getTarget()))
                {
                    continue;
                }
                final int resultingWidth;
                int resultingHeight;
                int width = getDepth(currentWidget) - getDepth(cockpitWidgetConnector.getTarget());
                int height = 1;
                String right = "2px";
                String left = "auto";
                final String barSize = "3px";
                if(width < 0)
                {
                    width = -width;
                    right = left;
                    left = "2px";
                }
                final Div anchor = new Div();
                anchor.setSclass("connectionAnchor");
                final Div cnt = new Div();
                cnt.setSclass("connectionContainer");
                anchor.appendChild(cnt);
                hbox.appendChild(anchor);
                final Div horConnection = new Div();
                horConnection.setSclass("connectionBar");
                resultingWidth = (width * 250);
                horConnection.setStyle("position: absolute; width: " + resultingWidth + "px; right: " + right + "; left: " + left
                                + "; height: " + barSize + ";");
                cnt.appendChild(horConnection);
                height = (getTopOffset(currentWidget) - getTopOffset(cockpitWidgetConnector.getTarget())) * 123;
                String top = "auto";
                String bottom = "0";
                if(height < 0)
                {
                    height = -height;
                    top = "0";
                    bottom = "auto";
                }
                resultingHeight = (height - 38);
                String vertConnectionSclass = "connectionBar " + ("auto".equals(right) ? "left" : "right") + ALIGNED;
                if(resultingHeight < 0)
                {
                    resultingHeight = 0;
                    vertConnectionSclass += " zeroHeight";
                }
                final Div vertConnection = new Div();
                vertConnection.setSclass(vertConnectionSclass);
                vertConnection.setStyle("position: absolute; width: " + barSize + "; right: " + ("auto".equals(right) ? "0" : "auto")
                                + "; left: " + ("auto".equals(left) ? "0" : "auto") + "; " + "height: " + resultingHeight + "px; top: " + top
                                + "; bottom: " + bottom);
                horConnection.appendChild(vertConnection);
                final Div endArrow1 = new Div();
                endArrow1.setSclass("endPoint " + ("auto".equals(bottom) ? "bottom" : "top") + ALIGNED);
                vertConnection.appendChild(endArrow1);
                final Div endArrow2 = new Div();
                endArrow2.setSclass("endPoint2 " + ("auto".equals(bottom) ? "bottom" : "top") + ALIGNED);
                vertConnection.appendChild(endArrow2);
                Component labelParent = null;
                String sclass = "connectionLabel";
                if(resultingWidth > 200 || resultingHeight < resultingWidth)
                {
                    labelParent = horConnection;
                }
                else
                {
                    labelParent = vertConnection;
                    sclass += " vertical";
                }
                final Label label = new Label(cockpitWidgetConnector.getOutputId() + " >>>> " + cockpitWidgetConnector.getInputId());
                label.setSclass(sclass);
                labelParent.appendChild(label);
            }
        }
    }


    private int getTopOffset(final Widget widget)
    {
        int ret = 0;
        final Widget rootWidget = WidgetTreeUtils.getRootWidget(widget);
        if(widget.equals(rootWidget))
        {
            return 0;
        }
        final Widget parent = widget.getParent();
        if(parent != null)
        {
            ret = getTopOffset(parent);
            for(final Widget sibling : parent.getChildren())
            {
                if(sibling.equals(widget))
                {
                    break;
                }
                ret += Math.max(getNumberOfLeafChildren(sibling), 1);
            }
        }
        return ret;
    }


    private int getNumberOfLeafChildren(final Widget widget)
    {
        int ret = 0;
        for(final Widget child : widget.getChildren())
        {
            if(child.getChildren().isEmpty())
            {
                ret++;
            }
            else
            {
                ret += getNumberOfLeafChildren(child);
            }
        }
        return ret;
    }


    private int getDepth(final Widget widget)
    {
        int ret = 0;
        Widget current = widget.getParent();
        while(current != null)
        {
            current = current.getParent();
            ret++;
        }
        return ret;
    }


    private Desktop getDesktop()
    {
        return Executions.getCurrent().getDesktop();
    }


    protected WidgetDefinition getWidgetDefinition(final Widget widget)
    {
        return (WidgetDefinition)widgetDefinitionService.getComponentDefinitionForCode(widget.getWidgetDefinitionId());
    }


    protected WidgetDefinition getWidgetDefinition(final String widgetDefiniionId)
    {
        return (WidgetDefinition)widgetDefinitionService.getComponentDefinitionForCode(widgetDefiniionId);
    }


    protected CockpitComponentDefinitionService getWidgetDefinitionService()
    {
        return widgetDefinitionService;
    }


    @Required
    public void setWidgetDefinitionService(final CockpitComponentDefinitionService widgetDefinitionService)
    {
        this.widgetDefinitionService = widgetDefinitionService;
    }


    public CockpitSessionService getCockpitSessionService()
    {
        return cockpitSessionService;
    }


    @Required
    public void setCockpitSessionService(final CockpitSessionService cockpitSessionService)
    {
        this.cockpitSessionService = cockpitSessionService;
    }


    @Required
    public void setWidgetUtils(final WidgetUtils widgetUtils)
    {
        this.widgetUtils = widgetUtils;
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
}
