package de.hybris.platform.promotionengineservices.promotionengine.report.populators;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotionengineservices.promotionengine.report.data.AbstractPromotionEngineResults;
import de.hybris.platform.promotionengineservices.promotionengine.report.data.OrderLevelPromotionEngineResults;
import de.hybris.platform.util.DiscountValue;
import java.util.List;

public class OrderDiscountPromotionEngineResultsPopulator extends AbstractPromotionEngineResultPopulator<AbstractOrderModel, OrderLevelPromotionEngineResults>
{
    public void populate(AbstractOrderModel source, OrderLevelPromotionEngineResults target)
    {
        super.populate(source, (AbstractPromotionEngineResults)target);
        target.setOrder(source);
    }


    protected List<DiscountValue> getDiscountValues(AbstractOrderModel source)
    {
        return source.getGlobalDiscountValues();
    }


    protected AbstractOrderModel getOrder(AbstractOrderModel source)
    {
        return source;
    }
}
