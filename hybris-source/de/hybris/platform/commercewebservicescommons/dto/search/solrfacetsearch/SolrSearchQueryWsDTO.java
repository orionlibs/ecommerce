package de.hybris.platform.commercewebservicescommons.dto.search.solrfacetsearch;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "SolrSearchQuery", description = "Representation of a Solr Search Query")
public class SolrSearchQueryWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "categoryCode", value = "Code of category")
    private String categoryCode;


    public void setCategoryCode(String categoryCode)
    {
        this.categoryCode = categoryCode;
    }


    public String getCategoryCode()
    {
        return this.categoryCode;
    }
}
