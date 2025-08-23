/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.excel;

public class ExcelConstants
{
    public static final String EXCEL_FORM_PROPERTY = "excelForm";
    public static final String EXCEL_IMPORT_VALIDATION_RESULT = "excelImportValidationResult";
    public static final String EXCEL_HAS_VALIDATION_ERRORS = "excelHasValidationErrors";
    public static final String NOTIFICATION_SOURCE_EXCEL_IMPORT = "excelImport";
    public static final String NOTIFICATION_EVENT_TYPE_MISSING_EXCEL_FILE = "excelMissingExcelFile";
    public static final String NOTIFICATION_EVENT_TYPE_EXCEL_FORM_IN_MODEL = "excelMissingExcelFormInModel";
    /**
     * @deprecated since 6.7 not longer used
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public static final String NOTIFICATION_EVENT_TYPE_EMPTY_LIST = "excelEmptyList";
    /**
     * @deprecated since 6.7 not longer used
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public static final String NOTIFICATION_EVENT_TYPE_FILE_EXISTS = "excelFileExists";
    /**
     * @deprecated since 6.7 not longer used
     */
    @Deprecated(since = "6.7", forRemoval = true)
    public static final String NOTIFICATION_EVENT_TYPE_INCORRECT_FORMAT = "excelIncorrectFormat";
    public static final String NOTIFICATION_SOURCE_EXCEL_EXPORT = "excelExport";
    public static final String NOTIFICATION_EVENT_TYPE_MISSING_FORM = "missingForm";
    public static final String NOTIFICATION_EVENT_TYPE_MISSING_ATTRIBUTES = "missingAttributes";
    public static final String NOTIFICATION_EVENT_TYPE_ATTRIBUTES_MAX_COUNT_EXCEEDED = "attributesMaxCountExceeded";
    public static final String NOTIFICATION_EVENT_CANNOT_GENERATE_WORKBOOK = "cannotGenerateWorkbook";


    private ExcelConstants()
    {
    }
}
