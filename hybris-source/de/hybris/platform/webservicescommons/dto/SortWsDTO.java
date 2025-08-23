package de.hybris.platform.webservicescommons.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "sort", description = "Sort option")
public class SortWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "code")
    private String code;
    @ApiModelProperty(name = "asc")
    private boolean asc;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setAsc(boolean asc)
    {
        this.asc = asc;
    }


    public boolean isAsc()
    {
        return this.asc;
    }
}
