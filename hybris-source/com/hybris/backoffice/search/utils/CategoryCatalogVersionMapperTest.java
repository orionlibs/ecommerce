package com.hybris.backoffice.search.utils;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CategoryCatalogVersionMapperTest
{
    private static final String CODE = "code";
    private static final String VERSION = "version";
    private static final String CATALOG = "catalog";
    private CategoryCatalogVersionMapper mapper;


    @Before
    public void setUp()
    {
        this.mapper = new CategoryCatalogVersionMapper();
    }


    @Test
    public void shouldEncodeCategoryModel()
    {
        CategoryModel categoryModel = (CategoryModel)Mockito.mock(CategoryModel.class);
        ((CategoryModel)Mockito.doReturn("code").when(categoryModel)).getCode();
        CatalogVersionModel catalogVersionModel = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        ((CategoryModel)Mockito.doReturn(catalogVersionModel).when(categoryModel)).getCatalogVersion();
        ((CatalogVersionModel)Mockito.doReturn("version").when(catalogVersionModel)).getVersion();
        CatalogModel catalogModel = (CatalogModel)Mockito.mock(CatalogModel.class);
        ((CatalogVersionModel)Mockito.doReturn(catalogModel).when(catalogVersionModel)).getCatalog();
        ((CatalogModel)Mockito.doReturn("catalog").when(catalogModel)).getId();
        String encoded = this.mapper.encode(categoryModel);
        Assertions.assertThat(encoded).isEqualTo("code@@catalog@@version");
    }


    @Test
    public void shouldDecode()
    {
        CategoryCatalogVersionMapper.CategoryWithCatalogVersion encoded = this.mapper.decode("code@@catalog@@version");
        Assertions.assertThat(encoded.categoryCode).isEqualTo("code");
        Assertions.assertThat(encoded.catalogId).isEqualTo("catalog");
        Assertions.assertThat(encoded.catalogVersion).isEqualTo("version");
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNoDelimiterIsUsed()
    {
        this.mapper.decode("noDelimiter");
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenWrongNumberDelimiterIsUsed()
    {
        this.mapper.decode("too@@many@@delimiter@@are@@used");
    }
}
