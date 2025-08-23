package com.hybris.backoffice.excel.importing;

import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelTypeSystemService<T extends com.hybris.backoffice.excel.importing.data.TypeSystem>
{
    T loadTypeSystem(Workbook paramWorkbook);
}
