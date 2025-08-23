package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.ChangeDescriptorModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.StepModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ItemSyncDescriptorModel extends ChangeDescriptorModel
{
    public static final String _TYPECODE = "ItemSyncDescriptor";
    public static final String TARGETITEM = "targetItem";
    public static final String DONE = "done";
    public static final String COPIEDIMPLICITELY = "copiedImplicitely";


    public ItemSyncDescriptorModel()
    {
    }


    public ItemSyncDescriptorModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ItemSyncDescriptorModel(String _changeType, SyncItemCronJobModel _cronJob, Integer _sequenceNumber, StepModel _step)
    {
        setChangeType(_changeType);
        setCronJob((CronJobModel)_cronJob);
        setSequenceNumber(_sequenceNumber);
        setStep(_step);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ItemSyncDescriptorModel(String _changeType, SyncItemCronJobModel _cronJob, ItemModel _owner, Integer _sequenceNumber, StepModel _step)
    {
        setChangeType(_changeType);
        setCronJob((CronJobModel)_cronJob);
        setOwner(_owner);
        setSequenceNumber(_sequenceNumber);
        setStep(_step);
    }


    @Accessor(qualifier = "copiedImplicitely", type = Accessor.Type.GETTER)
    public Boolean getCopiedImplicitely()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("copiedImplicitely");
    }


    @Accessor(qualifier = "cronJob", type = Accessor.Type.GETTER)
    public SyncItemCronJobModel getCronJob()
    {
        return (SyncItemCronJobModel)super.getCronJob();
    }


    @Accessor(qualifier = "done", type = Accessor.Type.GETTER)
    public Boolean getDone()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("done");
    }


    @Accessor(qualifier = "targetItem", type = Accessor.Type.GETTER)
    public ItemModel getTargetItem()
    {
        return (ItemModel)getPersistenceContext().getPropertyValue("targetItem");
    }


    @Accessor(qualifier = "copiedImplicitely", type = Accessor.Type.SETTER)
    public void setCopiedImplicitely(Boolean value)
    {
        getPersistenceContext().setPropertyValue("copiedImplicitely", value);
    }


    @Accessor(qualifier = "cronJob", type = Accessor.Type.SETTER)
    public void setCronJob(CronJobModel value)
    {
        if(value == null || value instanceof SyncItemCronJobModel)
        {
            super.setCronJob(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.catalog.model.SyncItemCronJobModel");
        }
    }


    @Accessor(qualifier = "done", type = Accessor.Type.SETTER)
    public void setDone(Boolean value)
    {
        getPersistenceContext().setPropertyValue("done", value);
    }


    @Accessor(qualifier = "targetItem", type = Accessor.Type.SETTER)
    public void setTargetItem(ItemModel value)
    {
        getPersistenceContext().setPropertyValue("targetItem", value);
    }
}
