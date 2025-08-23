package de.hybris.platform.servicelayer.i18n.daos;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import java.util.List;

public interface CurrencyDao
{
    List<CurrencyModel> findCurrencies();


    List<CurrencyModel> findCurrenciesByCode(String paramString);


    List<CurrencyModel> findBaseCurrencies();
}
