package de.hybris.platform.commercewebservices.core.product.data;

import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import java.io.Serializable;
import java.util.List;

public class PromotionResultDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<PromotionResultData> promotions;


    public void setPromotions(List<PromotionResultData> promotions)
    {
        this.promotions = promotions;
    }


    public List<PromotionResultData> getPromotions()
    {
        return this.promotions;
    }
}
