package com.hybris.backoffice.solrsearch.providers;

import com.hybris.backoffice.proxy.LabelServiceProxy;
import com.hybris.backoffice.search.utils.CategoryCatalogVersionMapper;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.Locale;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CategoryWithCatalogVersionFacetDisplayNameProviderTest
{
    private static final String CATEGORY_CODE = "category";
    private static final String CATALOG_NAME = "Default";
    private static final String CATALOG_VERSION_NAME = "Staged";
    private static final String NAME = "category@@Default@@Staged";
    private static final String LANGUAGE_EN = "en";
    @InjectMocks
    private CategoryWithCatalogVersionFacetDisplayNameProvider provider;
    @Mock
    private CategoryCatalogVersionMapper categoryCatalogVersionMapper;
    @Mock
    private CatalogVersionService catalogVersionService;
    @Mock
    private CategoryService categoryService;
    @Mock
    private LabelServiceProxy labelServiceProxy;


    @Test
    public void shouldReturnCategoryName()
    {
        SearchQuery query = (SearchQuery)Mockito.mock(SearchQuery.class);
        ((SearchQuery)Mockito.doReturn("en").when(query)).getLanguage();
        CategoryCatalogVersionMapper.CategoryWithCatalogVersion decoded = new CategoryCatalogVersionMapper.CategoryWithCatalogVersion("category", "Default", "Staged");
        ((CategoryCatalogVersionMapper)Mockito.doReturn(decoded).when(this.categoryCatalogVersionMapper)).decode("category@@Default@@Staged");
        CatalogVersionModel catalogVersionMock = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        ((CatalogVersionService)Mockito.doReturn(catalogVersionMock).when(this.catalogVersionService)).getCatalogVersion("Default", "Staged");
        CategoryModel categoryMock = (CategoryModel)Mockito.mock(CategoryModel.class);
        ((CategoryService)Mockito.doReturn(categoryMock).when(this.categoryService)).getCategoryForCode(catalogVersionMock, "category");
        this.provider.getDisplayName(query, "category@@Default@@Staged");
        ((LabelServiceProxy)Mockito.verify(this.labelServiceProxy)).getObjectLabel(Matchers.eq(categoryMock), (Locale)Matchers.any());
    }


    @Test
    public void shouldReturnNameWhenExceptionIsThrownOnGettingLabelForName()
    {
        String name = "category@@Default@@Staged";
        SearchQuery query = (SearchQuery)Mockito.mock(SearchQuery.class);
        CategoryCatalogVersionMapper.CategoryWithCatalogVersion decoded = new CategoryCatalogVersionMapper.CategoryWithCatalogVersion("category", "Default", "Staged");
        ((CategoryCatalogVersionMapper)Mockito.doReturn(decoded).when(this.categoryCatalogVersionMapper)).decode("category@@Default@@Staged");
        ((CatalogVersionService)Mockito.doThrow(RuntimeException.class).when(this.catalogVersionService)).getCatalogVersion((String)Matchers.any(), (String)Matchers.any());
        String displayName = this.provider.getDisplayName(query, "category@@Default@@Staged");
        Assertions.assertThat(displayName).isEqualTo("category@@Default@@Staged");
    }
}
