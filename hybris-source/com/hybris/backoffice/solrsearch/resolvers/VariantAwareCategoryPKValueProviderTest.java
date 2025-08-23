package com.hybris.backoffice.solrsearch.resolvers;

import com.hybris.backoffice.solrsearch.providers.ProductCategoryAssignmentResolver;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VariantAwareCategoryPKValueProviderTest
{
    @Spy
    private VariantAwareCategoryPKValueResolver provider;
    @Mock
    private ProductCategoryAssignmentResolver valueProvider;


    @Test
    public void shouldAddPKsToDocument() throws FieldValueProviderException
    {
        this.provider.setCategoryAttributeValueProvider(this.valueProvider);
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        Set<CategoryModel> categories = new LinkedHashSet();
        CategoryModel classA = (CategoryModel)Mockito.mock(CategoryModel.class);
        categories.add(classA);
        Mockito.when(classA.getPk()).thenReturn(PK.fromLong(1L));
        CategoryModel classB = (CategoryModel)Mockito.mock(CategoryModel.class);
        categories.add(classB);
        Mockito.when(classB.getPk()).thenReturn(PK.fromLong(2L));
        ((ProductCategoryAssignmentResolver)Mockito.doReturn(categories).when(this.valueProvider)).getIndexedCategories(product);
        IndexedProperty property = (IndexedProperty)Mockito.mock(IndexedProperty.class);
        InputDocument document = (InputDocument)Mockito.mock(InputDocument.class);
        this.provider.addFieldValues(document, null, property, product, null);
        ((InputDocument)Mockito.verify(document)).addField(property, Long.valueOf(1L));
        ((InputDocument)Mockito.verify(document)).addField(property, Long.valueOf(2L));
    }
}
