package de.hybris.platform.ruleengineservices.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.hybris.order.calculation.domain.AbstractCharge;
import de.hybris.order.calculation.domain.OrderCharge;
import de.hybris.order.calculation.money.AbstractAmount;
import de.hybris.order.calculation.money.Currency;
import de.hybris.order.calculation.money.Money;
import de.hybris.order.calculation.money.Percentage;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections.MapUtils;

public class OrderUtils
{
    private ModelService modelService;


    public OrderCharge createShippingCharge(Currency currency, boolean absolute, BigDecimal value)
    {
        Percentage percentage;
        if(absolute)
        {
            Money money = new Money(value, currency);
        }
        else
        {
            percentage = new Percentage(value);
        }
        return new OrderCharge((AbstractAmount)percentage, AbstractCharge.ChargeType.SHIPPING);
    }


    public void updateOrderQuantities(OrderModel order, Map<Integer, Long> quantities)
    {
        Preconditions.checkArgument((order != null), "cart cannot be null");
        if(MapUtils.isNotEmpty(quantities))
        {
            Collection<OrderEntryModel> toRemove = Lists.newArrayList();
            Collection<OrderEntryModel> toSave = Lists.newArrayList();
            for(Map.Entry<OrderEntryModel, Long> e : getEntryQuantityMap(order, quantities).entrySet())
            {
                OrderEntryModel cartEntry = e.getKey();
                Long quantity = e.getValue();
                if(quantity == null || quantity.longValue() < 1L)
                {
                    toRemove.add(cartEntry);
                    continue;
                }
                cartEntry.setQuantity(quantity);
                toSave.add(cartEntry);
            }
            getModelService().removeAll(toRemove);
            getModelService().saveAll(toSave);
            getModelService().refresh(order);
        }
    }


    protected Map<OrderEntryModel, Long> getEntryQuantityMap(OrderModel order, Map<Integer, Long> quantities)
    {
        List<OrderEntryModel> entries = order.getEntries();
        return (Map<OrderEntryModel, Long>)quantities.entrySet().stream().collect(Collectors.toMap(e -> getEntry(entries, (Integer)e.getKey()), Map.Entry::getValue));
    }


    protected OrderEntryModel getEntry(List<OrderEntryModel> entries, Integer entryNumber)
    {
        return (OrderEntryModel)entries
                        .stream()
                        .filter(e -> entryNumber.equals(e.getEntryNumber()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("no cart entry found with entry number " + entryNumber + " (got " + entries + ")"));
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
