package com.hybris.backoffice.solrsearch.resolvers;

import com.hybris.backoffice.solrsearch.providers.ProductCategoryAssignmentResolver;
import de.hybris.platform.category.model.CategoryModel;
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
public class VariantAwareCategoryCodeValueResolverTest
{
    @Spy
    private VariantAwareCategoryCodeValueResolver resolver;
    @Mock
    private ProductCategoryAssignmentResolver valueProvider;


    @Test
    public void shouldAddCodesToDocument() throws FieldValueProviderException
    {
        this.resolver.setCategoryAttributeValueProvider(this.valueProvider);
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        Set<CategoryModel> categories = new LinkedHashSet();
        CategoryModel classA = (CategoryModel)Mockito.mock(CategoryModel.class);
        categories.add(classA);
        Mockito.when(classA.getCode()).thenReturn("a");
        CategoryModel classB = (CategoryModel)Mockito.mock(CategoryModel.class);
        categories.add(classB);
        Mockito.when(classB.getCode()).thenReturn("b");
        ((ProductCategoryAssignmentResolver)Mockito.doReturn(categories).when(this.valueProvider)).getIndexedCategories(product);
        IndexedProperty property = (IndexedProperty)Mockito.mock(IndexedProperty.class);
        InputDocument document = (InputDocument)Mockito.mock(InputDocument.class);
        this.resolver.addFieldValues(document, null, property, product, null);
        ((InputDocument)Mockito.verify(document)).addField(property, "a");
        ((InputDocument)Mockito.verify(document)).addField(property, "b");
    }
}
