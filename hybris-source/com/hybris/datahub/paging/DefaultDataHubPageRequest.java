package com.hybris.datahub.paging;

import com.google.common.base.Preconditions;

public class DefaultDataHubPageRequest implements DataHubPageable
{
    private final int pageNumber;
    private final int pageSize;


    public DefaultDataHubPageRequest(int number, int size)
    {
        Preconditions.checkArgument((number >= 0), "Page number cannot be negative");
        Preconditions.checkArgument((size > 0), "Page size must be greater than zero");
        this.pageNumber = number;
        this.pageSize = size;
    }


    public static DataHubPageable firstPage(int pageSize)
    {
        return new DefaultDataHubPageRequest(0, pageSize);
    }


    public static DataHubPageable nextPageFor(DataHubPage<?> page)
    {
        return new DefaultDataHubPageRequest(page.getNumber() + 1, page.getSize());
    }


    public int getPageNumber()
    {
        return this.pageNumber;
    }


    public int getPageSize()
    {
        return this.pageSize;
    }


    public int getOffset()
    {
        return getPageNumber() * getPageSize();
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
        DefaultDataHubPageRequest that = (DefaultDataHubPageRequest)o;
        return (this.pageNumber == that.pageNumber && this.pageSize == that.pageSize);
    }


    public int hashCode()
    {
        int result = this.pageNumber;
        result = 31 * result + this.pageSize;
        return result;
    }


    public String toString()
    {
        return "DefaultDataHubPageRequest{pageNumber=" + this.pageNumber + ", pageSize=" + this.pageSize + "}";
    }
}
