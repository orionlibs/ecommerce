package de.hybris.platform.personalizationservices.trigger.strategy.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.personalizationservices.trigger.strategy.CxTriggerStrategy;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class CxDefaultTriggerStrategy implements CxTriggerStrategy
{
    private static final String DEFAULT_VARIATION_QUERY = "SELECT DISTINCT {variation} FROM {CxDefaultTrigger} WHERE {catalogVersion} = ?catalogVersion ";
    private FlexibleSearchService flexibleSearchService;


    public Collection<CxVariationModel> getVariations(UserModel user, CatalogVersionModel catalogVersion)
    {
        return findDefaultVariations(catalogVersion);
    }


    protected Collection<CxVariationModel> findDefaultVariations(CatalogVersionModel catalogVersion)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("catalogVersion", catalogVersion);
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery("SELECT DISTINCT {variation} FROM {CxDefaultTrigger} WHERE {catalogVersion} = ?catalogVersion ");
        searchQuery.addQueryParameters(params);
        SearchResult<CxVariationModel> searchResult = getFlexibleSearchService().search(searchQuery);
        List<CxVariationModel> result = searchResult.getResult();
        return (result == null) ? Collections.<CxVariationModel>emptyList() : result;
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
}
