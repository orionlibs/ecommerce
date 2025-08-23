package com.hybris.backoffice.excel.data;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ExcelWorksheet
{
    private final String sheetName;
    private final Table<Integer, ExcelColumn, ImportParameters> table;


    public ExcelWorksheet(String sheetName)
    {
        this.sheetName = sheetName;
        this.table = (Table<Integer, ExcelColumn, ImportParameters>)HashBasedTable.create();
    }


    public void forEachColumn(Consumer<ExcelColumn> consumer)
    {
        Objects.requireNonNull(consumer);
        getTable().columnKeySet().forEach(consumer::accept);
    }


    public void forEachRow(ExcelColumn excelColumn, BiConsumer<Integer, ImportParameters> biConsumer)
    {
        Objects.requireNonNull(biConsumer);
        getTable().column(excelColumn).forEach(biConsumer::accept);
    }


    public String getSheetName()
    {
        return this.sheetName;
    }


    public Table<Integer, ExcelColumn, ImportParameters> getTable()
    {
        return (Table<Integer, ExcelColumn, ImportParameters>)ImmutableTable.copyOf(this.table);
    }


    public void add(int rowIndex, ExcelColumn column, ImportParameters value)
    {
        this.table.put(Integer.valueOf(rowIndex), column, value);
    }
}
