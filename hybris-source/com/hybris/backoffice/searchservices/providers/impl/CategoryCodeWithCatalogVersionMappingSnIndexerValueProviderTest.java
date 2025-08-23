package com.hybris.backoffice.searchservices.providers.impl;

import com.hybris.backoffice.search.utils.CategoryCatalogVersionMapper;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.core.service.SnExpressionEvaluator;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CategoryCodeWithCatalogVersionMappingSnIndexerValueProviderTest
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
    private CategoryCodeWithCatalogVersionMappingSnIndexerValueProvider provider;
    @Mock
    private SnExpressionEvaluator snExpressionEvaluator;
    @Mock
    private SnField snField;
    @Mock
    private CategoryModel item;
    @Mock
    private Map<String, Set<ProductModel>> productsMap;
    @Mock
    private CategoryCatalogVersionMapper categoryCatalogVersionMapper;
    private static final String FIELD_1_ID = "field1";


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
        this.provider.setCategoryCatalogVersionMapper(this.categoryCatalogVersionMapper);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("categorySelector", "categories");
        String categoryCodeFieldValue = "categoryCodeFieldValue";
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Mockito.when(this.data.getProducts()).thenReturn(this.productsMap);
        Mockito.when(this.productsMap.get(Matchers.any())).thenReturn(products);
        Mockito.when(product.getSupercategories()).thenReturn(categorys);
        Mockito.when(this.categoryCatalogVersionMapper.encode(category)).thenReturn(categoryCodeFieldValue);
        Object categoryCodeFieldValueCollection = this.provider.getFieldValue(this.indexerContext, this.fieldWrapper, this.source, this.data);
        Assertions.assertThat(categoryCodeFieldValueCollection instanceof List).isEqualTo(true);
        List categoryCodeFieldValueList = (List)categoryCodeFieldValueCollection;
        Assertions.assertThat(categoryCodeFieldValueList.size()).isEqualTo(1);
        Assertions.assertThat(categoryCodeFieldValueList.get(0)).isEqualTo(categoryCodeFieldValue);
    }
}
