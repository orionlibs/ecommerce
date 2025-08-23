package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CompositeEntryModel extends ItemModel
{
    public static final String _TYPECODE = "CompositeEntry";
    public static final String _COMPOSITECRONJOBENTRIESRELATION = "CompositeCronJobEntriesRelation";
    public static final String CODE = "code";
    public static final String EXECUTABLECRONJOB = "executableCronJob";
    public static final String TRIGGERABLEJOB = "triggerableJob";
    public static final String COMPOSITECRONJOBPOS = "compositeCronJobPOS";
    public static final String COMPOSITECRONJOB = "compositeCronJob";


    public CompositeEntryModel()
    {
    }


    public CompositeEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CompositeEntryModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CompositeEntryModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "compositeCronJob", type = Accessor.Type.GETTER)
    public CompositeCronJobModel getCompositeCronJob()
    {
        return (CompositeCronJobModel)getPersistenceContext().getPropertyValue("compositeCronJob");
    }


    @Accessor(qualifier = "executableCronJob", type = Accessor.Type.GETTER)
    public CronJobModel getExecutableCronJob()
    {
        return (CronJobModel)getPersistenceContext().getPropertyValue("executableCronJob");
    }


    @Accessor(qualifier = "triggerableJob", type = Accessor.Type.GETTER)
    public JobModel getTriggerableJob()
    {
        return (JobModel)getPersistenceContext().getPropertyValue("triggerableJob");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "compositeCronJob", type = Accessor.Type.SETTER)
    public void setCompositeCronJob(CompositeCronJobModel value)
    {
        getPersistenceContext().setPropertyValue("compositeCronJob", value);
    }


    @Accessor(qualifier = "executableCronJob", type = Accessor.Type.SETTER)
    public void setExecutableCronJob(CronJobModel value)
    {
        getPersistenceContext().setPropertyValue("executableCronJob", value);
    }


    @Accessor(qualifier = "triggerableJob", type = Accessor.Type.SETTER)
    public void setTriggerableJob(JobModel value)
    {
        getPersistenceContext().setPropertyValue("triggerableJob", value);
    }
}
