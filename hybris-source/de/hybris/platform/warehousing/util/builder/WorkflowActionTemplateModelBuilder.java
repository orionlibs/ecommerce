package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collection;
import java.util.Locale;

public class WorkflowActionTemplateModelBuilder
{
    private final WorkflowActionTemplateModel model = new WorkflowActionTemplateModel();


    private WorkflowActionTemplateModel getModel()
    {
        return this.model;
    }


    public static WorkflowActionTemplateModelBuilder aModel()
    {
        return new WorkflowActionTemplateModelBuilder();
    }


    public WorkflowActionTemplateModel build()
    {
        return getModel();
    }


    public WorkflowActionTemplateModelBuilder withCode(String code)
    {
        getModel().setCode(code);
        return this;
    }


    public WorkflowActionTemplateModelBuilder withName(String name)
    {
        getModel().setName(name, Locale.ENGLISH);
        return this;
    }


    public WorkflowActionTemplateModelBuilder withPrincipal(PrincipalModel principal)
    {
        getModel().setPrincipalAssigned(principal);
        return this;
    }


    public WorkflowActionTemplateModelBuilder withAction(WorkflowActionType action)
    {
        getModel().setActionType(action);
        return this;
    }


    public WorkflowActionTemplateModelBuilder withWorkflow(WorkflowTemplateModel workflow)
    {
        getModel().setWorkflow(workflow);
        return this;
    }


    public WorkflowActionTemplateModelBuilder withDecision(Collection<WorkflowDecisionTemplateModel> decisions)
    {
        getModel().setDecisionTemplates(decisions);
        return this;
    }
}
