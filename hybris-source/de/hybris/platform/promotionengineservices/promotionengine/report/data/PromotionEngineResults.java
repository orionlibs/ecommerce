package de.hybris.platform.promotionengineservices.promotionengine.report.data;

import java.io.Serializable;
import java.util.List;

public class PromotionEngineResults implements Serializable
{
    private static final long serialVersionUID = 1L;
    private OrderLevelPromotionEngineResults orderLevelPromotionEngineResults;
    private List<OrderEntryLevelPromotionEngineResults> orderEntryLevelPromotionEngineResults;


    public void setOrderLevelPromotionEngineResults(OrderLevelPromotionEngineResults orderLevelPromotionEngineResults)
    {
        this.orderLevelPromotionEngineResults = orderLevelPromotionEngineResults;
    }


    public OrderLevelPromotionEngineResults getOrderLevelPromotionEngineResults()
    {
        return this.orderLevelPromotionEngineResults;
    }


    public void setOrderEntryLevelPromotionEngineResults(List<OrderEntryLevelPromotionEngineResults> orderEntryLevelPromotionEngineResults)
    {
        this.orderEntryLevelPromotionEngineResults = orderEntryLevelPromotionEngineResults;
    }


    public List<OrderEntryLevelPromotionEngineResults> getOrderEntryLevelPromotionEngineResults()
    {
        return this.orderEntryLevelPromotionEngineResults;
    }
}
