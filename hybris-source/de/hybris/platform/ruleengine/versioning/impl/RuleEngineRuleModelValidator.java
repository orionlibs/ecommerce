package de.hybris.platform.ruleengine.versioning.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.versioning.AbstractValidationResult;
import de.hybris.platform.ruleengine.versioning.ComposableValidationResult;
import de.hybris.platform.ruleengine.versioning.RuleModelChecksumCalculator;
import de.hybris.platform.ruleengine.versioning.RuleModelValidator;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import java.util.Objects;
import java.util.function.Supplier;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class RuleEngineRuleModelValidator implements RuleModelValidator
{
    private RuleModelChecksumCalculator ruleModelChecksumCalculator;
    private EngineRuleDao engineRuleDao;


    public AbstractValidationResult validate(AbstractRuleEngineRuleModel rule, InterceptorContext context)
    {
        Preconditions.checkArgument(rule instanceof DroolsRuleModel, "DroolsRuleModel type of rule is expected here");
        Objects.requireNonNull(rule);
        Objects.requireNonNull(context);
        DroolsRuleModel ruleModel = (DroolsRuleModel)rule;
        if(context.isNew(ruleModel))
        {
            return (AbstractValidationResult)validateNewContent(ruleModel);
        }
        if(context.isModified(ruleModel))
        {
            return (AbstractValidationResult)validateModifiedContent(ruleModel);
        }
        if(context.isRemoved(ruleModel))
        {
            return (AbstractValidationResult)validateRemovedContent(ruleModel);
        }
        throw new IllegalStateException("Unknown state of rule " + ruleModel);
    }


    protected ComposableValidationResult validateNewContent(DroolsRuleModel droolsRule)
    {
        if(isGeneratedFromSourceRule(droolsRule))
        {
            return validateAutomaticallyGenerated(droolsRule);
        }
        return validateManuallyCreated(droolsRule);
    }


    protected ComposableValidationResult validateAutomaticallyGenerated(DroolsRuleModel droolsRule)
    {
        ComposableValidationResult validationResult = mustBeCurrentVersion(droolsRule).and(activeFlagMustBeSet(droolsRule));
        if(!validationResult.succeeded())
        {
            return validationResult;
        }
        if(droolsRule.getActive().booleanValue())
        {
            return validationResult.and(checksumMustMatch(droolsRule)).and(versionMustBeSet(droolsRule))
                            .and(codeMustBeSet(droolsRule));
        }
        validationResult = validationResult.and(versionMustBeSet(droolsRule)).and(codeMustBeSet(droolsRule));
        if(!validationResult.succeeded())
        {
            return validationResult;
        }
        if(hasKieModuleAssigned(droolsRule))
        {
            validationResult = validationResult.and(checksumVersionForNotActive(droolsRule));
            if(!validationResult.succeeded())
            {
                return validationResult;
            }
        }
        return validationResult.and(nonActiveChecksumMustMatch(droolsRule));
    }


    protected ComposableValidationResult validateManuallyCreated(DroolsRuleModel droolsRule)
    {
        return mustBeCreatedUsingLatestVersion(droolsRule).and(nonActiveChecksumMustMatch(droolsRule));
    }


    protected boolean isGeneratedFromSourceRule(DroolsRuleModel droolsRule)
    {
        return Objects.nonNull(droolsRule.getSourceRule());
    }


    protected ComposableValidationResult validateModifiedContent(DroolsRuleModel droolsRule)
    {
        ComposableValidationResult validationResult = mustBeCurrentVersion(droolsRule).and(activeFlagMustBeSet(droolsRule));
        if(!validationResult.succeeded())
        {
            return validationResult;
        }
        return validationResult.and(checksumMustMatch(droolsRule)).and(versionMustBeSet(droolsRule))
                        .and(versionMustBeLast(droolsRule));
    }


    protected Supplier<ComposableValidationResult> activeFlagMustBeSet(DroolsRuleModel droolsRule)
    {
        return () -> errorIf(Objects.isNull(droolsRule.getActive()), "Active flag must be set");
    }


    protected ComposableValidationResult validateRemovedContent(DroolsRuleModel droolsRule)
    {
        return errorIf(BooleanUtils.isNotTrue(droolsRule.getCurrentVersion()), "Rule must be active.");
    }


    protected ComposableValidationResult mustBeCurrentVersion(DroolsRuleModel droolsRule)
    {
        return errorIf(!hasLatestVersionOrNew((AbstractRuleEngineRuleModel)droolsRule), "Historical version of the rule cannot be modified");
    }


    protected ComposableValidationResult mustBeCreatedUsingLatestVersion(DroolsRuleModel droolsRule)
    {
        return errorIf(!hasLatestVersionOrNew((AbstractRuleEngineRuleModel)droolsRule), "Rule must be created using latest rule module version.");
    }


    protected boolean hasLatestVersionOrNew(AbstractRuleEngineRuleModel rule)
    {
        Preconditions.checkArgument(Objects.nonNull(rule), "rule must not be null");
        Preconditions.checkArgument(rule instanceof DroolsRuleModel, "rule must be an instance of DroolsRuleModel");
        DroolsRuleModel droolsRule = (DroolsRuleModel)rule;
        Preconditions.checkArgument(Objects.nonNull(droolsRule.getKieBase()), "rule must have correct associated KieBase");
        Preconditions.checkArgument(Objects.nonNull(droolsRule.getKieBase().getKieModule()), "rule must have correct associated KieModule");
        DroolsKIEModuleModel module = droolsRule.getKieBase().getKieModule();
        Long lastVersion = getEngineRuleDao().getRuleVersion(rule.getCode(), module.getName());
        Long version = rule.getVersion();
        return (Objects.isNull(lastVersion) || Objects.isNull(version) || version.longValue() >= lastVersion.longValue());
    }


    protected Supplier<ComposableValidationResult> kieModuleMustBeKnown(DroolsRuleModel droolsRule)
    {
        return () -> errorIf(!hasKieModuleAssigned(droolsRule), "Kie base and kie module are required.");
    }


    protected boolean hasKieModuleAssigned(DroolsRuleModel droolsRule)
    {
        return (Objects.nonNull(droolsRule.getKieBase()) && Objects.nonNull(droolsRule.getKieBase().getKieModule()));
    }


    protected Supplier<ComposableValidationResult> checksumVersionForNotActive(DroolsRuleModel droolsRule)
    {
        return () -> {
            Long currentRulesSnapshotVersion = getEngineRuleDao().getCurrentRulesSnapshotVersion((AbstractRulesModuleModel)droolsRule.getKieBase().getKieModule());
            return errorIf((droolsRule.getVersion().longValue() > currentRulesSnapshotVersion.longValue() || (Objects.isNull(droolsRule.getRuleContent()) && droolsRule.getVersion().longValue() < currentRulesSnapshotVersion.longValue())),
                            "Non active rule version cannot increase overall knowledgebase snapshot version");
        };
    }


    protected Supplier<ComposableValidationResult> checksumMustMatch(DroolsRuleModel droolsRule)
    {
        return () -> {
            String expected = getRuleModelChecksumCalculator().calculateChecksumOf((AbstractRuleEngineRuleModel)droolsRule);
            return errorIf(!StringUtils.equals(expected, droolsRule.getChecksum()), String.format("Checksum doesn't match the rule content. Expected %s but was %s", new Object[] {expected, droolsRule.getChecksum()}));
        };
    }


    protected Supplier<ComposableValidationResult> nonActiveChecksumMustMatch(DroolsRuleModel droolsRule)
    {
        return () -> {
            String expected = null;
            if(Objects.nonNull(droolsRule.getRuleContent()))
            {
                expected = getRuleModelChecksumCalculator().calculateChecksumOf((AbstractRuleEngineRuleModel)droolsRule);
            }
            return errorIf((Objects.nonNull(expected) && !expected.equals(droolsRule.getChecksum())), "Checksum doesn't match the content.");
        };
    }


    protected Supplier<ComposableValidationResult> versionMustBeSet(DroolsRuleModel droolsRule)
    {
        return () -> errorIf(Objects.isNull(droolsRule.getVersion()), "Version must be set");
    }


    protected Supplier<ComposableValidationResult> codeMustBeSet(DroolsRuleModel droolsRule)
    {
        return () -> errorIf(Objects.isNull(droolsRule.getCode()), "Code must be set.");
    }


    protected Supplier<ComposableValidationResult> versionMustBeLast(DroolsRuleModel droolsRule)
    {
        return () -> errorIf((hasKieModuleAssigned(droolsRule) && !isVersionLast(droolsRule)), "Only update of the most recent rule version is possible");
    }


    protected boolean isVersionLast(DroolsRuleModel droolsRule)
    {
        DroolsKIEModuleModel module = droolsRule.getKieBase().getKieModule();
        Long ruleVersion = getEngineRuleDao().getRuleVersion(droolsRule.getCode(), module.getName());
        return (Objects.isNull(ruleVersion) || droolsRule.getVersion().longValue() >= ruleVersion.longValue());
    }


    protected final ComposableValidationResult errorIf(boolean condition, String errorMessage)
    {
        if(!condition)
        {
            return ComposableValidationResult.SUCCESS;
        }
        return ComposableValidationResult.makeError(errorMessage);
    }


    protected RuleModelChecksumCalculator getRuleModelChecksumCalculator()
    {
        return this.ruleModelChecksumCalculator;
    }


    @Required
    public void setRuleModelChecksumCalculator(RuleModelChecksumCalculator ruleModelChecksumCalculator)
    {
        this.ruleModelChecksumCalculator = ruleModelChecksumCalculator;
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
}
