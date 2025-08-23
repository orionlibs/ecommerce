package com.hybris.backoffice.excel.template.populator.appender;

import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import org.springframework.beans.factory.annotation.Required;

public class UniqueExcelMarkAppender implements ExcelMarkAppender<ExcelAttributeDescriptorAttribute>
{
    private static final int ORDER_OF_UNIQUE_EXCEL_MARK_APPENDER = 10000;
    private ExcelFilter<AttributeDescriptorModel> uniqueFilter;


    public String apply(String s, ExcelAttributeDescriptorAttribute excelAttributeDescriptorAttribute)
    {
        return this.uniqueFilter.test(excelAttributeDescriptorAttribute.getAttributeDescriptorModel()) ? (
                        s + s) : s;
    }


    public int getOrder()
    {
        return 10000;
    }


    @Required
    public void setUniqueFilter(ExcelFilter<AttributeDescriptorModel> uniqueFilter)
    {
        this.uniqueFilter = uniqueFilter;
    }
}
