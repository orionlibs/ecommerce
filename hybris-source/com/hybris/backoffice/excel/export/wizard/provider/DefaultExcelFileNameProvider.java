/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.export.wizard.provider;

import com.hybris.backoffice.excel.export.wizard.ExcelExportWizardForm;
import de.hybris.platform.servicelayer.time.TimeService;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Required;

/**
 * Provides file name with the following pattern : typeCode_timestamp.xlsx
 */
public class DefaultExcelFileNameProvider implements ExcelFileNameProvider
{
    private TimeService timeService;


    @Override
    public String provide(final ExcelExportWizardForm form)
    {
        return provide(form.getTypeCode());
    }


    @Override
    public String provide(final String typeCode)
    {
        return String.format("%s_%s.xlsx", typeCode, getFormattedDate());
    }


    private String getFormattedDate()
    {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(timeService.getCurrentTime());
    }


    public TimeService getTimeService()
    {
        return timeService;
    }


    @Required
    public void setTimeService(final TimeService timeService)
    {
        this.timeService = timeService;
    }
}
