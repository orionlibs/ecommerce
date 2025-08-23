package com.hybris.backoffice.excel.template.populator;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import javax.annotation.Nonnull;

class ClassificationAttributeLocLangPopulator implements ExcelClassificationCellPopulator
{
    public String apply(@Nonnull ExcelAttributeContext<ExcelClassificationAttribute> context)
    {
        ExcelClassificationAttribute excelAttribute = (ExcelClassificationAttribute)context.getExcelAttribute(ExcelClassificationAttribute.class);
        return excelAttribute.isLocalized() ? excelAttribute.getIsoCode() : "";
    }
}
