package de.hybris.platform.ruleengine.strategies.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.ruleengine.dao.CatalogVersionToRuleEngineContextMappingDao;
import de.hybris.platform.ruleengine.dao.RuleEngineContextDao;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.CatalogVersionToRuleEngineContextMappingModel;
import de.hybris.platform.ruleengine.strategies.CatalogVersionFinderStrategy;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCatalogVersionFinderStrategy implements CatalogVersionFinderStrategy
{
    private CatalogVersionToRuleEngineContextMappingDao catalogVersionToRuleEngineContextMappingDao;
    private RuleEngineContextDao ruleEngineContextDao;


    public List<CatalogVersionModel> findCatalogVersionsByRulesModule(AbstractRulesModuleModel rulesModule)
    {
        List<AbstractRuleEngineContextModel> ruleEngineContextList = getRuleEngineContextDao().findRuleEngineContextByRulesModule(rulesModule);
        if(CollectionUtils.isNotEmpty(ruleEngineContextList))
        {
            Collection<CatalogVersionToRuleEngineContextMappingModel> catalogVersionsToRuleEngineMappings = getCatalogVersionToRuleEngineContextMappingDao().findByContext(ruleEngineContextList);
            return (List<CatalogVersionModel>)catalogVersionsToRuleEngineMappings.stream()
                            .map(CatalogVersionToRuleEngineContextMappingModel::getCatalogVersion).filter(Objects::nonNull).distinct()
                            .collect(Collectors.toList());
        }
        return Collections.emptyList();
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


    protected RuleEngineContextDao getRuleEngineContextDao()
    {
        return this.ruleEngineContextDao;
    }


    @Required
    public void setRuleEngineContextDao(RuleEngineContextDao ruleEngineContextDao)
    {
        this.ruleEngineContextDao = ruleEngineContextDao;
    }
}
