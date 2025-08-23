package de.hybris.platform.ruleengine.event;

import com.google.common.collect.Lists;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.TenantListener;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.ruleengine.MessageLevel;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.RuleEngineService;
import de.hybris.platform.ruleengine.constants.RuleEngineConstants;
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.init.InitializationFuture;
import de.hybris.platform.ruleengine.init.RuleEngineBootstrap;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.RedeployUtilities;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.retry.support.RetryTemplate;

public class OnTenantStartupProcessor
{
    private static final Logger LOG = LoggerFactory.getLogger(OnTenantStartupProcessor.class);
    private RulesModuleDao rulesModuleDao;
    private RuleEngineBootstrap ruleEngineBootstrap;
    private RuleEngineService ruleEngineService;
    private ConfigurationService configurationService;
    private Set<String> excludedTenants = Collections.emptySet();
    private Tenant currentTenant;
    private RetryTemplate ruleEngineInitRetryTemplate;
    private Set<String> droolsProperties = Collections.emptySet();


    protected void processOnTenantStartup()
    {
        if(ignoreTenant())
        {
            return;
        }
        setDroolsSystemProperties();
        if(isSystemInitialized())
        {
            activateRulesModules();
        }
        else
        {
            LOG.info("Not initializing rule engine as system is not initialized or currently initializing");
        }
    }


    protected void setDroolsSystemProperties()
    {
        if(Objects.isNull(getDroolsProperties()))
        {
            return;
        }
        for(String property : this.droolsProperties)
        {
            if(Objects.nonNull(property))
            {
                LOG.debug("looking in configuration for drools property: {} ", property);
                String value = getConfigurationService().getConfiguration().getString(property);
                if(Objects.nonNull(value))
                {
                    System.setProperty(property, value);
                    LOG.info("setting drools property as system.property with key=value: {}={}", property, value);
                }
            }
        }
    }


    protected boolean activateRulesModules()
    {
        if(getConfigurationService().getConfiguration().getBoolean("ruleengine.engine.active", true))
        {
            refreshCurrentSessionWithRetry(() -> {
                JaloSession.getCurrentSession();
                return null;
            });
            List<RuleEngineActionResult> bootstrapResults = Lists.newArrayList();
            try
            {
                List<AbstractRulesModuleModel> modules = getRulesModuleDao().findAll();
                List<String> moduleNames = (List<String>)modules.stream().map(AbstractRulesModuleModel::getName).collect(Collectors.toList());
                LOG.info("[{}]: Initializing rule engine modules after tenant [{}] startup. Modules to be initialized: {}", new Object[] {Thread.currentThread().getName(),
                                (this.currentTenant == null) ? "null" : this.currentTenant.getTenantID(), moduleNames
                                .stream().collect(Collectors.joining(", "))});
                RuleEngineConstants.RuleEngineInitMode initMode = RuleEngineConstants.RuleEngineInitMode.valueOf(getConfigurationService().getConfiguration().getString("ruleengine.engine.init.mode", RuleEngineConstants.RuleEngineInitMode.SYNC
                                .toString()));
                if(RuleEngineConstants.RuleEngineInitMode.SYNC.equals(initMode))
                {
                    moduleNames.forEach(moduleName -> bootstrapResults.add(getRuleEngineBootstrap().startup(moduleName)));
                }
                else if(RuleEngineConstants.RuleEngineInitMode.ASYNC.equals(initMode))
                {
                    InitializationFuture initializationFuture = getRuleEngineService().initialize(modules, false, false).waitForInitializationToFinish();
                    bootstrapResults.addAll(initializationFuture.getResults());
                }
                else
                {
                    LOG.error("Unsupported Rule Engine initialisation mode [{}], skipping initialization.", initMode);
                }
            }
            catch(Exception ex)
            {
                LOG.error("[{}]: Exception caught in rule module initialization during tenant startup or after initialization: {}",
                                Thread.currentThread().getName(), ex);
            }
            finally
            {
                for(RuleEngineActionResult bootstrapResult : bootstrapResults)
                {
                    if(bootstrapResult.isActionFailed())
                    {
                        LOG.error("[{}]: Rules module initialization for [{}] failed during tenant startup or after initialization. Error details: {}", new Object[] {Thread.currentThread().getName(), bootstrapResult.getModuleName(), bootstrapResult
                                        .getMessagesAsString(MessageLevel.ERROR)});
                    }
                }
            }
        }
        return true;
    }


    protected void refreshCurrentSessionWithRetry(Supplier<Void> failingMethodSupplier)
    {
        RetryTemplate retryTemplate = getRuleEngineInitRetryTemplate();
        retryTemplate.execute(retryContext -> (Void)failingMethodSupplier.get());
    }


    @PostConstruct
    protected void init()
    {
        this.currentTenant = Registry.getCurrentTenantNoFallback();
        Registry.registerTenantListener((TenantListener)new DefaultRuleEngineTenantListener(this));
    }


    protected boolean isSystemInitialized()
    {
        if(this.currentTenant == null)
        {
            return false;
        }
        if(!this.currentTenant.getJaloConnection().isSystemInitialized())
        {
            return false;
        }
        boolean result = !RedeployUtilities.isShutdownInProgress();
        if(result)
        {
            LOG.info("System is initialised, tenantId=[{}] ", this.currentTenant.getTenantID());
        }
        return result;
    }


    protected boolean ignoreTenant()
    {
        Tenant tenant = Registry.getCurrentTenant();
        if(getExcludedTenants() != null && getExcludedTenants().contains(tenant.getTenantID()))
        {
            LOG.info("ignoring rule module activation on tenant:" + tenant.getTenantID() + " as it is part of the excludedTenants set.");
            return true;
        }
        return false;
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


    protected Set<String> getExcludedTenants()
    {
        return this.excludedTenants;
    }


    public void setExcludedTenants(Set<String> excludedTenants)
    {
        this.excludedTenants = excludedTenants;
    }


    protected Set<String> getDroolsProperties()
    {
        return this.droolsProperties;
    }


    public void setDroolsProperties(Set<String> droolsProperties)
    {
        this.droolsProperties = droolsProperties;
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


    protected RuleEngineBootstrap getRuleEngineBootstrap()
    {
        return this.ruleEngineBootstrap;
    }


    @Required
    public void setRuleEngineBootstrap(RuleEngineBootstrap ruleEngineBootstrap)
    {
        this.ruleEngineBootstrap = ruleEngineBootstrap;
    }


    protected RuleEngineService getRuleEngineService()
    {
        return this.ruleEngineService;
    }


    @Required
    public void setRuleEngineService(RuleEngineService ruleEngineService)
    {
        this.ruleEngineService = ruleEngineService;
    }


    protected RetryTemplate getRuleEngineInitRetryTemplate()
    {
        return this.ruleEngineInitRetryTemplate;
    }


    @Required
    public void setRuleEngineInitRetryTemplate(RetryTemplate ruleEngineInitRetryTemplate)
    {
        this.ruleEngineInitRetryTemplate = ruleEngineInitRetryTemplate;
    }
}
