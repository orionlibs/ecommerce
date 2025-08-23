package com.hybris.backoffice.solrsearch.resolvers;

import com.hybris.backoffice.search.utils.CategoryCatalogVersionMapper;
import com.hybris.backoffice.solrsearch.providers.ProductCategoryAssignmentResolver;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CategoryCodeWithCatalogVersionMappingValueResolverTest
{
    @InjectMocks
    private CategoryCodeWithCatalogVersionMappingValueResolver resolver;
    @Mock
    private ProductCategoryAssignmentResolver valueProvider;
    @Mock
    private CategoryCatalogVersionMapper categoryCatalogVersionMapper;


    @Test
    public void shouldAddEncodedCategoryToDocument() throws FieldValueProviderException
    {
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        Set<CategoryModel> categories = new LinkedHashSet();
        CategoryModel classA = (CategoryModel)Mockito.mock(CategoryModel.class);
        categories.add(classA);
        CategoryModel classB = (CategoryModel)Mockito.mock(CategoryModel.class);
        categories.add(classB);
        ((ProductCategoryAssignmentResolver)Mockito.doReturn(categories).when(this.valueProvider)).getIndexedCategories(product);
        ((CategoryCatalogVersionMapper)Mockito.doReturn("cat1@@default@@staged").when(this.categoryCatalogVersionMapper)).encode(classA);
        ((CategoryCatalogVersionMapper)Mockito.doReturn("cat2@@default@@staged").when(this.categoryCatalogVersionMapper)).encode(classB);
        IndexedProperty property = (IndexedProperty)Mockito.mock(IndexedProperty.class);
        InputDocument document = (InputDocument)Mockito.mock(InputDocument.class);
        this.resolver.addFieldValues(document, null, property, product, null);
        ((InputDocument)Mockito.verify(document)).addField(property, "cat1@@default@@staged");
        ((InputDocument)Mockito.verify(document)).addField(property, "cat2@@default@@staged");
    }
}
