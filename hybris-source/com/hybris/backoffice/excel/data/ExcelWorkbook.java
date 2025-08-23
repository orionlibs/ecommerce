package com.hybris.backoffice.excel.data;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class ExcelWorkbook
{
    private final Set<ExcelWorksheet> worksheets;


    public ExcelWorkbook(ExcelWorksheet... excelWorksheets)
    {
        this.worksheets = Sets.newHashSet((Object[])excelWorksheets);
    }


    public void add(ExcelWorksheet excelWorksheet)
    {
        this.worksheets.add(excelWorksheet);
    }


    public Set<ExcelWorksheet> getWorksheets()
    {
        return (Set<ExcelWorksheet>)ImmutableSet.copyOf(this.worksheets);
    }


    public void forEachWorksheet(Consumer<ExcelWorksheet> consumer)
    {
        Objects.requireNonNull(consumer);
        this.worksheets.forEach(consumer::accept);
    }
}
