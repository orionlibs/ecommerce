package de.hybris.platform.workflow.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class WorkflowActionModel extends AbstractWorkflowActionModel
{
    public static final String _TYPECODE = "WorkflowAction";
    public static final String _WORKFLOWACTIONORDERINGRELATION = "WorkflowActionOrderingRelation";
    public static final String _WORKFLOWACTIONRELATION = "WorkflowActionRelation";
    public static final String _WORKFLOWACTIONLINKRELATION = "WorkflowActionLinkRelation";
    public static final String INCOMINGLINKS = "incomingLinks";
    public static final String INCOMINGLINKSSTR = "incomingLinksStr";
    public static final String SELECTEDDECISION = "selectedDecision";
    public static final String FIRSTACTIVATED = "firstActivated";
    public static final String ACTIVATED = "activated";
    public static final String COMMENT = "comment";
    public static final String STATUS = "status";
    public static final String TEMPLATE = "template";
    public static final String ATTACHMENTITEMS = "attachmentItems";
    public static final String WORKFLOWPOS = "workflowPOS";
    public static final String WORKFLOW = "workflow";
    public static final String DECISIONS = "decisions";
    public static final String INCOMINGDECISIONS = "incomingDecisions";
    public static final String ATTACHMENTS = "attachments";


    public WorkflowActionModel()
    {
    }


    public WorkflowActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public WorkflowActionModel(WorkflowActionType _actionType, PrincipalModel _principalAssigned, WorkflowActionTemplateModel _template, WorkflowModel _workflow)
    {
        setActionType(_actionType);
        setPrincipalAssigned(_principalAssigned);
        setTemplate(_template);
        setWorkflow(_workflow);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public WorkflowActionModel(WorkflowActionType _actionType, String _code, UserModel _owner, PrincipalModel _principalAssigned, WorkflowActionTemplateModel _template, WorkflowModel _workflow)
    {
        setActionType(_actionType);
        setCode(_code);
        setOwner((ItemModel)_owner);
        setPrincipalAssigned(_principalAssigned);
        setTemplate(_template);
        setWorkflow(_workflow);
    }


    @Accessor(qualifier = "activated", type = Accessor.Type.GETTER)
    public Date getActivated()
    {
        return (Date)getPersistenceContext().getPropertyValue("activated");
    }


    @Accessor(qualifier = "attachmentItems", type = Accessor.Type.GETTER)
    public List<ItemModel> getAttachmentItems()
    {
        return (List<ItemModel>)getPersistenceContext().getPropertyValue("attachmentItems");
    }


    @Accessor(qualifier = "attachments", type = Accessor.Type.GETTER)
    public List<WorkflowItemAttachmentModel> getAttachments()
    {
        return (List<WorkflowItemAttachmentModel>)getPersistenceContext().getPropertyValue("attachments");
    }


    @Accessor(qualifier = "comment", type = Accessor.Type.GETTER)
    public String getComment()
    {
        return (String)getPersistenceContext().getPropertyValue("comment");
    }


    @Accessor(qualifier = "decisions", type = Accessor.Type.GETTER)
    public Collection<WorkflowDecisionModel> getDecisions()
    {
        return (Collection<WorkflowDecisionModel>)getPersistenceContext().getPropertyValue("decisions");
    }


    @Accessor(qualifier = "firstActivated", type = Accessor.Type.GETTER)
    public Date getFirstActivated()
    {
        return (Date)getPersistenceContext().getPropertyValue("firstActivated");
    }


    @Accessor(qualifier = "incomingDecisions", type = Accessor.Type.GETTER)
    public Collection<WorkflowDecisionModel> getIncomingDecisions()
    {
        return (Collection<WorkflowDecisionModel>)getPersistenceContext().getPropertyValue("incomingDecisions");
    }


    @Accessor(qualifier = "incomingLinks", type = Accessor.Type.GETTER)
    public List<LinkModel> getIncomingLinks()
    {
        return (List<LinkModel>)getPersistenceContext().getPropertyValue("incomingLinks");
    }


    @Accessor(qualifier = "incomingLinksStr", type = Accessor.Type.GETTER)
    public String getIncomingLinksStr()
    {
        return (String)getPersistenceContext().getPropertyValue("incomingLinksStr");
    }


    @Accessor(qualifier = "selectedDecision", type = Accessor.Type.GETTER)
    public WorkflowDecisionModel getSelectedDecision()
    {
        return (WorkflowDecisionModel)getPersistenceContext().getPropertyValue("selectedDecision");
    }


    @Accessor(qualifier = "status", type = Accessor.Type.GETTER)
    public WorkflowActionStatus getStatus()
    {
        return (WorkflowActionStatus)getPersistenceContext().getPropertyValue("status");
    }


    @Accessor(qualifier = "template", type = Accessor.Type.GETTER)
    public WorkflowActionTemplateModel getTemplate()
    {
        return (WorkflowActionTemplateModel)getPersistenceContext().getPropertyValue("template");
    }


    @Accessor(qualifier = "workflow", type = Accessor.Type.GETTER)
    public WorkflowModel getWorkflow()
    {
        return (WorkflowModel)getPersistenceContext().getPropertyValue("workflow");
    }


    @Accessor(qualifier = "activated", type = Accessor.Type.SETTER)
    public void setActivated(Date value)
    {
        getPersistenceContext().setPropertyValue("activated", value);
    }


    @Accessor(qualifier = "attachments", type = Accessor.Type.SETTER)
    public void setAttachments(List<WorkflowItemAttachmentModel> value)
    {
        getPersistenceContext().setPropertyValue("attachments", value);
    }


    @Accessor(qualifier = "comment", type = Accessor.Type.SETTER)
    public void setComment(String value)
    {
        getPersistenceContext().setPropertyValue("comment", value);
    }


    @Accessor(qualifier = "decisions", type = Accessor.Type.SETTER)
    public void setDecisions(Collection<WorkflowDecisionModel> value)
    {
        getPersistenceContext().setPropertyValue("decisions", value);
    }


    @Accessor(qualifier = "firstActivated", type = Accessor.Type.SETTER)
    public void setFirstActivated(Date value)
    {
        getPersistenceContext().setPropertyValue("firstActivated", value);
    }


    @Accessor(qualifier = "incomingDecisions", type = Accessor.Type.SETTER)
    public void setIncomingDecisions(Collection<WorkflowDecisionModel> value)
    {
        getPersistenceContext().setPropertyValue("incomingDecisions", value);
    }


    @Accessor(qualifier = "selectedDecision", type = Accessor.Type.SETTER)
    public void setSelectedDecision(WorkflowDecisionModel value)
    {
        getPersistenceContext().setPropertyValue("selectedDecision", value);
    }


    @Accessor(qualifier = "status", type = Accessor.Type.SETTER)
    public void setStatus(WorkflowActionStatus value)
    {
        getPersistenceContext().setPropertyValue("status", value);
    }


    @Accessor(qualifier = "template", type = Accessor.Type.SETTER)
    public void setTemplate(WorkflowActionTemplateModel value)
    {
        getPersistenceContext().setPropertyValue("template", value);
    }


    @Accessor(qualifier = "workflow", type = Accessor.Type.SETTER)
    public void setWorkflow(WorkflowModel value)
    {
        getPersistenceContext().setPropertyValue("workflow", value);
    }
}
