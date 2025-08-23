package de.hybris.platform.commercewebservicescommons.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "TitleList", description = "Representation of a Title List")
public class TitleListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "titles", value = "List of titles")
    private List<TitleWsDTO> titles;


    public void setTitles(List<TitleWsDTO> titles)
    {
        this.titles = titles;
    }


    public List<TitleWsDTO> getTitles()
    {
        return this.titles;
    }
}
