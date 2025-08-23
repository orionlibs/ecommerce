package de.hybris.platform.promotionengineservices.promotionengine.report.data;

import de.hybris.platform.core.model.order.AbstractOrderModel;

public class OrderLevelPromotionEngineResults extends AbstractPromotionEngineResults
{
    private AbstractOrderModel order;


    public void setOrder(AbstractOrderModel order)
    {
        this.order = order;
    }


    public AbstractOrderModel getOrder()
    {
        return this.order;
    }
}
