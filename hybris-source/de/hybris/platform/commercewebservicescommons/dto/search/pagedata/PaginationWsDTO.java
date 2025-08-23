package de.hybris.platform.commercewebservicescommons.dto.search.pagedata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "deprecatedPagination", description = "Representation of a search results pagination")
@Deprecated(since = "6.5", forRemoval = true)
public class PaginationWsDTO extends PageableWsDTO
{
    @ApiModelProperty(name = "totalPages", value = "The total number of pages. This is the number of pages, each of pageSize, required to display the totalResults.")
    private Integer totalPages;
    @ApiModelProperty(name = "totalResults", value = "The total number of matched results across all pages")
    private Long totalResults;


    public void setTotalPages(Integer totalPages)
    {
        this.totalPages = totalPages;
    }


    public Integer getTotalPages()
    {
        return this.totalPages;
    }


    public void setTotalResults(Long totalResults)
    {
        this.totalResults = totalResults;
    }


    public Long getTotalResults()
    {
        return this.totalResults;
    }
}
