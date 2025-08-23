package de.hybris.platform.ruleengine.dao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.CatalogVersionToRuleEngineContextMappingModel;
import java.util.Collection;

public interface CatalogVersionToRuleEngineContextMappingDao
{
    Collection<CatalogVersionToRuleEngineContextMappingModel> findMappingsByCatalogVersion(Collection<CatalogVersionModel> paramCollection, RuleType paramRuleType);


    Collection<CatalogVersionToRuleEngineContextMappingModel> findByContext(Collection<AbstractRuleEngineContextModel> paramCollection);
}
