package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

@ApiModel(value = "Suggestion", description = "Representation of a Suggestion")
public class SuggestionWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "value", value = "Suggestion value")
    private String value;


    public void setValue(String value)
    {
        this.value = value;
    }


    public String getValue()
    {
        return this.value;
    }
}
