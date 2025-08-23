package com.hybris.backoffice.excel.template.filter;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;

public class DefaultValueCheckingFilter implements ExcelFilter<AttributeDescriptorModel>
{
    public boolean test(AttributeDescriptorModel attributeDescriptorModel)
    {
        return (attributeDescriptorModel.getDefaultValue() != null);
    }
}
