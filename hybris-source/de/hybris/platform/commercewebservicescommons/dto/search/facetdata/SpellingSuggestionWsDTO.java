package de.hybris.platform.commercewebservicescommons.dto.search.facetdata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "SpellingSuggestion", description = "Representation of a Spell Checker Suggestion")
public class SpellingSuggestionWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "suggestion", value = "Spelling suggestion")
    private String suggestion;
    @ApiModelProperty(name = "query", value = "Query for spelling suggestion")
    private String query;


    public void setSuggestion(String suggestion)
    {
        this.suggestion = suggestion;
    }


    public String getSuggestion()
    {
        return this.suggestion;
    }


    public void setQuery(String query)
    {
        this.query = query;
    }


    public String getQuery()
    {
        return this.query;
    }
}
