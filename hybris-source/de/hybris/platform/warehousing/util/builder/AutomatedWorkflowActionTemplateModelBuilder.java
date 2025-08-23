package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.AutomatedWorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collection;
import java.util.Locale;

public class AutomatedWorkflowActionTemplateModelBuilder
{
    private final AutomatedWorkflowActionTemplateModel model = new AutomatedWorkflowActionTemplateModel();


    private AutomatedWorkflowActionTemplateModel getModel()
    {
        return this.model;
    }


    public static AutomatedWorkflowActionTemplateModelBuilder aModel()
    {
        return new AutomatedWorkflowActionTemplateModelBuilder();
    }


    public AutomatedWorkflowActionTemplateModel build()
    {
        return getModel();
    }


    public AutomatedWorkflowActionTemplateModelBuilder withCode(String code)
    {
        getModel().setCode(code);
        return this;
    }


    public AutomatedWorkflowActionTemplateModelBuilder withName(String name)
    {
        getModel().setName(name, Locale.ENGLISH);
        return this;
    }


    public AutomatedWorkflowActionTemplateModelBuilder withPrincipal(PrincipalModel principal)
    {
        getModel().setPrincipalAssigned(principal);
        return this;
    }


    public AutomatedWorkflowActionTemplateModelBuilder withDecision(Collection<WorkflowDecisionTemplateModel> decisions)
    {
        getModel().setDecisionTemplates(decisions);
        return this;
    }


    public AutomatedWorkflowActionTemplateModelBuilder withAction(WorkflowActionType action)
    {
        getModel().setActionType(action);
        return this;
    }


    public AutomatedWorkflowActionTemplateModelBuilder withJobHandler(String jobHandler)
    {
        getModel().setJobHandler(jobHandler);
        return this;
    }


    public AutomatedWorkflowActionTemplateModelBuilder withWorkflow(WorkflowTemplateModel workflow)
    {
        getModel().setWorkflow(workflow);
        return this;
    }
}
