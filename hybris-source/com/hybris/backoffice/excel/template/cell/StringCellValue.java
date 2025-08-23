package com.hybris.backoffice.excel.template.cell;

import java.util.Optional;
import javax.annotation.Nullable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public class StringCellValue implements CellValue
{
    public Optional<String> getValue(@Nullable Cell cell)
    {
        return Optional.<Cell>ofNullable(cell).map(Cell::getStringCellValue);
    }


    public boolean canHandle(CellType cellType)
    {
        return (cellType == CellType.STRING);
    }


    public int getOrder()
    {
        return 0;
    }
}
