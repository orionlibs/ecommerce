package com.hybris.backoffice.excel.template.populator;

import com.hybris.backoffice.excel.data.ExcelExportResult;
import javax.annotation.Nonnull;

@FunctionalInterface
public interface ExcelSheetPopulator
{
    void populate(@Nonnull ExcelExportResult paramExcelExportResult);
}
