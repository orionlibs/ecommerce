package de.hybris.platform.workflow.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

public class WorkflowModel extends CronJobModel
{
    public static final String _TYPECODE = "Workflow";
    public static final String _JOBCRONJOBRELATION = "JobCronJobRelation";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String ACTIONS = "actions";
    public static final String ATTACHMENTS = "attachments";


    public WorkflowModel()
    {
    }


    public WorkflowModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public WorkflowModel(WorkflowTemplateModel _job)
    {
        setJob((JobModel)_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public WorkflowModel(WorkflowTemplateModel _job, UserModel _owner)
    {
        setJob((JobModel)_job);
        setOwner((ItemModel)_owner);
    }


    @Accessor(qualifier = "actions", type = Accessor.Type.GETTER)
    public List<WorkflowActionModel> getActions()
    {
        return (List<WorkflowActionModel>)getPersistenceContext().getPropertyValue("actions");
    }


    @Accessor(qualifier = "attachments", type = Accessor.Type.GETTER)
    public List<WorkflowItemAttachmentModel> getAttachments()
    {
        return (List<WorkflowItemAttachmentModel>)getPersistenceContext().getPropertyValue("attachments");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return getDescription(null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("description", loc);
    }


    @Accessor(qualifier = "job", type = Accessor.Type.GETTER)
    public WorkflowTemplateModel getJob()
    {
        return (WorkflowTemplateModel)super.getJob();
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "owner", type = Accessor.Type.GETTER)
    public UserModel getOwner()
    {
        return (UserModel)super.getOwner();
    }


    @Accessor(qualifier = "actions", type = Accessor.Type.SETTER)
    public void setActions(List<WorkflowActionModel> value)
    {
        getPersistenceContext().setPropertyValue("actions", value);
    }


    @Accessor(qualifier = "attachments", type = Accessor.Type.SETTER)
    public void setAttachments(List<WorkflowItemAttachmentModel> value)
    {
        getPersistenceContext().setPropertyValue("attachments", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        setDescription(value, null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("description", loc, value);
    }


    @Accessor(qualifier = "job", type = Accessor.Type.SETTER)
    public void setJob(JobModel value)
    {
        if(value == null || value instanceof WorkflowTemplateModel)
        {
            super.setJob(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.workflow.model.WorkflowTemplateModel");
        }
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "owner", type = Accessor.Type.SETTER)
    public void setOwner(ItemModel value)
    {
        if(value == null || value instanceof UserModel)
        {
            super.setOwner(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.core.model.user.UserModel");
        }
    }
}
