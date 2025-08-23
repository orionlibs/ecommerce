package com.hybris.backoffice.daos.impl;

import com.hybris.backoffice.daos.BackofficeThemeConfigDao;
import de.hybris.platform.processengine.model.BackofficeThemeConfigModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collections;
import java.util.List;

public class DefaultBackofficeThemeConfigDao implements BackofficeThemeConfigDao
{
    private FlexibleSearchService flexibleSearchService;


    public List<BackofficeThemeConfigModel> findByCodeAndActive(String code, boolean active)
    {
        String queryString = "SELECT {PK} FROM {BackofficeThemeConfig as btc} WHERE {btc.code} = ?code AND {active}=?active ORDER BY {version}";
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {PK} FROM {BackofficeThemeConfig as btc} WHERE {btc.code} = ?code AND {active}=?active ORDER BY {version}");
        query.addQueryParameter("code", code);
        query.addQueryParameter("active", Boolean.valueOf(active));
        SearchResult<BackofficeThemeConfigModel> search = this.flexibleSearchService.search(query);
        List<BackofficeThemeConfigModel> result = search.getResult();
        return (result == null) ? Collections.<BackofficeThemeConfigModel>emptyList() : result;
    }


    public List<BackofficeThemeConfigModel> findByCode(String code)
    {
        String queryString = "SELECT {PK} FROM {BackofficeThemeConfig as btc} WHERE {btc.code} = ?code";
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {PK} FROM {BackofficeThemeConfig as btc} WHERE {btc.code} = ?code");
        query.addQueryParameter("code", code);
        SearchResult<BackofficeThemeConfigModel> search = this.flexibleSearchService.search(query);
        List<BackofficeThemeConfigModel> result = search.getResult();
        return (result == null) ? Collections.<BackofficeThemeConfigModel>emptyList() : result;
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
