package de.hybris.platform.ruleengineservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

public class RuleEngineCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "RuleEngineCronJob";
    public static final String SOURCERULES = "sourceRules";
    public static final String SRCMODULENAME = "srcModuleName";
    public static final String TARGETMODULENAME = "targetModuleName";
    public static final String ENABLEINCREMENTALUPDATE = "enableIncrementalUpdate";
    public static final String LOCKACQUIRED = "lockAcquired";


    public RuleEngineCronJobModel()
    {
    }


    public RuleEngineCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RuleEngineCronJobModel(JobModel _job)
    {
        setJob(_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public RuleEngineCronJobModel(JobModel _job, ItemModel _owner)
    {
        setJob(_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "enableIncrementalUpdate", type = Accessor.Type.GETTER)
    public Boolean getEnableIncrementalUpdate()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("enableIncrementalUpdate");
    }


    @Accessor(qualifier = "lockAcquired", type = Accessor.Type.GETTER)
    public Boolean getLockAcquired()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("lockAcquired");
    }


    @Accessor(qualifier = "sourceRules", type = Accessor.Type.GETTER)
    public List<SourceRuleModel> getSourceRules()
    {
        return (List<SourceRuleModel>)getPersistenceContext().getPropertyValue("sourceRules");
    }


    @Accessor(qualifier = "srcModuleName", type = Accessor.Type.GETTER)
    public String getSrcModuleName()
    {
        return (String)getPersistenceContext().getPropertyValue("srcModuleName");
    }


    @Accessor(qualifier = "targetModuleName", type = Accessor.Type.GETTER)
    public String getTargetModuleName()
    {
        return (String)getPersistenceContext().getPropertyValue("targetModuleName");
    }


    @Accessor(qualifier = "enableIncrementalUpdate", type = Accessor.Type.SETTER)
    public void setEnableIncrementalUpdate(Boolean value)
    {
        getPersistenceContext().setPropertyValue("enableIncrementalUpdate", value);
    }


    @Accessor(qualifier = "lockAcquired", type = Accessor.Type.SETTER)
    public void setLockAcquired(Boolean value)
    {
        getPersistenceContext().setPropertyValue("lockAcquired", value);
    }


    @Accessor(qualifier = "sourceRules", type = Accessor.Type.SETTER)
    public void setSourceRules(List<SourceRuleModel> value)
    {
        getPersistenceContext().setPropertyValue("sourceRules", value);
    }


    @Accessor(qualifier = "srcModuleName", type = Accessor.Type.SETTER)
    public void setSrcModuleName(String value)
    {
        getPersistenceContext().setPropertyValue("srcModuleName", value);
    }


    @Accessor(qualifier = "targetModuleName", type = Accessor.Type.SETTER)
    public void setTargetModuleName(String value)
    {
        getPersistenceContext().setPropertyValue("targetModuleName", value);
    }
}
