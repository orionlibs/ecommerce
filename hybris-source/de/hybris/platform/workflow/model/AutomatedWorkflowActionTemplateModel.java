package de.hybris.platform.workflow.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.enums.WorkflowActionType;

public class AutomatedWorkflowActionTemplateModel extends WorkflowActionTemplateModel
{
    public static final String _TYPECODE = "AutomatedWorkflowActionTemplate";
    public static final String JOBCLASS = "jobClass";
    public static final String JOBHANDLER = "jobHandler";


    public AutomatedWorkflowActionTemplateModel()
    {
    }


    public AutomatedWorkflowActionTemplateModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AutomatedWorkflowActionTemplateModel(WorkflowActionType _actionType, PrincipalModel _principalAssigned, WorkflowTemplateModel _workflow)
    {
        setActionType(_actionType);
        setPrincipalAssigned(_principalAssigned);
        setWorkflow(_workflow);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AutomatedWorkflowActionTemplateModel(WorkflowActionType _actionType, String _code, UserModel _owner, PrincipalModel _principalAssigned, WorkflowTemplateModel _workflow)
    {
        setActionType(_actionType);
        setCode(_code);
        setOwner((ItemModel)_owner);
        setPrincipalAssigned(_principalAssigned);
        setWorkflow(_workflow);
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "jobClass", type = Accessor.Type.GETTER)
    public Class getJobClass()
    {
        return (Class)getPersistenceContext().getPropertyValue("jobClass");
    }


    @Accessor(qualifier = "jobHandler", type = Accessor.Type.GETTER)
    public String getJobHandler()
    {
        return (String)getPersistenceContext().getPropertyValue("jobHandler");
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "jobClass", type = Accessor.Type.SETTER)
    public void setJobClass(Class value)
    {
        getPersistenceContext().setPropertyValue("jobClass", value);
    }


    @Accessor(qualifier = "jobHandler", type = Accessor.Type.SETTER)
    public void setJobHandler(String value)
    {
        getPersistenceContext().setPropertyValue("jobHandler", value);
    }
}
