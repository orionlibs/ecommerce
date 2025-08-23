package com.hybris.backoffice.excel.template.cell;

import java.util.Optional;
import javax.annotation.Nullable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.core.Ordered;

@FunctionalInterface
interface CellValue extends Ordered
{
    Optional<String> getValue(@Nullable Cell paramCell);


    default boolean canHandle(CellType cellType)
    {
        return false;
    }


    default int getOrder()
    {
        return 0;
    }
}
