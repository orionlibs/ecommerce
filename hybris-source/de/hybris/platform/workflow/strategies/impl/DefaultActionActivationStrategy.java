package de.hybris.platform.workflow.strategies.impl;

import de.hybris.platform.workflow.EmailService;
import de.hybris.platform.workflow.exceptions.ActivationWorkflowActionException;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.strategies.ActionActivationStrategy;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class DefaultActionActivationStrategy implements ActionActivationStrategy
{
    private EmailService emailService;


    public void doAfterActivation(WorkflowActionModel action) throws ActivationWorkflowActionException
    {
        try
        {
            HtmlEmail email = this.emailService.createActivationEmail(action);
            if(email != null)
            {
                email.send();
            }
        }
        catch(EmailException e)
        {
            throw new ActivationWorkflowActionException(e);
        }
    }


    public void setEmailService(EmailService emailService)
    {
        this.emailService = emailService;
    }
}
