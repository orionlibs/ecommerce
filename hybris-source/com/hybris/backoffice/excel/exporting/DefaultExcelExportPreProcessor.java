package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelExportParams;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nonnull;
import org.springframework.core.OrderComparator;

public class DefaultExcelExportPreProcessor implements ExcelExportPreProcessor
{
    private List<ExcelExportParamsDecorator> decorators = new LinkedList<>();


    @Nonnull
    public ExcelExportParams process(@Nonnull ExcelExportParams excelExportParams)
    {
        ExcelExportParams accumulatedParams = excelExportParams;
        for(ExcelExportParamsDecorator decorator : this.decorators)
        {
            accumulatedParams = decorator.decorate(accumulatedParams);
        }
        return accumulatedParams;
    }


    public void setDecorators(List<ExcelExportParamsDecorator> decorators)
    {
        this.decorators = decorators;
        OrderComparator.sort(this.decorators);
    }


    public List<ExcelExportParamsDecorator> getDecorators()
    {
        return this.decorators;
    }
}
