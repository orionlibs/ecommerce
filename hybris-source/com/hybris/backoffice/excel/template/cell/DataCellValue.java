package com.hybris.backoffice.excel.template.cell;

import java.util.Optional;
import javax.annotation.Nullable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;

public class DataCellValue implements CellValue
{
    private static final int ORDER_OF_DATA_CELL_VALUE = 2147483637;


    public Optional<String> getValue(@Nullable Cell cell)
    {
        return Optional.ofNullable((new DataFormatter()).formatCellValue(cell));
    }


    public boolean canHandle(CellType cellType)
    {
        return true;
    }


    public int getOrder()
    {
        return 2147483637;
    }
}
