package com.hybris.backoffice.excel.data;

public class ExcelColumn
{
    private final SelectedAttribute selectedAttribute;
    private final Integer columnIndex;


    public ExcelColumn(SelectedAttribute selectedAttribute, Integer columnIndex)
    {
        this.selectedAttribute = selectedAttribute;
        this.columnIndex = columnIndex;
    }


    public SelectedAttribute getSelectedAttribute()
    {
        return this.selectedAttribute;
    }


    public Integer getColumnIndex()
    {
        return this.columnIndex;
    }
}
