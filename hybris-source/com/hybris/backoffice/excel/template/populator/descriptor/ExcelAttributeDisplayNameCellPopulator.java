package com.hybris.backoffice.excel.template.populator.descriptor;

import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.template.AttributeNameFormatter;
import com.hybris.backoffice.excel.template.populator.ExcelAttributeContext;
import com.hybris.backoffice.excel.template.populator.ExcelCellPopulator;
import org.springframework.beans.factory.annotation.Required;

class ExcelAttributeDisplayNameCellPopulator implements ExcelCellPopulator<ExcelAttributeDescriptorAttribute>
{
    private AttributeNameFormatter<ExcelAttributeDescriptorAttribute> attributeNameFormatter;


    public String apply(ExcelAttributeContext<ExcelAttributeDescriptorAttribute> populatorContext)
    {
        return this.attributeNameFormatter.format(populatorContext);
    }


    @Required
    public void setAttributeNameFormatter(AttributeNameFormatter<ExcelAttributeDescriptorAttribute> attributeNameFormatter)
    {
        this.attributeNameFormatter = attributeNameFormatter;
    }
}
