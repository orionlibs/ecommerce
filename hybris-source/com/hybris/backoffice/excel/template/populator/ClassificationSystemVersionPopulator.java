package com.hybris.backoffice.excel.template.populator;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import javax.annotation.Nonnull;

class ClassificationSystemVersionPopulator implements ExcelClassificationCellPopulator
{
    public String apply(@Nonnull ExcelAttributeContext<ExcelClassificationAttribute> context)
    {
        return ((ExcelClassificationAttribute)context.getExcelAttribute(ExcelClassificationAttribute.class)).getAttributeAssignment().getSystemVersion()
                        .getVersion();
    }
}
