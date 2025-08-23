package de.hybris.platform.commercewebservicescommons.dto.search.pagedata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "Pageable", description = "Representation of a search query pagination")
@Deprecated(since = "6.5", forRemoval = true)
public class PageableWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "pageSize", value = "The number of results per page. A page may have less results if there are less than a full page of results, only on the last page in the results")
    private Integer pageSize;
    @ApiModelProperty(name = "currentPage", value = "The current page number. The first page is number zero (0), the second page is number one (1), and so on")
    private Integer currentPage;
    @ApiModelProperty(name = "sort", value = "The selected sort code")
    private String sort;


    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }


    public Integer getPageSize()
    {
        return this.pageSize;
    }


    public void setCurrentPage(Integer currentPage)
    {
        this.currentPage = currentPage;
    }


    public Integer getCurrentPage()
    {
        return this.currentPage;
    }


    public void setSort(String sort)
    {
        this.sort = sort;
    }


    public String getSort()
    {
        return this.sort;
    }
}
