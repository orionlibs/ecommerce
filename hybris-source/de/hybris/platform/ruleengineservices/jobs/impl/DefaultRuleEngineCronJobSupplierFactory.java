package de.hybris.platform.ruleengineservices.jobs.impl;

import de.hybris.platform.ruleengineservices.jobs.RuleEngineCronJobSupplierFactory;
import de.hybris.platform.ruleengineservices.model.RuleEngineCronJobModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.servicelayer.cluster.ClusterService;
import java.util.List;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleEngineCronJobSupplierFactory implements RuleEngineCronJobSupplierFactory
{
    private ClusterService clusterService;
    private Integer nodeId;
    private String nodeGroup;


    public Supplier<RuleEngineCronJobModel> createCompileAndPublishSupplier(List<SourceRuleModel> rules, String moduleName, boolean enableIncrementalUpdate)
    {
        return () -> {
            RuleEngineCronJobModel cronJob = newCronJob();
            cronJob.setSourceRules(rules);
            cronJob.setTargetModuleName(moduleName);
            cronJob.setEnableIncrementalUpdate(Boolean.valueOf(enableIncrementalUpdate));
            return cronJob;
        };
    }


    public Supplier<RuleEngineCronJobModel> createUndeploySupplier(List<SourceRuleModel> rules, String moduleName)
    {
        return () -> {
            RuleEngineCronJobModel cronJob = newCronJob();
            cronJob.setSourceRules(rules);
            cronJob.setTargetModuleName(moduleName);
            return cronJob;
        };
    }


    public Supplier<RuleEngineCronJobModel> createSynchronizeSupplier(String srcModuleName, String targetModuleName)
    {
        return () -> {
            RuleEngineCronJobModel cronJob = newCronJob();
            cronJob.setSrcModuleName(srcModuleName);
            cronJob.setTargetModuleName(targetModuleName);
            return cronJob;
        };
    }


    public Supplier<RuleEngineCronJobModel> createModuleInitializationSupplier(String moduleName)
    {
        return () -> {
            RuleEngineCronJobModel cronJob = newCronJob();
            cronJob.setTargetModuleName(moduleName);
            return cronJob;
        };
    }


    public Supplier<RuleEngineCronJobModel> createAllModulesInitializationSupplier()
    {
        return this::newCronJob;
    }


    protected RuleEngineCronJobModel newCronJob()
    {
        RuleEngineCronJobModel cronJob = new RuleEngineCronJobModel();
        if(getClusterService().isClusteringEnabled())
        {
            cronJob.setNodeID(getNodeId());
            cronJob.setNodeGroup(getNodeGroup());
        }
        return cronJob;
    }


    protected Integer getNodeId()
    {
        return this.nodeId;
    }


    public void setNodeId(Integer nodeId)
    {
        this.nodeId = nodeId;
    }


    protected String getNodeGroup()
    {
        return this.nodeGroup;
    }


    public void setNodeGroup(String nodeGroup)
    {
        this.nodeGroup = nodeGroup;
    }


    protected ClusterService getClusterService()
    {
        return this.clusterService;
    }


    @Required
    public void setClusterService(ClusterService clusterService)
    {
        this.clusterService = clusterService;
    }
}
