package de.hybris.platform.commercefacades.store.data;

import java.io.Serializable;
import java.util.List;

public class StoreCountData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String type;
    private String name;
    private String isoCode;
    private Integer count;
    private List<StoreCountData> storeCountDataList;


    public void setType(String type)
    {
        this.type = type;
    }


    public String getType()
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


    public void setStoreCountDataList(List<StoreCountData> storeCountDataList)
    {
        this.storeCountDataList = storeCountDataList;
    }


    public List<StoreCountData> getStoreCountDataList()
    {
        return this.storeCountDataList;
    }
}
