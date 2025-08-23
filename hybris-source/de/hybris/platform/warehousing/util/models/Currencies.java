package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.i18n.daos.CurrencyDao;
import de.hybris.platform.warehousing.util.builder.CurrencyModelBuilder;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Required;

public class Currencies extends AbstractItems<CurrencyModel>
{
    public static final String ISOCODE_USD = "USD";
    public static final String SYMBOL_DOLLAR = "$";
    public static final String ISOCODE_EUR = "EUR";
    public static final String SYMBOL_EURO = "€";
    private CurrencyDao currencyDao;


    public CurrencyModel AmericanDollar()
    {
        return (CurrencyModel)getFromCollectionOrSaveAndReturn(() -> getCurrencyDao().findCurrenciesByCode("USD"),
                        () -> CurrencyModelBuilder.aModel().withIsoCode("USD").withName("US Dollar", Locale.ENGLISH).withActive(Boolean.TRUE).withConversion(Double.valueOf(1.0D)).withDigits(Integer.valueOf(2)).withSymbol("$").build());
    }


    public CurrencyModel Euro()
    {
        return (CurrencyModel)getFromCollectionOrSaveAndReturn(() -> getCurrencyDao().findCurrenciesByCode("EUR"),
                        () -> CurrencyModelBuilder.aModel().withIsoCode("EUR").withName("Euro", Locale.ENGLISH).withActive(Boolean.TRUE).withConversion(Double.valueOf(1.0D)).withDigits(Integer.valueOf(2)).withSymbol("€").build());
    }


    public CurrencyDao getCurrencyDao()
    {
        return this.currencyDao;
    }


    @Required
    public void setCurrencyDao(CurrencyDao currencyDao)
    {
        this.currencyDao = currencyDao;
    }
}
