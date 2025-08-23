package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.RelationDescriptorModel;
import de.hybris.platform.core.model.type.RelationMetaTypeModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelCategoryFieldValidatorTest
{
    public static final String FIRST_CATALOG = "firstCatalog";
    public static final String FIRST_VERSION = "firstVersion";
    public static final String FIRST_CATEGORY = "firstCategory";
    public static final String SECOND_CATEGORY = "secondCategory";
    public static final String NOT_EXISTING_CATEGORY = "not existing category";
    public static final String NOT_BLANK = "notBlank";
    @Mock
    private CatalogVersionService catalogVersionService;
    @Mock
    private CategoryService categoryService;
    @InjectMocks
    private ExcelCategoryValidator excelCategoryValidator;


    @Before
    public void setup()
    {
        CategoryModel firstCategory = (CategoryModel)Mockito.mock(CategoryModel.class);
        CategoryModel secondCategory = (CategoryModel)Mockito.mock(CategoryModel.class);
        CatalogVersionModel firstCatalogVersion = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        BDDMockito.given(this.catalogVersionService.getCatalogVersion("firstCatalog", "firstVersion")).willReturn(firstCatalogVersion);
        BDDMockito.given(this.categoryService.getCategoryForCode(firstCatalogVersion, "firstCategory")).willReturn(firstCategory);
        Mockito.lenient().when(this.categoryService.getCategoryForCode(firstCatalogVersion, "secondCategory")).thenReturn(secondCategory);
    }


    @Test
    public void shouldHandleCategoryWhenAttributeIsCategoryProductRelation()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, "notBlank", null, new ArrayList());
        RelationDescriptorModel attributeDescriptor = (RelationDescriptorModel)Mockito.mock(RelationDescriptorModel.class);
        RelationMetaTypeModel relationMetaTypeModel = (RelationMetaTypeModel)Mockito.mock(RelationMetaTypeModel.class);
        BDDMockito.given(attributeDescriptor.getRelationType()).willReturn(relationMetaTypeModel);
        BDDMockito.given(relationMetaTypeModel.getCode()).willReturn("CategoryProductRelation");
        boolean canHandle = this.excelCategoryValidator.canHandle(importParameters, (AttributeDescriptorModel)attributeDescriptor);
        Assertions.assertThat(canHandle).isTrue();
    }


    @Test
    public void shouldNotHandleCategoryWhenCellIsEmpty()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, "", null, new ArrayList());
        RelationDescriptorModel attributeDescriptor = (RelationDescriptorModel)Mockito.mock(RelationDescriptorModel.class);
        RelationMetaTypeModel relationMetaTypeModel = (RelationMetaTypeModel)Mockito.mock(RelationMetaTypeModel.class);
        Mockito.lenient().when(attributeDescriptor.getRelationType()).thenReturn(relationMetaTypeModel);
        Mockito.lenient().when(relationMetaTypeModel.getCode()).thenReturn("CategoryProductRelation");
        boolean canHandle = this.excelCategoryValidator.canHandle(importParameters, (AttributeDescriptorModel)attributeDescriptor);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldNotHandleCategoryWhenAttributeIsNotCategoryProductRelation()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, "notBlank", null, new ArrayList());
        RelationDescriptorModel attributeDescriptor = (RelationDescriptorModel)Mockito.mock(RelationDescriptorModel.class);
        RelationMetaTypeModel relationMetaTypeModel = (RelationMetaTypeModel)Mockito.mock(RelationMetaTypeModel.class);
        BDDMockito.given(attributeDescriptor.getRelationType()).willReturn(relationMetaTypeModel);
        BDDMockito.given(relationMetaTypeModel.getCode()).willReturn("Product2KeywordRelation");
        boolean canHandle = this.excelCategoryValidator.canHandle(importParameters, (AttributeDescriptorModel)attributeDescriptor);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldNotHandleCategoryWhenAttributeIsNotRelationDescriptor()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, "notBlank", null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        boolean canHandle = this.excelCategoryValidator.canHandle(importParameters, attributeDescriptor);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldNotReturnErrorWhenCategoryExists()
    {
        List<Map<String, String>> parametersList = new ArrayList<>();
        Map<String, String> singleParams = new HashMap<>();
        parametersList.add(singleParams);
        singleParams.put("catalog", "firstCatalog");
        singleParams.put("version", "firstVersion");
        singleParams.put("category", "firstCategory");
        ImportParameters importParameters = new ImportParameters("Product", null, "notBlank", null, parametersList);
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        ExcelValidationResult validationCellResult = this.excelCategoryValidator.validate(importParameters, attributeDescriptor, new HashMap<>());
        Assertions.assertThat(validationCellResult.hasErrors()).isFalse();
        Assertions.assertThat(validationCellResult.getValidationErrors()).isEmpty();
    }


    @Test
    public void shouldReturnErrorWhenCategoryDoesNotExist()
    {
        List<Map<String, String>> parametersList = new ArrayList<>();
        Map<String, String> singleParams = new HashMap<>();
        parametersList.add(singleParams);
        singleParams.put("catalog", "firstCatalog");
        singleParams.put("version", "firstVersion");
        singleParams.put("category", "not existing category");
        ImportParameters importParameters = new ImportParameters("Product", null, "notBlank", null, parametersList);
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        ExcelValidationResult validationCellResult = this.excelCategoryValidator.validate(importParameters, attributeDescriptor, new HashMap<>());
        Assertions.assertThat(validationCellResult.hasErrors()).isTrue();
        Assertions.assertThat(validationCellResult.getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validationCellResult.getValidationErrors().get(0)).getMessageKey()).isEqualTo("excel.import.validation.category.doesntmatch");
    }
}
