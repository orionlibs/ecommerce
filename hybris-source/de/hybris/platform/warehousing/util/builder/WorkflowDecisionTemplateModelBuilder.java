package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import java.util.Locale;

public class WorkflowDecisionTemplateModelBuilder
{
    private final WorkflowDecisionTemplateModel model = new WorkflowDecisionTemplateModel();


    private WorkflowDecisionTemplateModel getModel()
    {
        return this.model;
    }


    public static WorkflowDecisionTemplateModelBuilder aModel()
    {
        return new WorkflowDecisionTemplateModelBuilder();
    }


    public WorkflowDecisionTemplateModel build()
    {
        return getModel();
    }


    public WorkflowDecisionTemplateModelBuilder withCode(String code)
    {
        getModel().setCode(code);
        return this;
    }


    public WorkflowDecisionTemplateModelBuilder withName(String name)
    {
        getModel().setName(name, Locale.ENGLISH);
        return this;
    }
}
