package de.hybris.platform.store.pojo;

import java.io.Serializable;
import java.util.List;

public class StoreCountInfo implements Serializable
{
    private static final long serialVersionUID = 1L;
    private StoreCountType type;
    private String name;
    private String isoCode;
    private Integer count;
    private List<StoreCountInfo> storeCountInfoList;


    public void setType(StoreCountType type)
    {
        this.type = type;
    }


    public StoreCountType getType()
    {
        return this.type;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setIsoCode(String isoCode)
    {
        this.isoCode = isoCode;
    }


    public String getIsoCode()
    {
        return this.isoCode;
    }


    public void setCount(Integer count)
    {
        this.count = count;
    }


    public Integer getCount()
    {
        return this.count;
    }


    public void setStoreCountInfoList(List<StoreCountInfo> storeCountInfoList)
    {
        this.storeCountInfoList = storeCountInfoList;
    }


    public List<StoreCountInfo> getStoreCountInfoList()
    {
        return this.storeCountInfoList;
    }
}
