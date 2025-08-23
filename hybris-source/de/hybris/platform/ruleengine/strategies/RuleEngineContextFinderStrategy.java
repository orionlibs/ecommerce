package de.hybris.platform.ruleengine.strategies;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ruleengine.enums.RuleType;
import java.util.Collection;
import java.util.Optional;

public interface RuleEngineContextFinderStrategy
{
    <T extends de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel> Optional<T> findRuleEngineContext(RuleType paramRuleType);


    <T extends de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel, O extends de.hybris.platform.core.model.order.AbstractOrderModel> Optional<T> findRuleEngineContext(O paramO, RuleType paramRuleType);


    <T extends de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel> Optional<T> findRuleEngineContext(ProductModel paramProductModel, RuleType paramRuleType);


    <T extends de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel> Optional<T> getRuleEngineContextForCatalogVersions(Collection<CatalogVersionModel> paramCollection, RuleType paramRuleType);
}
