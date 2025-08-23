package com.hybris.backoffice.excel.template;

import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelSheetNamingStrategy
{
    String generateName(Workbook paramWorkbook, String paramString);
}
