package de.hybris.y2ysync.rest.resources;

import java.util.List;

public class DataStream
{
    private final String itemType;
    private String dataHubType;
    private final String columns;
    private final boolean delete;
    private final List<String> urls;


    public DataStream(String itemType, String columns, boolean delete, List<String> urls)
    {
        this.itemType = itemType;
        this.columns = columns;
        this.urls = urls;
        this.delete = delete;
    }


    public DataStream(String itemType, String columns, boolean delete, List<String> urls, String dataHubType)
    {
        this.itemType = itemType;
        this.columns = columns;
        this.urls = urls;
        this.delete = delete;
        this.dataHubType = dataHubType;
    }


    public String getItemType()
    {
        return this.itemType;
    }


    public String getColumns()
    {
        return this.columns;
    }


    public List<String> getUrls()
    {
        return this.urls;
    }


    public void addUrl(String url)
    {
        this.urls.add(url);
    }


    public boolean isDelete()
    {
        return this.delete;
    }


    public String getDataHubType()
    {
        return this.dataHubType;
    }


    public void setDataHubType(String dataHubType)
    {
        this.dataHubType = dataHubType;
    }


    public String toString()
    {
        return "DataStream{itemType='" + this.itemType + "', columns='" + this.columns + "', urls=" + this.urls + "}";
    }
}
