package com.hybris.backoffice.excel.validators.util;

import com.hybris.backoffice.excel.data.ExcelWorksheet;
import java.util.Collection;

public interface ExcelWorkbookEntriesService
{
    Collection<WorksheetEntryKey> generateEntryKeys(ExcelWorksheet paramExcelWorksheet);
}
