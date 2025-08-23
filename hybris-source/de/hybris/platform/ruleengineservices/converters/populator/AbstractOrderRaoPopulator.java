package de.hybris.platform.ruleengineservices.converters.populator;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountValueRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rao.PaymentModeRAO;
import de.hybris.platform.ruleengineservices.rao.UserRAO;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractOrderRaoPopulator<T extends AbstractOrderModel, P extends AbstractOrderRAO> implements Populator<T, P>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractOrderRaoPopulator.class);
    private Converter<DiscountModel, DiscountValueRAO> discountConverter;
    private Converter<AbstractOrderEntryModel, OrderEntryRAO> entryConverter;
    private Converter<UserModel, UserRAO> userConverter;
    private Converter<PaymentModeModel, PaymentModeRAO> paymentModeConverter;


    public void populate(T source, P target)
    {
        if(target.getActions() == null)
        {
            target.setActions(new LinkedHashSet());
        }
        target.setCode(source.getCode());
        if(source.getCurrency() != null)
        {
            target.setCurrencyIsoCode(source.getCurrency().getIsocode());
        }
        target.setTotal(
                        Objects.isNull(source.getTotalPrice()) ? BigDecimal.ZERO : BigDecimal.valueOf(source.getTotalPrice().doubleValue()));
        target.setSubTotal(Objects.isNull(source.getSubtotal()) ? BigDecimal.ZERO : BigDecimal.valueOf(source.getSubtotal().doubleValue()));
        target.setDeliveryCost(
                        Objects.isNull(source.getDeliveryCost()) ? BigDecimal.ZERO : BigDecimal.valueOf(source.getDeliveryCost().doubleValue()));
        target.setPaymentCost(
                        Objects.isNull(source.getPaymentCost()) ? BigDecimal.ZERO : BigDecimal.valueOf(source.getPaymentCost().doubleValue()));
        if(CollectionUtils.isNotEmpty(source.getEntries()))
        {
            List<OrderEntryRAO> list = Converters.convertAll(source.getEntries(), getEntryConverter());
            list.forEach(entry -> entry.setOrder(target));
            target.setEntries(new LinkedHashSet<>(list));
        }
        else
        {
            LOGGER.debug("Order entry list is empty, skipping the conversion");
        }
        if(CollectionUtils.isNotEmpty(source.getDiscounts()))
        {
            target.setDiscountValues(Converters.convertAll(source.getDiscounts(), getDiscountConverter()));
        }
        else
        {
            LOGGER.debug("Order discount list is empty, skipping the conversion");
        }
        convertAndSetUser(target, source.getUser());
        convertAndSetPaymentMode(target, source.getPaymentMode());
    }


    protected void convertAndSetUser(P target, UserModel user)
    {
        if(Objects.nonNull(user))
        {
            target.setUser((UserRAO)getUserConverter().convert(user));
        }
    }


    protected void convertAndSetPaymentMode(P target, PaymentModeModel paymentMode)
    {
        if(Objects.nonNull(paymentMode))
        {
            target.setPaymentMode((PaymentModeRAO)getPaymentModeConverter().convert(paymentMode));
        }
    }


    protected Converter<DiscountModel, DiscountValueRAO> getDiscountConverter()
    {
        return this.discountConverter;
    }


    @Required
    public void setDiscountConverter(Converter<DiscountModel, DiscountValueRAO> discountConverter)
    {
        this.discountConverter = discountConverter;
    }


    protected Converter<AbstractOrderEntryModel, OrderEntryRAO> getEntryConverter()
    {
        return this.entryConverter;
    }


    @Required
    public void setEntryConverter(Converter<AbstractOrderEntryModel, OrderEntryRAO> entryConverter)
    {
        this.entryConverter = entryConverter;
    }


    protected Converter<UserModel, UserRAO> getUserConverter()
    {
        return this.userConverter;
    }


    @Required
    public void setUserConverter(Converter<UserModel, UserRAO> userConverter)
    {
        this.userConverter = userConverter;
    }


    protected Converter<PaymentModeModel, PaymentModeRAO> getPaymentModeConverter()
    {
        return this.paymentModeConverter;
    }


    @Required
    public void setPaymentModeConverter(Converter<PaymentModeModel, PaymentModeRAO> paymentModeConverter)
    {
        this.paymentModeConverter = paymentModeConverter;
    }
}
