package de.hybris.platform.commercewebservicescommons.dto.storesession;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "LanguageList", description = "Lists all available languages (all languages used for a particular store). If the list of languages for a base store is empty, a list of all languages available in the system will be returned")
public class LanguageListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "languages", value = "This is the list of Language fields that should be returned in the response body")
    private List<LanguageWsDTO> languages;


    public void setLanguages(List<LanguageWsDTO> languages)
    {
        this.languages = languages;
    }


    public List<LanguageWsDTO> getLanguages()
    {
        return this.languages;
    }
}
