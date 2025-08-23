package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "SuggestionList", description = "Representation of a Suggestion List")
public class SuggestionListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "suggestions", value = "List of suggestions")
    private List<SuggestionWsDTO> suggestions;


    public void setSuggestions(List<SuggestionWsDTO> suggestions)
    {
        this.suggestions = suggestions;
    }


    public List<SuggestionWsDTO> getSuggestions()
    {
        return this.suggestions;
    }
}
