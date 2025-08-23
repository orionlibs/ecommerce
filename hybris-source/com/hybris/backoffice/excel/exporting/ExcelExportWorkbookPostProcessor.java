package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelExportResult;

public interface ExcelExportWorkbookPostProcessor
{
    void process(ExcelExportResult paramExcelExportResult);
}
