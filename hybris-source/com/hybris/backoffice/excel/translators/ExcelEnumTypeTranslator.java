package com.hybris.backoffice.excel.translators;

import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public class ExcelEnumTypeTranslator extends AbstractExcelValueTranslator<HybrisEnumValue>
{
    private ExcelFilter<AttributeDescriptorModel> excelUniqueFilter;
    private ExcelFilter<AttributeDescriptorModel> mandatoryFilter;


    public boolean canHandle(AttributeDescriptorModel attributeDescriptorModel)
    {
        return attributeDescriptorModel.getAttributeType() instanceof de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
    }


    public Optional<Object> exportData(HybrisEnumValue enumToExport)
    {
        return Optional.<HybrisEnumValue>ofNullable(enumToExport).map(HybrisEnumValue::getCode);
    }


    public ImpexValue importValue(AttributeDescriptorModel attributeDescriptor, ImportParameters importParameters)
    {
        return new ImpexValue(importParameters.getCellValue(), (new ImpexHeaderValue.Builder(
                        String.format("%s(%s)", new Object[] {attributeDescriptor.getQualifier(), "code"}))).withUnique(this.excelUniqueFilter.test(attributeDescriptor))
                        .withMandatory(this.mandatoryFilter.test(attributeDescriptor)).withLang(importParameters.getIsoCode())
                        .withQualifier(attributeDescriptor.getQualifier()).build());
    }


    @Required
    public void setExcelUniqueFilter(ExcelFilter<AttributeDescriptorModel> excelUniqueFilter)
    {
        this.excelUniqueFilter = excelUniqueFilter;
    }


    public ExcelFilter<AttributeDescriptorModel> getMandatoryFilter()
    {
        return this.mandatoryFilter;
    }


    @Required
    public void setMandatoryFilter(ExcelFilter<AttributeDescriptorModel> mandatoryFilter)
    {
        this.mandatoryFilter = mandatoryFilter;
    }
}
