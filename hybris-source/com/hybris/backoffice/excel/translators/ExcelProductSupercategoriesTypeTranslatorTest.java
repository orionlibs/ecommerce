package com.hybris.backoffice.excel.translators;

import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.RelationDescriptorModel;
import de.hybris.platform.core.model.type.RelationMetaTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
public class ExcelProductSupercategoriesTypeTranslatorTest
{
    @Mock
    public CatalogTypeService catalogTypeService;
    @Mock
    public TypeService typeService;
    @InjectMocks
    private ExcelProductSupercategoriesTypeTranslator translator;


    @Before
    public void setUp()
    {
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
        Collection<CategoryModel> categories = generate(3, "some", "defaultcatalog", "Staged");
        Objects.requireNonNull(String.class);
        String result = this.translator.exportData(categories).map(String.class::cast).get();
        String expectedResultPart = String.format("%s:%s:%s", new Object[] {"some", "defaultcatalog", "Staged"});
        String expectedResult = String.format("%s,%s,%s", new Object[] {expectedResultPart, expectedResultPart, expectedResultPart});
        Assertions.assertThat(result).isEqualTo(expectedResult);
    }


    @Test
    public void shouldGivenTypeBeHandled()
    {
        RelationDescriptorModel relationDescriptor = (RelationDescriptorModel)Mockito.mock(RelationDescriptorModel.class);
        RelationMetaTypeModel relationMetaType = (RelationMetaTypeModel)Mockito.mock(RelationMetaTypeModel.class);
        ComposedTypeModel productComposedType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        BDDMockito.given(relationMetaType.getCode()).willReturn("CategoryProductRelation");
        BDDMockito.given(relationDescriptor.getRelationType()).willReturn(relationMetaType);
        BDDMockito.given(relationDescriptor.getEnclosingType()).willReturn(productComposedType);
        BDDMockito.given(productComposedType.getCode()).willReturn("Product");
        BDDMockito.given(Boolean.valueOf(this.typeService.isAssignableFrom("Product", "Product"))).willReturn(Boolean.valueOf(true));
        boolean canHandle = this.translator.canHandle((AttributeDescriptorModel)relationDescriptor);
        Assertions.assertThat(canHandle).isTrue();
    }


    private Collection<CategoryModel> generate(int noOfItems, String code, String id, String version)
    {
        return (Collection<CategoryModel>)IntStream.range(0, noOfItems).mapToObj(idx -> {
            CategoryModel category = (CategoryModel)Mockito.mock(CategoryModel.class);
            CatalogVersionModel catalogVersion = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
            CatalogModel catalog = (CatalogModel)Mockito.mock(CatalogModel.class);
            BDDMockito.given(catalog.getId()).willReturn(id);
            BDDMockito.given(catalogVersion.getVersion()).willReturn(version);
            BDDMockito.given(catalogVersion.getCatalog()).willReturn(catalog);
            BDDMockito.given(category.getCatalogVersion()).willReturn(catalogVersion);
            BDDMockito.given(category.getCode()).willReturn(code);
            return category;
        }).collect(Collectors.toList());
    }


    @Test
    public void shouldImportSupercategories()
    {
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptor.getQualifier()).willReturn("supercategories");
        List<Map<String, String>> parameters = new ArrayList<>();
        Map<String, String> firstCategoryParams = new HashMap<>();
        firstCategoryParams.put("catalog", "Clothing");
        firstCategoryParams.put("version", "Online");
        firstCategoryParams.put("category", "First category");
        parameters.add(firstCategoryParams);
        Map<String, String> secondCategoryParams = new HashMap<>();
        secondCategoryParams.put("catalog", "Default");
        secondCategoryParams.put("version", "Staged");
        secondCategoryParams.put("category", "Second category");
        parameters.add(secondCategoryParams);
        ImportParameters importParameters = new ImportParameters("Product", null, null, UUID.randomUUID().toString(), parameters);
        ImpexValue impexValue = this.translator.importValue(attributeDescriptor, importParameters);
        Assertions.assertThat(impexValue.getValue()).isEqualTo("First category:Online:Clothing,Second category:Staged:Default");
        Assertions.assertThat(impexValue.getHeaderValue().getName()).isEqualTo("supercategories(code, catalogVersion(version,catalog(id)))");
    }


    @Test
    public void shouldImportEmptyCellWhenProductDoesntHaveAnySupercategories()
    {
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptor.getQualifier()).willReturn("supercategories");
        List<Map<String, String>> parameters = new ArrayList<>();
        ImportParameters importParameters = new ImportParameters("Product", null, null, UUID.randomUUID().toString(), parameters);
        ImpexValue impexValue = this.translator.importValue(attributeDescriptor, importParameters);
        Assertions.assertThat(impexValue.getValue()).isEqualTo("");
        Assertions.assertThat(impexValue.getHeaderValue().getName()).isEqualTo("supercategories(code, catalogVersion(version,catalog(id)))");
    }
}
