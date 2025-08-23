package com.hybris.backoffice.excel.translators;

import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.RelationDescriptorModel;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "2005", forRemoval = true)
public class ExcelBaseProductTypeTranslator extends AbstractCatalogVersionAwareTranslator<ProductModel>
{
    private ExcelFilter<AttributeDescriptorModel> excelUniqueFilter;
    private ExcelFilter<AttributeDescriptorModel> mandatoryFilter;
    private static final String PATTERN = "%s:%s";


    public boolean canHandle(AttributeDescriptorModel attributeDescriptor)
    {
        return (attributeDescriptor instanceof RelationDescriptorModel && attributeDescriptor
                        .getAttributeType() instanceof de.hybris.platform.core.model.type.ComposedTypeModel && "Product2VariantRelation"
                        .equals(((RelationDescriptorModel)attributeDescriptor).getRelationType().getCode()));
    }


    public Optional<Object> exportData(ProductModel objectToExport)
    {
        if(objectToExport != null && objectToExport.getCatalogVersion() != null)
        {
            CatalogVersionModel catalogVersion = objectToExport.getCatalogVersion();
            return Optional.ofNullable(String.format("%s:%s", new Object[] {objectToExport.getCode(), exportCatalogVersionData(catalogVersion)}));
        }
        return Optional.empty();
    }


    public String referenceFormat(AttributeDescriptorModel attributeDescriptor)
    {
        return String.format("%s:%s", new Object[] {"baseProduct", referenceCatalogVersionFormat()});
    }


    public ImpexValue importValue(AttributeDescriptorModel attributeDescriptor, ImportParameters importParameters)
    {
        String catalogVersion = catalogVersionData(importParameters.getSingleValueParameters());
        String baseProduct = (catalogVersion != null) ? String.format("%s:%s", new Object[] {importParameters.getSingleValueParameters().get("baseProduct"), catalogVersion}) : null;
        return new ImpexValue(baseProduct, (new ImpexHeaderValue.Builder(
                        String.format("%s(%s, %s)", new Object[] {"baseProduct", "code", catalogVersionHeader("Product")}))).withUnique(this.excelUniqueFilter.test(attributeDescriptor))
                        .withMandatory(getMandatoryFilter().test(attributeDescriptor)).withLang(importParameters.getIsoCode())
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
