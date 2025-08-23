package de.hybris.platform.impex.jalo.exp.converter;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

public abstract class AbstractExcelConverter extends AbstractExportConverter
{
    private final HSSFWorkbook workbook = new HSSFWorkbook();
    private HSSFSheet currentSheet = null;
    private HSSFRow currentRow = null;
    private HSSFCell currentCell;


    protected HSSFWorkbook getWorkbook()
    {
        return this.workbook;
    }


    protected HSSFSheet getCurrentSheet()
    {
        return this.currentSheet;
    }


    protected HSSFRow getCurrentRow()
    {
        return this.currentRow;
    }


    protected HSSFCell getCurrentCell()
    {
        return this.currentCell;
    }


    protected void setCurrentSheet(HSSFSheet newOne)
    {
        this.currentSheet = newOne;
    }


    protected HSSFSheet createSheet(String name)
    {
        this.currentSheet = getWorkbook().createSheet(name);
        return this.currentSheet;
    }


    protected HSSFRow createRow(short no)
    {
        this.currentRow = getCurrentSheet().createRow(no);
        return this.currentRow;
    }


    protected HSSFCell createCell(int celltype)
    {
        this.currentCell = createCell(getCurrentRow());
        this.currentCell.setCellType(CellType.forInt(celltype));
        return this.currentCell;
    }


    protected HSSFCell createCell(HSSFRow row)
    {
        return row.createCell(getCurrentRow().getLastCellNum() + 1);
    }
}
