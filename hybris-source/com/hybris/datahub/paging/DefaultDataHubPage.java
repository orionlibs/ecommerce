package com.hybris.datahub.paging;

import java.util.List;
import java.util.function.Consumer;

public class DefaultDataHubPage<T> implements DataHubPage<T>
{
    private final int pageSize;
    private final int currentPage;
    private final long totalElements;
    private final List<T> content;


    public DefaultDataHubPage(int pageSize, int currentPage, List<T> content)
    {
        this(pageSize, currentPage, 0L, content);
    }


    public DefaultDataHubPage(int pageSize, int currentPage, long totalElements, List<T> content)
    {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.content = content;
        this.totalElements = totalElements;
    }


    public static Builder<Object> page()
    {
        return pageOf(Object.class);
    }


    public static <T> Builder<T> pageOf(Class<T> contentType)
    {
        return new Builder();
    }


    public List<T> getContent()
    {
        return this.content;
    }


    public int getNumber()
    {
        return this.currentPage;
    }


    public int getNumberOfElements()
    {
        return this.content.size();
    }


    public int getSize()
    {
        return this.pageSize;
    }


    public long getTotalElements()
    {
        return this.totalElements;
    }


    public int getTotalPages()
    {
        return (this.totalElements == 0L) ? 0 : (int)Math.ceil(((float)this.totalElements / this.pageSize));
    }


    public boolean isEmpty()
    {
        return this.content.isEmpty();
    }


    public boolean isFull()
    {
        return (getNumberOfElements() == getSize());
    }


    public void forEach(Consumer<T> action)
    {
        this.content.forEach(action);
    }
}
