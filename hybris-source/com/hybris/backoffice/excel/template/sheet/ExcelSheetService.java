package com.hybris.backoffice.excel.template.sheet;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import java.util.Collection;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelSheetService
{
    default Row createEmptyRow(@Nonnull Sheet sheet)
    {
        return sheet.createRow(sheet.getLastRowNum() + 1);
    }


    Sheet createOrGetTypeSheet(@WillNotClose Workbook paramWorkbook, @Nonnull String paramString);


    Sheet createTypeSheet(@WillNotClose Workbook paramWorkbook, @Nonnull String paramString);


    Sheet createOrGetUtilitySheet(@WillNotClose Workbook paramWorkbook, @Nonnull String paramString);


    String findSheetNameForTypeCode(@WillNotClose Workbook paramWorkbook, String paramString);


    String findTypeCodeForSheetName(@WillNotClose Workbook paramWorkbook, String paramString);


    int findColumnIndex(Sheet paramSheet1, @Nonnull Sheet paramSheet2, ExcelAttribute paramExcelAttribute);


    default Optional<Sheet> getSheet(@WillNotClose Workbook workbook, @Nonnull String sheetName)
    {
        return Optional.ofNullable(workbook.getSheet(sheetName));
    }


    Collection<Sheet> getSheets(@WillNotClose Workbook paramWorkbook);


    Collection<String> getSheetsNames(@WillNotClose Workbook paramWorkbook);
}
