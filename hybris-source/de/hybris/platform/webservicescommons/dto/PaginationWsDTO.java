package de.hybris.platform.webservicescommons.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "pagination", description = "Pagination info")
public class PaginationWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "count", value = "Number of elements on this page")
    private Integer count;
    @ApiModelProperty(name = "totalCount", value = "Total number of elements")
    private Long totalCount;
    @ApiModelProperty(name = "page", value = "Current page number")
    private Integer page;
    @ApiModelProperty(name = "totalPages", value = "Total number of pages")
    private Integer totalPages;
    @ApiModelProperty(name = "hasNext", value = "Indicates if there is next page")
    private Boolean hasNext;
    @ApiModelProperty(name = "hasPrevious", value = "Indicates if there is previous page")
    private Boolean hasPrevious;


    public void setCount(Integer count)
    {
        this.count = count;
    }


    public Integer getCount()
    {
        return this.count;
    }


    public void setTotalCount(Long totalCount)
    {
        this.totalCount = totalCount;
    }


    public Long getTotalCount()
    {
        return this.totalCount;
    }


    public void setPage(Integer page)
    {
        this.page = page;
    }


    public Integer getPage()
    {
        return this.page;
    }


    public void setTotalPages(Integer totalPages)
    {
        this.totalPages = totalPages;
    }


    public Integer getTotalPages()
    {
        return this.totalPages;
    }


    public void setHasNext(Boolean hasNext)
    {
        this.hasNext = hasNext;
    }


    public Boolean getHasNext()
    {
        return this.hasNext;
    }


    public void setHasPrevious(Boolean hasPrevious)
    {
        this.hasPrevious = hasPrevious;
    }


    public Boolean getHasPrevious()
    {
        return this.hasPrevious;
    }
}
