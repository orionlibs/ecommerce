package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.template.populator.ExcelSheetPopulator;
import java.util.Collection;
import java.util.LinkedList;

public class ExcelSheetWorkbookDecorator implements ExcelExportWorkbookDecorator
{
    private int order = 0;
    private Collection<ExcelSheetPopulator> populators = new LinkedList<>();


    public void decorate(ExcelExportResult excelExportResult)
    {
        this.populators.forEach(populator -> populator.populate(excelExportResult));
    }


    public int getOrder()
    {
        return this.order;
    }


    public void setOrder(int order)
    {
        this.order = order;
    }


    public void setPopulators(Collection<ExcelSheetPopulator> populators)
    {
        this.populators = populators;
    }
}
