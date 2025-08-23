package com.hybris.backoffice.excel.template.cell;

import java.util.Optional;
import javax.annotation.Nullable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class FormulaCellValue implements CellValue
{
    private static final int ORDER_OF_FORMULA_CELL_VALUE = 10;


    public Optional<String> getValue(@Nullable Cell cell)
    {
        return Optional.<Cell>ofNullable(cell)
                        .map(Cell::getRow)
                        .map(Row::getSheet)
                        .map(Sheet::getWorkbook)
                        .map(Workbook::getCreationHelper)
                        .map(CreationHelper::createFormulaEvaluator)
                        .map(c -> c.evaluate(cell))
                        .map(CellValue::getStringValue);
    }


    public boolean canHandle(CellType cellType)
    {
        return (cellType == CellType.FORMULA);
    }


    public int getOrder()
    {
        return 10;
    }
}
