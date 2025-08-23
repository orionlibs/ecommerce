package de.hybris.platform.auditreport.service.impl;

import de.hybris.platform.auditreport.model.AuditReportDataModel;
import de.hybris.platform.auditreport.model.CreateAuditReportCronJobModel;
import de.hybris.platform.auditreport.service.AuditReportDataService;
import de.hybris.platform.auditreport.service.CreateAuditReportParams;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class CreateAuditReportJobPerformable extends AbstractJobPerformable<CreateAuditReportCronJobModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(CreateAuditReportJobPerformable.class);
    private AuditReportDataService auditReportDataService;
    private String workflowTemplateName;
    private UserService userService;
    private WorkflowTemplateService workflowTemplateService;
    private WorkflowService workflowService;
    private WorkflowProcessingService workflowProcessingService;
    private I18NService i18NService;


    private static CreateAuditReportParams createAuditReportParams(CreateAuditReportCronJobModel cronJob)
    {
        return new CreateAuditReportParams(cronJob.getRootItem(), cronJob.getConfigName(), cronJob.getReportId(), cronJob
                        .getAudit().booleanValue(), cronJob.getIncludedLanguages(), cronJob.getAuditReportTemplate());
    }


    public PerformResult perform(CreateAuditReportCronJobModel cronJobModel)
    {
        AuditReportDataModel report;
        try
        {
            CreateAuditReportParams auditReportParams = createAuditReportParams(cronJobModel);
            report = getAuditReportDataService().createReport(auditReportParams);
        }
        catch(RuntimeException e)
        {
            LOG.error(String.format("Cannot create audit report for item (pk: %s)", new Object[] {cronJobModel.getRootItem().getPk()}), e);
            return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
        }
        if(StringUtils.isNotBlank(getWorkflowTemplateName()))
        {
            startWorkflow(report);
        }
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }


    protected void startWorkflow(AuditReportDataModel report)
    {
        try
        {
            WorkflowTemplateModel workflowTemplate = getWorkflowTemplateService().getWorkflowTemplateForCode(getWorkflowTemplateName());
            String workflowName = createWorkflowName(report, workflowTemplate);
            WorkflowModel workflow = getWorkflowService().createWorkflow(workflowName, workflowTemplate,
                            Collections.singletonList(report), getUserService().getCurrentUser());
            getWorkflowProcessingService().startWorkflow(workflow);
        }
        catch(UnknownIdentifierException | de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException exc)
        {
            LOG.warn("Could not start workflow for report generation", (Throwable)exc);
        }
    }


    protected String createWorkflowName(AuditReportDataModel report, WorkflowTemplateModel workflowTemplate)
    {
        return (String)this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, workflowTemplate, report));
    }


    protected AuditReportDataService getAuditReportDataService()
    {
        return this.auditReportDataService;
    }


    @Required
    public void setAuditReportDataService(AuditReportDataService auditReportDataService)
    {
        this.auditReportDataService = auditReportDataService;
    }


    protected String getWorkflowTemplateName()
    {
        return this.workflowTemplateName;
    }


    @Required
    public void setWorkflowTemplateName(String workflowTemplateName)
    {
        this.workflowTemplateName = workflowTemplateName;
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    protected WorkflowTemplateService getWorkflowTemplateService()
    {
        return this.workflowTemplateService;
    }


    @Required
    public void setWorkflowTemplateService(WorkflowTemplateService workflowTemplateService)
    {
        this.workflowTemplateService = workflowTemplateService;
    }


    protected WorkflowService getWorkflowService()
    {
        return this.workflowService;
    }


    @Required
    public void setWorkflowService(WorkflowService workflowService)
    {
        this.workflowService = workflowService;
    }


    protected WorkflowProcessingService getWorkflowProcessingService()
    {
        return this.workflowProcessingService;
    }


    @Required
    public void setWorkflowProcessingService(WorkflowProcessingService workflowProcessingService)
    {
        this.workflowProcessingService = workflowProcessingService;
    }


    public I18NService getI18NService()
    {
        return this.i18NService;
    }


    @Required
    public void setI18NService(I18NService i18NService)
    {
        this.i18NService = i18NService;
    }
}
