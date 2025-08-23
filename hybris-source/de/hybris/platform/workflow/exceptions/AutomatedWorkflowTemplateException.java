package de.hybris.platform.workflow.exceptions;

import de.hybris.platform.servicelayer.exceptions.BusinessException;

public class AutomatedWorkflowTemplateException extends BusinessException
{
    public AutomatedWorkflowTemplateException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public AutomatedWorkflowTemplateException(String message)
    {
        super(message);
    }
}
