package com.hybris.backoffice.excel.translators;

import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "2005", forRemoval = true)
public class ExcelCatalogVersionTypeTranslator extends AbstractCatalogVersionAwareTranslator<CatalogVersionModel>
{
    private ExcelFilter<AttributeDescriptorModel> excelUniqueFilter;
    private ExcelFilter<AttributeDescriptorModel> mandatoryFilter;


    public boolean canHandle(AttributeDescriptorModel attributeDescriptorModel)
    {
        TypeModel typeModel = getTypeService().getTypeForCode("CatalogVersion");
        return getTypeService().isAssignableFrom(typeModel, attributeDescriptorModel.getAttributeType());
    }


    public Optional<Object> exportData(CatalogVersionModel objectToExport)
    {
        return Optional.ofNullable(exportCatalogVersionData(objectToExport));
    }


    public ImpexValue importValue(AttributeDescriptorModel attributeDescriptor, ImportParameters importParameters)
    {
        return new ImpexValue(catalogVersionData(importParameters.getSingleValueParameters()), (new ImpexHeaderValue.Builder(
                        catalogVersionHeader("Product")))
                        .withUnique(this.excelUniqueFilter.test(attributeDescriptor))
                        .withMandatory(getMandatoryFilter().test(attributeDescriptor)).withLang(importParameters.getIsoCode())
                        .withQualifier(attributeDescriptor.getQualifier()).build());
    }


    public String referenceFormat(AttributeDescriptorModel attributeDescriptor)
    {
        return referenceCatalogVersionFormat();
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
