package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import java.util.Collection;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Required;

public class RemoveSheetsDecorator implements ExcelExportWorkbookDecorator
{
    private static final int ORDER_OF_REMOVE_SHEETS_DECORATOR = 2147483637;
    private Collection<ExcelTemplateConstants.UtilitySheet> sheetsToRemove;


    public void decorate(ExcelExportResult excelExportResult)
    {
        Workbook workbook = excelExportResult.getWorkbook();
        this.sheetsToRemove.forEach(sheetToRemove -> {
            int sheetIndex = workbook.getSheetIndex(sheetToRemove.getSheetName());
            workbook.removeSheetAt(sheetIndex);
        });
    }


    public int getOrder()
    {
        return 2147483637;
    }


    @Required
    public void setSheetsToRemove(Collection<ExcelTemplateConstants.UtilitySheet> sheetsToRemove)
    {
        this.sheetsToRemove = sheetsToRemove;
    }
}
