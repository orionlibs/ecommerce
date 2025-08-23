package com.hybris.backoffice.searchservices.providers.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnExpressionEvaluator;
import de.hybris.platform.searchservices.core.service.SnQualifier;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VariantAwareCategorySnIndexerValueProviderTest
{
    @Mock
    private SnIndexerContext indexerContext;
    @Mock
    private SnIndexerFieldWrapper fieldWrapper;
    @Mock
    private ProductModel source;
    @Mock
    private ProductAttributeSnIndexerValueProvider.ProductData data;
    @InjectMocks
    private VariantAwareCategorySnIndexerValueProvider provider;
    @Mock
    private SnExpressionEvaluator snExpressionEvaluator;
    @Mock
    private SnField snField;
    @Mock
    private ProductModel item;
    @Mock
    private Object value;
    @Mock
    private Map<String, Set<ProductModel>> productsMap;
    private static final String FIELD_1_ID = "field1";


    @Test
    public void shouldGetSupportedQualifierClasses() throws SnIndexerException
    {
        Assertions.assertThat(this.provider.getSupportedQualifierClasses()).containsExactly((Object[])new Class[] {Locale.class});
    }


    @Test
    public void shouldGetNullFieldValue() throws SnIndexerException
    {
        Set<ProductModel> products = new HashSet<>();
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("categorySelector", "categories");
        this.provider.setSnExpressionEvaluator(this.snExpressionEvaluator);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.when(this.snField.getId()).thenReturn("field1");
        Mockito.when(this.data.getProducts()).thenReturn(this.productsMap);
        Mockito.when(this.productsMap.get(Matchers.any())).thenReturn(products);
        Object fieldValue = this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, this.source, this.data);
        Assertions.assertThat(fieldValue).isEqualTo(null);
    }


    @Test
    public void shouldGetFieldValue() throws SnIndexerException, SnException
    {
        List<CategoryModel> categorys = new ArrayList<>();
        CategoryModel category = (CategoryModel)Mockito.mock(CategoryModel.class);
        categorys.add(category);
        Set<ProductModel> products = new HashSet<>();
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        products.add(product);
        this.provider.setSnExpressionEvaluator(this.snExpressionEvaluator);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("categorySelector", "categories");
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.when(this.snField.getId()).thenReturn("field1");
        Mockito.when(this.data.getProducts()).thenReturn(this.productsMap);
        Mockito.when(this.productsMap.get(Matchers.any())).thenReturn(products);
        Mockito.when(product.getSupercategories()).thenReturn(categorys);
        Mockito.when(this.snExpressionEvaluator.evaluate(Matchers.any(), (String)Matchers.any())).thenReturn(this.value);
        Object fieldValue = this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, this.source, this.data);
        Assertions.assertThat(fieldValue).isEqualTo(this.value);
    }


    @Test
    public void shouldGetLocalizedFieldValue() throws SnIndexerException, SnException
    {
        List<CategoryModel> categorys = new ArrayList<>();
        CategoryModel category = (CategoryModel)Mockito.mock(CategoryModel.class);
        categorys.add(category);
        Set<ProductModel> products = new HashSet<>();
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("categorySelector", "categories");
        SnQualifier qualifier1 = (SnQualifier)Mockito.mock(SnQualifier.class);
        SnQualifier qualifier2 = (SnQualifier)Mockito.mock(SnQualifier.class);
        List<SnQualifier> qualifiers = List.of(qualifier1, qualifier2);
        products.add(product);
        this.provider.setSnExpressionEvaluator(this.snExpressionEvaluator);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.when(this.snField.getId()).thenReturn("field1");
        Mockito.when(Boolean.valueOf(this.fieldWrapper.isLocalized())).thenReturn(Boolean.valueOf(true));
        Mockito.when(this.fieldWrapper.getQualifiers()).thenReturn(qualifiers);
        Mockito.when(qualifier1.getAs((Class)Matchers.any())).thenReturn(Locale.ENGLISH);
        Mockito.when(qualifier2.getAs((Class)Matchers.any())).thenReturn(Locale.GERMAN);
        Mockito.when(this.data.getProducts()).thenReturn(this.productsMap);
        Mockito.when(this.productsMap.get(Matchers.any())).thenReturn(products);
        Mockito.when(product.getSupercategories()).thenReturn(categorys);
        Mockito.when(this.snExpressionEvaluator.evaluate(Matchers.any(), (String)Matchers.any(), (List)Matchers.eq(List.of(Locale.ENGLISH, Locale.GERMAN)))).thenReturn(this.value);
        Object fieldValue = this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, this.source, this.data);
        Assertions.assertThat(fieldValue).isEqualTo(this.value);
    }


    @Test(expected = SnIndexerException.class)
    public void shouldCatchExceptionWhenGetFieldValue() throws SnException
    {
        List<CategoryModel> categorys = new ArrayList<>();
        CategoryModel category = (CategoryModel)Mockito.mock(CategoryModel.class);
        categorys.add(category);
        Set<ProductModel> products = new HashSet<>();
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("categorySelector", "categories");
        SnQualifier qualifier1 = (SnQualifier)Mockito.mock(SnQualifier.class);
        SnQualifier qualifier2 = (SnQualifier)Mockito.mock(SnQualifier.class);
        List<SnQualifier> qualifiers = List.of(qualifier1, qualifier2);
        products.add(product);
        this.provider.setSnExpressionEvaluator(this.snExpressionEvaluator);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.when(this.snField.getId()).thenReturn("field1");
        Mockito.when(Boolean.valueOf(this.fieldWrapper.isLocalized())).thenReturn(Boolean.valueOf(true));
        Mockito.when(this.fieldWrapper.getQualifiers()).thenReturn(qualifiers);
        Mockito.when(qualifier1.getAs((Class)Matchers.any())).thenReturn(Locale.ENGLISH);
        Mockito.when(qualifier2.getAs((Class)Matchers.any())).thenReturn(Locale.GERMAN);
        Mockito.when(this.data.getProducts()).thenReturn(this.productsMap);
        Mockito.when(this.productsMap.get(Matchers.any())).thenReturn(products);
        Mockito.when(product.getSupercategories()).thenReturn(categorys);
        BDDMockito.given(this.snExpressionEvaluator.evaluate(Matchers.any(), (String)Matchers.any(), (List)Matchers.eq(List.of(Locale.ENGLISH, Locale.GERMAN))))
                        .willThrow(new Throwable[] {(Throwable)new SnException()});
        this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, this.source, this.data);
    }


    @Test
    public void shouldCollectDirectCategories()
    {
        List<CategoryModel> categorys = new ArrayList<>();
        CategoryModel category = (CategoryModel)Mockito.mock(CategoryModel.class);
        categorys.add(category);
        Set<ProductModel> products = new HashSet<>();
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        products.add(product);
        Mockito.when(product.getSupercategories()).thenReturn(categorys);
        Assertions.assertThat(this.provider.collectDirectCategories(products).contains(category)).isEqualTo(true);
    }


    @Test
    public void shouldCollectAllCategories()
    {
        List<CategoryModel> categorys = new ArrayList<>();
        List<CategoryModel> superCategorys = new ArrayList<>();
        CategoryModel category = (CategoryModel)Mockito.mock(CategoryModel.class);
        CategoryModel superCategory = (CategoryModel)Mockito.mock(CategoryModel.class);
        categorys.add(category);
        superCategorys.add(superCategory);
        Set<ProductModel> products = new HashSet<>();
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        products.add(product);
        Mockito.when(product.getSupercategories()).thenReturn(categorys);
        Mockito.when(category.getAllSupercategories()).thenReturn(superCategorys);
        Assertions.assertThat(this.provider.collectAllCategories(products).contains(category)).isEqualTo(true);
        Assertions.assertThat(this.provider.collectAllCategories(products).contains(superCategory)).isEqualTo(true);
    }


    @Test(expected = SnIndexerException.class)
    public void shouldCatchExceptionWhenCollectAllCategories() throws SnIndexerException
    {
        this.provider.setSnExpressionEvaluator(this.snExpressionEvaluator);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("categorySelector", "exceptionParameters");
        List<CategoryModel> categorys = new ArrayList<>();
        List<CategoryModel> superCategorys = new ArrayList<>();
        CategoryModel category = (CategoryModel)Mockito.mock(CategoryModel.class);
        CategoryModel superCategory = (CategoryModel)Mockito.mock(CategoryModel.class);
        categorys.add(category);
        superCategorys.add(superCategory);
        Set<ProductModel> products = new HashSet<>();
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        products.add(product);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Mockito.when(this.data.getProducts()).thenReturn(this.productsMap);
        Mockito.when(this.productsMap.get(Matchers.any())).thenReturn(products);
        Mockito.lenient().when(product.getSupercategories()).thenReturn(categorys);
        Mockito.lenient().when(category.getAllSupercategories()).thenReturn(superCategorys);
        this.provider.collectCategories(this.fieldWrapper, this.data);
    }


    @Test
    public void shouldCollectCategoriesWithDirectCategories() throws SnIndexerException
    {
        this.provider.setSnExpressionEvaluator(this.snExpressionEvaluator);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("categorySelector", "categories");
        List<CategoryModel> categorys = new ArrayList<>();
        List<CategoryModel> superCategorys = new ArrayList<>();
        CategoryModel category = (CategoryModel)Mockito.mock(CategoryModel.class);
        CategoryModel superCategory = (CategoryModel)Mockito.mock(CategoryModel.class);
        categorys.add(category);
        superCategorys.add(superCategory);
        Set<ProductModel> products = new HashSet<>();
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        products.add(product);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Mockito.when(this.data.getProducts()).thenReturn(this.productsMap);
        Mockito.when(this.productsMap.get(Matchers.any())).thenReturn(products);
        Mockito.when(product.getSupercategories()).thenReturn(categorys);
        Mockito.lenient().when(category.getAllSupercategories()).thenReturn(superCategorys);
        Assertions.assertThat(this.provider.collectCategories(this.fieldWrapper, this.data).size()).isEqualTo(1);
        Assertions.assertThat(this.provider.collectCategories(this.fieldWrapper, this.data).contains(category)).isEqualTo(true);
    }


    @Test
    public void shouldCollectCategoriesWithAllCategories() throws SnIndexerException
    {
        this.provider.setSnExpressionEvaluator(this.snExpressionEvaluator);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("categorySelector", "allCategories");
        List<CategoryModel> categorys = new ArrayList<>();
        List<CategoryModel> superCategorys = new ArrayList<>();
        CategoryModel category = (CategoryModel)Mockito.mock(CategoryModel.class);
        CategoryModel superCategory = (CategoryModel)Mockito.mock(CategoryModel.class);
        categorys.add(category);
        superCategorys.add(superCategory);
        Set<ProductModel> products = new HashSet<>();
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        products.add(product);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Mockito.when(this.data.getProducts()).thenReturn(this.productsMap);
        Mockito.when(this.productsMap.get(Matchers.any())).thenReturn(products);
        Mockito.when(product.getSupercategories()).thenReturn(categorys);
        Mockito.when(category.getAllSupercategories()).thenReturn(superCategorys);
        Assertions.assertThat(this.provider.collectCategories(this.fieldWrapper, this.data).size()).isEqualTo(2);
        Assertions.assertThat(this.provider.collectCategories(this.fieldWrapper, this.data).contains(category)).isEqualTo(true);
        Assertions.assertThat(this.provider.collectAllCategories(products).contains(superCategory)).isEqualTo(true);
    }


    @Test
    public void shouldResolveCategorySelector()
    {
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("categorySelector", "categories");
        this.provider.setSnExpressionEvaluator(this.snExpressionEvaluator);
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Assertions.assertThat(this.provider.resolveCategorySelector(this.fieldWrapper))
                        .isEqualTo("categories");
    }
}
