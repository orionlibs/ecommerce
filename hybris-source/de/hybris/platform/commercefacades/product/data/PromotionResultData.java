package de.hybris.platform.commercefacades.product.data;

import de.hybris.platform.commercefacades.coupon.data.CouponData;
import de.hybris.platform.commercefacades.order.data.PromotionOrderEntryConsumedData;
import java.io.Serializable;
import java.util.List;

public class PromotionResultData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String description;
    private List<CouponData> giveAwayCouponCodes;
    private PromotionData promotionData;
    private List<PromotionOrderEntryConsumedData> consumedEntries;


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setGiveAwayCouponCodes(List<CouponData> giveAwayCouponCodes)
    {
        this.giveAwayCouponCodes = giveAwayCouponCodes;
    }


    public List<CouponData> getGiveAwayCouponCodes()
    {
        return this.giveAwayCouponCodes;
    }


    public void setPromotionData(PromotionData promotionData)
    {
        this.promotionData = promotionData;
    }


    public PromotionData getPromotionData()
    {
        return this.promotionData;
    }


    public void setConsumedEntries(List<PromotionOrderEntryConsumedData> consumedEntries)
    {
        this.consumedEntries = consumedEntries;
    }


    public List<PromotionOrderEntryConsumedData> getConsumedEntries()
    {
        return this.consumedEntries;
    }
}
