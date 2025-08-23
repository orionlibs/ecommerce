package de.hybris.platform.ruleengineservices.converters.populator;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ruleengineservices.rao.DiscountValueRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.util.CurrencyUtils;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.util.DiscountValue;
import java.math.BigDecimal;
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class OrderEntryRaoPopulator implements Populator<AbstractOrderEntryModel, OrderEntryRAO>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEntryRaoPopulator.class);
    private Converter<DiscountValue, DiscountValueRAO> discountValueConverter;
    private CurrencyUtils currencyUtils;
    private Populator<ProductModel, OrderEntryRAO> orderEntryRaoProductPopulator;


    public void populate(AbstractOrderEntryModel source, OrderEntryRAO target)
    {
        getOrderEntryRaoProductPopulator().populate(source.getProduct(), target);
        if(Objects.nonNull(source.getQuantity()))
        {
            target.setQuantity(source.getQuantity().intValue());
        }
        Double basePrice = source.getBasePrice();
        if(Objects.nonNull(basePrice))
        {
            target.setBasePrice(BigDecimal.valueOf(basePrice.doubleValue()));
            target.setPrice(target.getBasePrice());
            AbstractOrderModel order = source.getOrder();
            if(Objects.nonNull(order) && Objects.nonNull(order.getCurrency()))
            {
                target.setCurrencyIsoCode(order.getCurrency().getIsocode());
            }
            else
            {
                LOGGER.warn("Order is null or the order currency is not set correctly");
            }
        }
        if(Objects.nonNull(source.getEntryNumber()))
        {
            target.setEntryNumber(source.getEntryNumber());
        }
        if(CollectionUtils.isNotEmpty(source.getDiscountValues()))
        {
            source.getDiscountValues().forEach(discountValue -> applyDiscount(target, discountValue));
            target.setDiscountValues(Converters.convertAll(source.getDiscountValues(), getDiscountValueConverter()));
        }
        target.setGiveAway(BooleanUtils.toBoolean(source.getGiveAway()));
        target.setBasePrice(target.getPrice());
    }


    protected void applyDiscount(OrderEntryRAO target, DiscountValue discountValue)
    {
        BigDecimal discountAmount = BigDecimal.valueOf(discountValue
                        .apply(1.0D, target.getPrice().doubleValue(),
                                        getCurrencyUtils().getDigitsOfCurrencyOrDefault(target.getCurrencyIsoCode()).intValue(), target
                                                        .getCurrencyIsoCode()).getAppliedValue());
        target.setPrice(target.getPrice().subtract(discountAmount));
    }


    protected Converter<DiscountValue, DiscountValueRAO> getDiscountValueConverter()
    {
        return this.discountValueConverter;
    }


    @Required
    public void setDiscountValueConverter(Converter<DiscountValue, DiscountValueRAO> discountValueConverter)
    {
        this.discountValueConverter = discountValueConverter;
    }


    protected CurrencyUtils getCurrencyUtils()
    {
        return this.currencyUtils;
    }


    @Required
    public void setCurrencyUtils(CurrencyUtils currencyUtils)
    {
        this.currencyUtils = currencyUtils;
    }


    protected Populator<ProductModel, OrderEntryRAO> getOrderEntryRaoProductPopulator()
    {
        return this.orderEntryRaoProductPopulator;
    }


    @Required
    public void setOrderEntryRaoProductPopulator(Populator<ProductModel, OrderEntryRAO> orderEntryRaoProductPopulator)
    {
        this.orderEntryRaoProductPopulator = orderEntryRaoProductPopulator;
    }
}
