package de.hybris.platform.ruleengine.strategies;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.ruleengine.enums.RuleType;
import java.util.Collection;
import java.util.List;

public interface RuleEngineContextForCatalogVersionsFinderStrategy
{
    <T extends de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel> List<T> findRuleEngineContexts(Collection<CatalogVersionModel> paramCollection, RuleType paramRuleType);
}
