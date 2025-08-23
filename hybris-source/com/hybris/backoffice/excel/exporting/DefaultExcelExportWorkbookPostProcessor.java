package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelExportResult;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.OrderComparator;

public class DefaultExcelExportWorkbookPostProcessor implements ExcelExportWorkbookPostProcessor
{
    private List<ExcelExportWorkbookDecorator> decorators;


    public void process(ExcelExportResult excelExportResult)
    {
        this.decorators.forEach(decorator -> decorator.decorate(excelExportResult));
    }


    public List<ExcelExportWorkbookDecorator> getDecorators()
    {
        return this.decorators;
    }


    @Required
    public void setDecorators(List<ExcelExportWorkbookDecorator> decorators)
    {
        if(decorators != null)
        {
            OrderComparator.sort(decorators);
        }
        this.decorators = decorators;
    }
}
