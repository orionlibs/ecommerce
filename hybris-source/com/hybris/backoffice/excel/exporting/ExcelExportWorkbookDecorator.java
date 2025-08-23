package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelExportResult;
import org.springframework.core.Ordered;

public interface ExcelExportWorkbookDecorator extends Ordered
{
    void decorate(ExcelExportResult paramExcelExportResult);
}
