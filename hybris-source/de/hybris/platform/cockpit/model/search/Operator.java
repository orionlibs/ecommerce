package de.hybris.platform.cockpit.model.search;

public class Operator
{
    public static final Operator EQUALS = new Operator("equals");
    public static final Operator BETWEEN = new Operator("between");
    public static final Operator IN = new Operator("in");
    public static final Operator CONTAINS = new Operator("contains");
    public static final Operator GREATER_OR_EQUALS = new Operator("greaterOrEquals");
    public static final Operator LESS_OR_EQUALS = new Operator("lessOrEquals");
    private final String qualifier;


    public String getQualifier()
    {
        return this.qualifier;
    }


    public Operator(String qualifier)
    {
        this.qualifier = qualifier;
    }


    public boolean equals(Object obj)
    {
        if(obj instanceof Operator)
        {
            return (this.qualifier == null) ? ((((Operator)obj).qualifier == null)) : this.qualifier.equals(((Operator)obj).qualifier);
        }
        return false;
    }


    public int hashCode()
    {
        return (this.qualifier == null) ? 0 : this.qualifier.hashCode();
    }


    public String toString()
    {
        return "Operator(" + this.qualifier + ")";
    }
}
