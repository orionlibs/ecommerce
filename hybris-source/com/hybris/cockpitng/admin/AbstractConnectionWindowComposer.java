/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.AbstractCockpitComponentDefinition;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.SocketConnectionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetConnection;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.WidgetService;
import com.hybris.cockpitng.core.WidgetSocket;
import com.hybris.cockpitng.core.persistence.WidgetPersistenceService;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.core.ui.impl.DefaultWidgetInstance;
import com.hybris.cockpitng.core.util.impl.WidgetTreeUtils;
import com.hybris.cockpitng.util.MessageboxUtils;
import com.hybris.cockpitng.util.ViewAnnotationAwareComposer;
import com.hybris.cockpitng.util.WidgetUtils;
import com.hybris.cockpitng.util.YTestTools;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbarbutton;

/**
 * Abstract class in order to compose the window connection
 */
public abstract class AbstractConnectionWindowComposer extends ViewAnnotationAwareComposer
{
    protected static final String SLOT = "slot";
    private static final long serialVersionUID = -5770327829806188953L;
    private static final String CONNECTION_NAME_PREFIX = "conn_";
    private static final String ARROW = "->";
    private static final Character CONNECTION_NAME_SEPARATOR = '_';
    protected transient SocketConnectionService socketConnectionService;
    protected transient WidgetUtils widgetUtils;
    protected transient WidgetPersistenceService widgetPersistenceService;
    protected transient WidgetService widgetService;
    protected transient CockpitAdminService cockpitAdminService;
    protected transient CockpitComponentDefinitionService widgetDefinitionService;


    protected boolean createConnection(final Widget sourceWidget, final WidgetSocket outSock, final Widget targetWidget,
                    final WidgetSocket inSock)
    {
        return createConnection(sourceWidget, outSock, targetWidget, inSock, null);
    }


    protected boolean createConnection(final Widget sourceWidget, final WidgetSocket outSock, final Widget targetWidget,
                    final WidgetSocket inSock, final EventListener<Event> callback)
    {
        final String resolvedOutputGenericType = socketConnectionService.resolveGenericType(outSock, sourceWidget);
        final String resolvedInputGenericType = socketConnectionService.resolveGenericType(inSock, targetWidget);
        if(StringUtils.isNotBlank(resolvedOutputGenericType) && resolvedOutputGenericType.charAt(0) == '#')
        {
            // Output socket has a generic type which is not yet set
            if(StringUtils.isNotBlank(resolvedInputGenericType) && resolvedInputGenericType.charAt(0) == '#')
            {
                // Input socket has also a generic type which is not yet set, so they are not connectable
                Messagebox.show("The socket data types are not compatible, generic type variable at the target widget must be "
                                + "set" + " " + "first.");
            }
            else
            {
                Messagebox
                                .show("The socket data types are not compatible but there is a generic type variable. Do you want to set the"
                                                                + " " + "type variable '" + resolvedOutputGenericType.substring(1) + "' to '" + resolvedInputGenericType
                                                                + "'?", "Auto adjust type", MessageboxUtils.order(Messagebox.Button.NO, Messagebox.Button.YES),
                                                Messagebox.QUESTION,
                                                new EventListener<Messagebox.ClickEvent>()
                                                {
                                                    @Override
                                                    public void onEvent(final Messagebox.ClickEvent event) throws Exception
                                                    {
                                                        if(!Messagebox.Button.YES.equals(event.getButton()))
                                                        {
                                                            return;
                                                        }
                                                        sourceWidget.getWidgetSettings().put(
                                                                        SocketConnectionService.SOCKET_DATA_TYPE_PREFIX + resolvedOutputGenericType.substring(1),
                                                                        resolvedInputGenericType);
                                                        createConnInternal(sourceWidget, outSock, targetWidget, inSock);
                                                        if(callback != null)
                                                        {
                                                            callback.onEvent(event);
                                                        }
                                                    }
                                                });
            }
        }
        else
        {
            createConnInternal(sourceWidget, outSock, targetWidget, inSock);
            if(callback != null)
            {
                try
                {
                    callback.onEvent(null);
                }
                catch(final Exception e)
                {
                    throw new IllegalStateException(e);
                }
            }
        }
        return true;
    }


    private void createConnInternal(final Widget sourceWidget, final WidgetSocket outSock, final Widget targetWidget,
                    final WidgetSocket inSock)
    {
        final WidgetConnection conn = widgetService.createWidgetConnection(sourceWidget, targetWidget, inSock.getId(),
                        outSock.getId());
        final StringBuilder connNameBuilder = new StringBuilder();
        connNameBuilder.append(CONNECTION_NAME_PREFIX).append(getWidgetDefinition(sourceWidget)).append(sourceWidget)
                        .append(CONNECTION_NAME_SEPARATOR).append(outSock.getId()).append(ARROW).append(getWidgetDefinition(targetWidget))
                        .append(targetWidget).append(CONNECTION_NAME_SEPARATOR).append(inSock.getId());
        conn.setName(connNameBuilder.toString());
        widgetPersistenceService.storeWidgetTree(WidgetTreeUtils.getCommonParent(sourceWidget, targetWidget));
        updateConnectButton(sourceWidget);
        updateConnectButton(targetWidget);
        final Component root = widgetUtils.getRoot().getFellowIfAny(SLOT);
        if(root instanceof Widgetslot)
        {
            ((Widgetslot)root).updateView();
        }
    }


