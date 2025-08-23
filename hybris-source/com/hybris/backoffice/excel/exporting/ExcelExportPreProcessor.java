package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelExportParams;
import javax.annotation.Nonnull;

public interface ExcelExportPreProcessor
{
    @Nonnull
    ExcelExportParams process(@Nonnull ExcelExportParams paramExcelExportParams);
}
