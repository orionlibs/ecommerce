package de.hybris.platform.workflow.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class AbstractWorkflowActionModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractWorkflowAction";
    public static final String _WORKFLOWACTIONORDERINGRELATION = "WorkflowActionOrderingRelation";
    public static final String ACTIONTYPE = "actionType";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String PRINCIPALASSIGNED = "principalAssigned";
    public static final String SENDEMAIL = "sendEmail";
    public static final String EMAILADDRESS = "emailAddress";
    public static final String PREDECESSORSSTR = "predecessorsStr";
    public static final String RENDERERTEMPLATE = "rendererTemplate";
    public static final String PREDECESSORS = "predecessors";
    public static final String SUCCESSORS = "successors";
    public static final String WORKFLOWACTIONCOMMENTS = "workflowActionComments";
    public static final String VISUALISATIONX = "visualisationX";
    public static final String VISUALISATIONY = "visualisationY";
    public static final String QUALIFIER = "qualifier";


    public AbstractWorkflowActionModel()
    {
    }


    public AbstractWorkflowActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractWorkflowActionModel(WorkflowActionType _actionType, PrincipalModel _principalAssigned)
    {
        setActionType(_actionType);
        setPrincipalAssigned(_principalAssigned);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractWorkflowActionModel(WorkflowActionType _actionType, String _code, UserModel _owner, PrincipalModel _principalAssigned)
    {
        setActionType(_actionType);
        setCode(_code);
        setOwner((ItemModel)_owner);
        setPrincipalAssigned(_principalAssigned);
    }


    @Accessor(qualifier = "actionType", type = Accessor.Type.GETTER)
    public WorkflowActionType getActionType()
    {
        return (WorkflowActionType)getPersistenceContext().getPropertyValue("actionType");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
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


    @Accessor(qualifier = "emailAddress", type = Accessor.Type.GETTER)
    public String getEmailAddress()
    {
        return (String)getPersistenceContext().getPropertyValue("emailAddress");
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


    @Accessor(qualifier = "predecessors", type = Accessor.Type.GETTER)
    public List<AbstractWorkflowActionModel> getPredecessors()
    {
        return (List<AbstractWorkflowActionModel>)getPersistenceContext().getPropertyValue("predecessors");
    }


    @Accessor(qualifier = "predecessorsStr", type = Accessor.Type.GETTER)
    public String getPredecessorsStr()
    {
        return (String)getPersistenceContext().getPropertyValue("predecessorsStr");
    }


    @Accessor(qualifier = "principalAssigned", type = Accessor.Type.GETTER)
    public PrincipalModel getPrincipalAssigned()
    {
        return (PrincipalModel)getPersistenceContext().getPropertyValue("principalAssigned");
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.GETTER)
    public String getQualifier()
    {
        return (String)getPersistenceContext().getPropertyValue("qualifier");
    }


    @Accessor(qualifier = "rendererTemplate", type = Accessor.Type.GETTER)
    public RendererTemplateModel getRendererTemplate()
    {
        return (RendererTemplateModel)getPersistenceContext().getPropertyValue("rendererTemplate");
    }


    @Accessor(qualifier = "sendEmail", type = Accessor.Type.GETTER)
    public Boolean getSendEmail()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("sendEmail");
    }


    @Accessor(qualifier = "successors", type = Accessor.Type.GETTER)
    public List<AbstractWorkflowActionModel> getSuccessors()
    {
        return (List<AbstractWorkflowActionModel>)getPersistenceContext().getPropertyValue("successors");
    }


    @Accessor(qualifier = "visualisationX", type = Accessor.Type.GETTER)
    public Integer getVisualisationX()
    {
        return (Integer)getPersistenceContext().getPropertyValue("visualisationX");
    }


    @Accessor(qualifier = "visualisationY", type = Accessor.Type.GETTER)
    public Integer getVisualisationY()
    {
        return (Integer)getPersistenceContext().getPropertyValue("visualisationY");
    }


    @Accessor(qualifier = "workflowActionComments", type = Accessor.Type.GETTER)
    public Collection<WorkflowActionCommentModel> getWorkflowActionComments()
    {
        return (Collection<WorkflowActionCommentModel>)getPersistenceContext().getPropertyValue("workflowActionComments");
    }


    @Accessor(qualifier = "actionType", type = Accessor.Type.SETTER)
    public void setActionType(WorkflowActionType value)
    {
        getPersistenceContext().setPropertyValue("actionType", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
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


    @Accessor(qualifier = "emailAddress", type = Accessor.Type.SETTER)
    public void setEmailAddress(String value)
    {
        getPersistenceContext().setPropertyValue("emailAddress", value);
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


    @Accessor(qualifier = "predecessors", type = Accessor.Type.SETTER)
    public void setPredecessors(List<AbstractWorkflowActionModel> value)
    {
        getPersistenceContext().setPropertyValue("predecessors", value);
    }


    @Accessor(qualifier = "principalAssigned", type = Accessor.Type.SETTER)
    public void setPrincipalAssigned(PrincipalModel value)
    {
        getPersistenceContext().setPropertyValue("principalAssigned", value);
    }


    @Accessor(qualifier = "qualifier", type = Accessor.Type.SETTER)
    public void setQualifier(String value)
    {
        getPersistenceContext().setPropertyValue("qualifier", value);
    }


    @Accessor(qualifier = "rendererTemplate", type = Accessor.Type.SETTER)
    public void setRendererTemplate(RendererTemplateModel value)
    {
        getPersistenceContext().setPropertyValue("rendererTemplate", value);
    }


    @Accessor(qualifier = "sendEmail", type = Accessor.Type.SETTER)
    public void setSendEmail(Boolean value)
    {
        getPersistenceContext().setPropertyValue("sendEmail", value);
    }


    @Accessor(qualifier = "successors", type = Accessor.Type.SETTER)
    public void setSuccessors(List<AbstractWorkflowActionModel> value)
    {
        getPersistenceContext().setPropertyValue("successors", value);
    }


    @Accessor(qualifier = "visualisationX", type = Accessor.Type.SETTER)
    public void setVisualisationX(Integer value)
    {
        getPersistenceContext().setPropertyValue("visualisationX", value);
    }


    @Accessor(qualifier = "visualisationY", type = Accessor.Type.SETTER)
    public void setVisualisationY(Integer value)
    {
        getPersistenceContext().setPropertyValue("visualisationY", value);
    }


    @Accessor(qualifier = "workflowActionComments", type = Accessor.Type.SETTER)
    public void setWorkflowActionComments(Collection<WorkflowActionCommentModel> value)
    {
        getPersistenceContext().setPropertyValue("workflowActionComments", value);
    }
}
