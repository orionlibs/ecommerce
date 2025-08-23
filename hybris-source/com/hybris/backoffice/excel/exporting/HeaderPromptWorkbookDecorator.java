package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.template.mapper.ExcelMapper;
import com.hybris.backoffice.excel.template.populator.ExcelSheetPopulator;
import java.util.Collection;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class HeaderPromptWorkbookDecorator implements ExcelExportWorkbookDecorator
{
    private ExcelMapper<ExcelExportResult, ExcelAttributeDescriptorAttribute> mapper;
    private ExcelSheetPopulator headerPromptPopulator;


    public void decorate(ExcelExportResult excelExportResult)
    {
        Collection<ExcelAttribute> attributes = CollectionUtils.union(excelExportResult.getAvailableAdditionalAttributes(), (Iterable)this.mapper
                        .apply(excelExportResult));
        ExcelExportResult result = new ExcelExportResult(excelExportResult.getWorkbook(), excelExportResult.getSelectedItems(), excelExportResult.getSelectedAttributes(), excelExportResult.getSelectedAdditionalAttributes(), attributes);
        getHeaderPromptPopulator().populate(result);
    }


    public int getOrder()
    {
        return 1000;
    }


    public ExcelSheetPopulator getHeaderPromptPopulator()
    {
        return this.headerPromptPopulator;
    }


    @Required
    public void setHeaderPromptPopulator(ExcelSheetPopulator headerPromptPopulator)
    {
        this.headerPromptPopulator = headerPromptPopulator;
    }


    public ExcelMapper<ExcelExportResult, ExcelAttributeDescriptorAttribute> getMapper()
    {
        return this.mapper;
    }


    @Required
    public void setMapper(ExcelMapper<ExcelExportResult, ExcelAttributeDescriptorAttribute> mapper)
    {
        this.mapper = mapper;
    }
}
