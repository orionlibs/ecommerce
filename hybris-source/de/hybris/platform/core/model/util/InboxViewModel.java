package de.hybris.platform.core.model.util;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;

public class InboxViewModel extends ItemModel
{
    public static final String _TYPECODE = "InboxView";
    public static final String ACTION = "action";
    public static final String ACTIVATED = "activated";
    public static final String ACTIONCOMMENT = "actioncomment";
    public static final String WORKFLOW = "workflow";
    public static final String STATUS = "status";


    public InboxViewModel()
    {
    }


    public InboxViewModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public InboxViewModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "action", type = Accessor.Type.GETTER)
    public WorkflowActionModel getAction()
    {
        return (WorkflowActionModel)getPersistenceContext().getPropertyValue("action");
    }


    @Accessor(qualifier = "actioncomment", type = Accessor.Type.GETTER)
    public String getActioncomment()
    {
        return (String)getPersistenceContext().getPropertyValue("actioncomment");
    }


    @Accessor(qualifier = "activated", type = Accessor.Type.GETTER)
    public String getActivated()
    {
        return (String)getPersistenceContext().getPropertyValue("activated");
    }


    @Accessor(qualifier = "status", type = Accessor.Type.GETTER)
    public WorkflowActionStatus getStatus()
    {
        return (WorkflowActionStatus)getPersistenceContext().getPropertyValue("status");
    }


    @Accessor(qualifier = "workflow", type = Accessor.Type.GETTER)
    public WorkflowModel getWorkflow()
    {
        return (WorkflowModel)getPersistenceContext().getPropertyValue("workflow");
    }
}
