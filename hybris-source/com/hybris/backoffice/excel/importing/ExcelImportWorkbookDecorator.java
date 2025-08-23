package com.hybris.backoffice.excel.importing;

import com.hybris.backoffice.excel.importing.data.ExcelImportResult;
import javax.annotation.Nonnull;
import org.springframework.core.Ordered;

public interface ExcelImportWorkbookDecorator extends Ordered
{
    void decorate(@Nonnull ExcelImportResult paramExcelImportResult);
}
