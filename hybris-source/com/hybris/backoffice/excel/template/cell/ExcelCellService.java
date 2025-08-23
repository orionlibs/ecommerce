package com.hybris.backoffice.excel.template.cell;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.poi.ss.usermodel.Cell;

public interface ExcelCellService
{
    String getCellValue(@Nullable Cell paramCell);


    void insertAttributeValue(@Nonnull Cell paramCell, @Nullable Object paramObject);
}
