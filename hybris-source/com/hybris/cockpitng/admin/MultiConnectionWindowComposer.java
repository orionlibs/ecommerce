/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.admin.model.tree.MatchingSocketsTreeModel;
import com.hybris.cockpitng.admin.model.tree.MatchingSocketsTreeModelInternalNode;
import com.hybris.cockpitng.admin.renderer.ConnectionWindowListItemRenderer;
import com.hybris.cockpitng.admin.strategy.socket.WidgetSocketMatchStrategy;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetConnection;
import com.hybris.cockpitng.core.WidgetSocket;
import com.hybris.cockpitng.core.util.impl.WidgetSocketUtils;
import com.hybris.cockpitng.core.util.impl.WidgetTreeUtils;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.SimpleGroupsModel;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.ext.TreeOpenableModel;

public class MultiConnectionWindowComposer extends AbstractConnectionWindowComposer
{
    private static final long serialVersionUID = -3339765146738576893L;
    private static final String WIDGET = "widget";
    @Resource
    private transient ConnectionWindowListItemRenderer socketListItemRenderer;
    private transient List<WidgetSocketMatchStrategy> socketMatchStrategies;
    private Listbox sourceSockets;
    private Listitem sourceSocketsSelected;
    private Listbox connections;
    private Tree matchingSockets;


    @Override
    public void doAfterCompose(final Component comp) throws Exception
    {
        super.doAfterCompose(comp);
        connections.setItemRenderer(prepareConnectionsListItemRenderer());
        sourceSockets.setModel(createSourceSocketsModel());
        sourceSockets.setItemRenderer(socketListItemRenderer);
        matchingSockets.setItemRenderer(socketListItemRenderer);
        matchingSockets.addEventListener(Events.ON_SELECT, prepareMatchingSocketSelectionListener());
        populateConnectionsList();
    }


    private EventListener<Event> prepareMatchingSocketSelectionListener()
    {
        return new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event)
            {
                final Treeitem selectedItem = matchingSockets.getSelectedItem();
                final MatchingSocketsTreeModelInternalNode value = selectedItem.getValue();
                matchingSockets.clearSelection();
                if(value.getElement() instanceof WidgetSocket)
                {
                    final WidgetSocket sourceSocket = sourceSockets.getSelectedItem().getValue();
                    final WidgetSocket matchingSocket = (WidgetSocket)value.getElement();
                    if(sourceSocket.isInput() ^ matchingSocket.isInput())
                    {
                        final EventListener<Event> callback = new EventListener<Event>()
                        {
                            @Override
                            public void onEvent(final Event evt)
                            {
                                widgetPersistenceService.storeWidgetTree(WidgetTreeUtils
                                                .getCommonParent(sourceSocket.getDeclaringWidget(), matchingSocket.getDeclaringWidget()));
                                final MatchingSocketsTreeModelInternalNode node = selectedItem.getValue();
                                node.getParent().remove(node);
                                detachAllEmptyNodes(selectedItem);
                                selectedItem.detach();
                                populateMatchingSocketsList(true, sourceSocketsSelected);
                                populateConnectionsList();
                            }
                        };
                        if(sourceSocket.isInput())
                        {
                            createConnection(matchingSocket.getDeclaringWidget(), matchingSocket, sourceSocket.getDeclaringWidget(),
                                            sourceSocket, callback);
                        }
                        else
                        {
                            createConnection(sourceSocket.getDeclaringWidget(), sourceSocket, matchingSocket.getDeclaringWidget(),
                                            matchingSocket, callback);
                        }
                    }
                    else
                    {
                        throw new IllegalStateException(
                                        "Cannot connect two " + (matchingSocket.isInput() ? "input" : "output") + " sockets.");
                    }
                }
                else
                {
                    selectedItem.setOpen(!selectedItem.isOpen());
                    selectedItem.setSelected(false);
                }
            }


