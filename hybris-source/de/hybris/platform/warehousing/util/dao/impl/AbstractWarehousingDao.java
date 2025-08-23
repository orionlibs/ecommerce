package de.hybris.platform.warehousing.util.dao.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.warehousing.util.dao.WarehousingDao;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractWarehousingDao<T> implements WarehousingDao<T>
{
    private FlexibleSearchService flexibleSearchService;
    private String code;


    protected abstract String getQuery();


    public T getByCode(String code)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(getQuery());
        query.addQueryParameter(getCode(), code);
        return (T)getFlexibleSearchService().searchUnique(query);
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    protected String getCode()
    {
        return this.code;
    }


    @Required
    public void setCode(String code)
    {
        this.code = code;
    }
}
