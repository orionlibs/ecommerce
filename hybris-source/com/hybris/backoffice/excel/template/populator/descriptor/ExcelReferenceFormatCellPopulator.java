package com.hybris.backoffice.excel.template.populator.descriptor;

import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.template.populator.ExcelAttributeContext;
import com.hybris.backoffice.excel.template.populator.ExcelCellPopulator;
import com.hybris.backoffice.excel.translators.ExcelTranslatorRegistry;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import org.springframework.beans.factory.annotation.Required;

class ExcelReferenceFormatCellPopulator implements ExcelCellPopulator<ExcelAttributeDescriptorAttribute>
{
    private ExcelTranslatorRegistry registry;


    public String apply(ExcelAttributeContext<ExcelAttributeDescriptorAttribute> populatorContext)
    {
        AttributeDescriptorModel attributeDescriptor = ((ExcelAttributeDescriptorAttribute)populatorContext.getExcelAttribute(ExcelAttributeDescriptorAttribute.class)).getAttributeDescriptorModel();
        return this.registry.getTranslator(attributeDescriptor)
                        .map(excelValueTranslator -> excelValueTranslator.referenceFormat(attributeDescriptor)).orElse("");
    }


    @Required
    public void setRegistry(ExcelTranslatorRegistry registry)
    {
        this.registry = registry;
    }
}
