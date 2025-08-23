package com.hybris.backoffice.solrsearch.providers.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantProductModel;
import java.util.Collections;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultProductCategoryAssignmentResolverTest
{
    @Spy
    private DefaultProductCategoryAssignmentResolver provider;


    @Test
    public void getIndexedCategoriesShouldReturnOwnAndBaseProductsCategoryAssignments()
    {
        VariantProductModel variant = (VariantProductModel)Mockito.mock(VariantProductModel.class);
        ProductModel baseProduct = (ProductModel)Mockito.mock(ProductModel.class);
        Mockito.when(variant.getBaseProduct()).thenReturn(baseProduct);
        CategoryModel baseCategory = (CategoryModel)Mockito.mock(CategoryModel.class);
        CategoryModel subCategory = (CategoryModel)Mockito.mock(CategoryModel.class);
        Mockito.when(baseProduct.getSupercategories()).thenReturn(Collections.singleton(baseCategory));
        Mockito.when(variant.getSupercategories()).thenReturn(Collections.singleton(subCategory));
        Set indexedCategories = this.provider.getIndexedCategories((ProductModel)variant);
        Assertions.assertThat(indexedCategories).contains(new Object[] {baseCategory, subCategory});
    }
}
