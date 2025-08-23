package de.hybris.platform.warehousing.sourcing.ban.dao.impl;

import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.warehousing.model.SourcingBanModel;
import de.hybris.platform.warehousing.sourcing.ban.dao.SourcingBanDao;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSourcingBanDao implements SourcingBanDao
{
    private FlexibleSearchService flexibleSearchService;


    public Collection<SourcingBanModel> getSourcingBan(Collection<WarehouseModel> warehouseModels, Date currentDateMinusBannedDays)
    {
        String query = "SELECT {pk} FROM {SourcingBan} WHERE {warehouse} IN (?warehousemodels) and {creationtime} >= ?currenttime-banneddays";
        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery("SELECT {pk} FROM {SourcingBan} WHERE {warehouse} IN (?warehousemodels) and {creationtime} >= ?currenttime-banneddays");
        fsQuery.addQueryParameter("warehousemodels", warehouseModels);
        fsQuery.addQueryParameter("currenttime-banneddays", currentDateMinusBannedDays);
        Collection<SourcingBanModel> results = getSourcingBans(fsQuery);
        return results.isEmpty() ? Collections.<SourcingBanModel>emptyList() : results;
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


    protected <T extends SourcingBanModel> Collection<T> getSourcingBans(FlexibleSearchQuery query)
    {
        SearchResult<T> result = getFlexibleSearchService().search(query);
        return result.getResult();
    }
}
