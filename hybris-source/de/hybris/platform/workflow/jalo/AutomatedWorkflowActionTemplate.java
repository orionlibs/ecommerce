package de.hybris.platform.workflow.jalo;

import de.hybris.platform.core.Registry;
import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public class AutomatedWorkflowActionTemplate extends GeneratedAutomatedWorkflowActionTemplate
{
    private static final Logger LOG = Logger.getLogger(AutomatedWorkflowActionTemplate.class.getName());


    protected void perform(WorkflowAction action)
    {
        try
        {
            Class automatedActionClass = getJobClass();
            Object obj = null;
            if(automatedActionClass != null)
            {
                obj = automatedActionClass.newInstance();
            }
            else if(!GenericValidator.isBlankOrNull(getJobHandler()))
            {
                try
                {
                    obj = getJobHnadlerBean();
                }
                catch(NoSuchBeanDefinitionException e)
                {
                    LOG.info("No job for current " + action.getName() + " defined.");
                }
            }
            else
            {
                LOG.info("No job for current " + action.getName() + " defined.");
            }
            String className = (getJobClass() != null) ? getJobClass().getName() : getJobHandler();
            if(obj instanceof AutomatedWorkflowTemplateJob)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("calling perform on action object");
                }
                writeAutomatedComment(action, "text.automatedworkflowactionTemplate.perform.start", new String[] {className});
                WorkflowDecision decision = ((AutomatedWorkflowTemplateJob)obj).perform(action);
                writeAutomatedComment(action, "text.automatedworkflowactionTemplate.perform.end", new String[] {className});
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("finish perform on action object");
                }
                if(decision != null)
                {
                    action.setSelectedDecision(decision);
                    action.decide();
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("decide for next workflow action");
                    }
                }
            }
            else
            {
                writeAutomatedComment(action, "text.automatedworkflowactionTemplate.perform.error", new String[] {className});
                throw new AutomatedWorkflowActionException("Incorrect type defined for automated workflow. " + className + " should implement " + AutomatedWorkflowTemplateJob.class
                                .getName());
            }
        }
        catch(InstantiationException e)
        {
            throw new AutomatedWorkflowActionException(e);
        }
        catch(IllegalAccessException e)
        {
            throw new AutomatedWorkflowActionException(e);
        }
    }


    protected Object getJobHnadlerBean()
    {
        return Registry.getApplicationContext().getBean(getJobHandler());
    }
}
