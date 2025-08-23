package de.hybris.platform.ruleengineservices.cache.impl;

import de.hybris.platform.ruleengine.RuleEvaluationContext;
import de.hybris.platform.ruleengine.cache.KIEModuleCacheBuilder;
import de.hybris.platform.ruleengine.cache.impl.DefaultRuleEngineCacheService;
import de.hybris.platform.ruleengine.model.DroolsRuleEngineContextModel;
import de.hybris.platform.ruleengineservices.cache.CommerceRuleEngineCache;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCommerceRuleEngineCacheService extends DefaultRuleEngineCacheService
{
    private CommerceRuleEngineCache commerceRuleEngineCache;


    public void addToCache(KIEModuleCacheBuilder cacheBuilder)
    {
        getCommerceRuleEngineCache().addKIEModuleCache(cacheBuilder);
    }


    public void provideCachedEntities(RuleEvaluationContext context)
    {
        super.provideCachedEntities(context);
        DroolsRuleEngineContextModel engineContext = (DroolsRuleEngineContextModel)context.getRuleEngineContext();
        Set<Object> facts = getOrCreateFacts(context);
        Collection<Object> cachedFacts = getCommerceRuleEngineCache().getCachedFacts(engineContext.getKieSession().getKieBase());
        facts.addAll(cachedFacts);
    }


    protected Set<Object> getOrCreateFacts(RuleEvaluationContext context)
    {
        if(context.getFacts() == null)
        {
            Set<Object> facts = new HashSet();
            context.setFacts(facts);
        }
        return context.getFacts();
    }


    protected CommerceRuleEngineCache getCommerceRuleEngineCache()
    {
        return this.commerceRuleEngineCache;
    }


    @Required
    public void setCommerceRuleEngineCache(CommerceRuleEngineCache factTemplateCache)
    {
        this.commerceRuleEngineCache = factTemplateCache;
    }
}
