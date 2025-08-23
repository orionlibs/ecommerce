/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.bulkedit;

import java.util.List;

/**
 * Service which allows to download validation report file
 */
public interface BulkEditDownloadValidationReportService
{
    /**
     * Triggers downloading of report file.
     *
     * @param validationResults
     *           source of the content of the generated file.
     */
    void downloadReport(final List<ValidationResult> validationResults);
}
