package de.hybris.platform.ruleengineservices.maintenance.systemsetup.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.hybris.platform.core.PK;
import de.hybris.platform.ruleengine.RuleEngineService;
import de.hybris.platform.ruleengine.jalo.AbstractRulesModule;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengineservices.jalo.SourceRule;
import de.hybris.platform.ruleengineservices.maintenance.RuleMaintenanceService;
import de.hybris.platform.ruleengineservices.maintenance.systemsetup.RuleEngineSystemSetup;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.events.AfterInitializationEndEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleEngineSystemSetup extends AbstractEventListener<AfterInitializationEndEvent> implements RuleEngineSystemSetup
{
    public DefaultRuleEngineSystemSetup()
    {
        this.initializationMap = new ConcurrentHashMap<>();
        this.sourceRuleModelValidator = (sr -> {
            try
            {
                Objects.requireNonNull(sr);
                Objects.requireNonNull(sr.getPk());
            }
            catch(Exception e)
            {
                if(failOnError())
                {
                    LOGGER.error("SourceRuleModel is null or has no pk.", e);
                    throw e;
                }
                LOGGER.info("SourceRuleModel is null or has no pk.", e);
                return false;
            }
            return true;
        });
    }


    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRuleEngineSystemSetup.class);
    private final Map<String, Set<PK>> initializationMap;
    private RuleEngineService ruleEngineService;
    private RuleMaintenanceService ruleMaintenanceService;
    private ModelService modelService;
    private ConfigurationService configurationService;
    private Predicate<SourceRuleModel> sourceRuleModelValidator;


    public <T extends AbstractRulesModule> void initializeModule(T module)
    {
        Preconditions.checkArgument(Objects.nonNull(module), "The provided module cannot be NULL");
        validateAndConvertFromJalo((AbstractRulesModule)module).ifPresent(m -> getRuleEngineService().initialize(Lists.newArrayList((Object[])new AbstractRulesModuleModel[] {m}, ), true, true));
    }


    public void registerSourceRuleForDeployment(SourceRule sourceRule, String... moduleNames)
    {
        ServicesUtil.validateParameterNotNull(sourceRule, "sourceRules must not be null!");
        ServicesUtil.validateParameterNotNull(moduleNames, "moduleNames must not be null!");
        validateAndConvertFromJalo(sourceRule)
                        .ifPresent(sr -> registerSourceRulesForDeployment(Collections.singleton(sr), Arrays.asList(moduleNames)));
    }


    public void registerSourceRulesForDeployment(Collection<SourceRuleModel> sourceRules, Collection<String> moduleNames)
    {
        ServicesUtil.validateParameterNotNull(sourceRules, "sourceRules must not be null!");
        ServicesUtil.validateParameterNotNull(moduleNames, "moduleNames must not be null!");
        Preconditions.checkArgument(!moduleNames.contains((Object)null), "moduleNames must not contain null values");
        moduleNames.forEach(name -> ((Set)getInitializationMap().computeIfAbsent(name, ())).addAll((Collection)sourceRules.stream().filter(getSourceRuleModelValidator()).map(AbstractItemModel::getPk).collect(Collectors.toSet())));
    }


    protected void onEvent(AfterInitializationEndEvent event)
    {
        LOGGER.info("Starting rule compilation and deployment after system initialization end event: {}",
                        getInitializationEventType(event));
        Map<String, List<SourceRuleModel>> moduleNameToRules = (Map<String, List<SourceRuleModel>>)getInitializationMap().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> convert((Set<PK>)e.getValue())));
        getInitializationMap().clear();
        moduleNameToRules.forEach(this::doRuleDeployment);
        LOGGER.info("Rule compilation and deployment after system initialization finished.");
    }


    protected void doRuleDeployment(String moduleName, List<SourceRuleModel> rules)
    {
        try
        {
            LOGGER.info("starting compilation and publication for module {}", moduleName);
            getRuleMaintenanceService().compileAndPublishRulesWithBlocking(rules, moduleName, false);
        }
        catch(Exception e)
        {
            LOGGER.error("error during rule deployment! Module " + moduleName + " is not initialized properly. ", e);
        }
    }


    protected String getInitializationEventType(AfterInitializationEndEvent event)
    {
        if(MapUtils.isNotEmpty(event.getParams()) && event.getParams().containsKey("initmethod"))
        {
            return (String)event.getParams().get("initmethod");
        }
        return "";
    }


    protected <T extends SourceRuleModel> Optional<T> validateAndConvertFromJalo(SourceRule sourceRule)
    {
        ServicesUtil.validateParameterNotNull(sourceRule, "sourceRule must not be null!");
        return (Optional)validateAndConvertFromJalo(sourceRule.getPK());
    }


    protected <T extends AbstractRulesModuleModel> Optional<T> validateAndConvertFromJalo(AbstractRulesModule rulesModule)
    {
        ServicesUtil.validateParameterNotNull(rulesModule, "rulesModule must not be null!");
        return (Optional)validateAndConvertFromJalo(rulesModule.getPK());
    }


    protected <T extends de.hybris.platform.core.model.ItemModel> Optional<T> validateAndConvertFromJalo(PK pk)
    {
        try
        {
            return Optional.of((T)getModelService().get(pk));
        }
        catch(Exception e)
        {
            LOGGER.warn("Couldn't get model for given pk: [{}]", pk);
            if(failOnError())
            {
                throw e;
            }
            return Optional.empty();
        }
    }


    protected boolean failOnError()
    {
        return getConfigurationService().getConfiguration().getBoolean("ruleengineservices.system.setup.failOnError", true);
    }


    protected <T extends de.hybris.platform.core.model.ItemModel> List<T> convert(Set<PK> pks)
    {
        return (List<T>)pks.stream().map(this::validateAndConvertFromJalo).filter(Optional::isPresent).map(Optional::get)
                        .collect(Collectors.toList());
    }


    protected Map<String, Set<PK>> getInitializationMap()
    {
        return this.initializationMap;
    }


    protected RuleMaintenanceService getRuleMaintenanceService()
    {
        return this.ruleMaintenanceService;
    }


    @Required
    public void setRuleMaintenanceService(RuleMaintenanceService ruleMaintenanceService)
    {
        this.ruleMaintenanceService = ruleMaintenanceService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
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


    protected Predicate<SourceRuleModel> getSourceRuleModelValidator()
    {
        return this.sourceRuleModelValidator;
    }


    public void setSourceRuleModelValidator(Predicate<SourceRuleModel> sourceRuleModelValidator)
    {
        this.sourceRuleModelValidator = sourceRuleModelValidator;
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
}
