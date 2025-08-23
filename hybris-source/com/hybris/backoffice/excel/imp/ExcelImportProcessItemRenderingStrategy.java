/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.imp;

import com.hybris.backoffice.model.ExcelImportCronJobModel;
import com.hybris.backoffice.widgets.processes.renderer.DefaultProcessItemRenderingStrategy;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.model.CronJobHistoryModel;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;

public class ExcelImportProcessItemRenderingStrategy extends DefaultProcessItemRenderingStrategy
{
    private static final String LABEL_PROCESSES_TITLE_EXCEL_IMPORT_FULL = "processes.title.excel.import.full";


    @Override
    public boolean canHandle(final CronJobHistoryModel cronJobHistory)
    {
        return cronJobHistory.getCronJob() instanceof ExcelImportCronJobModel;
    }


    @Override
    public String getTitle(final CronJobHistoryModel cronJobHistory)
    {
        if(cronJobHistory.getCronJob() instanceof ExcelImportCronJobModel)
        {
            final MediaModel excelFile = ((ExcelImportCronJobModel)cronJobHistory.getCronJob()).getExcelFile();
            final String excelFileName = excelFile != null ? excelFile.getRealFileName() : StringUtils.EMPTY;
            return String.format("%s - %s", getLabel(LABEL_PROCESSES_TITLE_EXCEL_IMPORT_FULL), excelFileName);
        }
        return super.getTitle(cronJobHistory);
    }


    protected String getLabel(final String label)
    {
        return Labels.getLabel(label);
    }
}
