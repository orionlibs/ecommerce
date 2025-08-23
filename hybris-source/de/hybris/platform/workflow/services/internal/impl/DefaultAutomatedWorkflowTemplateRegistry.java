package de.hybris.platform.workflow.services.internal.impl;

import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.exceptions.AutomatedWorkflowTemplateException;
import de.hybris.platform.workflow.jalo.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.model.AutomatedWorkflowActionTemplateModel;
import de.hybris.platform.workflow.services.internal.AutomatedWorkflowTemplateRegistry;
import java.util.Map;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAutomatedWorkflowTemplateRegistry implements AutomatedWorkflowTemplateRegistry
{
    @Autowired
    private Map<String, AutomatedWorkflowTemplateJob> automatedWorkflows;
    private ModelService modelService;


    public AutomatedWorkflowTemplateJob getAutomatedWorkflowTemplateJobForTemplate(AutomatedWorkflowActionTemplateModel template) throws AutomatedWorkflowTemplateException
    {
        AutomatedWorkflowTemplateJob automatedWorkflow = null;
        String jobHandler = template.getJobHandler();
        if(GenericValidator.isBlankOrNull(jobHandler))
        {
            Class workflowClass = template.getJobClass();
            try
            {
                Object _automatedWorkflow = workflowClass.newInstance();
                if(_automatedWorkflow instanceof AutomatedWorkflowTemplateJob)
                {
                    AutomatedWorkflowTemplateJobWrapper automatedWorkflowTemplateJobWrapper = new AutomatedWorkflowTemplateJobWrapper(this, (AutomatedWorkflowTemplateJob)_automatedWorkflow, this.modelService);
                }
                else
                {
                    automatedWorkflow = (AutomatedWorkflowTemplateJob)_automatedWorkflow;
                }
            }
            catch(InstantiationException e)
            {
                throw new AutomatedWorkflowTemplateException("Cannot instantiate given class: " + workflowClass, e);
            }
            catch(IllegalAccessException e)
            {
                throw new AutomatedWorkflowTemplateException("Cannot instantiate given class: " + workflowClass, e);
            }
        }
        else
        {
            automatedWorkflow = this.automatedWorkflows.get(jobHandler);
        }
        if(automatedWorkflow == null)
        {
            throw new AutomatedWorkflowTemplateException("No automated workflow has been found for workflow template: " + template);
        }
        return automatedWorkflow;
    }


    public void setAutomatedWorkflows(Map<String, AutomatedWorkflowTemplateJob> automatedWorkflows)
    {
        this.automatedWorkflows = automatedWorkflows;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
