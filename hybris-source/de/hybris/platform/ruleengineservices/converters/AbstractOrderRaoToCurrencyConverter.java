package de.hybris.platform.ruleengineservices.converters;

import de.hybris.order.calculation.money.Currency;
import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.util.CurrencyUtils;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.springframework.beans.factory.annotation.Required;

public class AbstractOrderRaoToCurrencyConverter implements Converter<AbstractOrderRAO, Currency>
{
    private CurrencyUtils currencyUtils;


    public Currency convert(AbstractOrderRAO source)
    {
        String currencyIso = source.getCurrencyIsoCode();
        ServicesUtil.validateParameterNotNull(currencyIso, "currencyIso must not be null");
        return new Currency(currencyIso, getCurrencyUtils().getDigitsOfCurrencyOrDefault(currencyIso).intValue());
    }


    public Currency convert(AbstractOrderRAO paramSOURCE, Currency paramTARGET)
    {
        throw new UnsupportedOperationException();
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
}
