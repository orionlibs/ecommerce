package com.hybris.backoffice.solrsearch.providers.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultAllProductCategoryAssignmentResolverTest
{
    @Spy
    private DefaultAllProductCategoryAssignmentResolver resolver;


    @Test
    public void getIndexedCategories()
    {
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        CategoryModel catA = (CategoryModel)Mockito.mock(CategoryModel.class);
        Set<CategoryModel> assignments = Collections.singleton(catA);
        ((ProductModel)Mockito.doReturn(assignments).when(product)).getSupercategories();
        this.resolver.getIndexedCategories(product);
        ArgumentCaptor<Collection> captor = ArgumentCaptor.forClass(Collection.class);
        ((DefaultAllProductCategoryAssignmentResolver)Mockito.verify(this.resolver)).resolveSuperCategories((Collection)captor.capture());
        Assertions.assertThat((Iterable)captor.getValue()).contains(new Object[] {catA});
    }


    @Test
    public void resolveSuperCategories()
    {
        CategoryModel catA = (CategoryModel)Mockito.mock(CategoryModel.class);
        CategoryModel catA1 = (CategoryModel)Mockito.mock(CategoryModel.class);
        ((CategoryModel)Mockito.doReturn(Collections.singletonList(catA1)).when(catA)).getAllSupercategories();
        CategoryModel catB = (CategoryModel)Mockito.mock(CategoryModel.class);
        CategoryModel catB1 = (CategoryModel)Mockito.mock(CategoryModel.class);
        CategoryModel catB2 = (CategoryModel)Mockito.mock(CategoryModel.class);
        ((CategoryModel)Mockito.doReturn(List.of(catB1, catB2)).when(catB)).getAllSupercategories();
        Collection<CategoryModel> res = this.resolver.resolveSuperCategories(List.of(catA, catB));
        Assertions.assertThat(res).containsAll(List.of(catA1, catB1, catB2));
    }
}
