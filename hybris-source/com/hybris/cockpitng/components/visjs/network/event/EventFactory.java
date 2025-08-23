/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.visjs.network.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.EdgeUpdate;
import com.hybris.cockpitng.components.visjs.network.data.Edges;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.components.visjs.network.data.Nodes;
import com.hybris.cockpitng.components.visjs.network.data.Point;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.ui.event.Event;

/**
 * Factory which converts zk's AuRequest to networkChart event
 */
public class EventFactory
{
    private final ObjectMapper mapper = new ObjectMapper();


    /**
     * Converts zk's AuRequest to networkChart event
     *
     * @param request
     *           ZK's AuRequest
     * @return converted networkchart's event. In case when event doesn't match, null value will be returned
     */
    public Event getEvent(final AuRequest request)
    {
        final String cmd = request.getCommand();
        switch(cmd)
        {
            case ClickNodeEvent.NAME:
                return new ClickNodeEvent(request.getComponent(), extractNode(request));
            case DoubleClickNodeEvent.NAME:
                return new DoubleClickNodeEvent(request.getComponent(), extractNode(request));
            case SelectNodeEvent.NAME:
                return new SelectNodeEvent(request.getComponent(), extractNode(request));
            case DeselectNodesEvent.NAME:
                return new DeselectNodesEvent(request.getComponent(), extractNodes(request));
            case AddNodeEvent.NAME:
                return new AddNodeEvent(request.getComponent(), extractNode(request));
            case EditNodeEvent.NAME:
                return new EditNodeEvent(request.getComponent(), extractNode(request));
            case RemoveNodesEvent.NAME:
                return new RemoveNodesEvent(request.getComponent(), extractNodes(request));
            case ClickEdgeEvent.NAME:
                return new ClickEdgeEvent(request.getComponent(), extractEdge(request));
            case DoubleClickEdgeEvent.NAME:
                return new DoubleClickEdgeEvent(request.getComponent(), extractEdge(request));
            case SelectEdgeEvent.NAME:
                return new SelectEdgeEvent(request.getComponent(), extractEdge(request));
            case DeselectEdgesEvent.NAME:
                return new DeselectEdgesEvent(request.getComponent(), extractEdges(request));
            case AddEdgeEvent.NAME:
                return new AddEdgeEvent(request.getComponent(), extractEdge(request));
            case EditEdgeEvent.NAME:
                return new EditEdgeEvent(request.getComponent(), extractEdgeUpdate(request));
            case RemoveEdgesEvent.NAME:
                return new RemoveEdgesEvent(request.getComponent(), extractEdges(request));
            case HoverEdgeEvent.NAME:
                return new HoverEdgeEvent(request.getComponent(), extractEdge(request));
            case HoverNodeEvent.NAME:
                return new HoverNodeEvent(request.getComponent(), extractNode(request));
            case BlurNodeEvent.NAME:
                return new BlurNodeEvent(request.getComponent(), extractNode(request));
            case BlurEdgeEvent.NAME:
                return new BlurEdgeEvent(request.getComponent(), extractEdge(request));
            case DragEndEvent.NAME:
                return new DragEndEvent(request.getComponent(), extractNode(request));
            case ClickOnAddNodeButtonEvent.NAME:
                return new ClickOnAddNodeButtonEvent(request.getComponent(), request);
            case ViewPositionChangeEvent.NAME:
                return new ViewPositionChangeEvent(request.getComponent(), extract(request, Point.class));
            default:
                return null;
        }
    }


    protected Node extractNode(final AuRequest request)
    {
        return extract(request, Node.class);
    }


    protected Edge extractEdge(final AuRequest request)
    {
        return extract(request, Edge.class);
    }


    protected Nodes extractNodes(final AuRequest request)
    {
        return extract(request, Nodes.class);
    }


    protected Edges extractEdges(final AuRequest request)
    {
        return extract(request, Edges.class);
    }


    protected EdgeUpdate extractEdgeUpdate(final AuRequest request)
    {
        return extract(request, EdgeUpdate.class);
    }


    protected <T> T extract(final AuRequest request, final Class<T> clazz)
    {
        return mapper.convertValue(request.getData(), clazz);
    }
}
