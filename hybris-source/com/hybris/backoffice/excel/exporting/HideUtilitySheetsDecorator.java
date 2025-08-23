package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import de.hybris.platform.util.Config;
import java.util.Collection;
import org.apache.poi.ss.usermodel.SheetVisibility;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Required;

public class HideUtilitySheetsDecorator implements ExcelExportWorkbookDecorator
{
    private static final String CONFIG_HIDDEN_SHEETS = "backoffice.excel.utility.sheets.hidden";
    private Collection<ExcelTemplateConstants.UtilitySheet> utilitySheets;


    public void decorate(ExcelExportResult excelExportResult)
    {
        this.utilitySheets.forEach(utilitySheet -> hideUtilitySheet(excelExportResult.getWorkbook(), utilitySheet.getSheetName()));
    }


    protected void hideUtilitySheet(Workbook workbook, String sheetName)
    {
        int sheetIndex = workbook.getSheetIndex(sheetName);
        if(!workbook.isSheetHidden(sheetIndex) || workbook.getSheetAt(sheetIndex).isSelected())
        {
            activateFirstNonUtilitySheet(workbook);
            workbook.getSheetAt(sheetIndex).setSelected(false);
            workbook.setSheetVisibility(sheetIndex, getUtilitySheetHiddenLevel());
        }
    }


    protected SheetVisibility getUtilitySheetHiddenLevel()
    {
        return Config.getBoolean("backoffice.excel.utility.sheets.hidden", true) ?
                        SheetVisibility.VERY_HIDDEN : SheetVisibility.HIDDEN;
    }


    protected void activateFirstNonUtilitySheet(Workbook workbook)
    {
        if(ExcelTemplateConstants.UtilitySheet.isUtilitySheet(this.utilitySheets, workbook
                        .getSheetName(workbook.getActiveSheetIndex())))
        {
            for(int i = 0; i < workbook.getNumberOfSheets(); i++)
            {
                if(!ExcelTemplateConstants.UtilitySheet.isUtilitySheet(this.utilitySheets, workbook.getSheetName(i)))
                {
                    workbook.setActiveSheet(i);
                }
            }
        }
    }


    public int getOrder()
    {
        return 2147482647;
    }


    @Required
    public void setUtilitySheets(Collection<ExcelTemplateConstants.UtilitySheet> utilitySheets)
    {
        this.utilitySheets = utilitySheets;
    }
}
