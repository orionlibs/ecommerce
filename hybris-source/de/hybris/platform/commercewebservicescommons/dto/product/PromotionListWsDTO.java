package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "PromotionList", description = "Representation of a Promotion list")
public class PromotionListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "promotions", value = "List of promotions")
    private List<PromotionWsDTO> promotions;


    public void setPromotions(List<PromotionWsDTO> promotions)
    {
        this.promotions = promotions;
    }


    public List<PromotionWsDTO> getPromotions()
    {
        return this.promotions;
    }
}
