package de.hybris.platform.ruleengine.cache.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.ruleengine.cache.KIEModuleCacheBuilder;
import de.hybris.platform.ruleengine.cache.RuleEngineCache;
import de.hybris.platform.ruleengine.cache.RuleGlobalsBeanProvider;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleEngineCache implements RuleEngineCache
{
    private final ConcurrentHashMap<Object, Map<Object, Map<String, Object>>> globalsCache;
    private ConfigurationService configurationService;
    private RuleGlobalsBeanProvider ruleGlobalsBeanProvider;
    protected Function<DroolsKIEModuleModel, Object> kieModuleCacheKeyGenerator;
    protected Function<DroolsKIEBaseModel, Object> kieBaseCacheKeyGenerator;


    public DefaultRuleEngineCache()
    {
        this.globalsCache = new ConcurrentHashMap<>();
        this.kieModuleCacheKeyGenerator = (km -> {
            ServicesUtil.validateParameterNotNull(km, "kie module must not be null");
            ServicesUtil.validateParameterNotNull(km.getPk(), "kie module must have a pk");
            return km.getPk().getLongValueAsString();
        });
        this.kieBaseCacheKeyGenerator = (kb -> {
            ServicesUtil.validateParameterNotNull(kb, "kie base must not be null");
            ServicesUtil.validateParameterNotNull(kb.getPk(), "kie base must have a pk");
            return kb.getPk().getLongValueAsString();
        });
    }


    public KIEModuleCacheBuilder createKIEModuleCacheBuilder(DroolsKIEModuleModel kieModule)
    {
        return (KIEModuleCacheBuilder)new DefaultKIEModuleCacheBuilder(getRuleGlobalsBeanProvider(), kieModule, getKieBaseCacheKeyGenerator(),
                        getConfigurationService()
                                        .getConfiguration().getBoolean("defaultRuleEngineCacheService.globals.fail.on.bean.mismatch", false));
    }


    public void addKIEModuleCache(KIEModuleCacheBuilder cacheBuilder)
    {
        Preconditions.checkArgument(cacheBuilder instanceof DefaultKIEModuleCacheBuilder, "cache must be of type DefaultRuleEngineKIEModuleCacheBuilder");
        DefaultKIEModuleCacheBuilder moduleCache = (DefaultKIEModuleCacheBuilder)cacheBuilder;
        Map<Object, Map<String, Object>> globals = moduleCache.getGlobalsCache();
        this.globalsCache.put(getKieModuleCacheKeyGenerator().apply(moduleCache.getKieModule()), ImmutableMap.copyOf(globals));
    }


    public Map<String, Object> getGlobalsForKIEBase(DroolsKIEBaseModel kieBase)
    {
        Object key = getKieBaseCacheKeyGenerator().apply(kieBase);
        return (Map<String, Object>)((Map)getGlobalsCacheForKIEModule(kieBase.getKieModule()).orElseGet(Collections::emptyMap)).getOrDefault(key,
                        Collections.emptyMap());
    }


    protected Optional<Map<Object, Map<String, Object>>> getGlobalsCacheForKIEModule(DroolsKIEModuleModel kieModule)
    {
        Object key = getKieModuleCacheKeyGenerator().apply(kieModule);
        return Optional.ofNullable(this.globalsCache.get(key));
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    protected Function<DroolsKIEModuleModel, Object> getKieModuleCacheKeyGenerator()
    {
        return this.kieModuleCacheKeyGenerator;
    }


    public void setKieModuleCacheKeyGenerator(Function<DroolsKIEModuleModel, Object> kieModuleCacheKeyGenerator)
    {
        this.kieModuleCacheKeyGenerator = kieModuleCacheKeyGenerator;
    }


    protected Function<DroolsKIEBaseModel, Object> getKieBaseCacheKeyGenerator()
    {
        return this.kieBaseCacheKeyGenerator;
    }


    public void setKieBaseCacheKeyGenerator(Function<DroolsKIEBaseModel, Object> kieBaseCacheKeyGenerator)
    {
        this.kieBaseCacheKeyGenerator = kieBaseCacheKeyGenerator;
    }


    protected RuleGlobalsBeanProvider getRuleGlobalsBeanProvider()
    {
        return this.ruleGlobalsBeanProvider;
    }


    @Required
    public void setRuleGlobalsBeanProvider(RuleGlobalsBeanProvider ruleGlobalsBeanProvider)
    {
        this.ruleGlobalsBeanProvider = ruleGlobalsBeanProvider;
    }
}
