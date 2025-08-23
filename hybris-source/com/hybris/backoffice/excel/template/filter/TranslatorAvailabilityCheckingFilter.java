package com.hybris.backoffice.excel.template.filter;

import com.hybris.backoffice.excel.translators.ExcelTranslatorRegistry;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Required;

public class TranslatorAvailabilityCheckingFilter implements ExcelFilter<AttributeDescriptorModel>
{
    private ExcelTranslatorRegistry excelTranslatorRegistry;


    public boolean test(@Nonnull AttributeDescriptorModel attributeDescriptor)
    {
        return this.excelTranslatorRegistry.getTranslator(attributeDescriptor).isPresent();
    }


    @Required
    public void setExcelTranslatorRegistry(ExcelTranslatorRegistry excelTranslatorRegistry)
    {
        this.excelTranslatorRegistry = excelTranslatorRegistry;
    }
}
