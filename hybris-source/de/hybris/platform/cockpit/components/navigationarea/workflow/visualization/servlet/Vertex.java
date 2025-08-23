package de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.servlet;

public class Vertex
{
    private final String id;
    private final String label;
    private final boolean startPoint;
    private final boolean endPoint;
    private VertexColor color = VertexColor.GREY;


    public Vertex(String id, String label)
    {
        this(id, label, false, false);
    }


    public Vertex(String id, String label, boolean startPoint, boolean endPoint)
    {
        if(id == null)
        {
            throw new IllegalArgumentException("Id must not be null!");
        }
        this.id = id;
        this.label = (label == null) ? "" : label;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }


    public int hashCode()
    {
        return this.id.hashCode();
    }


    public boolean equals(Object obj)
    {
        if(obj instanceof Vertex)
        {
            return this.id.equals(((Vertex)obj).id);
        }
        return false;
    }


    public String getId()
    {
        return this.id;
    }


    public String getLabel()
    {
        return this.label;
    }


    public boolean isStartPoint()
    {
        return this.startPoint;
    }


    public boolean isEndPoint()
    {
        return this.endPoint;
    }


    public VertexColor getColor()
    {
        return this.color;
    }


    public void setColor(VertexColor color)
    {
        if(color == null)
        {
            this.color = VertexColor.GREY;
        }
        else
        {
            this.color = color;
        }
    }
}
