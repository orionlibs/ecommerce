package com.hybris.backoffice.excel.template.populator.appender;

import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

public class ReadonlyExcelMarkAppender implements ExcelMarkAppender<ExcelAttributeDescriptorAttribute>
{
    private static final int ORDER_OF_READONLY_EXCEL_MARK_APPENDER = 40000;


    public String apply(String s, ExcelAttributeDescriptorAttribute excelAttributeDescriptorAttribute)
    {
        return isReadonly(excelAttributeDescriptorAttribute.getAttributeDescriptorModel()) ? (
                        s + s) : s;
    }


    private boolean isReadonly(AttributeDescriptorModel attributeDescriptor)
    {
        return (attributeDescriptor.getReadable().booleanValue() && !attributeDescriptor.getWritable().booleanValue());
    }


    public int getOrder()
    {
        return 40000;
    }
}
