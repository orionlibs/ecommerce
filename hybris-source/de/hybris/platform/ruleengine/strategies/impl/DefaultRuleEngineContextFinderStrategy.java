package de.hybris.platform.ruleengine.strategies.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ruleengine.dao.RuleEngineContextDao;
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.strategies.RuleEngineContextFinderStrategy;
import de.hybris.platform.ruleengine.strategies.RuleEngineContextForCatalogVersionsFinderStrategy;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleEngineContextFinderStrategy implements RuleEngineContextFinderStrategy
{
    private static final String ILLEGAL_STATUS_MESSAGE_CATALOG_RULE_TYPE = "Cannot determine unique rule engine context for rule evaluation: more than one rule engine context %s found for catalog versions %s and rule type [%s]";
    private static final String ILLEGAL_STATUS_MESSAGE_RULE_MODULE = "Cannot determine unique rule engine context for rule evaluation: the derived RuleEngineContext is not unique %s for rule module [%s]";
    private static final String ILLEGAL_STATUS_MESSAGE_BY_RULE_TYPE = "Cannot determine unique rule engine context for rule evaluation: More than one Rules Modules (%s) found for type %s";
    private RulesModuleDao rulesModuleDao;
    private RuleEngineContextDao ruleEngineContextDao;
    private CatalogVersionService catalogVersionService;
    private RuleEngineContextForCatalogVersionsFinderStrategy ruleEngineContextForCatalogVersionsFinderStrategy;


    public <T extends AbstractRuleEngineContextModel> Optional<T> findRuleEngineContext(RuleType ruleType)
    {
        List<AbstractRulesModuleModel> rulesModules = getRulesModuleDao().findActiveRulesModulesByRuleType(ruleType);
        if(CollectionUtils.isEmpty(rulesModules))
        {
            return Optional.empty();
        }
        if(rulesModules.size() != 1)
        {
            throw new IllegalStateException(String.format("Cannot determine unique rule engine context for rule evaluation: More than one Rules Modules (%s) found for type %s", new Object[] {rulesModules
                            .stream().map(AbstractRulesModuleModel::getName).collect(Collectors.joining(", ")), ruleType
                            .getCode()}));
        }
        List<T> ruleEngineContextByRulesModule = getRuleEngineContextDao().findRuleEngineContextByRulesModule(rulesModules.get(0));
        List<String> ruleEngineContextNames = (List<String>)ruleEngineContextByRulesModule.stream().map(AbstractRuleEngineContextModel::getName).distinct().collect(Collectors.toList());
        if(ruleEngineContextNames.size() > 1)
        {
            throw new IllegalStateException(
                            String.format("Cannot determine unique rule engine context for rule evaluation: the derived RuleEngineContext is not unique %s for rule module [%s]", new Object[] {ruleEngineContextNames, rulesModules.get(0)}));
        }
        return ruleEngineContextByRulesModule.stream().findFirst();
    }


    public <T extends AbstractRuleEngineContextModel, O extends de.hybris.platform.core.model.order.AbstractOrderModel> Optional<T> findRuleEngineContext(O order, RuleType ruleType)
    {
        Collection<CatalogVersionModel> catalogVersions = getCatalogVersionsForProducts(getProductsForOrder(order));
        if(CollectionUtils.isEmpty(catalogVersions))
        {
            catalogVersions = getAvailableCatalogVersions();
        }
        return getRuleEngineContextForCatalogVersions(catalogVersions, ruleType);
    }


    public <T extends AbstractRuleEngineContextModel> Optional<T> findRuleEngineContext(ProductModel product, RuleType ruleType)
    {
        Collection<CatalogVersionModel> catalogVersions = getCatalogVersionsForProducts(Collections.singletonList(product));
        if(CollectionUtils.isEmpty(catalogVersions))
        {
            catalogVersions = getAvailableCatalogVersions();
        }
        return getRuleEngineContextForCatalogVersions(catalogVersions, ruleType);
    }


    public <T extends AbstractRuleEngineContextModel> Optional<T> getRuleEngineContextForCatalogVersions(Collection<CatalogVersionModel> catalogVersions, RuleType ruleType)
    {
        List<T> ruleEngineContexts = getRuleEngineContextForCatalogVersionsFinderStrategy().findRuleEngineContexts(catalogVersions, ruleType);
        if(CollectionUtils.isNotEmpty(ruleEngineContexts))
        {
            List<String> ruleEngineContextNames = (List<String>)ruleEngineContexts.stream().map(AbstractRuleEngineContextModel::getName).distinct().collect(Collectors.toList());
            if(ruleEngineContextNames.size() > 1)
            {
                throw new IllegalStateException(
                                String.format("Cannot determine unique rule engine context for rule evaluation: more than one rule engine context %s found for catalog versions %s and rule type [%s]", new Object[] {ruleEngineContextNames, catalogVersions.stream().map(this::catalogVersionToString)
                                                .collect(Collectors.toList()), ruleType}));
            }
            return ruleEngineContexts.stream().findFirst();
        }
        return findRuleEngineContext(ruleType);
    }


    protected String catalogVersionToString(CatalogVersionModel catalogVersion)
    {
        CatalogModel catalog = catalogVersion.getCatalog();
        return catalog.getName() + ":" + catalog.getName();
    }


    protected Collection<CatalogVersionModel> getCatalogVersionsForProducts(Collection<ProductModel> products)
    {
        if(CollectionUtils.isNotEmpty(products))
        {
            return (Collection<CatalogVersionModel>)products.stream().map(ProductModel::getCatalogVersion).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    protected Collection<CatalogVersionModel> getAvailableCatalogVersions()
    {
        Collection<CatalogVersionModel> sessionCatalogVersions = getCatalogVersionService().getSessionCatalogVersions();
        if(CollectionUtils.isNotEmpty(sessionCatalogVersions))
        {
            return (Collection<CatalogVersionModel>)sessionCatalogVersions.stream().distinct().collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    protected <T extends de.hybris.platform.core.model.order.AbstractOrderModel> Collection<ProductModel> getProductsForOrder(T order)
    {
        List<AbstractOrderEntryModel> orderEntries = order.getEntries();
        if(CollectionUtils.isNotEmpty(orderEntries))
        {
            return (Collection<ProductModel>)orderEntries.stream().map(AbstractOrderEntryModel::getProduct).filter(Objects::nonNull).collect(Collectors.toSet());
        }
        return Collections.emptyList();
    }


    protected CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    protected RuleEngineContextForCatalogVersionsFinderStrategy getRuleEngineContextForCatalogVersionsFinderStrategy()
    {
        return this.ruleEngineContextForCatalogVersionsFinderStrategy;
    }


    @Required
    public void setRuleEngineContextForCatalogVersionsFinderStrategy(RuleEngineContextForCatalogVersionsFinderStrategy ruleEngineContextForCatalogVersionsFinderStrategy)
    {
        this.ruleEngineContextForCatalogVersionsFinderStrategy = ruleEngineContextForCatalogVersionsFinderStrategy;
    }


    protected RulesModuleDao getRulesModuleDao()
    {
        return this.rulesModuleDao;
    }


    @Required
    public void setRulesModuleDao(RulesModuleDao rulesModuleDao)
    {
        this.rulesModuleDao = rulesModuleDao;
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
