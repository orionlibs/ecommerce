package de.hybris.platform.commercewebservicescommons.dto.search;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "SearchState", description = "Representation of a Search State")
public class SearchStateWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "url", value = "Url address of search state")
    private String url;
    @ApiModelProperty(name = "query", value = "Query of search state")
    private SearchQueryWsDTO query;


    public void setUrl(String url)
    {
        this.url = url;
    }


    public String getUrl()
    {
        return this.url;
    }


    public void setQuery(SearchQueryWsDTO query)
    {
        this.query = query;
    }


    public SearchQueryWsDTO getQuery()
    {
        return this.query;
    }
}
