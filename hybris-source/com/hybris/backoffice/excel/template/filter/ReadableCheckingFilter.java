package com.hybris.backoffice.excel.template.filter;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;

public class ReadableCheckingFilter implements ExcelFilter<AttributeDescriptorModel>
{
    public boolean test(AttributeDescriptorModel attributeDescriptorModel)
    {
        return attributeDescriptorModel.getReadable().booleanValue();
    }
}
