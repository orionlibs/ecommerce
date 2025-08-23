/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.auditreport.imp;

import com.hybris.backoffice.widgets.processes.renderer.DefaultProcessItemRenderingStrategy;
import de.hybris.platform.auditreport.model.AuditReportDataModel;
import de.hybris.platform.auditreport.model.CreateAuditReportCronJobModel;
import de.hybris.platform.cronjob.model.CronJobHistoryModel;
import org.zkoss.util.resource.Labels;

public class PersonalDataReportProcessItemRenderingStrategy extends DefaultProcessItemRenderingStrategy
{
    private static final String I18N_PROCESSES_AUDITREPORT_CRONJOB_TITLE = "processes.auditreport.cronjob.title";


    @Override
    public boolean canHandle(final CronJobHistoryModel cronJobHistory)
    {
        return cronJobHistory != null && cronJobHistory.getCronJob() instanceof CreateAuditReportCronJobModel;
    }


    @Override
    public String getJobTitle(final CronJobHistoryModel cronJobHistory)
    {
        return getLabelService().getObjectLabel(cronJobHistory.getCronJob());
    }


    @Override
    public String getTitle(final CronJobHistoryModel cronJobHistory)
    {
        return Labels.getLabel(I18N_PROCESSES_AUDITREPORT_CRONJOB_TITLE, new Object[]
                        {getLabelService().getObjectLabel(AuditReportDataModel._TYPECODE),
                                        ((CreateAuditReportCronJobModel)cronJobHistory.getCronJob()).getReportId()});
    }
}
