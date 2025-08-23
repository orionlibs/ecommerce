package com.hybris.backoffice.excel.template.filter;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;

public class MandatoryCheckingFilter implements ExcelFilter<AttributeDescriptorModel>
{
    public boolean test(AttributeDescriptorModel attributeDescriptorModel)
    {
        return (!attributeDescriptorModel.getOptional().booleanValue() && !attributeDescriptorModel.getPrivate().booleanValue());
    }
}
