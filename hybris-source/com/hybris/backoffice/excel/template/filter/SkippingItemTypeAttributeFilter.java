package com.hybris.backoffice.excel.template.filter;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import org.apache.commons.lang3.StringUtils;

public class SkippingItemTypeAttributeFilter implements ExcelFilter<AttributeDescriptorModel>
{
    public boolean test(AttributeDescriptorModel attributeDescriptorModel)
    {
        return !StringUtils.equals("itemtype", attributeDescriptorModel.getQualifier());
    }
}
