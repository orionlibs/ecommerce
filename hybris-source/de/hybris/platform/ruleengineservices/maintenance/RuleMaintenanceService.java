package de.hybris.platform.ruleengineservices.maintenance;

import de.hybris.platform.cronjob.jalo.CronJobProgressTracker;
import java.util.List;
import java.util.Optional;

public interface RuleMaintenanceService
{
    <T extends de.hybris.platform.ruleengineservices.model.SourceRuleModel> RuleCompilerPublisherResult compileAndPublishRules(List<T> paramList, String paramString, boolean paramBoolean);


    RuleCompilerPublisherResult initializeModule(String paramString, boolean paramBoolean);


    RuleCompilerPublisherResult initializeAllModules(boolean paramBoolean);


    <T extends de.hybris.platform.ruleengineservices.model.SourceRuleModel> RuleCompilerPublisherResult compileAndPublishRulesWithBlocking(List<T> paramList, String paramString, boolean paramBoolean);


    <T extends de.hybris.platform.ruleengineservices.model.SourceRuleModel> RuleCompilerPublisherResult compileAndPublishRulesWithBlocking(List<T> paramList, String paramString, boolean paramBoolean, CronJobProgressTracker paramCronJobProgressTracker);


    <T extends de.hybris.platform.ruleengineservices.model.SourceRuleModel> Optional<RuleCompilerPublisherResult> undeployRules(List<T> paramList, String paramString);


    Optional<RuleCompilerPublisherResult> synchronizeModules(String paramString1, String paramString2);
}
