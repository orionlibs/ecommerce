package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelExportParams;
import javax.annotation.Nonnull;
import org.springframework.core.Ordered;

public interface ExcelExportParamsDecorator extends Ordered
{
    @Nonnull
    ExcelExportParams decorate(@Nonnull ExcelExportParams paramExcelExportParams);


    int getOrder();
}
