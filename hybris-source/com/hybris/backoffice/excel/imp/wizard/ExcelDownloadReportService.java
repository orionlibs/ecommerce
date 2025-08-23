/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel.imp.wizard;

import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import java.util.Collection;

/**
 * Service which allows to download validation report file
 */
public interface ExcelDownloadReportService
{
    /**
     * Triggers downloading of report file.
     *
     * @param excelValidationResults
     *           source of the content of the generated file.
     */
    void downloadReport(final Collection<ExcelValidationResult> excelValidationResults);
}
