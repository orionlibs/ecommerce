package de.hybris.platform.commercewebservicescommons.dto.search.solrfacetsearch;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "SolrSearchQueryTerm", description = "Representation of a Solr Search Query Term")
public class SolrSearchQueryTermWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "key", value = "Key of solr search query term")
    private String key;
    @ApiModelProperty(name = "value", value = "Value of solr search query term")
    private String value;


    public void setKey(String key)
    {
        this.key = key;
    }


    public String getKey()
    {
        return this.key;
    }


    public void setValue(String value)
    {
        this.value = value;
    }


    public String getValue()
    {
        return this.value;
    }
}
