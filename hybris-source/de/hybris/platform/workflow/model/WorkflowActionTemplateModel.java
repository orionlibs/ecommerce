package de.hybris.platform.workflow.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import java.util.Collection;
import java.util.List;

public class WorkflowActionTemplateModel extends AbstractWorkflowActionModel
{
    public static final String _TYPECODE = "WorkflowActionTemplate";
    public static final String _WORKFLOWACTIONORDERINGRELATION = "WorkflowActionOrderingRelation";
    public static final String _WORKFLOWTEMPLATEACTIONTEMPLATERELATION = "WorkflowTemplateActionTemplateRelation";
    public static final String _WORKFLOWACTIONTEMPLATELINKTEMPLATERELATION = "WorkflowActionTemplateLinkTemplateRelation";
    public static final String INCOMINGLINKTEMPLATES = "incomingLinkTemplates";
    public static final String INCOMINGLINKTEMPLATESSTR = "incomingLinkTemplatesStr";
    public static final String CREATIONTYPE = "creationType";
    public static final String WORKFLOWPOS = "workflowPOS";
    public static final String WORKFLOW = "workflow";
    public static final String DECISIONTEMPLATES = "decisionTemplates";
    public static final String INCOMINGTEMPLATEDECISIONS = "incomingTemplateDecisions";


    public WorkflowActionTemplateModel()
    {
    }


    public WorkflowActionTemplateModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public WorkflowActionTemplateModel(WorkflowActionType _actionType, PrincipalModel _principalAssigned, WorkflowTemplateModel _workflow)
    {
        setActionType(_actionType);
        setPrincipalAssigned(_principalAssigned);
        setWorkflow(_workflow);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public WorkflowActionTemplateModel(WorkflowActionType _actionType, String _code, UserModel _owner, PrincipalModel _principalAssigned, WorkflowTemplateModel _workflow)
    {
        setActionType(_actionType);
        setCode(_code);
        setOwner((ItemModel)_owner);
        setPrincipalAssigned(_principalAssigned);
        setWorkflow(_workflow);
    }


    @Accessor(qualifier = "creationType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getCreationType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("creationType");
    }


    @Accessor(qualifier = "decisionTemplates", type = Accessor.Type.GETTER)
    public Collection<WorkflowDecisionTemplateModel> getDecisionTemplates()
    {
        return (Collection<WorkflowDecisionTemplateModel>)getPersistenceContext().getPropertyValue("decisionTemplates");
    }


    @Accessor(qualifier = "incomingLinkTemplates", type = Accessor.Type.GETTER)
    public List<LinkModel> getIncomingLinkTemplates()
    {
        return (List<LinkModel>)getPersistenceContext().getPropertyValue("incomingLinkTemplates");
    }


    @Accessor(qualifier = "incomingLinkTemplatesStr", type = Accessor.Type.GETTER)
    public String getIncomingLinkTemplatesStr()
    {
        return (String)getPersistenceContext().getPropertyValue("incomingLinkTemplatesStr");
    }


    @Accessor(qualifier = "incomingTemplateDecisions", type = Accessor.Type.GETTER)
    public Collection<WorkflowDecisionTemplateModel> getIncomingTemplateDecisions()
    {
        return (Collection<WorkflowDecisionTemplateModel>)getPersistenceContext().getPropertyValue("incomingTemplateDecisions");
    }


    @Accessor(qualifier = "workflow", type = Accessor.Type.GETTER)
    public WorkflowTemplateModel getWorkflow()
    {
        return (WorkflowTemplateModel)getPersistenceContext().getPropertyValue("workflow");
    }


    @Accessor(qualifier = "creationType", type = Accessor.Type.SETTER)
    public void setCreationType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("creationType", value);
    }


    @Accessor(qualifier = "decisionTemplates", type = Accessor.Type.SETTER)
    public void setDecisionTemplates(Collection<WorkflowDecisionTemplateModel> value)
    {
        getPersistenceContext().setPropertyValue("decisionTemplates", value);
    }


    @Accessor(qualifier = "incomingTemplateDecisions", type = Accessor.Type.SETTER)
    public void setIncomingTemplateDecisions(Collection<WorkflowDecisionTemplateModel> value)
    {
        getPersistenceContext().setPropertyValue("incomingTemplateDecisions", value);
    }


    @Accessor(qualifier = "workflow", type = Accessor.Type.SETTER)
    public void setWorkflow(WorkflowTemplateModel value)
    {
        getPersistenceContext().setPropertyValue("workflow", value);
    }
}
