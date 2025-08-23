package de.hybris.platform.ruleengineservices.calculation.impl;

import de.hybris.order.calculation.domain.Order;
import de.hybris.platform.ruleengineservices.calculation.NumberedLineItem;
import de.hybris.platform.ruleengineservices.calculation.NumberedLineItemLookupStrategy;
import de.hybris.platform.ruleengineservices.calculation.PriceAdjustmentStrategy;
import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPriceAdjustmentStrategy implements PriceAdjustmentStrategy<OrderEntryRAO>
{
    private Converter<AbstractOrderRAO, Order> abstractOrderRaoToOrderConverter;
    private NumberedLineItemLookupStrategy lineItemLookupStrategy;


    public BigDecimal get(OrderEntryRAO orderEntryRao, int quantity)
    {
        Order cart = (Order)getAbstractOrderRaoToOrderConverter().convert(orderEntryRao.getOrder());
        NumberedLineItem lineItem = getLineItemLookupStrategy().lookup(cart, orderEntryRao);
        return lineItem.getTotalDiscount().getAmount().divide(BigDecimal.valueOf(quantity), RoundingMode.HALF_UP);
    }


    protected Converter<AbstractOrderRAO, Order> getAbstractOrderRaoToOrderConverter()
    {
        return this.abstractOrderRaoToOrderConverter;
    }


    @Required
    public void setAbstractOrderRaoToOrderConverter(Converter<AbstractOrderRAO, Order> abstractOrderRaoToOrderConverter)
    {
        this.abstractOrderRaoToOrderConverter = abstractOrderRaoToOrderConverter;
    }


    protected NumberedLineItemLookupStrategy getLineItemLookupStrategy()
    {
        return this.lineItemLookupStrategy;
    }


    @Required
    public void setLineItemLookupStrategy(NumberedLineItemLookupStrategy lineItemLookupStrategy)
    {
        this.lineItemLookupStrategy = lineItemLookupStrategy;
    }
}
