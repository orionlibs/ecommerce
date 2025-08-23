package de.hybris.platform.ruleengineservices.converters;

import de.hybris.order.calculation.domain.AbstractCharge;
import de.hybris.order.calculation.domain.LineItem;
import de.hybris.order.calculation.domain.LineItemDiscount;
import de.hybris.order.calculation.domain.Order;
import de.hybris.order.calculation.domain.OrderCharge;
import de.hybris.order.calculation.domain.OrderDiscount;
import de.hybris.order.calculation.money.AbstractAmount;
import de.hybris.order.calculation.money.Currency;
import de.hybris.order.calculation.money.Money;
import de.hybris.order.calculation.money.Percentage;
import de.hybris.order.calculation.strategies.CalculationStrategies;
import de.hybris.platform.ruleengineservices.calculation.NumberedLineItem;
import de.hybris.platform.ruleengineservices.rao.AbstractActionedRAO;
import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rao.ShipmentRAO;
import de.hybris.platform.ruleengineservices.util.OrderUtils;
import de.hybris.platform.ruleengineservices.util.RaoUtils;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class AbstractOrderRaoToOrderConverter implements Converter<AbstractOrderRAO, Order>
{
    private CalculationStrategies calculationStrategies;
    private Converter<AbstractOrderRAO, Currency> abstractOrderRaoToCurrencyConverter;
    private Converter<OrderEntryRAO, NumberedLineItem> orderEntryRaoToNumberedLineItemConverter;
    private OrderUtils orderUtils;
    private RaoUtils raoUtils;


    public Order convert(AbstractOrderRAO cartRao)
    {
        Currency currency = (Currency)getAbstractOrderRaoToCurrencyConverter().convert(cartRao);
        Order cart = new Order(currency, getCalculationStrategies());
        OrderCharge shippingCharge = convertToShippingOrderCharge(cartRao);
        if(shippingCharge != null)
        {
            cart.addCharge(shippingCharge);
        }
        OrderCharge paymentCharge = convertToPaymentOrderCharge(cartRao);
        if(paymentCharge != null)
        {
            cart.addCharge(paymentCharge);
        }
        if(CollectionUtils.isNotEmpty(cartRao.getActions()))
        {
            List<OrderDiscount> orderDiscounts = new ArrayList<>();
            getRaoUtils().getDiscounts((AbstractActionedRAO)cartRao)
                            .forEach(action -> orderDiscounts.add(convertToOrderDiscount(action, cartRao)));
            cart.addDiscounts(orderDiscounts);
        }
        cart.addLineItems(convertEntriesToLineItems(cartRao));
        return cart;
    }


    protected List<LineItem> convertEntriesToLineItems(AbstractOrderRAO cartRao)
    {
        List<LineItem> lineItems = new ArrayList<>();
        if(CollectionUtils.isEmpty(cartRao.getEntries()))
        {
            return lineItems;
        }
        for(Iterator<OrderEntryRAO> iterator = cartRao.getEntries().iterator(); iterator.hasNext(); )
        {
            OrderEntryRAO entryRao = iterator.next();
            NumberedLineItem lineItem = (NumberedLineItem)getOrderEntryRaoToNumberedLineItemConverter().convert(entryRao);
            lineItems.add(lineItem);
            if(CollectionUtils.isNotEmpty(entryRao.getActions()))
            {
                List<LineItemDiscount> lineItemDiscounts = new ArrayList<>();
                Objects.requireNonNull(DiscountRAO.class);
                entryRao.getActions().stream().filter(DiscountRAO.class::isInstance)
                                .filter(a -> isDiscountNotOrderLevel(cartRao, (DiscountRAO)a))
                                .forEach(action -> lineItemDiscounts.add(convertToLineItemDiscount((DiscountRAO)action, cartRao)));
                if(CollectionUtils.isNotEmpty(lineItemDiscounts))
                {
                    lineItem.addDiscounts(lineItemDiscounts);
                }
            }
        }
        return lineItems;
    }


    protected boolean isDiscountNotOrderLevel(AbstractOrderRAO orderRAO, DiscountRAO discount)
    {
        if(CollectionUtils.isNotEmpty(orderRAO.getActions()))
        {
            Objects.requireNonNull(DiscountRAO.class);
            Objects.requireNonNull(discount);
            return orderRAO.getActions().stream().filter(DiscountRAO.class::isInstance).noneMatch(discount::equals);
        }
        return true;
    }


    protected OrderDiscount convertToOrderDiscount(DiscountRAO discountRao, AbstractOrderRAO cartRao)
    {
        Money money;
        if(StringUtils.isEmpty(discountRao.getCurrencyIsoCode()))
        {
            Percentage percentage = new Percentage(discountRao.getValue());
        }
        else
        {
            money = new Money(discountRao.getValue(), (Currency)getAbstractOrderRaoToCurrencyConverter().convert(cartRao));
        }
        return new OrderDiscount((AbstractAmount)money);
    }


    protected LineItemDiscount convertToLineItemDiscount(DiscountRAO discountRao, AbstractOrderRAO cartRao)
    {
        Money money;
        if(StringUtils.isEmpty(discountRao.getCurrencyIsoCode()))
        {
            Percentage percentage = new Percentage(discountRao.getValue());
        }
        else
        {
            money = new Money(discountRao.getValue(), (Currency)getAbstractOrderRaoToCurrencyConverter().convert(cartRao));
        }
        boolean perUnit = (discountRao.isPerUnit() || getRaoUtils().isAbsolute(discountRao));
        if(discountRao.getAppliedToQuantity() > 0L)
        {
            return new LineItemDiscount((AbstractAmount)money, perUnit, (int)discountRao.getAppliedToQuantity());
        }
        return new LineItemDiscount((AbstractAmount)money, perUnit);
    }


    protected OrderCharge convertToShippingOrderCharge(AbstractOrderRAO cartRao)
    {
        Optional<ShipmentRAO> shipment = getRaoUtils().getShipment((AbstractActionedRAO)cartRao);
        if(shipment.isPresent() && ((ShipmentRAO)shipment.get()).getMode() != null)
        {
            return getOrderUtils().createShippingCharge((Currency)getAbstractOrderRaoToCurrencyConverter().convert(cartRao), true, ((ShipmentRAO)shipment
                            .get()).getMode().getCost());
        }
        if(cartRao.getDeliveryCost() != null)
        {
            return new OrderCharge((AbstractAmount)new Money(cartRao.getDeliveryCost(), (Currency)getAbstractOrderRaoToCurrencyConverter().convert(cartRao)), AbstractCharge.ChargeType.SHIPPING);
        }
        return null;
    }


    protected OrderCharge convertToPaymentOrderCharge(AbstractOrderRAO cartRao)
    {
        if(cartRao.getPaymentCost() != null)
        {
            return new OrderCharge((AbstractAmount)new Money(cartRao.getPaymentCost(), (Currency)getAbstractOrderRaoToCurrencyConverter().convert(cartRao)), AbstractCharge.ChargeType.PAYMENT);
        }
        return null;
    }


    public Order convert(AbstractOrderRAO paramSOURCE, Order paramTARGET)
    {
        throw new UnsupportedOperationException();
    }


    protected Converter<AbstractOrderRAO, Currency> getAbstractOrderRaoToCurrencyConverter()
    {
        return this.abstractOrderRaoToCurrencyConverter;
    }


    @Required
    public void setAbstractOrderRaoToCurrencyConverter(Converter<AbstractOrderRAO, Currency> abstractOrderRaoToCurrencyConverter)
    {
        this.abstractOrderRaoToCurrencyConverter = abstractOrderRaoToCurrencyConverter;
    }


    protected Converter<OrderEntryRAO, NumberedLineItem> getOrderEntryRaoToNumberedLineItemConverter()
    {
        return this.orderEntryRaoToNumberedLineItemConverter;
    }


    @Required
    public void setOrderEntryRaoToNumberedLineItemConverter(Converter<OrderEntryRAO, NumberedLineItem> orderEntryRaoToNumberedLineItemConverter)
    {
        this.orderEntryRaoToNumberedLineItemConverter = orderEntryRaoToNumberedLineItemConverter;
    }


    protected CalculationStrategies getCalculationStrategies()
    {
        return this.calculationStrategies;
    }


    @Required
    public void setCalculationStrategies(CalculationStrategies calculationStrategies)
    {
        this.calculationStrategies = calculationStrategies;
    }


    protected OrderUtils getOrderUtils()
    {
        return this.orderUtils;
    }


    @Required
    public void setOrderUtils(OrderUtils orderUtils)
    {
        this.orderUtils = orderUtils;
    }


    protected RaoUtils getRaoUtils()
    {
        return this.raoUtils;
    }


    @Required
    public void setRaoUtils(RaoUtils raoUtils)
    {
        this.raoUtils = raoUtils;
    }
}
