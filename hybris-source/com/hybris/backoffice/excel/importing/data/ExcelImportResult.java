package com.hybris.backoffice.excel.importing.data;

import com.hybris.backoffice.excel.data.Impex;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelImportResult
{
    private final Workbook workbook;
    private final Impex impex;


    public ExcelImportResult(Workbook workbook, Impex impex)
    {
        this.workbook = workbook;
        this.impex = impex;
    }


    public Workbook getWorkbook()
    {
        return this.workbook;
    }


    public Impex getImpex()
    {
        return this.impex;
    }
}
