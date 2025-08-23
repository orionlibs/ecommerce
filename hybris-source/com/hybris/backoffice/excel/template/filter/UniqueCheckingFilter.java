package com.hybris.backoffice.excel.template.filter;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;

public class UniqueCheckingFilter implements ExcelFilter<AttributeDescriptorModel>
{
    public boolean test(AttributeDescriptorModel attributeDescriptorModel)
    {
        return (attributeDescriptorModel.getUnique().booleanValue() || attributeDescriptorModel.getEnclosingType().getUniqueKeyAttributes().contains(attributeDescriptorModel));
    }
}
