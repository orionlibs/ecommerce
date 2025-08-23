package de.hybris.platform.webservicescommons.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "searchPage", description = "Includes all necessary data for creating proper result in refine search")
public class SearchPageWsDTO<RESULT> implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "results", value = "Result list")
    private List<RESULT> results;
    @ApiModelProperty(name = "sorts")
    private List<SortWsDTO> sorts;
    @ApiModelProperty(name = "pagination", value = "Pagination info")
    private PaginationWsDTO pagination;


    public void setResults(List<RESULT> results)
    {
        this.results = results;
    }


    public List<RESULT> getResults()
    {
        return this.results;
    }


    public void setSorts(List<SortWsDTO> sorts)
    {
        this.sorts = sorts;
    }


    public List<SortWsDTO> getSorts()
    {
        return this.sorts;
    }


    public void setPagination(PaginationWsDTO pagination)
    {
        this.pagination = pagination;
    }


    public PaginationWsDTO getPagination()
    {
        return this.pagination;
    }
}
