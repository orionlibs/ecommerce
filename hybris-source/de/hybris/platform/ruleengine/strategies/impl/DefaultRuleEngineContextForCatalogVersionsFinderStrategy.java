package de.hybris.platform.ruleengine.strategies.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.ruleengine.dao.CatalogVersionToRuleEngineContextMappingDao;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.CatalogVersionToRuleEngineContextMappingModel;
import de.hybris.platform.ruleengine.strategies.RuleEngineContextForCatalogVersionsFinderStrategy;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleEngineContextForCatalogVersionsFinderStrategy implements RuleEngineContextForCatalogVersionsFinderStrategy
{
    private CatalogVersionToRuleEngineContextMappingDao catalogVersionToRuleEngineContextMappingDao;


    public <T extends AbstractRuleEngineContextModel> List<T> findRuleEngineContexts(Collection<CatalogVersionModel> catalogVersions, RuleType ruleType)
    {
        Collection<CatalogVersionToRuleEngineContextMappingModel> mappings = getCatalogVersionToRuleEngineContextMappingDao().findMappingsByCatalogVersion(catalogVersions, ruleType);
        if(CollectionUtils.isEmpty(mappings))
        {
            return Collections.emptyList();
        }
        return (List<T>)mappings.stream().map(m -> m.getContext()).collect(Collectors.toList());
    }


    protected CatalogVersionToRuleEngineContextMappingDao getCatalogVersionToRuleEngineContextMappingDao()
    {
        return this.catalogVersionToRuleEngineContextMappingDao;
    }


    @Required
    public void setCatalogVersionToRuleEngineContextMappingDao(CatalogVersionToRuleEngineContextMappingDao catalogVersionToRuleEngineContextMappingDao)
    {
        this.catalogVersionToRuleEngineContextMappingDao = catalogVersionToRuleEngineContextMappingDao;
    }
}
