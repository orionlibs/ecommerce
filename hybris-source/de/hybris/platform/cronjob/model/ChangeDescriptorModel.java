package de.hybris.platform.cronjob.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.Map;

public class ChangeDescriptorModel extends ItemModel
{
    public static final String _TYPECODE = "ChangeDescriptor";
    public static final String CRONJOB = "cronJob";
    public static final String STEP = "step";
    public static final String CHANGEDITEM = "changedItem";
    public static final String SEQUENCENUMBER = "sequenceNumber";
    public static final String SAVETIMESTAMP = "saveTimestamp";
    public static final String PREVIOUSITEMSTATE = "previousItemState";
    public static final String CHANGETYPE = "changeType";
    public static final String DESCRIPTION = "description";


    public ChangeDescriptorModel()
    {
    }


    public ChangeDescriptorModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ChangeDescriptorModel(String _changeType, CronJobModel _cronJob, Integer _sequenceNumber, StepModel _step)
    {
        setChangeType(_changeType);
        setCronJob(_cronJob);
        setSequenceNumber(_sequenceNumber);
        setStep(_step);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ChangeDescriptorModel(String _changeType, CronJobModel _cronJob, ItemModel _owner, Integer _sequenceNumber, StepModel _step)
    {
        setChangeType(_changeType);
        setCronJob(_cronJob);
        setOwner(_owner);
        setSequenceNumber(_sequenceNumber);
        setStep(_step);
    }


    @Accessor(qualifier = "changedItem", type = Accessor.Type.GETTER)
    public ItemModel getChangedItem()
    {
        return (ItemModel)getPersistenceContext().getPropertyValue("changedItem");
    }


    @Accessor(qualifier = "changeType", type = Accessor.Type.GETTER)
    public String getChangeType()
    {
        return (String)getPersistenceContext().getPropertyValue("changeType");
    }


    @Accessor(qualifier = "cronJob", type = Accessor.Type.GETTER)
    public CronJobModel getCronJob()
    {
        return (CronJobModel)getPersistenceContext().getPropertyValue("cronJob");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return (String)getPersistenceContext().getPropertyValue("description");
    }


    @Accessor(qualifier = "previousItemState", type = Accessor.Type.GETTER)
    public Map getPreviousItemState()
    {
        return (Map)getPersistenceContext().getPropertyValue("previousItemState");
    }


    @Accessor(qualifier = "saveTimestamp", type = Accessor.Type.GETTER)
    public Date getSaveTimestamp()
    {
        return (Date)getPersistenceContext().getPropertyValue("saveTimestamp");
    }


    @Accessor(qualifier = "sequenceNumber", type = Accessor.Type.GETTER)
    public Integer getSequenceNumber()
    {
        return (Integer)getPersistenceContext().getPropertyValue("sequenceNumber");
    }


    @Accessor(qualifier = "step", type = Accessor.Type.GETTER)
    public StepModel getStep()
    {
        return (StepModel)getPersistenceContext().getPropertyValue("step");
    }


    @Accessor(qualifier = "changedItem", type = Accessor.Type.SETTER)
    public void setChangedItem(ItemModel value)
    {
        getPersistenceContext().setPropertyValue("changedItem", value);
    }


    @Accessor(qualifier = "changeType", type = Accessor.Type.SETTER)
    public void setChangeType(String value)
    {
        getPersistenceContext().setPropertyValue("changeType", value);
    }


    @Accessor(qualifier = "cronJob", type = Accessor.Type.SETTER)
    public void setCronJob(CronJobModel value)
    {
        getPersistenceContext().setPropertyValue("cronJob", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        getPersistenceContext().setPropertyValue("description", value);
    }


    @Accessor(qualifier = "previousItemState", type = Accessor.Type.SETTER)
    public void setPreviousItemState(Map value)
    {
        getPersistenceContext().setPropertyValue("previousItemState", value);
    }


    @Accessor(qualifier = "saveTimestamp", type = Accessor.Type.SETTER)
    public void setSaveTimestamp(Date value)
    {
        getPersistenceContext().setPropertyValue("saveTimestamp", value);
    }


    @Accessor(qualifier = "sequenceNumber", type = Accessor.Type.SETTER)
    public void setSequenceNumber(Integer value)
    {
        getPersistenceContext().setPropertyValue("sequenceNumber", value);
    }


    @Accessor(qualifier = "step", type = Accessor.Type.SETTER)
    public void setStep(StepModel value)
    {
        getPersistenceContext().setPropertyValue("step", value);
    }
}
