package de.hybris.platform.ruleengineservices.cache.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import de.hybris.platform.ruleengine.cache.KIEModuleCacheBuilder;
import de.hybris.platform.ruleengine.cache.impl.DefaultRuleEngineCache;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengineservices.cache.CommerceRuleEngineCache;
import de.hybris.platform.ruleengineservices.rao.providers.RAOProvider;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.collections4.MapUtils;

public class DefaultCommerceRuleEngineCache extends DefaultRuleEngineCache implements CommerceRuleEngineCache
{
    private final ConcurrentHashMap<Object, Map<Object, Collection<Object>>> factTemplateCache = new ConcurrentHashMap<>();
    protected static final RAOProvider<Object> identityRAOProvider = Collections::singleton;
    private Map<Class, RAOProvider> raoProviders;
    private List<RAOProvider> raoCacheCreators;


    public KIEModuleCacheBuilder createKIEModuleCacheBuilder(DroolsKIEModuleModel kieModule)
    {
        return (KIEModuleCacheBuilder)new DefaultCommerceKIEModuleCacheBuilder(getRuleGlobalsBeanProvider(), kieModule,
                        (List)ImmutableList.copyOf(getRaoCacheCreators()), getKieBaseCacheKeyGenerator(), false);
    }


    public void addKIEModuleCache(KIEModuleCacheBuilder cacheBuilder)
    {
        Preconditions.checkArgument(cacheBuilder instanceof DefaultCommerceKIEModuleCacheBuilder, "cache must be of type DefaultCommerceRuleEngineKIEModuleCache");
        super.addKIEModuleCache(cacheBuilder);
        DefaultCommerceKIEModuleCacheBuilder cacheBuilderImpl = (DefaultCommerceKIEModuleCacheBuilder)cacheBuilder;
        Map<Object, Collection<Object>> factTemplates = cacheBuilderImpl.getFactTemplateCache();
        checkFactTemplates(factTemplates);
        this.factTemplateCache.put(getKieModuleCacheKeyGenerator().apply(cacheBuilderImpl.getKieModule()), factTemplates);
    }


    public Collection<Object> getCachedFacts(DroolsKIEBaseModel kieBase)
    {
        Collection<Object> factTemplates = getFactTemplateCacheForKieBase(kieBase);
        Collection<Object> facts = new HashSet();
        factTemplates.forEach(ft -> facts.addAll(((RAOProvider)getRaoProvider(ft).orElse(identityRAOProvider)).expandFactModel(ft)));
        return facts;
    }


    protected void checkFactTemplates(Map<Object, Collection<Object>> factTemplates)
    {
        boolean doSanityCheck = getConfigurationService().getConfiguration().getBoolean("defaultCommerceRuleEngineCache.checkRAOProvidersForCache", true);
        if(doSanityCheck && MapUtils.isNotEmpty(factTemplates))
        {
            factTemplates.entrySet().stream().map(Map.Entry::getValue).flatMap(Collection::stream)
                            .forEach(ft -> getRaoProvider(ft).orElseThrow(()));
        }
    }


    protected Collection<Object> getFactTemplateCacheForKieBase(DroolsKIEBaseModel kieBase)
    {
        Object key = getKieBaseCacheKeyGenerator().apply(kieBase);
        return (Collection<Object>)((Map)getFactTemplateCacheForKIEModule(kieBase.getKieModule()).orElseGet(Collections::emptyMap)).getOrDefault(key, Collections.emptyList());
    }


    protected Optional<Map<Object, Collection<Object>>> getFactTemplateCacheForKIEModule(DroolsKIEModuleModel kieModule)
    {
        Object key = getKieModuleCacheKeyGenerator().apply(kieModule);
        return Optional.ofNullable(this.factTemplateCache.get(key));
    }


    protected Optional<RAOProvider> getRaoProvider(Object factTemplate)
    {
        return Optional.ofNullable(getRaoProviders().get(factTemplate.getClass()));
    }


    protected Map<Class, RAOProvider> getRaoProviders()
    {
        return this.raoProviders;
    }


    public void setRaoProviders(Map<Class<?>, RAOProvider> raoProviders)
    {
        this.raoProviders = raoProviders;
    }


    protected List<RAOProvider> getRaoCacheCreators()
    {
        return this.raoCacheCreators;
    }


    public void setRaoCacheCreators(List<RAOProvider> raoCacheCreators)
    {
        this.raoCacheCreators = raoCacheCreators;
    }
}