            private void detachAllEmptyNodes(final Treeitem item)
            {
                final MatchingSocketsTreeModelInternalNode value = item.getValue();
                if(value.getChildren().isEmpty())
                {
                    final Treeitem parent = item.getParentItem();
                    item.detach();
                    if(parent != null)
                    {
                        detachAllEmptyNodes(parent);
                    }
                }
            }
        };
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
                final Widget target = con.getTarget();
                source.removeOutConnection(con);
                target.removeInConnection(con);
                widgetPersistenceService.storeWidgetTree(WidgetTreeUtils.getCommonParent(source, con.getTarget()));
                item.detach();
                populateMatchingSocketsList(true, sourceSockets.getSelectedItem());
                updateRootView();
            }


            /**
             * Update orchestration view.
             */
            private void updateRootView()
            {
                final Component root = widgetUtils.getRoot().getFellowIfAny(SLOT);
                if(root instanceof Widgetslot)
                {
                    ((Widgetslot)root).updateView();
                }
            }
        };
    }


    private void populateConnectionsList()
    {
        final List<WidgetConnection> list = Lists.newArrayList();
        if(sourceSocketsSelected == null)
        {
            list.addAll(getWidget().getInConnections());
            list.addAll(getWidget().getOutConnections());
        }
        else
        {
            final Listitem selected = sourceSockets.getSelectedItem();
            if(selected != null)
            {
                final WidgetSocket socket = selected.getValue();
                if(socket.isInput())
                {
                    for(final WidgetConnection con : getWidget().getInConnections())
                    {
                        if(con.getInputId().equals(socket.getId()))
                        {
                            list.add(con);
                        }
                    }
                }
                else
                {
                    for(final WidgetConnection con : getWidget().getOutConnections())
                    {
                        if(con.getOutputId().equals(socket.getId()))
                        {
                            list.add(con);
                        }
                    }
                }
            }
        }
        connections.setModel(new SimpleListModel<WidgetConnection>(list));
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = "sourceSockets")
    public void populateMatchingSocketsList()
    {
        if(sourceSocketsSelected == null || !sourceSocketsSelected.equals(sourceSockets.getSelectedItem()))
        {
            sourceSocketsSelected = sourceSockets.getSelectedItem();
            populateMatchingSocketsList(false, sourceSocketsSelected);
        }
        else
        {
            sourceSocketsSelected = null;
            sourceSockets.clearSelection();
            matchingSockets.setModel(null);
        }
        populateConnectionsList();
    }


    private void populateMatchingSocketsList(final boolean preserveOpenPaths, final Listitem selectedItem)
    {
        int[][] openPaths = null;
        if(preserveOpenPaths)
        {
            final TreeOpenableModel model = (TreeOpenableModel)matchingSockets.getModel();
            if(model != null)
            {
                openPaths = model.getOpenPaths();
            }
        }
        if(selectedItem == null)
        {
            matchingSockets.setModel(null);
        }
        else
        {
            final MatchingSocketsTreeModel treeModel = new MatchingSocketsTreeModel(getWidget(),
                            selectedItem.<WidgetSocket>getValue(), socketMatchStrategies, widgetDefinitionService, widgetService);
            if(openPaths != null)
            {
                treeModel.addOpenPaths(openPaths);
            }
            matchingSockets.setModel(treeModel);
        }
    }


    private SimpleGroupsModel<WidgetSocket, String, Object, Object> createSourceSocketsModel()
    {
        final List<WidgetSocket> outputs = WidgetSocketUtils.getAllOutputs(getWidget(), getWidgetDefinition(getWidget()));
        final List<WidgetSocket> inputs = WidgetSocketUtils.getAllInputs(getWidget(), getWidgetDefinition(getWidget()));
        final List<WidgetSocket> socketsInMetadata = Lists.newArrayList();
        final List<WidgetSocket> socketsOutMetadata = Lists.newArrayList();
        for(final WidgetSocket socket : outputs)
        {
            socketsOutMetadata.add(socket);
        }
        for(final WidgetSocket socket : inputs)
        {
            socketsInMetadata.add(socket);
        }
        return new SimpleGroupsModel<>(Lists.newArrayList(socketsInMetadata, socketsOutMetadata), Lists.newArrayList("in", "out"));
    }


    private Widget getWidget()
    {
        return (Widget)arg.get(WIDGET);
    }


    @Required
    public void setSocketMatchStrategies(final List<WidgetSocketMatchStrategy> socketMatchStrategies)
    {
        this.socketMatchStrategies = socketMatchStrategies;
    }
}
