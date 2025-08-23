package de.hybris.platform.ruleengineservices.jobs.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengineservices.jobs.RuleEngineJobExecutionSynchronizer;
import de.hybris.platform.ruleengineservices.model.RuleEngineCronJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleEngineJobExecutionSynchronizer implements RuleEngineJobExecutionSynchronizer
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultRuleEngineJobExecutionSynchronizer.class);
    private RulesModuleDao rulesModuleDao;
    private ModelService modelService;
    private final Lock lock = new ReentrantLock();


    public boolean acquireLock(RuleEngineCronJobModel cronJob)
    {
        try
        {
            this.lock.lock();
            List<AbstractRulesModuleModel> modules = getRulesModules(cronJob);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Trying to acquire lock on rule modules [{}] for cron job [{}]", ruleModulesAsString(modules), cronJob
                                .getCode());
            }
            boolean isLockAlreadyAcquired = modules.stream().anyMatch(m -> BooleanUtils.toBoolean(m.getLockAcquired()));
            if(isLockAlreadyAcquired)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Not able to acquire all necessary locks for the cron job [{}]", cronJob.getCode());
                }
                return false;
            }
            setLockAcquired(cronJob, modules, true);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Acquired locks on rule modules [{}] for the cron job [{}]", ruleModulesAsString(modules), cronJob.getCode());
            }
            return true;
        }
        finally
        {
            this.lock.unlock();
        }
    }


    public void releaseLock(RuleEngineCronJobModel cronJob)
    {
        try
        {
            this.lock.lock();
            List<AbstractRulesModuleModel> modules = getRulesModules(cronJob);
            setLockAcquired(cronJob, modules, false);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Released locks on rule modules [{}] for the cron job [{}]", ruleModulesAsString(modules), cronJob.getCode());
            }
        }
        finally
        {
            this.lock.unlock();
        }
    }


    protected void setLockAcquired(RuleEngineCronJobModel cronJob, List<AbstractRulesModuleModel> modules, boolean value)
    {
        modules.forEach(m -> m.setLockAcquired(Boolean.valueOf(value)));
        cronJob.setLockAcquired(Boolean.valueOf(value));
        getModelService().saveAll(Lists.asList(cronJob, modules.toArray()));
    }


    protected List<AbstractRulesModuleModel> getRulesModules(RuleEngineCronJobModel cronJob)
    {
        List<AbstractRulesModuleModel> modules = new ArrayList<>();
        if(StringUtils.isNotEmpty(cronJob.getSrcModuleName()))
        {
            modules.add(getRulesModuleDao().findByName(cronJob.getSrcModuleName()));
        }
        modules.addAll(getRulesModules(cronJob.getTargetModuleName()));
        return modules;
    }


    protected List<AbstractRulesModuleModel> getRulesModules(String moduleName)
    {
        return StringUtils.isEmpty(moduleName) ? getRulesModuleDao().findAll() : Collections.<AbstractRulesModuleModel>singletonList(getRulesModuleDao().findByName(moduleName));
    }


    protected String ruleModulesAsString(List<AbstractRulesModuleModel> modules)
    {
        return modules.stream().map(AbstractRulesModuleModel::getName).collect(Collectors.joining(", "));
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


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
