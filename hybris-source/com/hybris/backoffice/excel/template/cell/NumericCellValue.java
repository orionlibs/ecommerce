package com.hybris.backoffice.excel.template.cell;

import com.hybris.backoffice.excel.util.ExcelDateUtils;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;
import javax.annotation.Nullable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class NumericCellValue implements CellValue
{
    private static final Logger LOG = LoggerFactory.getLogger(NumericCellValue.class);
    private static final int ORDER_OF_NUMERIC_CELL_VALUE = 20;
    private ExcelDateUtils excelDateUtils;


    public Optional<String> getValue(@Nullable Cell cell)
    {
        return Optional.ofNullable(getNumericCellValue(cell));
    }


    public boolean canHandle(CellType cellType)
    {
        return (cellType == CellType.NUMERIC);
    }


    public int getOrder()
    {
        return 20;
    }


    protected String getNumericCellValue(Cell cell)
    {
        if(isCellDateFormatted(cell))
        {
            Date formattedDate = getJavaDate(cell);
            return this.excelDateUtils.exportDate(formattedDate);
        }
        return (cell instanceof XSSFCell) ? ((XSSFCell)cell).getRawValue() : Double.toString(cell.getNumericCellValue());
    }


    protected boolean isCellDateFormatted(Cell cell)
    {
        return DateUtil.isCellDateFormatted(cell);
    }


    protected Date getJavaDate(Cell cell)
    {
        return DateUtil.getJavaDate(cell.getNumericCellValue(), getTimeZone());
    }


    protected TimeZone getTimeZone()
    {
        try
        {
            return TimeZone.getTimeZone(this.excelDateUtils.getExportTimeZone());
        }
        catch(Exception ex)
        {
            LOG.warn(String.format("Cannot find timezone for %s code", new Object[] {this.excelDateUtils.getExportTimeZone()}), ex);
            return TimeZone.getDefault();
        }
    }


    public ExcelDateUtils getExcelDateUtils()
    {
        return this.excelDateUtils;
    }


    @Required
    public void setExcelDateUtils(ExcelDateUtils excelDateUtils)
    {
        this.excelDateUtils = excelDateUtils;
    }
}
