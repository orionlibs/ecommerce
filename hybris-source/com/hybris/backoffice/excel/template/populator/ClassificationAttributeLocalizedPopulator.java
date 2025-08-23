package com.hybris.backoffice.excel.template.populator;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import javax.annotation.Nonnull;

class ClassificationAttributeLocalizedPopulator implements ExcelClassificationCellPopulator
{
    public String apply(@Nonnull ExcelAttributeContext<ExcelClassificationAttribute> context)
    {
        return String.valueOf(((ExcelClassificationAttribute)context.getExcelAttribute(ExcelClassificationAttribute.class)).isLocalized());
    }
}