    private void updateConnectButton(final Widget widget)
    {
        final WidgetInstance widgetInstance = new DefaultWidgetInstance(widget, null, 0);
        final Widgetslot widgetSlot = widgetUtils.getRegisteredWidgetslot(widgetInstance);
        if(widgetSlot != null)
        {
            final Toolbarbutton connectButton = getConnectButton(widgetSlot);
            if(connectButton != null)
            {
                connectButton.setSclass("connectBtn connectBtn_connected");
            }
        }
    }


    private static Toolbarbutton getConnectButton(final Widgetslot widgetslot)
    {
        final Component fellowIfAny = widgetslot.getFellowIfAny("cng_admin_connectButton");
        return fellowIfAny instanceof Toolbarbutton ? (Toolbarbutton)fellowIfAny : null;
    }


    protected WidgetDefinition getWidgetDefinition(final Widget widget)
    {
        return widgetDefinitionService.getComponentDefinitionForCode(widget.getWidgetDefinitionId(), WidgetDefinition.class);
    }


    protected ListitemRenderer<WidgetConnection> prepareConnectionsListItemRenderer()
    {
        return (item, con, index) -> {
            final String tooltip = prepareStringRepresentation(con);
            final Listcell sourceWidgetCell = new Listcell();
            AbstractCockpitComponentDefinition def = widgetDefinitionService.getComponentDefinitionForCode(con.getSource()
                            .getWidgetDefinitionId());
            Label label = new Label(con.getSource().getId() + (def == null ? StringUtils.EMPTY : (" : " + def.getName())));
            sourceWidgetCell.setTooltiptext(tooltip);
            sourceWidgetCell.appendChild(label);
            final Listcell sourceWidgetSocketCell = new Listcell();
            label = new Label(con.getOutputId());
            sourceWidgetSocketCell.setTooltiptext(tooltip);
            sourceWidgetSocketCell.appendChild(label);
            final Listcell targetWidgetCell = new Listcell();
            def = widgetDefinitionService.getComponentDefinitionForCode(con.getTarget().getWidgetDefinitionId());
            label = new Label(con.getTarget().getId() + (def == null ? StringUtils.EMPTY : (" : " + def.getName())));
            targetWidgetCell.setTooltiptext(tooltip);
            targetWidgetCell.appendChild(label);
            final Listcell targetWidgetSocketCell = new Listcell();
            label = new Label(con.getInputId());
            targetWidgetSocketCell.setTooltiptext(tooltip);
            targetWidgetSocketCell.appendChild(label);
            final Listcell actionCell = new Listcell();
            actionCell.setSclass("widgetConnWizardRemoveBtnContainer");
            final Button remove = new Button();
            remove.setSclass("yo-delete-btn");
            remove.addEventListener(Events.ON_CLICK, prepareConnectionRemoveListener(item, con));
            actionCell.appendChild(remove);
            YTestTools.modifyYTestId(remove, String.format("remove_%s_%s", con.getOutputId(), con.getInputId()));
            item.appendChild(sourceWidgetCell);
            item.appendChild(sourceWidgetSocketCell);
            item.appendChild(targetWidgetCell);
            item.appendChild(targetWidgetSocketCell);
            item.appendChild(actionCell);
        };
    }


    public abstract EventListener<Event> prepareConnectionRemoveListener(final Listitem item, final WidgetConnection con);


    protected String prepareStringRepresentation(final WidgetConnection con)
    {
        final String inputId = con.getInputId();
        final String outputId = con.getOutputId();
        final StringBuilder builder = new StringBuilder(outputId);
        ArrayList<WidgetSocket> sockets = Lists.newArrayList(getWidgetDefinition(con.getSource()).getOutputs());
        sockets.addAll(con.getSource().getVirtualOutputs());
        appendSocketType(outputId, builder, sockets);
        builder.append(" -> ").append(inputId);
        sockets = Lists.newArrayList(getWidgetDefinition(con.getTarget()).getInputs());
        sockets.addAll(con.getTarget().getVirtualInputs());
        appendSocketType(inputId, builder, sockets);
        return builder.toString();
    }


    private static void appendSocketType(final String socketId, final StringBuilder builder, final List<WidgetSocket> sockets)
    {
        for(final WidgetSocket socket : sockets)
        {
            if(StringUtils.equals(socket.getId(), socketId))
            {
                builder.append('[').append(StringUtils.defaultIfBlank(socket.getDataType(), "java.lang.Object"));
                if(socket.getDataMultiplicity() != null)
                {
                    builder.append(", ").append(socket.getDataMultiplicity().getCode());
                }
                builder.append(']');
                return;
            }
        }
    }
}
