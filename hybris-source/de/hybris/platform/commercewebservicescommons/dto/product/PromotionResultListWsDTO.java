package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "PromotionResultList", description = "Representation of a Promotion result list")
public class PromotionResultListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "promotions", value = "List of promotion results")
    private List<PromotionResultWsDTO> promotions;


    public void setPromotions(List<PromotionResultWsDTO> promotions)
    {
        this.promotions = promotions;
    }


    public List<PromotionResultWsDTO> getPromotions()
    {
        return this.promotions;
    }
}
