package de.hybris.platform.ruleengine.cache.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import de.hybris.platform.ruleengine.RuleEvaluationContext;
import de.hybris.platform.ruleengine.cache.KIEModuleCacheBuilder;
import de.hybris.platform.ruleengine.cache.RuleEngineCache;
import de.hybris.platform.ruleengine.cache.RuleEngineCacheService;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleEngineContextModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleEngineCacheService implements RuleEngineCacheService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRuleEngineCacheService.class);
    private RuleEngineCache ruleEngineCache;


    public KIEModuleCacheBuilder createKIEModuleCacheBuilder(DroolsKIEModuleModel kieModule)
    {
        return getRuleEngineCache().createKIEModuleCacheBuilder(kieModule);
    }


    public void addToCache(KIEModuleCacheBuilder cacheBuilder)
    {
        getRuleEngineCache().addKIEModuleCache(cacheBuilder);
    }


    public void provideCachedEntities(RuleEvaluationContext context)
    {
        ServicesUtil.validateParameterNotNull(context, "context must not be null");
        Preconditions.checkArgument(context.getRuleEngineContext() instanceof DroolsRuleEngineContextModel, "rule engine context must be of type DroolsRuleEngineContext");
        DroolsRuleEngineContextModel engineContext = (DroolsRuleEngineContextModel)context.getRuleEngineContext();
        ServicesUtil.validateParameterNotNull(engineContext.getKieSession().getKieBase(), "rule engine context must have a kie session and kie base set");
        Map<String, Object> globalsForKIEBase = getRuleEngineCache().getGlobalsForKIEBase(engineContext.getKieSession().getKieBase());
        Map<String, Object> globals = Maps.newHashMap();
        if(MapUtils.isNotEmpty(globalsForKIEBase))
        {
            globals = Maps.newHashMap(globalsForKIEBase);
        }
        else
        {
            LOGGER.warn("Globals map for evaluation context [{}] is empty. Either there are no globals defined in the actions or the cache is broken.", context
                            .getRuleEngineContext().getName());
        }
        context.setGlobals(globals);
    }


    protected RuleEngineCache getRuleEngineCache()
    {
        return this.ruleEngineCache;
    }


    @Required
    public void setRuleEngineCache(RuleEngineCache globalsCache)
    {
        this.ruleEngineCache = globalsCache;
    }
}
