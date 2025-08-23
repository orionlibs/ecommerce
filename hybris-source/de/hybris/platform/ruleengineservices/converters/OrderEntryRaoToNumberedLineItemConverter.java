package de.hybris.platform.ruleengineservices.converters;

import de.hybris.order.calculation.money.Currency;
import de.hybris.order.calculation.money.Money;
import de.hybris.platform.ruleengineservices.calculation.NumberedLineItem;
import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.springframework.beans.factory.annotation.Required;

public class OrderEntryRaoToNumberedLineItemConverter implements Converter<OrderEntryRAO, NumberedLineItem>
{
    private Converter<AbstractOrderRAO, Currency> abstractOrderRaoToCurrencyConverter;


    public NumberedLineItem convert(OrderEntryRAO entryRao)
    {
        ServicesUtil.validateParameterNotNull(entryRao, "order entry rao must not be null");
        ServicesUtil.validateParameterNotNull(entryRao.getOrder(), "corresponding entry cart rao must not be null");
        AbstractOrderRAO rao = entryRao.getOrder();
        Money money = new Money(entryRao.getPrice(), (Currency)getAbstractOrderRaoToCurrencyConverter().convert(rao));
        NumberedLineItem lineItem = new NumberedLineItem(money, entryRao.getQuantity());
        lineItem.setEntryNumber(entryRao.getEntryNumber());
        return lineItem;
    }


    public NumberedLineItem convert(OrderEntryRAO paramSOURCE, NumberedLineItem paramTARGET)
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
}
