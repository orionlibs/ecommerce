package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.services.AsCategoryService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractAsCategoryPathResolver
{
    public static final String FILTER_INDEX_PROPERTY_KEY = "adaptivesearch.categoryPathResolver.filter.indexProperty";
    public static final String FACET_FILTER_INDEX_PROPERTY_KEY = "adaptivesearch.categoryPathResolver.facetFilter.indexProperty";
    private ConfigurationService configurationService;
    private AsCategoryService asCategoryService;


    protected String resolveFilterIndexProperty()
    {
        return this.configurationService.getConfiguration().getString("adaptivesearch.categoryPathResolver.filter.indexProperty", "allCategories");
    }


    protected String resolveFacetFilterIndexProperty()
    {
        return this.configurationService.getConfiguration().getString("adaptivesearch.categoryPathResolver.facetFilter.indexProperty", "allCategories");
    }


    protected void addCategoryCodes(List<String> targetCategoryCodes, Collection<String> categoryCodes)
    {
        for(String categoryCode : categoryCodes)
        {
            addCategoryCode(targetCategoryCodes, categoryCode);
        }
    }


    protected void addCategoryCode(List<String> targetCategoryCodes, String categoryCode)
    {
        if(StringUtils.isNotBlank(categoryCode))
        {
            targetCategoryCodes.add(categoryCode);
        }
    }


    public ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    public AsCategoryService getAsCategoryService()
    {
        return this.asCategoryService;
    }


    @Required
    public void setAsCategoryService(AsCategoryService asCategoryService)
    {
        this.asCategoryService = asCategoryService;
    }
}
