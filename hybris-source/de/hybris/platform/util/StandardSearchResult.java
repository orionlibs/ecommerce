package de.hybris.platform.util;

import de.hybris.platform.jalo.SearchResult;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class StandardSearchResult<E> implements SearchResult<E>, Serializable
{
    private final List<E> originalList;
    private final List<E> unmodifieableList;
    private final int totalcount;
    private final int requestedstart;
    private final int requestedcount;
    private final String dataSourceId;
    private boolean fromCache = false;
    private int unitHash = 0;


    public StandardSearchResult(List<E> list, int totalcount, int requestedstart, int requestedcount, String dataSourceId)
    {
        this.originalList = list;
        this.unmodifieableList = (list != null) ? Collections.<E>unmodifiableList(list) : null;
        this.totalcount = totalcount;
        this.requestedstart = requestedstart;
        this.requestedcount = requestedcount;
        this.dataSourceId = dataSourceId;
    }


    @Deprecated(since = "2105", forRemoval = true)
    public StandardSearchResult(List<E> list, int totalcount, int requestedstart, int requestedcount)
    {
        this(list, totalcount, requestedstart, requestedcount, "undefined");
    }


    public int getCount()
    {
        return this.originalList.size();
    }


    public int getTotalCount()
    {
        return this.totalcount;
    }


    public List<E> getOriginalResultList()
    {
        return this.originalList;
    }


    public List<E> getResult()
    {
        return this.unmodifieableList;
    }


    public int getRequestedStart()
    {
        return this.requestedstart;
    }


    public int getRequestedCount()
    {
        return this.requestedcount;
    }


    public boolean isFromCache()
    {
        return this.fromCache;
    }


    public int getUnitHash()
    {
        return this.unitHash;
    }


    public void setUnitHash(int hash)
    {
        this.unitHash = hash;
    }


    public void setFromCache(boolean fromCache)
    {
        this.fromCache = fromCache;
    }


    public String getDataSourceId()
    {
        return this.dataSourceId;
    }


    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }


    public boolean equals(Object obj)
    {
        if(!(obj instanceof StandardSearchResult))
        {
            return super.equals(obj);
        }
        return EqualsBuilder.reflectionEquals(this, obj);
    }


    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
