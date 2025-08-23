package de.hybris.platform.ruleengineservices.cache.impl;

import de.hybris.platform.regioncache.ConcurrentHashSet;
import de.hybris.platform.ruleengine.cache.RuleGlobalsBeanProvider;
import de.hybris.platform.ruleengine.cache.impl.DefaultKIEModuleCacheBuilder;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengineservices.rao.providers.RAOProvider;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class DefaultCommerceKIEModuleCacheBuilder extends DefaultKIEModuleCacheBuilder
{
    private final Map<Object, Collection<Object>> factTemplateCache = new ConcurrentHashMap<>();
    private List<RAOProvider> raoCacheCreators;


    public DefaultCommerceKIEModuleCacheBuilder(RuleGlobalsBeanProvider ruleGlobalsBeanProvider, DroolsKIEModuleModel kieModule, List<RAOProvider> raoCacheCreators, Function<DroolsKIEBaseModel, Object> kieBaseCacheKeyGenerator, boolean failOnBeanMismatch)
    {
        super(ruleGlobalsBeanProvider, kieModule, kieBaseCacheKeyGenerator, failOnBeanMismatch);
        this.raoCacheCreators = raoCacheCreators;
    }


    public <T extends AbstractRuleEngineRuleModel> void processRule(T rule)
    {
        super.processRule((AbstractRuleEngineRuleModel)rule);
        DroolsRuleModel droolsRule = (DroolsRuleModel)rule;
        Collection<Object> cacheSegment = getFactTemplateCacheSegmentForKieBase(droolsRule.getKieBase());
        getRaoCacheCreators().forEach(creator -> cacheSegment.addAll(creator.expandFactModel(rule)));
    }


    protected Collection<Object> getFactTemplateCacheSegmentForKieBase(DroolsKIEBaseModel kieBase)
    {
        return this.factTemplateCache.computeIfAbsent(getKieBaseCacheKeyGenerator().apply(kieBase), k -> new ConcurrentHashSet());
    }


    protected List<RAOProvider> getRaoCacheCreators()
    {
        return this.raoCacheCreators;
    }


    public Map<Object, Collection<Object>> getFactTemplateCache()
    {
        return this.factTemplateCache;
    }
}
