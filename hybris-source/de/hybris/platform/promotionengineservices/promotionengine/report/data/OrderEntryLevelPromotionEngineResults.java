package de.hybris.platform.promotionengineservices.promotionengine.report.data;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import java.math.BigDecimal;

public class OrderEntryLevelPromotionEngineResults extends AbstractPromotionEngineResults
{
    private AbstractOrderEntryModel orderEntry;
    private BigDecimal totalPrice;
    private BigDecimal estimatedAdjustedBasePrice;


    public void setOrderEntry(AbstractOrderEntryModel orderEntry)
    {
        this.orderEntry = orderEntry;
    }


    public AbstractOrderEntryModel getOrderEntry()
    {
        return this.orderEntry;
    }


    public void setTotalPrice(BigDecimal totalPrice)
    {
        this.totalPrice = totalPrice;
    }


    public BigDecimal getTotalPrice()
    {
        return this.totalPrice;
    }


    public void setEstimatedAdjustedBasePrice(BigDecimal estimatedAdjustedBasePrice)
    {
        this.estimatedAdjustedBasePrice = estimatedAdjustedBasePrice;
    }


    public BigDecimal getEstimatedAdjustedBasePrice()
    {
        return this.estimatedAdjustedBasePrice;
    }
}
