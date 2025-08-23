package de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.servlet;

public class Edge
{
    private final boolean directed;
    private final Vertex target;
    private final Vertex source;
    private final String label;


    public Edge(Vertex source, Vertex target, String label)
    {
        this(source, target, true, label);
    }


    public Edge(Vertex source, Vertex target, boolean directed, String label)
    {
        if(source == null)
        {
            throw new IllegalArgumentException("Source must not be null");
        }
        if(target == null)
        {
            throw new IllegalArgumentException("Target must not be null");
        }
        if(label == null)
        {
            throw new IllegalArgumentException("Label must not be null");
        }
        this.source = source;
        this.target = target;
        this.directed = directed;
        this.label = label;
    }


    public int hashCode()
    {
        if(this.directed)
        {
            return this.target.hashCode() + this.source.hashCode();
        }
        return -(this.target.hashCode() + this.source.hashCode() + 1);
    }


    public boolean equals(Object obj)
    {
        if(obj instanceof Edge)
        {
            if(((Edge)obj).directed == this.directed)
            {
                if(this.directed)
                {
                    return (this.source.equals(((Edge)obj).source) && this.target.equals(((Edge)obj).target));
                }
                return ((this.source.equals(((Edge)obj).source) && this.target.equals(((Edge)obj).target)) || (this.source
                                .equals(((Edge)obj).target) && this.target.equals(((Edge)obj).source)));
            }
        }
        return false;
    }


    public boolean isDirected()
    {
        return this.directed;
    }


    public Vertex getTarget()
    {
        return this.target;
    }


    public Vertex getSource()
    {
        return this.source;
    }


    public String getLabel()
    {
        return this.label;
    }
}
