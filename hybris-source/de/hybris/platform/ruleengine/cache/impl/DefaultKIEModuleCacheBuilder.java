package de.hybris.platform.ruleengine.cache.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengine.cache.KIEModuleCacheBuilder;
import de.hybris.platform.ruleengine.cache.RuleGlobalsBeanProvider;
import de.hybris.platform.ruleengine.impl.DefaultPlatformRuleEngineService;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.util.EngineRulePreconditions;
import de.hybris.platform.ruleengine.util.RuleMappings;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultKIEModuleCacheBuilder implements KIEModuleCacheBuilder
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPlatformRuleEngineService.class);
    private final Map<Object, Map<String, Object>> globalsCache = new ConcurrentHashMap<>();
    private final Map<Object, DroolsRuleModel> duplicateGlobalsCheckMap = new ConcurrentHashMap<>();
    private DroolsKIEModuleModel kieModule;
    private boolean failOnBeanMismatches;
    private Function<DroolsKIEBaseModel, Object> kieBaseCacheKeyGenerator;
    private RuleGlobalsBeanProvider ruleGlobalsBeanProvider;


    public DefaultKIEModuleCacheBuilder(RuleGlobalsBeanProvider ruleGlobalsBeanProvider, DroolsKIEModuleModel kieModule, Function<DroolsKIEBaseModel, Object> kieBaseCacheKeyGenerator, boolean failOnBeanMismatches)
    {
        ServicesUtil.validateParameterNotNull(kieModule, "kieModule must not be null");
        ServicesUtil.validateParameterNotNull(kieModule.getPk(), "kieModule must be persisted (i.e. have a non-null PK)");
        ServicesUtil.validateParameterNotNull(kieBaseCacheKeyGenerator, "kieBaseCacheKeyGenerator must not be null");
        this.ruleGlobalsBeanProvider = ruleGlobalsBeanProvider;
        this.kieModule = kieModule;
        this.failOnBeanMismatches = failOnBeanMismatches;
        this.kieBaseCacheKeyGenerator = kieBaseCacheKeyGenerator;
    }


    public <T extends AbstractRuleEngineRuleModel> void processRule(T rule)
    {
        EngineRulePreconditions.checkRuleHasKieModule((AbstractRuleEngineRuleModel)rule);
        DroolsRuleModel droolsRule = (DroolsRuleModel)rule;
        Preconditions.checkArgument(this.kieModule.getName().equals(RuleMappings.moduleName(droolsRule)), "rule must have the same kie module as cache builder");
        Map<String, Object> kieBaseGlobals = getCachedGlobalsForKieBase(droolsRule.getKieBase());
        if(MapUtils.isNotEmpty(droolsRule.getGlobals()))
        {
            for(Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)droolsRule.getGlobals().entrySet())
            {
                Object bean = getRuleGlobalsBeanProvider().getRuleGlobals(entry.getValue());
                Object oldBean = kieBaseGlobals.put(entry.getKey(), bean);
                if(oldBean != null && !bean.equals(oldBean) && !bean.getClass().isAssignableFrom(oldBean.getClass()))
                {
                    DroolsRuleModel oldRule = this.duplicateGlobalsCheckMap.get(oldBean);
                    String errorMessage = MessageFormat.format("Error when registering global of type {4} for rule {0}. Bean for global {1} was already defined by rule {2} which added bean of type {3}.\n Check your rules! Rule {2} might encounter runtime errors as it expects a global of type {3}",
                                    new Object[] {rule
                                                    .getCode(), entry.getKey(),
                                                    (oldRule == null) ? "" : oldRule.getCode(), oldBean.getClass().getName(), bean.getClass().getName()});
                    LOGGER.error(errorMessage);
                    escalateOnBeanMismatchesIfNecessary(errorMessage);
                }
                this.duplicateGlobalsCheckMap.put(bean, droolsRule);
            }
        }
    }


    protected void escalateOnBeanMismatchesIfNecessary(String message)
    {
        if(this.failOnBeanMismatches)
        {
            throw new IllegalArgumentException(message);
        }
    }


    protected Map<String, Object> getCachedGlobalsForKieBase(DroolsKIEBaseModel kieBase)
    {
        return this.globalsCache.computeIfAbsent(getKieBaseCacheKeyGenerator().apply(kieBase), k -> new ConcurrentHashMap<>());
    }


    public Map<Object, Map<String, Object>> getGlobalsCache()
    {
        return this.globalsCache;
    }


    public DroolsKIEModuleModel getKieModule()
    {
        return this.kieModule;
    }


    protected Function<DroolsKIEBaseModel, Object> getKieBaseCacheKeyGenerator()
    {
        return this.kieBaseCacheKeyGenerator;
    }


    protected RuleGlobalsBeanProvider getRuleGlobalsBeanProvider()
    {
        return this.ruleGlobalsBeanProvider;
    }
}
