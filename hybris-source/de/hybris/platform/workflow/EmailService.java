package de.hybris.platform.workflow;

import de.hybris.platform.workflow.model.WorkflowActionModel;
import org.apache.commons.mail.HtmlEmail;

public interface EmailService
{
    HtmlEmail createActivationEmail(WorkflowActionModel paramWorkflowActionModel);
}
