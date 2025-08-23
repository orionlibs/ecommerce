/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.admin.CockpitAdminService;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetConnection;
import com.hybris.cockpitng.core.persistence.WidgetPersistenceService;
import com.hybris.cockpitng.core.security.WidgetAuthorizationService;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.ui.impl.DefaultWidgetInstance;
import com.hybris.cockpitng.core.util.impl.WidgetTreeUtils;
import com.hybris.cockpitng.engine.ConnectButtonRenderer;
import com.hybris.cockpitng.util.WidgetUtils;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Toolbarbutton;

public class DefaultConnectButtonRenderer implements ConnectButtonRenderer
{
    private WidgetPersistenceService widgetPersistenceService;
    private CockpitAdminService cockpitAdminService;
    private WidgetAuthorizationService widgetAuthorizationService;
    private WidgetUtils widgetUtils;


    public void renderConnectButton(final Component parent, final Widget currentWidget, final Widgetslot widgetslot)
    {
        final Toolbarbutton connectBtn = new Toolbarbutton(StringUtils.EMPTY);
        connectBtn.setId("cng_admin_connectButton");
        final boolean connected = hasDisplayedConnections(currentWidget);
        connectBtn.setSclass(connected ? "connectBtn connectBtn_connected" : "connectBtn connectBtn_disconnected");
        connectBtn.setTooltiptext("Drag to another widgets connect button to manage connections with drop widget. Left click - "
                        + "manage connections. Right click - show/delete existing connections.");
        connectBtn.setAttribute("connectedWidget", currentWidget);
        connectBtn.setDraggable("widgetConnector");
        connectBtn.setDroppable("widgetConnector");
        connectBtn.addEventListener(Events.ON_DROP, event -> {
            if(event instanceof DropEvent)
            {
                final Widget draggedWidget = (Widget)((DropEvent)event).getDragged().getAttribute("connectedWidget");
                getCockpitAdminService().showWidgetConnectionWizard(draggedWidget, currentWidget, widgetslot);
            }
        });
        connectBtn.addEventListener(Events.ON_CLICK,
                        event -> cockpitAdminService.showWidgetMultiConnectionWizard(currentWidget, connectBtn));
        connectBtn.addEventListener(Events.ON_RIGHT_CLICK, event -> showConnectionsToolbar(parent, currentWidget, connectBtn));
        parent.appendChild(connectBtn);
    }


    private void showConnectionsToolbar(final Component parent, final Widget widget, final Toolbarbutton toolbarButton)
    {
        final Popup popup = new Popup();
        popup.addSclass("connectorPopup");
        parent.appendChild(popup);
        final Div inputDiv = new Div();
        popup.appendChild(inputDiv);
        final Label inputLabel = new Label(Labels.getLabel("connectors.input"));
        inputLabel.setSclass("connectorTitle");
        inputDiv.appendChild(inputLabel);
        final Div outputDiv = new Div();
        popup.appendChild(outputDiv);
        final Label outputLabel = new Label(Labels.getLabel("connectors.output"));
        outputLabel.setSclass("connectorTitle");
        outputDiv.appendChild(outputLabel);
        final List<WidgetConnection> inputConnectors = widget.getInConnections();
        for(final WidgetConnection widgetConnector : inputConnectors)
        {
            renderConnectorEntry(inputDiv, widgetConnector);
        }
        final List<WidgetConnection> outputConnectors = widget.getOutConnections();
        for(final WidgetConnection widgetConnector : outputConnectors)
        {
            renderConnectorEntry(outputDiv, widgetConnector);
        }
        popup.addEventListener(Events.ON_OPEN, (final OpenEvent openEvent) -> {
            if(!openEvent.isOpen())
            {
                popup.detach();
            }
        });
        popup.open(toolbarButton);
    }


    protected boolean hasDisplayedConnections(final Widget widget)
    {
        if(widget == null)
        {
            return false;
        }
        for(final WidgetConnection connection : widget.getOutConnections())
        {
            if(getWidgetAuthorizationService().isAccessAllowed(connection.getTarget()))
            {
                return true;
            }
        }
        for(final WidgetConnection connection : widget.getInConnections())
        {
            if(getWidgetAuthorizationService().isAccessAllowed(connection.getSource()))
            {
                return true;
            }
        }
        return false;
    }


    protected void renderConnectorEntry(final Component parent, final WidgetConnection widgetConnection)
    {
        final Div div = new Div();
        parent.appendChild(div);
        final Hbox hbox = new Hbox();
        final Label label = new Label(widgetConnection.toString());
        hbox.appendChild(label);
        final Toolbarbutton remBtn = new Toolbarbutton();
        remBtn.addSclass("connectorRemoveBtn");
        hbox.appendChild(remBtn);
        div.appendChild(hbox);
        remBtn.addEventListener(Events.ON_CLICK, event -> {
            div.detach();
            widgetConnection.getSource().removeOutConnection(widgetConnection);
            widgetConnection.getTarget().removeInConnection(widgetConnection);
            final Widget commonParent = WidgetTreeUtils.getCommonParent(widgetConnection.getSource(), widgetConnection.getTarget());
            getWidgetPersistenceService().storeWidgetTree(commonParent);
            final Widgetslot sourceWidget = getRegisteredWidgetSlot(widgetConnection.getSource());
            if(sourceWidget != null)
            {
                sourceWidget.updateView();
            }
            final Widgetslot targetWidget = getRegisteredWidgetSlot(widgetConnection.getTarget());
            if(targetWidget != null)
            {
                targetWidget.updateView();
            }
        });
    }


    private Widgetslot getRegisteredWidgetSlot(final Widget widget)
    {
        final WidgetInstance singleWidgetInstance = getSingleWidgetInstance(widget);
        if(singleWidgetInstance != null)
        {
            return getWidgetUtils().getRegisteredWidgetslot(singleWidgetInstance);
        }
        return null;
    }


    private WidgetInstance getSingleWidgetInstance(final Widget widget)
    {
        final List<WidgetInstance> widgetInstances = widget.getWidgetInstances();
        if(widgetInstances != null && widgetInstances.size() == 1)
        {
            return widgetInstances.iterator().next();
        }
        return new DefaultWidgetInstance(widget, null, 0);
    }


    public WidgetPersistenceService getWidgetPersistenceService()
    {
        return widgetPersistenceService;
    }


    @Required
    public void setWidgetPersistenceService(final WidgetPersistenceService widgetPersistenceService)
    {
        this.widgetPersistenceService = widgetPersistenceService;
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


    public WidgetUtils getWidgetUtils()
    {
        return widgetUtils;
    }


    @Required
    public void setWidgetUtils(final WidgetUtils widgetUtils)
    {
        this.widgetUtils = widgetUtils;
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
