package de.hybris.platform.ruleengine.versioning.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.util.RuleEngineUtils;
import de.hybris.platform.ruleengine.versioning.HistoricalRuleContentProvider;
import de.hybris.platform.ruleengine.versioning.RuleModelHistoricalContentCreator;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class RuleEngineRuleModelHistoricalContentCreator implements RuleModelHistoricalContentCreator
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleEngineRuleModelHistoricalContentCreator.class);
    private static final String RULE_CONTENT_CHECK_ENABLED = "ruleengine.rule.content.check.enabled";
    private List<HistoricalRuleContentProvider> historicalRuleContentProviders;
    private EngineRuleDao engineRuleDao;
    private ConfigurationService configurationService;


    public void createHistoricalVersion(AbstractRuleEngineRuleModel rule, InterceptorContext context)
    {
        Preconditions.checkArgument(Objects.nonNull(rule), "Model should not be null here");
        Preconditions.checkArgument(Objects.nonNull(context), "InterceptorContext should not be null here");
        Preconditions.checkArgument(rule instanceof DroolsRuleModel, "The model must be an instance of DroolsRuleModel type");
        DroolsRuleModel droolsRule = (DroolsRuleModel)rule;
        createHistoricalVersionIfNeeded(droolsRule, context);
    }


    protected void createHistoricalVersionIfNeeded(DroolsRuleModel droolsRule, InterceptorContext ctx)
    {
        if(hasAssociatedKieBase(droolsRule) && historicalVersionMustBeCreated((AbstractRuleEngineRuleModel)droolsRule, ctx))
        {
            checkIfKieModuleIsTheSame(droolsRule, ctx);
            incrementActiveModelVersion((AbstractRuleEngineRuleModel)droolsRule);
            droolsRule.setCurrentVersion(Boolean.TRUE);
            DroolsRuleModel historicalDroolsRule = createHistoricalVersionForDroolsRule(droolsRule, ctx);
            LOGGER.debug("Registering historical model: PK={}, code={}, uuid={}, version={}, active={}, currentVersion={}", new Object[] {historicalDroolsRule
                            .getPk(), historicalDroolsRule.getCode(), historicalDroolsRule.getUuid(), historicalDroolsRule
                            .getVersion(), historicalDroolsRule.getActive(), historicalDroolsRule.getCurrentVersion()});
            ctx.registerElementFor(historicalDroolsRule, PersistenceOperation.SAVE);
        }
        LOGGER.debug("Registering modified model: PK={}, code={}, uuid={}, version={}, active={}, currentVersion={}", new Object[] {droolsRule
                        .getPk(), droolsRule.getCode(), droolsRule.getUuid(), droolsRule.getVersion(), droolsRule.getActive(), droolsRule
                        .getCurrentVersion()});
    }


    protected void checkIfKieModuleIsTheSame(DroolsRuleModel droolsRule, InterceptorContext ctx)
    {
        DroolsKIEBaseModel origKieBase = getOriginal((AbstractRuleEngineRuleModel)droolsRule, ctx, "kieBase");
        if(Objects.nonNull(origKieBase) && Objects.nonNull(origKieBase.getKieModule()) &&
                        !origKieBase.getKieModule().getName().equals(droolsRule.getKieBase().getKieModule().getName()))
        {
            throw new IllegalStateException("KieModule of the modified drools rule should not change. Consider associating the rule KieBase to the same KieModule first");
        }
    }


    protected void incrementActiveModelVersion(AbstractRuleEngineRuleModel ruleModel)
    {
        AbstractRulesModuleModel rulesModule = getKieModule(ruleModel);
        Long maxRuleVersion = getEngineRuleDao().getCurrentRulesSnapshotVersion(rulesModule);
        Objects.requireNonNull(maxRuleVersion, "Maximum rule version cannot be found");
        long nextVersion = 1L + maxRuleVersion.longValue();
        ruleModel.setVersion(Long.valueOf(nextVersion));
    }


    protected boolean historicalVersionMustBeCreated(AbstractRuleEngineRuleModel droolsRule, InterceptorContext context)
    {
        return (hasLastVersion(droolsRule) && modelIsBeeingModified(droolsRule, context) && modelIsValid(droolsRule) &&
                        drivingAttributesModified(droolsRule, context));
    }


    protected boolean drivingAttributesModified(AbstractRuleEngineRuleModel droolsRule, InterceptorContext context)
    {
        return ((isActive(droolsRule, context) && contentHasChanged(droolsRule, context)) || activeFlagChanged(droolsRule, context));
    }


    protected boolean hasLastVersion(AbstractRuleEngineRuleModel ruleModel)
    {
        AbstractRulesModuleModel rulesModule = getKieModule(ruleModel);
        Long lastVersion = getEngineRuleDao().getRuleVersion(ruleModel.getCode(), rulesModule.getName());
        return (Objects.isNull(lastVersion) || ruleModel.getVersion().longValue() >= lastVersion.longValue());
    }


    protected boolean activeFlagChanged(AbstractRuleEngineRuleModel model, InterceptorContext context)
    {
        return !model.getActive().equals(getOriginal(model, context, "active"));
    }


    protected DroolsRuleModel createHistoricalVersionForDroolsRule(DroolsRuleModel droolsRule, InterceptorContext context)
    {
        DroolsRuleModel historicalDroolsRule = (DroolsRuleModel)context.getModelService().clone(droolsRule);
        putOriginalValuesIntoHistoricalVersion((AbstractRuleEngineRuleModel)droolsRule, (AbstractRuleEngineRuleModel)historicalDroolsRule, context);
        deactivateHistoricalVersion(historicalDroolsRule);
        return historicalDroolsRule;
    }


    protected void putOriginalValuesIntoHistoricalVersion(AbstractRuleEngineRuleModel droolsRule, AbstractRuleEngineRuleModel historicalDroolsRule, InterceptorContext ctx)
    {
        getHistoricalRuleContentProviders()
                        .forEach(p -> p.copyOriginalValuesIntoHistoricalVersion(droolsRule, historicalDroolsRule, ctx));
    }


    protected void deactivateHistoricalVersion(DroolsRuleModel historicalDroolsRule)
    {
        historicalDroolsRule.setCurrentVersion(Boolean.FALSE);
    }


    protected boolean modelIsValid(AbstractRuleEngineRuleModel ruleModel)
    {
        return (Objects.nonNull(ruleModel.getRuleContent()) && Objects.nonNull(ruleModel.getCode()));
    }


    protected boolean modelIsBeeingModified(AbstractRuleEngineRuleModel ruleModel, InterceptorContext ctx)
    {
        return (!ctx.isNew(ruleModel) && !ctx.isRemoved(ruleModel) && ctx.isModified(ruleModel));
    }


    protected boolean isActive(AbstractRuleEngineRuleModel ruleModel, InterceptorContext ctx)
    {
        Boolean result = getOriginal(ruleModel, ctx, "active");
        return (Objects.nonNull(result) && result.booleanValue());
    }


    protected boolean hasAssociatedKieBase(DroolsRuleModel droolsRule)
    {
        return Objects.nonNull(droolsRule.getKieBase());
    }


    protected AbstractRulesModuleModel getKieModule(AbstractRuleEngineRuleModel ruleModel)
    {
        if(ruleModel instanceof DroolsRuleModel)
        {
            return (AbstractRulesModuleModel)Objects.requireNonNull(((DroolsRuleModel)ruleModel).getKieBase().getKieModule());
        }
        return null;
    }


    protected <T> T getOriginal(AbstractRuleEngineRuleModel droolsRule, InterceptorContext context, String attributeQualifier)
    {
        if(context.isModified(droolsRule, attributeQualifier))
        {
            ItemModelContext modelContext = ModelContextUtils.getItemModelContext((AbstractItemModel)droolsRule);
            return (T)modelContext.getOriginalValue(attributeQualifier);
        }
        ModelService modelService = Objects.<ModelService>requireNonNull(context.getModelService());
        return (T)modelService.getAttributeValue(droolsRule, attributeQualifier);
    }


    protected boolean contentHasChanged(AbstractRuleEngineRuleModel ruleModel, InterceptorContext ctx)
    {
        if(getConfigurationService().getConfiguration().getBoolean("ruleengine.rule.content.check.enabled", false))
        {
            String cleanedContent = RuleEngineUtils.getCleanedContent(ruleModel.getRuleContent(), ruleModel.getUuid());
            String origCleanedContent = RuleEngineUtils.getCleanedContent(
                            getOriginal(ruleModel, ctx, "ruleContent"),
                            getOriginal(ruleModel, ctx, "uuid"));
            if(Objects.nonNull(cleanedContent))
            {
                return !cleanedContent.equals(origCleanedContent);
            }
            return Objects.nonNull(origCleanedContent);
        }
        return true;
    }


    protected EngineRuleDao getEngineRuleDao()
    {
        return this.engineRuleDao;
    }


    @Required
    public void setEngineRuleDao(EngineRuleDao engineRuleDao)
    {
        this.engineRuleDao = engineRuleDao;
    }


    protected List<HistoricalRuleContentProvider> getHistoricalRuleContentProviders()
    {
        return this.historicalRuleContentProviders;
    }


    @Required
    public void setHistoricalRuleContentProviders(List<HistoricalRuleContentProvider> historicalRuleContentProviders)
    {
        this.historicalRuleContentProviders = historicalRuleContentProviders;
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
}
