package com.hybris.backoffice.searchservices.providers.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.core.service.SnExpressionEvaluator;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.variants.model.VariantProductModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractProductSnIndexerValueProviderTest
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
    private TestAbstractProductSnIndexerValueProvider provider;
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
    protected static final Set<Class<?>> SUPPORTED_QUALIFIER_CLASSES = Set.of(Locale.class);
    private static final String FIELD_1_ID = "field1";


    @Test
    public void shouldCollectProductsWithCurrentProduct() throws SnIndexerException
    {
        List<SnIndexerFieldWrapper> fieldWrappers = new ArrayList<>();
        fieldWrappers.add(this.fieldWrapper);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("productSelector", "CURRENT");
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        Map<String, Set<ProductModel>> productsResult = this.provider.collectProducts(fieldWrappers, product);
        Assertions.assertThat(((Set)productsResult.get("CURRENT")).size()).isEqualTo(1);
        Assertions.assertThat(((Set)productsResult.get("CURRENT")).contains(product))
                        .isEqualTo(true);
    }


    @Test
    public void shouldCollectProductsWithCurrentParentProduct() throws SnIndexerException
    {
        List<SnIndexerFieldWrapper> fieldWrappers = new ArrayList<>();
        fieldWrappers.add(this.fieldWrapper);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("productSelector", "CURRENT_PARENT");
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        Map<String, Set<ProductModel>> productsResult = this.provider.collectProducts(fieldWrappers, product);
        Assertions.assertThat(((Set)productsResult.get("CURRENT_PARENT")).size())
                        .isEqualTo(1);
        Assertions.assertThat(((Set)productsResult
                                        .get("CURRENT_PARENT")).contains(product))
                        .isEqualTo(true);
    }


    @Test
    public void shouldCollectProductsWithBaseProduct() throws SnIndexerException
    {
        List<SnIndexerFieldWrapper> fieldWrappers = new ArrayList<>();
        fieldWrappers.add(this.fieldWrapper);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("productSelector", "BASE");
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        Map<String, Set<ProductModel>> productsResult = this.provider.collectProducts(fieldWrappers, product);
        Assertions.assertThat(((Set)productsResult.get("BASE")).size()).isEqualTo(1);
        Assertions.assertThat(((Set)productsResult.get("BASE")).contains(product))
                        .isEqualTo(true);
    }


    @Test(expected = SnIndexerException.class)
    public void shouldThrowExceptionWhenCollectProducts() throws SnIndexerException
    {
        List<SnIndexerFieldWrapper> fieldWrappers = new ArrayList<>();
        fieldWrappers.add(this.fieldWrapper);
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("productSelector", "exceptionParameters");
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        this.provider.collectProducts(fieldWrappers, product);
    }


    @Test
    public void shouldCollectCurrentProduct() throws SnIndexerException
    {
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        Set<ProductModel> productsResult = this.provider.collectCurrentProduct(product);
        Assertions.assertThat(productsResult.size()).isEqualTo(1);
        Assertions.assertThat(productsResult.contains(product)).isEqualTo(true);
    }


    @Test
    public void shouldCollectCurrentParentProductsOfProduct() throws SnIndexerException
    {
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        Set<ProductModel> productsResult = this.provider.collectCurrentParentProducts(product);
        Assertions.assertThat(productsResult.size()).isEqualTo(1);
        Assertions.assertThat(productsResult.contains(product)).isEqualTo(true);
    }


    @Test
    public void shouldCollectCurrentParentProductsOfVariantProduct() throws SnIndexerException
    {
        VariantProductModel variantProduct = (VariantProductModel)Mockito.mock(VariantProductModel.class);
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        Mockito.when(variantProduct.getBaseProduct()).thenReturn(product);
        Set<ProductModel> productsResult = this.provider.collectCurrentParentProducts((ProductModel)variantProduct);
        Assertions.assertThat(productsResult.size()).isEqualTo(2);
        Assertions.assertThat(productsResult.contains(product)).isEqualTo(true);
        Assertions.assertThat(productsResult.contains(product)).isEqualTo(true);
    }


    @Test
    public void shouldCollectBaseProductOfProduct() throws SnIndexerException
    {
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        Set<ProductModel> productsResult = this.provider.collectBaseProduct(product);
        Assertions.assertThat(productsResult.size()).isEqualTo(1);
        Assertions.assertThat(productsResult.contains(product)).isEqualTo(true);
    }


    @Test
    public void shouldCollectBaseProductOfVariantProduct() throws SnIndexerException
    {
        VariantProductModel variantProduct = (VariantProductModel)Mockito.mock(VariantProductModel.class);
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        Mockito.when(variantProduct.getBaseProduct()).thenReturn(product);
        Set<ProductModel> productsResult = this.provider.collectBaseProduct((ProductModel)variantProduct);
        Assertions.assertThat(productsResult.size()).isEqualTo(1);
        Assertions.assertThat(productsResult.contains(product)).isEqualTo(true);
    }


    @Test
    public void shouldMergeCurrentParentProducts() throws SnIndexerException
    {
        Set<ProductModel> currentProducts = new HashSet<>();
        Set<ProductModel> baseProducts = new HashSet<>();
        Set<ProductModel> currentParentProducts = new HashSet<>();
        ProductModel product1 = (ProductModel)Mockito.mock(ProductModel.class);
        ProductModel product2 = (ProductModel)Mockito.mock(ProductModel.class);
        ProductModel product3 = (ProductModel)Mockito.mock(ProductModel.class);
        currentProducts.add(product1);
        baseProducts.add(product2);
        currentParentProducts.add(product3);
        Mockito.when(this.productsMap.get("CURRENT_PARENT"))
                        .thenReturn(currentParentProducts);
        Mockito.lenient().when(this.productsMap.getOrDefault("CURRENT", Collections.emptySet()))
                        .thenReturn(currentProducts);
        Mockito.lenient().when(this.productsMap.getOrDefault("BASE", Collections.emptySet()))
                        .thenReturn(baseProducts);
        Assertions.assertThat(this.provider.mergeProducts(this.productsMap).size()).isEqualTo(1);
        Assertions.assertThat(this.provider.mergeProducts(this.productsMap).contains(product3)).isEqualTo(true);
    }


    @Test
    public void shouldMergeCurrentAndBaseProducts() throws SnIndexerException
    {
        Set<ProductModel> currentProducts = new HashSet<>();
        Set<ProductModel> baseProducts = new HashSet<>();
        ProductModel product1 = (ProductModel)Mockito.mock(ProductModel.class);
        ProductModel product2 = (ProductModel)Mockito.mock(ProductModel.class);
        currentProducts.add(product1);
        baseProducts.add(product2);
        Mockito.when(this.productsMap.get("CURRENT_PARENT")).thenReturn(null);
        Mockito.when(this.productsMap.getOrDefault("CURRENT", Collections.emptySet()))
                        .thenReturn(currentProducts);
        Mockito.when(this.productsMap.getOrDefault("BASE", Collections.emptySet()))
                        .thenReturn(baseProducts);
        Assertions.assertThat(this.provider.mergeProducts(this.productsMap).size()).isEqualTo(2);
        Assertions.assertThat(this.provider.mergeProducts(this.productsMap).contains(product1)).isEqualTo(true);
        Assertions.assertThat(this.provider.mergeProducts(this.productsMap).contains(product2)).isEqualTo(true);
    }


    @Test
    public void shouldResolveProductSelector()
    {
        Map<String, String> valueProviderParameters = new HashMap<>();
        valueProviderParameters.put("productSelector", "CURRENT");
        Mockito.when(this.fieldWrapper.getValueProviderParameters()).thenReturn(valueProviderParameters);
        Mockito.lenient().when(this.fieldWrapper.getField()).thenReturn(this.snField);
        Mockito.lenient().when(this.snField.getId()).thenReturn("field1");
        Assertions.assertThat(this.provider.resolveProductSelector(this.fieldWrapper))
                        .isEqualTo("CURRENT");
    }
}
