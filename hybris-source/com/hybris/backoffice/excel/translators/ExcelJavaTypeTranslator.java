package com.hybris.backoffice.excel.translators;

import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import com.hybris.backoffice.excel.util.ExcelDateUtils;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public class ExcelJavaTypeTranslator extends AbstractExcelValueTranslator<Object>
{
    private ExcelDateUtils excelDateUtils;
    private ExcelFilter<AttributeDescriptorModel> excelUniqueFilter;
    private ExcelFilter<AttributeDescriptorModel> mandatoryFilter;


    public boolean canHandle(AttributeDescriptorModel attributeDescriptorModel)
    {
        boolean isAtomicType = attributeDescriptorModel.getAttributeType() instanceof de.hybris.platform.core.model.type.AtomicTypeModel;
        boolean isLocalizedAtomicField = (attributeDescriptorModel.getLocalized().booleanValue() && attributeDescriptorModel.getAttributeType() instanceof MapTypeModel
                        && ((MapTypeModel)attributeDescriptorModel.getAttributeType()).getReturntype() instanceof de.hybris.platform.core.model.type.AtomicTypeModel);
        boolean isPk = "pk".equals(attributeDescriptorModel.getQualifier());
        return (!isPk && (isAtomicType || isLocalizedAtomicField));
    }


    public Optional<Object> exportData(Object objectToExport)
    {
        if(objectToExport instanceof Date)
        {
            return Optional.of(this.excelDateUtils.exportDate((Date)objectToExport));
        }
        return Optional.ofNullable(objectToExport);
    }


    public ImpexValue importValue(AttributeDescriptorModel attributeDescriptor, ImportParameters importParameters)
    {
        if(Date.class.getCanonicalName().equals(attributeDescriptor.getAttributeType().getCode()))
        {
            return importDate(attributeDescriptor, importParameters);
        }
        return new ImpexValue(importParameters.getCellValue(), (new ImpexHeaderValue.Builder(attributeDescriptor.getQualifier()))
                        .withUnique(this.excelUniqueFilter.test(attributeDescriptor)).withMandatory(getMandatoryFilter().test(attributeDescriptor))
                        .withLang(importParameters.getIsoCode()).withQualifier(attributeDescriptor.getQualifier()).build());
    }


    protected ImpexValue importDate(AttributeDescriptorModel attributeDescriptor, ImportParameters importParameters)
    {
        String dateToImport = (importParameters.getCellValue() != null) ? this.excelDateUtils.importDate(importParameters.getCellValue().toString()) : null;
        return new ImpexValue(dateToImport, (new ImpexHeaderValue.Builder(attributeDescriptor
                        .getQualifier()))
                        .withUnique(this.excelUniqueFilter.test(attributeDescriptor))
                        .withMandatory(getMandatoryFilter().test(attributeDescriptor)).withLang(importParameters.getIsoCode())
                        .withDateFormat(this.excelDateUtils.getDateTimeFormat()).withQualifier(attributeDescriptor.getQualifier()).build());
    }


    public String referenceFormat(AttributeDescriptorModel attributeDescriptor)
    {
        return Date.class.getCanonicalName().equals(attributeDescriptor.getAttributeType().getCode()) ?
                        this.excelDateUtils.getDateTimeFormat() : "";
    }


    public ExcelDateUtils getExcelDateUtils()
    {
        return this.excelDateUtils;
    }


    @Required
    public void setExcelDateUtils(ExcelDateUtils excelDateUtils)
    {
        this.excelDateUtils = excelDateUtils;
    }


    public ExcelFilter<AttributeDescriptorModel> getExcelUniqueFilter()
    {
        return this.excelUniqueFilter;
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
