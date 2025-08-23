package de.hybris.platform.commercewebservicescommons.dto.quote;

import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PaginationWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "QuoteList", description = "Representation of a Quote result list.")
public class QuoteListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "quotes", value = "List of quotes.")
    private List<QuoteWsDTO> quotes;
    @ApiModelProperty(name = "pagination", value = "Pagination of quotes list.")
    private PaginationWsDTO pagination;


    public void setQuotes(List<QuoteWsDTO> quotes)
    {
        this.quotes = quotes;
    }


    public List<QuoteWsDTO> getQuotes()
    {
        return this.quotes;
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
