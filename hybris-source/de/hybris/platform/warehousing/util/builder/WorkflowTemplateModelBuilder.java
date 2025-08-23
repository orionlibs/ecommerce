package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collection;
import java.util.Locale;

public class WorkflowTemplateModelBuilder
{
    private final WorkflowTemplateModel model = new WorkflowTemplateModel();


    private WorkflowTemplateModel getModel()
    {
        return this.model;
    }


    public static WorkflowTemplateModelBuilder aModel()
    {
        return new WorkflowTemplateModelBuilder();
    }


    public WorkflowTemplateModel build()
    {
        return getModel();
    }


    public WorkflowTemplateModelBuilder withCode(String code)
    {
        getModel().setCode(code);
        return this;
    }


    public WorkflowTemplateModelBuilder withName(String name)
    {
        getModel().setName(name, Locale.ENGLISH);
        return this;
    }


    public WorkflowTemplateModelBuilder withPrincipals(Collection<PrincipalModel> principals)
    {
        getModel().setVisibleForPrincipals(principals);
        return this;
    }
}
