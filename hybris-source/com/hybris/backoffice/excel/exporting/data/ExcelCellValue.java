package com.hybris.backoffice.excel.exporting.data;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import de.hybris.platform.core.model.ItemModel;
import org.apache.poi.ss.usermodel.Cell;

public class ExcelCellValue
{
    private final Cell cell;
    private final ExcelAttribute excelAttribute;
    private final ItemModel itemModel;


    public ExcelCellValue(Cell cell, ExcelAttribute excelAttribute, ItemModel itemModel)
    {
        this.cell = cell;
        this.excelAttribute = excelAttribute;
        this.itemModel = itemModel;
    }


    public Cell getCell()
    {
        return this.cell;
    }


    public ExcelAttribute getExcelAttribute()
    {
        return this.excelAttribute;
    }


    public ItemModel getItemModel()
    {
        return this.itemModel;
    }
}
