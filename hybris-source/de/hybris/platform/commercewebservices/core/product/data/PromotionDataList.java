package de.hybris.platform.commercewebservices.core.product.data;

import de.hybris.platform.commercefacades.product.data.PromotionData;
import java.io.Serializable;
import java.util.List;

public class PromotionDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<PromotionData> promotions;


    public void setPromotions(List<PromotionData> promotions)
    {
        this.promotions = promotions;
    }


    public List<PromotionData> getPromotions()
    {
        return this.promotions;
    }
}
