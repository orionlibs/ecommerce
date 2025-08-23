package de.hybris.platform.promotionengineservices.promotionengine.report.populators;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotionengineservices.promotionengine.report.data.AbstractPromotionEngineResults;
import de.hybris.platform.promotionengineservices.promotionengine.report.data.OrderEntryLevelPromotionEngineResults;
import de.hybris.platform.util.DiscountValue;
import java.math.BigDecimal;
import java.util.Collection;

public class OrderEntryDiscountPromotionEngineResultsPopulator extends AbstractPromotionEngineResultPopulator<AbstractOrderEntryModel, OrderEntryLevelPromotionEngineResults>
{
    public void populate(AbstractOrderEntryModel source, OrderEntryLevelPromotionEngineResults target)
    {
        super.populate(source, (AbstractPromotionEngineResults)target);
        target.setOrderEntry(source);
        target.setEstimatedAdjustedBasePrice(estimateAdjustedBasePriceTotalPrice(source));
        target.setTotalPrice(calculateTotalPrice(source));
    }


    protected BigDecimal estimateAdjustedBasePriceTotalPrice(AbstractOrderEntryModel source)
    {
        if(source.getQuantity().longValue() == 0L)
        {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(source.getTotalPrice().doubleValue()).setScale(3, 4)
                        .divide(BigDecimal.valueOf(source.getQuantity().longValue()), 4);
    }


    protected BigDecimal calculateTotalPrice(AbstractOrderEntryModel source)
    {
        return BigDecimal.valueOf(source.getBasePrice().doubleValue()).setScale(2, 4)
                        .multiply(BigDecimal.valueOf(source.getQuantity().longValue()));
    }


    protected Collection<DiscountValue> getDiscountValues(AbstractOrderEntryModel source)
    {
        return source.getDiscountValues();
    }


    protected AbstractOrderModel getOrder(AbstractOrderEntryModel source)
    {
        return source.getOrder();
    }
}
