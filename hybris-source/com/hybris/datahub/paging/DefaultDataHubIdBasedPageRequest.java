package com.hybris.datahub.paging;

import com.hybris.datahub.model.BaseDataItem;

public class DefaultDataHubIdBasedPageRequest implements DataHubIdBasedPageable
{
    private final int pageSize;
    private final long lastProcessedId;


    public static DataHubIdBasedPageable firstPage(int size)
    {
        return new DefaultDataHubIdBasedPageRequest(size, 0L);
    }


    public static DataHubIdBasedPageable nextPageFor(DataHubPage<? extends BaseDataItem> page)
    {
        Long lastId = page.isEmpty() ? null : ((BaseDataItem)page.getContent().get(page.getNumberOfElements() - 1)).getId();
        return new DefaultDataHubIdBasedPageRequest(page.getSize(), (lastId != null) ? lastId.longValue() : Long.MAX_VALUE);
    }


    public DefaultDataHubIdBasedPageRequest(int pageSize, long lastProcessedId)
    {
        if(pageSize <= 0 || lastProcessedId < 0L)
        {
            throw new IllegalArgumentException("Illegal page size or last processed id.");
        }
        this.pageSize = pageSize;
        this.lastProcessedId = lastProcessedId;
    }


    public int getPageSize()
    {
        return this.pageSize;
    }


    public long getLastProcessedId()
    {
        return this.lastProcessedId;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        DefaultDataHubIdBasedPageRequest that = (DefaultDataHubIdBasedPageRequest)o;
        if(this.lastProcessedId != that.lastProcessedId)
        {
            return false;
        }
        if(this.pageSize != that.pageSize)
        {
            return false;
        }
        return true;
    }


    public int hashCode()
    {
        int result = this.pageSize;
        result = 31 * result + (int)(this.lastProcessedId ^ this.lastProcessedId >>> 32L);
        return result;
    }


    public String toString()
    {
        return "DefaultDataHubIdBasedPageRequest{lastProcessedId=" + this.lastProcessedId + ", pageSize=" + this.pageSize + "}";
    }
}
