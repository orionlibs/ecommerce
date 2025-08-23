package de.hybris.platform.workflow.interceptors;

import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.workflow.jalo.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.model.AutomatedWorkflowActionTemplateModel;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;

public class AutomatedWorkflowActionTemplateValidator implements ValidateInterceptor
{
    private static final Logger LOG = Logger.getLogger(AutomatedWorkflowActionTemplateValidator.class);


    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof AutomatedWorkflowActionTemplateModel)
        {
            AutomatedWorkflowActionTemplateModel _model = (AutomatedWorkflowActionTemplateModel)model;
            if(_model.getJobClass() == null && GenericValidator.isBlankOrNull(_model.getJobHandler()))
            {
                throw new InterceptorException("jobHandler is required");
            }
            if(_model.getJobClass() != null && !GenericValidator.isBlankOrNull(_model.getJobHandler()))
            {
                throw new InterceptorException("Both jobHandler and jobClass are used, use only one");
            }
            if(_model.getJobClass() != null && GenericValidator.isBlankOrNull(_model.getJobHandler()))
            {
                if(!ArrayUtils.contains(ClassUtils.getAllInterfaces(_model.getJobClass()).toArray(), AutomatedWorkflowTemplateJob.class) &&
                                !ArrayUtils.contains(ClassUtils.getAllInterfaces(_model.getJobClass()).toArray(), AutomatedWorkflowTemplateJob.class))
                {
                    throw new InterceptorException("Only a class which implements de.hybris.platform.workflow.jalo.AutomatedWorkflowTemplateJob or de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob interface is allowed as jobClass but got " + _model
                                    .getJobClass()
                                    .getName());
                }
                LOG.warn("jobClass attribute is deprecated. Please use jobHandler instead.");
            }
        }
    }
}
