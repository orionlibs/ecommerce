package com.hybris.backoffice.excel.template.filter;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;

public class WritableCheckingFilter implements ExcelFilter<AttributeDescriptorModel>
{
    public boolean test(AttributeDescriptorModel attributeDescriptorModel)
    {
        return (attributeDescriptorModel.getWritable().booleanValue() || attributeDescriptorModel.getInitial().booleanValue());
    }
}
