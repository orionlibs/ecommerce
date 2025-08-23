package de.hybris.platform.workflow.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class WorkflowDecisionModel extends AbstractWorkflowDecisionModel
{
    public static final String _TYPECODE = "WorkflowDecision";
    public static final String _WORKFLOWACTIONDECISIONSRELATION = "WorkflowActionDecisionsRelation";
    public static final String ACTION = "action";
    public static final String TOACTIONS = "toActions";


    public WorkflowDecisionModel()
    {
    }


    public WorkflowDecisionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public WorkflowDecisionModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "action", type = Accessor.Type.GETTER)
    public WorkflowActionModel getAction()
    {
        return (WorkflowActionModel)getPersistenceContext().getPropertyValue("action");
    }


    @Accessor(qualifier = "toActions", type = Accessor.Type.GETTER)
    public Collection<WorkflowActionModel> getToActions()
    {
        return (Collection<WorkflowActionModel>)getPersistenceContext().getPropertyValue("toActions");
    }


    @Accessor(qualifier = "action", type = Accessor.Type.SETTER)
    public void setAction(WorkflowActionModel value)
    {
        getPersistenceContext().setPropertyValue("action", value);
    }


    @Accessor(qualifier = "toActions", type = Accessor.Type.SETTER)
    public void setToActions(Collection<WorkflowActionModel> value)
    {
        getPersistenceContext().setPropertyValue("toActions", value);
    }
}
