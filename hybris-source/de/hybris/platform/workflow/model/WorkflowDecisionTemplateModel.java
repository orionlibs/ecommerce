package de.hybris.platform.workflow.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class WorkflowDecisionTemplateModel extends AbstractWorkflowDecisionModel
{
    public static final String _TYPECODE = "WorkflowDecisionTemplate";
    public static final String _WORKFLOWACTIONTEMPLATEDECISIONSTEMPLATERELATION = "WorkflowActionTemplateDecisionsTemplateRelation";
    public static final String PARENTWORKFLOWTEMPLATE = "parentWorkflowTemplate";
    public static final String ACTIONTEMPLATE = "actionTemplate";
    public static final String TOTEMPLATEACTIONS = "toTemplateActions";


    public WorkflowDecisionTemplateModel()
    {
    }


    public WorkflowDecisionTemplateModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public WorkflowDecisionTemplateModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "actionTemplate", type = Accessor.Type.GETTER)
    public WorkflowActionTemplateModel getActionTemplate()
    {
        return (WorkflowActionTemplateModel)getPersistenceContext().getPropertyValue("actionTemplate");
    }


    @Accessor(qualifier = "parentWorkflowTemplate", type = Accessor.Type.GETTER)
    public WorkflowTemplateModel getParentWorkflowTemplate()
    {
        return (WorkflowTemplateModel)getPersistenceContext().getPropertyValue("parentWorkflowTemplate");
    }


    @Accessor(qualifier = "toTemplateActions", type = Accessor.Type.GETTER)
    public Collection<WorkflowActionTemplateModel> getToTemplateActions()
    {
        return (Collection<WorkflowActionTemplateModel>)getPersistenceContext().getPropertyValue("toTemplateActions");
    }


    @Accessor(qualifier = "actionTemplate", type = Accessor.Type.SETTER)
    public void setActionTemplate(WorkflowActionTemplateModel value)
    {
        getPersistenceContext().setPropertyValue("actionTemplate", value);
    }


    @Accessor(qualifier = "toTemplateActions", type = Accessor.Type.SETTER)
    public void setToTemplateActions(Collection<WorkflowActionTemplateModel> value)
    {
        getPersistenceContext().setPropertyValue("toTemplateActions", value);
    }
}
