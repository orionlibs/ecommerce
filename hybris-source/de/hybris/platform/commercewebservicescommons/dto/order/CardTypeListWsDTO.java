package de.hybris.platform.commercewebservicescommons.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "CardTypeList", description = "Representation of a Card Type List")
public class CardTypeListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "cardTypes", value = "List of card types")
    private List<CardTypeWsDTO> cardTypes;


    public void setCardTypes(List<CardTypeWsDTO> cardTypes)
    {
        this.cardTypes = cardTypes;
    }


    public List<CardTypeWsDTO> getCardTypes()
    {
        return this.cardTypes;
    }
}
