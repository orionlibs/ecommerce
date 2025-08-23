/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetConnection;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.WidgetSocket;
import com.hybris.cockpitng.core.util.impl.WidgetSocketUtils;
import com.hybris.cockpitng.core.util.impl.WidgetTreeUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.SimpleListModel;

/**
 * Compose the connection between the windows
 */
public class ConnectionWindowComposer extends AbstractConnectionWindowComposer
{
    private static final long serialVersionUID = -8454047211462586913L;
    private static final String TARGET_WIDGET = "targetWidget";
    private static final String SOURCE_WIDGET = "sourceWidget";
    private transient ListitemRenderer<WidgetSocket> socketListItemRenderer;
    private Listbox sourceOutputs;
    private Listbox targetInputs;
    private Listbox connections;
    private Label sourceLabel;
    private Label targetLabel;


    @Override
    public void doAfterCompose(final Component comp) throws Exception
    {
        super.doAfterCompose(comp);
        sourceOutputs.setModel(createOutputConnectionListmodel(getSourceWidget(), getTargetWidget()));
        sourceOutputs.setItemRenderer(socketListItemRenderer);
        sourceOutputs.addEventListener(Events.ON_SELECT, event -> refreshTargetList());
        targetInputs.setItemRenderer(socketListItemRenderer);
        connections.setItemRenderer(prepareConnectionsListItemRenderer());
        sourceLabel.setValue(getSourceWidget().getWidgetDefinitionId());
        sourceLabel.setTooltip(getSourceWidget().getId());
        targetLabel.setValue(getTargetWidget().getWidgetDefinitionId());
        targetLabel.setTooltip(getTargetWidget().getId());
        targetInputs.addEventListener(Events.ON_SELECT, getSelectListener());
        comp.addEventListener("onClickCloseWindow", event -> comp.detach());
        refreshConnectionsList();
    }


    private EventListener<SelectEvent<Listitem, ?>> getSelectListener()
    {
        return selectEvent -> {
            final WidgetSocket sourceSocket = sourceOutputs.getSelectedItem().getValue();
            final WidgetSocket targetSocket = targetInputs.getSelectedItem().getValue();
            createConnection(getSourceWidget(), sourceSocket, getTargetWidget(), targetSocket, event -> {
                final Set<Listitem> selected = selectEvent.getSelectedItems();
                if(selected.size() == 1)
                {
                    targetInputs.removeItemAt(targetInputs.getIndexOfItem(selected.iterator().next()));
                }
                targetInputs.setSelectedItem(null);
                refreshConnectionsList();
            });
            targetInputs.clearSelection();
        };
    }


    private void refreshTargetList()
    {
        final Listitem selectedItem = sourceOutputs.getSelectedItem();
        if(selectedItem != null)
        {
            targetInputs.setModel(calcTargetModel(selectedItem));
        }
    }


    private void refreshConnectionsList()
    {
        final List<WidgetSocket> allOutputs = WidgetSocketUtils.getAllOutputs(getSourceWidget(),
                        getWidgetDefinition(getSourceWidget()));
        final List<WidgetConnection> listModel = Lists.newArrayList();
        for(final WidgetSocket socket : allOutputs)
        {
            for(final WidgetConnection con : widgetService.getWidgetConnectionsForOutputWidgetAndSocketID(getSourceWidget(),
                            socket.getId()))
            {
                if(getTargetWidget().equals(con.getTarget()))
                {
                    listModel.add(con);
                }
            }
        }
        connections.setModel(new SimpleListModel<Object>(listModel));
    }


    public ListModel<WidgetSocket> createOutputConnectionListmodel(final Widget sourceWidget, final Widget targetWidget)
    {
        final Collection<WidgetSocket> outputs = WidgetSocketUtils.getAllOutputs(sourceWidget, getWidgetDefinition(sourceWidget));
        final List<WidgetSocket> arrayList = new ArrayList<WidgetSocket>();
        for(final WidgetSocket outSock : outputs)
        {
            if(!WidgetSocket.SocketVisibility.HIDDEN.equals(outSock.getVisibility())
                            && socketConnectionService.checkVisibilityRestrictions(null, targetWidget, outSock, sourceWidget))
            {
                arrayList.add(outSock);
            }
        }
        return new SimpleListModel<WidgetSocket>(arrayList);
    }


    private SimpleListModel<WidgetSocket> calcTargetModel(final Listitem srcItem)
    {
        final Widget sourceWidget = getSourceWidget();
        final Widget targetWidget = getTargetWidget();
        final WidgetDefinition sourceWidgetDefinition = getWidgetDefinition(sourceWidget);
        final WidgetDefinition targetWidgetDefinition = getWidgetDefinition(targetWidget);
        final WidgetSocket outSock = srcItem.getValue();
        final List<WidgetSocket> filtered = new ArrayList<WidgetSocket>();
        for(final WidgetSocket inpSock : WidgetSocketUtils.getAllInputs(targetWidget,
                        (WidgetDefinition)widgetDefinitionService.getComponentDefinitionForCode(targetWidget.getWidgetDefinitionId())))
        {
            boolean add = true;
            for(final WidgetConnection conn : widgetService.getWidgetConnectionsForOutputWidgetAndSocketID(sourceWidget,
                            outSock.getId()))
            {
                if(targetWidget.equals(conn.getTarget()) && conn.getInputId().equals(inpSock.getId()))
                {
                    add = false;
                    break;
                }
            }
            if(add && cockpitAdminService.canReceiveFrom(inpSock, targetWidget, outSock, sourceWidget)
                            && !(sourceWidgetDefinition.isStubWidget() && targetWidgetDefinition.isStubWidget()))
            {
                filtered.add(inpSock);
            }
        }
        return new SimpleListModel<WidgetSocket>(filtered);
    }


    private Widget getSourceWidget()
    {
        return (Widget)arg.get(SOURCE_WIDGET);
    }


    private Widget getTargetWidget()
    {
        return (Widget)arg.get(TARGET_WIDGET);
    }


    @Override
    public EventListener<Event> prepareConnectionRemoveListener(final Listitem item, final WidgetConnection con)
    {
        return new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event)
            {
                final Widget source = con.getSource();
                source.removeOutConnection(con);
                widgetPersistenceService.storeWidgetTree(WidgetTreeUtils.getCommonParent(source, con.getTarget()));
                item.detach();
                refreshTargetList();
            }
        };
    }
}
