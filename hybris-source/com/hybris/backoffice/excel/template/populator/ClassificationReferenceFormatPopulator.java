package com.hybris.backoffice.excel.template.populator;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.translators.ExcelAttributeTranslatorRegistry;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Required;

class ClassificationReferenceFormatPopulator implements ExcelClassificationCellPopulator
{
    private ExcelAttributeTranslatorRegistry registry;


    public String apply(@Nonnull ExcelAttributeContext<ExcelClassificationAttribute> populatorContext)
    {
        return this.registry.findTranslator(populatorContext.getExcelAttribute(ExcelClassificationAttribute.class))
                        .map(translator -> translator.referenceFormat(populatorContext.getExcelAttribute(ExcelClassificationAttribute.class)))
                        .orElse("");
    }


    @Required
    public void setRegistry(ExcelAttributeTranslatorRegistry registry)
    {
        this.registry = registry;
    }
}
