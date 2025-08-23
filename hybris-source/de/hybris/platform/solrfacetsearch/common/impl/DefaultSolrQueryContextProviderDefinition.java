package de.hybris.platform.solrfacetsearch.common.impl;

import de.hybris.platform.solrfacetsearch.common.SolrQueryContextProvider;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class DefaultSolrQueryContextProviderDefinition implements Comparable<DefaultSolrQueryContextProviderDefinition>
{
    private int priority;
    private SolrQueryContextProvider queryContextProvider;


    public int getPriority()
    {
        return this.priority;
    }


    public void setPriority(int priority)
    {
        this.priority = priority;
    }


    public SolrQueryContextProvider getQueryContextProvider()
    {
        return this.queryContextProvider;
    }


    public void setQueryContextProvider(SolrQueryContextProvider queryContextProvider)
    {
        this.queryContextProvider = queryContextProvider;
    }


    public int compareTo(DefaultSolrQueryContextProviderDefinition that)
    {
        return Integer.compare(that.getPriority(), getPriority());
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
        DefaultSolrQueryContextProviderDefinition that = (DefaultSolrQueryContextProviderDefinition)obj;
        return (new EqualsBuilder()).append(this.priority, that.priority).append(this.queryContextProvider, that.queryContextProvider)
                        .isEquals();
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.queryContextProvider, Integer.valueOf(this.priority)});
    }
}
