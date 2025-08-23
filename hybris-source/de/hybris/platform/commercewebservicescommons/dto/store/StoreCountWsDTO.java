package de.hybris.platform.commercewebservicescommons.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "StoreCount", description = "Representation of a Store Count")
public class StoreCountWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "type", value = "Type of store count")
    private String type;
    @ApiModelProperty(name = "name", value = "Name of store count")
    private String name;
    @ApiModelProperty(name = "isoCode", value = "Iso code of store")
    private String isoCode;
    @ApiModelProperty(name = "count", value = "Count")
    private Integer count;
    @ApiModelProperty(name = "storeCountDataList", value = "List of store counts")
    private List<StoreCountWsDTO> storeCountDataList;


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


    public void setStoreCountDataList(List<StoreCountWsDTO> storeCountDataList)
    {
        this.storeCountDataList = storeCountDataList;
    }


    public List<StoreCountWsDTO> getStoreCountDataList()
    {
        return this.storeCountDataList;
    }
}
