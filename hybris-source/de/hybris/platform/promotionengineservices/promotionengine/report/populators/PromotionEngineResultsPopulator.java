package de.hybris.platform.promotionengineservices.promotionengine.report.populators;

import com.google.common.base.Preconditions;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotionengineservices.promotionengine.report.data.OrderEntryLevelPromotionEngineResults;
import de.hybris.platform.promotionengineservices.promotionengine.report.data.OrderLevelPromotionEngineResults;
import de.hybris.platform.promotionengineservices.promotionengine.report.data.PromotionEngineResults;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

public class PromotionEngineResultsPopulator implements Populator<AbstractOrderModel, PromotionEngineResults>
{
    private Converter<AbstractOrderModel, OrderLevelPromotionEngineResults> orderDiscountPromotionsConverter;
    private Converter<AbstractOrderEntryModel, OrderEntryLevelPromotionEngineResults> orderEntryDiscountPromotionConverter;


    public void populate(AbstractOrderModel source, PromotionEngineResults target)
    {
        Preconditions.checkArgument(Objects.nonNull(source), "Source cannot be null");
        Preconditions.checkArgument(Objects.nonNull(target), "Target cannot be null");
        OrderLevelPromotionEngineResults orderDiscountPromotions = (OrderLevelPromotionEngineResults)getOrderDiscountPromotionsConverter().convert(source);
        target.setOrderLevelPromotionEngineResults(orderDiscountPromotions);
        List<OrderEntryLevelPromotionEngineResults> orderEntryDiscountPromotions = getOrderEntryDiscountPromotionConverter().convertAll(source.getEntries());
        target.setOrderEntryLevelPromotionEngineResults(orderEntryDiscountPromotions);
    }


    protected Converter<AbstractOrderModel, OrderLevelPromotionEngineResults> getOrderDiscountPromotionsConverter()
    {
        return this.orderDiscountPromotionsConverter;
    }


    @Required
    public void setOrderDiscountPromotionsConverter(Converter<AbstractOrderModel, OrderLevelPromotionEngineResults> orderDiscountPromotionsConverter)
    {
        this.orderDiscountPromotionsConverter = orderDiscountPromotionsConverter;
    }


    protected Converter<AbstractOrderEntryModel, OrderEntryLevelPromotionEngineResults> getOrderEntryDiscountPromotionConverter()
    {
        return this.orderEntryDiscountPromotionConverter;
    }


    @Required
    public void setOrderEntryDiscountPromotionConverter(Converter<AbstractOrderEntryModel, OrderEntryLevelPromotionEngineResults> orderEntryDiscountPromotionConverter)
    {
        this.orderEntryDiscountPromotionConverter = orderEntryDiscountPromotionConverter;
    }
}
