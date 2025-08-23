package com.hybris.backoffice.excel.translators;

import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.RelationDescriptorModel;
import de.hybris.platform.core.model.type.RelationMetaTypeModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelBaseProductTypeTranslatorTest
{
    @Mock
    private ExcelFilter<AttributeDescriptorModel> uniqueFilter;
    @Mock
    private ExcelFilter<AttributeDescriptorModel> mandatoryFilter;
    @Mock
    public CatalogTypeService catalogTypeService;
    @InjectMocks
    private ExcelBaseProductTypeTranslator translator;


    @Before
    public void setup()
    {
        ((ExcelFilter)Mockito.doAnswer(inv -> ((AttributeDescriptorModel)inv.getArguments()[0]).getUnique()).when(this.uniqueFilter)).test(Matchers.any());
        Mockito.when(this.catalogTypeService.getCatalogVersionContainerAttribute((String)Matchers.any())).thenReturn("catalogVersion");
    }


    @Test
    public void shouldExportDataBeNullSafe()
    {
        Assertions.assertThat(this.translator.exportData(null).isPresent()).isFalse();
    }


    @Test
    public void shouldExportedDataBeInProperFormat()
    {
        String id = "defaultcatalog";
        String code = "some";
        String version = "Staged";
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        CatalogVersionModel catalogVersion = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        CatalogModel catalog = (CatalogModel)Mockito.mock(CatalogModel.class);
        BDDMockito.given(catalog.getId()).willReturn("defaultcatalog");
        BDDMockito.given(catalogVersion.getVersion()).willReturn("Staged");
        BDDMockito.given(catalogVersion.getCatalog()).willReturn(catalog);
        BDDMockito.given(product.getCatalogVersion()).willReturn(catalogVersion);
        BDDMockito.given(product.getCode()).willReturn("some");
        Objects.requireNonNull(String.class);
        String output = this.translator.exportData(product).map(String.class::cast).get();
        Assertions.assertThat(output).isEqualTo(String.format("%s:%s:%s", new Object[] {"some", "defaultcatalog", "Staged"}));
    }


    @Test
    public void shouldGivenTypeBeHandled()
    {
        RelationDescriptorModel relationDescriptor = (RelationDescriptorModel)Mockito.mock(RelationDescriptorModel.class);
        RelationMetaTypeModel relationMetaType = (RelationMetaTypeModel)Mockito.mock(RelationMetaTypeModel.class);
        ComposedTypeModel composedType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        BDDMockito.given(relationMetaType.getCode()).willReturn("Product2VariantRelation");
        BDDMockito.given(relationDescriptor.getAttributeType()).willReturn(composedType);
        BDDMockito.given(relationDescriptor.getRelationType()).willReturn(relationMetaType);
        boolean canHandle = this.translator.canHandle((AttributeDescriptorModel)relationDescriptor);
        Assertions.assertThat(canHandle).isTrue();
    }


    @Test
    public void shouldImportBaseProduct()
    {
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptor.getQualifier()).willReturn("baseProduct");
        List<Map<String, String>> parameters = new ArrayList<>();
        Map<String, String> variantParams = new HashMap<>();
        variantParams.put("catalog", "Clothing");
        variantParams.put("version", "Online");
        variantParams.put("baseProduct", "Abc");
        parameters.add(variantParams);
        ImportParameters importParameters = new ImportParameters("Product", null, null, UUID.randomUUID().toString(), parameters);
        ImpexValue impexValue = this.translator.importValue(attributeDescriptor, importParameters);
        Assertions.assertThat(impexValue.getValue()).isEqualTo("Abc:Online:Clothing");
        Assertions.assertThat(impexValue.getHeaderValue().getName()).isEqualTo("baseProduct(code, catalogVersion(version,catalog(id)))");
    }
}
