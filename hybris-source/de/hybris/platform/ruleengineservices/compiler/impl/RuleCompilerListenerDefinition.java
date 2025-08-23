package de.hybris.platform.ruleengineservices.compiler.impl;

public class RuleCompilerListenerDefinition implements Comparable<RuleCompilerListenerDefinition>
{
    private int priority;
    private Object listener;


    public int getPriority()
    {
        return this.priority;
    }


    public void setPriority(int priority)
    {
        this.priority = priority;
    }


    public Object getListener()
    {
        return this.listener;
    }


    public void setListener(Object listener)
    {
        this.listener = listener;
    }


    public int compareTo(RuleCompilerListenerDefinition other)
    {
        return Integer.compare(other.getPriority(), getPriority());
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null)
        {
            return false;
        }
        if(!o.getClass().equals(RuleCompilerListenerDefinition.class))
        {
            return false;
        }
        RuleCompilerListenerDefinition that = (RuleCompilerListenerDefinition)o;
        if(getPriority() != that.getPriority())
        {
            return false;
        }
        return (getListener() != null) ? getListener().equals(that.getListener()) : ((that.getListener() == null));
    }


    public int hashCode()
    {
        return getPriority() + ((getListener() == null) ? 0 : getListener().hashCode());
    }
}
