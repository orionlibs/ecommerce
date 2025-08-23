package de.hybris.platform.commercewebservicescommons.dto.product;

import de.hybris.platform.commercewebservicescommons.dto.order.PromotionOrderEntryConsumedWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "PromotionResult", description = "Representation of a Promotion result")
public class PromotionResultWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "description", value = "Description of promotion result")
    private String description;
    @ApiModelProperty(name = "promotion", value = "Promotion information for given promotion result")
    private PromotionWsDTO promotion;
    @ApiModelProperty(name = "consumedEntries", value = "List of promotion order entries consumed")
    private List<PromotionOrderEntryConsumedWsDTO> consumedEntries;


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setPromotion(PromotionWsDTO promotion)
    {
        this.promotion = promotion;
    }


    public PromotionWsDTO getPromotion()
    {
        return this.promotion;
    }


    public void setConsumedEntries(List<PromotionOrderEntryConsumedWsDTO> consumedEntries)
    {
        this.consumedEntries = consumedEntries;
    }


    public List<PromotionOrderEntryConsumedWsDTO> getConsumedEntries()
    {
        return this.consumedEntries;
    }
}
