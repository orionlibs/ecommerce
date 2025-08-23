package de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.servlet;

import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.EdgeStyle;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class GraphRepresentation
{
    private final Set<Vertex> vertexes = new HashSet<>();
    private final Set<Edge> edges = new HashSet<>();
    private final boolean showStart;
    private final boolean showEnd;
    private EdgeStyle edgeStyle = EdgeStyle.LABELS_IN_CIRCLE;


    private GraphRepresentation(boolean showStart, boolean showEnd)
    {
        this.showStart = showStart;
        this.showEnd = showEnd;
    }


    public static GraphRepresentation getInstance(boolean showStart, boolean showEnd)
    {
        return new GraphRepresentation(showStart, showEnd);
    }


    public boolean addVertex(Vertex vertex)
    {
        return this.vertexes.add(vertex);
    }


    public boolean addEdge(Edge edge)
    {
        if(!this.vertexes.contains(edge.getSource()))
        {
            this.vertexes.add(edge.getSource());
        }
        if(!this.vertexes.contains(edge.getTarget()))
        {
            this.vertexes.add(edge.getTarget());
        }
        return this.edges.add(edge);
    }


    public Set<Vertex> getVertexes()
    {
        return Collections.unmodifiableSet(this.vertexes);
    }


    public Set<Edge> getEdges()
    {
        return Collections.unmodifiableSet(this.edges);
    }


    public boolean isShowStart()
    {
        return this.showStart;
    }


    public boolean isShowEnd()
    {
        return this.showEnd;
    }


    public Vertex getVertex(String id)
    {
        for(Vertex vertex : this.vertexes)
        {
            if(vertex.getId().equals(id))
            {
                return vertex;
            }
        }
        return null;
    }


    public EdgeStyle getEdgeStyle()
    {
        return this.edgeStyle;
    }


    public void setEdgeStyle(EdgeStyle edgeStyle)
    {
        this.edgeStyle = edgeStyle;
    }
}
