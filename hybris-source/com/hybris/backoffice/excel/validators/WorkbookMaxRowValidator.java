package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.template.ExcelTemplateService;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.util.Config;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Required;

public class WorkbookMaxRowValidator implements WorkbookValidator
{
    public static final String BACKOFFICE_EXCEL_IMPORT_MAX_ROWS_PROPERTY_KEY = "backoffice.excel.import.max.rows";
    private static final String VALIDATION_MESSAGE_HEADER = "excel.import.validation.max.row.exceeded.header";
    private static final String VALIDATION_MESSAGE_DESCRIPTION = "excel.import.validation.max.row.exceeded.description";
    public static final int DEFAULT_MAX_ROW = 2000;
    public static final int FIRST_DATA_ROW_INDEX = 3;
    @Deprecated(since = "1808", forRemoval = true)
    private ExcelTemplateService excelTemplateService;
    private ExcelSheetService excelSheetService;
    private ExcelCellService excelCellService;


    public List<ExcelValidationResult> validate(Workbook workbook)
    {
        Collection<Sheet> sheets = getExcelSheetService().getSheets(workbook);
        int numberOfRows = 0;
        for(Sheet sheet : sheets)
        {
            numberOfRows += getNumberOfCorrectRows(sheet);
        }
        return prepareResult(numberOfRows);
    }


    protected Integer getMaxRow()
    {
        return Integer.valueOf(Config.getInt("backoffice.excel.import.max.rows", 2000));
    }


    protected List<ExcelValidationResult> prepareResult(int totalNumberOfRows)
    {
        Integer maxRow = getMaxRow();
        if(totalNumberOfRows <= maxRow.intValue())
        {
            return new ArrayList<>();
        }
        ValidationMessage header = new ValidationMessage("excel.import.validation.max.row.exceeded.header");
        ExcelValidationResult validationResult = new ExcelValidationResult(new ValidationMessage("excel.import.validation.max.row.exceeded.description", new Serializable[] {maxRow, Integer.valueOf(totalNumberOfRows)}));
        validationResult.setHeader(header);
        return Collections.singletonList(validationResult);
    }


    protected int getNumberOfCorrectRows(Sheet sheet)
    {
        int count = 0;
        for(int i = 3; i <= sheet.getLastRowNum(); i++)
        {
            if(isRowCorrect(sheet.getRow(i)))
            {
                count++;
            }
        }
        return count;
    }


    protected boolean isRowCorrect(Row row)
    {
        if(row != null)
        {
            for(int i = 0; i < row.getLastCellNum(); i++)
            {
                if(StringUtils.isNotBlank(getExcelCellService().getCellValue(row.getCell(i))))
                {
                    return true;
                }
            }
        }
        return false;
    }


    @Deprecated(since = "1808", forRemoval = true)
    public ExcelTemplateService getExcelTemplateService()
    {
        return this.excelTemplateService;
    }


    @Deprecated(since = "1808", forRemoval = true)
    @Required
    public void setExcelTemplateService(ExcelTemplateService excelTemplateService)
    {
        this.excelTemplateService = excelTemplateService;
    }


    public ExcelSheetService getExcelSheetService()
    {
        return this.excelSheetService;
    }


    @Required
    public void setExcelSheetService(ExcelSheetService excelSheetService)
    {
        this.excelSheetService = excelSheetService;
    }


    public ExcelCellService getExcelCellService()
    {
        return this.excelCellService;
    }


    @Required
    public void setExcelCellService(ExcelCellService excelCellService)
    {
        this.excelCellService = excelCellService;
    }
}
