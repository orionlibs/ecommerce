package de.hybris.platform.servicelayer.i18n.daos.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.i18n.daos.CurrencyDao;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.internal.dao.SortParameters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultCurrencyDao extends DefaultGenericDao<CurrencyModel> implements CurrencyDao
{
    public DefaultCurrencyDao()
    {
        this("Currency");
    }


    private DefaultCurrencyDao(String typecode)
    {
        super(typecode);
    }


    public List<CurrencyModel> findCurrencies()
    {
        return find();
    }


    public List<CurrencyModel> findCurrenciesByCode(String isoCode)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("isocode", isoCode);
        return find(params);
    }


    public List<CurrencyModel> findBaseCurrencies()
    {
        Map<Object, Object> paramsWhere = new HashMap<>();
        paramsWhere.put("base", Boolean.TRUE);
        SortParameters paramsSort = new SortParameters();
        paramsSort.addSortParameter("pk", SortParameters.SortOrder.ASCENDING);
        return find(paramsWhere, paramsSort);
    }
}
