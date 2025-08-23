package de.hybris.platform.ruleengineservices.jobs.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengineservices.jobs.RuleEngineCronJobLauncher;
import de.hybris.platform.ruleengineservices.jobs.RuleEngineCronJobSupplierFactory;
import de.hybris.platform.ruleengineservices.jobs.RuleEngineJobService;
import de.hybris.platform.ruleengineservices.model.RuleEngineCronJobModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleEngineCronJobLauncher implements RuleEngineCronJobLauncher
{
    protected static final String COMPILE_PUBLISH_PERFORMABLE_BEAN_NAME = "ruleEngineCompilePublishJobPerformable";
    protected static final String COMPILE_PUBLISH_JOB_CODE_TEMPLATE = "rules -> Compilation and Publishing for [%s]";
    protected static final String UNDEPLOY_PERFORMABLE_BEAN_NAME = "ruleEngineUndeployJobPerformable";
    protected static final String UNDEPLOY_JOB_CODE_TEMPLATE = "rules -> Undeploy for [%s]";
    protected static final String MODULES_SYNCH_PERFORMABLE_BEAN_NAME = "ruleEngineModuleSyncJobPerformable";
    protected static final String MODULES_SYNCH_JOB_CODE_TEMPLATE = "rules -> Modules Sync from [%s] to [%s]";
    protected static final String MODULE_INIT_PERFORMABLE_BEAN_NAME = "ruleEngineModuleInitJobPerformable";
    protected static final String MODULE_INIT_JOB_CODE_TEMPLATE = "rules -> Module Init for [%s]";
    protected static final String ALL_MODULES_INIT_PERFORMABLE_BEAN_NAME = "ruleEngineAllModulesInitJobPerformable";
    protected static final String ALL_MODULES_INIT_JOB_CODE = "rules -> All Modules Init";
    private RuleEngineJobService ruleEngineJobService;
    private RulesModuleDao rulesModuleDao;
    private RuleEngineCronJobSupplierFactory ruleEngineCronJobSupplierFactory;
    private L10NService l10nService;
    private int maximumNumberOfParallelCronJobs;


    public RuleEngineCronJobModel triggerCompileAndPublish(List<SourceRuleModel> rules, String moduleName, boolean enableIncrementalUpdate)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("rules", rules);
        ServicesUtil.validateParameterNotNullStandardMessage("moduleName", moduleName);
        String compilePublishJobCode = String.format("rules -> Compilation and Publishing for [%s]", new Object[] {moduleName});
        verifyIfJobsAreNotRunning((String[])Stream.<List>of(new List[] {Lists.newArrayList((Object[])new String[] {compilePublishJobCode,
                                        String.format("rules -> Undeploy for [%s]", new Object[] {moduleName}), String.format("rules -> Module Init for [%s]", new Object[] {moduleName}), "rules -> All Modules Init"}), getPossibleModuleSyncJobCodes(moduleName)}).flatMap(Collection::stream)
                        .toArray(x$0 -> new String[x$0]));
        return getRuleEngineJobService()
                        .triggerCronJob(compilePublishJobCode, "ruleEngineCompilePublishJobPerformable",
                                        getRuleEngineCronJobSupplierFactory().createCompileAndPublishSupplier(rules, moduleName, enableIncrementalUpdate));
    }


    public RuleEngineCronJobModel triggerUndeployRules(List<SourceRuleModel> rules, String moduleName)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("rules", rules);
        ServicesUtil.validateParameterNotNullStandardMessage("moduleName", moduleName);
        String undeployJobCode = String.format("rules -> Undeploy for [%s]", new Object[] {moduleName});
        verifyIfJobsAreNotRunning((String[])Stream.<List>of(new List[] {Lists.newArrayList((Object[])new String[] {undeployJobCode,
                                        String.format("rules -> Compilation and Publishing for [%s]", new Object[] {moduleName}), String.format("rules -> Module Init for [%s]", new Object[] {moduleName}), "rules -> All Modules Init"}), getPossibleModuleSyncJobCodes(moduleName)}).flatMap(Collection::stream)
                        .toArray(x$0 -> new String[x$0]));
        return getRuleEngineJobService()
                        .triggerCronJob(undeployJobCode, "ruleEngineUndeployJobPerformable",
                                        getRuleEngineCronJobSupplierFactory().createUndeploySupplier(rules, moduleName));
    }


    public RuleEngineCronJobModel triggerSynchronizeModules(String srcModuleName, String targetModuleName)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("srcModuleName", srcModuleName);
        ServicesUtil.validateParameterNotNullStandardMessage("targetModuleName", targetModuleName);
        String moduleSyncJobCode = String.format("rules -> Modules Sync from [%s] to [%s]", new Object[] {srcModuleName, targetModuleName});
        verifyIfJobsAreNotRunning(new String[] {moduleSyncJobCode,
                        String.format("rules -> Modules Sync from [%s] to [%s]", new Object[] {targetModuleName, srcModuleName}), String.format("rules -> Compilation and Publishing for [%s]", new Object[] {srcModuleName}),
                        String.format("rules -> Compilation and Publishing for [%s]", new Object[] {targetModuleName}), String.format("rules -> Module Init for [%s]", new Object[] {srcModuleName}), String.format("rules -> Module Init for [%s]", new Object[] {targetModuleName}),
                        String.format("rules -> Undeploy for [%s]", new Object[] {srcModuleName}), String.format("rules -> Undeploy for [%s]", new Object[] {targetModuleName}), "rules -> All Modules Init"});
        return getRuleEngineJobService()
                        .triggerCronJob(moduleSyncJobCode, "ruleEngineModuleSyncJobPerformable",
                                        getRuleEngineCronJobSupplierFactory().createSynchronizeSupplier(srcModuleName, targetModuleName));
    }


    public RuleEngineCronJobModel triggerModuleInitialization(String moduleName)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("moduleName", moduleName);
        String moduleInitSyncJobCode = String.format("rules -> Module Init for [%s]", new Object[] {moduleName});
        verifyIfJobsAreNotRunning((String[])Stream.<List>of(new List[] {Lists.newArrayList((Object[])new String[] {moduleInitSyncJobCode,
                        String.format("rules -> Compilation and Publishing for [%s]", new Object[] {moduleName}), String.format("rules -> Undeploy for [%s]", new Object[] {moduleName}), String.format("rules -> Module Init for [%s]", new Object[] {moduleName}), "rules -> All Modules Init"}),
                        getPossibleModuleSyncJobCodes(moduleName)}).flatMap(Collection::stream).toArray(x$0 -> new String[x$0]));
        return getRuleEngineJobService()
                        .triggerCronJob(moduleInitSyncJobCode, "ruleEngineModuleInitJobPerformable",
                                        getRuleEngineCronJobSupplierFactory().createModuleInitializationSupplier(moduleName));
    }


    public RuleEngineCronJobModel triggerAllModulesInitialization()
    {
        String[] jobCodes = (String[])Stream.<List>of(
                        new List[] {Lists.newArrayList((Object[])new String[] {"rules -> All Modules Init"}), getPossibleJobCodes("rules -> Module Init for [%s]"), getPossibleJobCodes("rules -> Compilation and Publishing for [%s]"), getPossibleJobCodes("rules -> Undeploy for [%s]"),
                                        getPossibleModuleSyncJobCodes(), getPossibleJobCodes("rules -> Module Init for [%s]")}).flatMap(Collection::stream).toArray(x$0 -> new String[x$0]);
        verifyIfJobsAreNotRunning(jobCodes);
        return getRuleEngineJobService()
                        .triggerCronJob("rules -> All Modules Init", "ruleEngineAllModulesInitJobPerformable",
                                        getRuleEngineCronJobSupplierFactory().createAllModulesInitializationSupplier());
    }


    protected List<String> getPossibleModuleSyncJobCodes()
    {
        List<String> moduleNames = getAllRuleModuleNames();
        List<String> possibleModuleSyncJobNames = Lists.newArrayList();
        for(String moduleName : moduleNames)
        {
            for(String moduleName2 : moduleNames)
            {
                if(!moduleName2.equals(moduleName))
                {
                    possibleModuleSyncJobNames.add(String.format("rules -> Modules Sync from [%s] to [%s]", new Object[] {moduleName, moduleName2}));
                }
            }
        }
        return possibleModuleSyncJobNames;
    }


    protected List<String> getPossibleModuleSyncJobCodes(String moduleName)
    {
        List<String> moduleNames = getAllRuleModuleNames();
        List<String> possibleModuleSyncJobNames = Lists.newArrayList();
        for(String moduleName1 : moduleNames)
        {
            if(!moduleName1.equals(moduleName))
            {
                possibleModuleSyncJobNames.add(String.format("rules -> Modules Sync from [%s] to [%s]", new Object[] {moduleName, moduleName1}));
                possibleModuleSyncJobNames.add(String.format("rules -> Modules Sync from [%s] to [%s]", new Object[] {moduleName1, moduleName}));
            }
        }
        return possibleModuleSyncJobNames;
    }


    protected List<String> getPossibleJobCodes(String template)
    {
        List<String> moduleNames = getAllRuleModuleNames();
        return (List<String>)moduleNames.stream().map(m -> String.format(template, new Object[] {m})).collect(Collectors.toList());
    }


    protected List<String> getAllRuleModuleNames()
    {
        return (List<String>)getRulesModuleDao().findAll().stream().filter(AbstractRulesModuleModel::getActive)
                        .map(AbstractRulesModuleModel::getName).collect(Collectors.toList());
    }


    protected void verifyIfJobsAreNotRunning(String... jobCodes)
    {
        if(ArrayUtils.isNotEmpty((Object[])jobCodes))
        {
            Objects.requireNonNull(getRuleEngineJobService());
            int numberOfCronJobsRunning = Arrays.<String>stream(jobCodes).mapToInt(getRuleEngineJobService()::countRunningJobs).sum();
            if(numberOfCronJobsRunning >= getMaximumNumberOfParallelCronJobs())
            {
                throw new IllegalStateException(getL10nService().getLocalizedString("rule.cronjob.launcher.limit.error",
                                Arrays.array(new Integer[] {Integer.valueOf(getMaximumNumberOfParallelCronJobs())})));
            }
        }
    }


    protected RuleEngineJobService getRuleEngineJobService()
    {
        return this.ruleEngineJobService;
    }


    @Required
    public void setRuleEngineJobService(RuleEngineJobService ruleEngineJobService)
    {
        this.ruleEngineJobService = ruleEngineJobService;
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


    protected RuleEngineCronJobSupplierFactory getRuleEngineCronJobSupplierFactory()
    {
        return this.ruleEngineCronJobSupplierFactory;
    }


    @Required
    public void setRuleEngineCronJobSupplierFactory(RuleEngineCronJobSupplierFactory ruleEngineCronJobSupplierFactory)
    {
        this.ruleEngineCronJobSupplierFactory = ruleEngineCronJobSupplierFactory;
    }


    protected int getMaximumNumberOfParallelCronJobs()
    {
        return this.maximumNumberOfParallelCronJobs;
    }


    @Required
    public void setMaximumNumberOfParallelCronJobs(int maximumNumberOfParallelCronJobs)
    {
        this.maximumNumberOfParallelCronJobs = maximumNumberOfParallelCronJobs;
    }


    protected L10NService getL10nService()
    {
        return this.l10nService;
    }


    @Required
    public void setL10nService(L10NService l10nService)
    {
        this.l10nService = l10nService;
    }
}
