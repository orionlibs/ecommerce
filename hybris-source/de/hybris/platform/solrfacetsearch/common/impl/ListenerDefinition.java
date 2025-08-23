package de.hybris.platform.solrfacetsearch.common.impl;

import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class ListenerDefinition implements Comparable<ListenerDefinition>
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


    public int compareTo(ListenerDefinition other)
    {
        return Integer.compare(other.getPriority(), getPriority());
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        ListenerDefinition that = (ListenerDefinition)obj;
        return (new EqualsBuilder())
                        .append(this.listener, that.listener)
                        .append(this.priority, that.priority)
                        .isEquals();
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.listener, Integer.valueOf(this.priority)});
    }
}
