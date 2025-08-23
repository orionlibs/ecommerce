package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.translators.generic.factory.RequiredAttributeTestFactory;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ExcelGenericReferenceValidatorTest
{
    @Mock
    TypeService typeService;
    @InjectMocks
    ExcelGenericReferenceValidator excelGenericReferenceValidator;


    @Before
    public void setUpMockito()
    {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void shouldNotHandleRequestWhenTypeIsBlacklisted()
    {
        List<String> blacklistedTypes = Collections.singletonList("Media");
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        ComposedTypeModel mediaComposedType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        BDDMockito.given(attributeDescriptorModel.getAttributeType()).willReturn(mediaComposedType);
        BDDMockito.given(mediaComposedType.getCode()).willReturn("Media");
        BDDMockito.given(Boolean.valueOf(importParameters.isCellValueNotBlank())).willReturn(Boolean.valueOf(true));
        BDDMockito.given(Boolean.valueOf(this.typeService.isAssignableFrom("Media", "Media"))).willReturn(Boolean.valueOf(true));
        this.excelGenericReferenceValidator.setBlacklistedTypes(blacklistedTypes);
        boolean result = this.excelGenericReferenceValidator.canHandle(importParameters, attributeDescriptorModel);
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void shouldHandleRequestWhenTypeIsNotBlacklisted()
    {
        List<String> blacklistedTypes = Collections.singletonList("Media");
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        ComposedTypeModel productComposedType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        BDDMockito.given(attributeDescriptorModel.getAttributeType()).willReturn(productComposedType);
        BDDMockito.given(productComposedType.getCode()).willReturn("Product");
        BDDMockito.given(Boolean.valueOf(importParameters.isCellValueNotBlank())).willReturn(Boolean.valueOf(true));
        BDDMockito.given(Boolean.valueOf(this.typeService.isAssignableFrom("Media", "Media"))).willReturn(Boolean.valueOf(true));
        this.excelGenericReferenceValidator.setBlacklistedTypes(blacklistedTypes);
        boolean result = this.excelGenericReferenceValidator.canHandle(importParameters, attributeDescriptorModel);
        Assertions.assertThat(result).isTrue();
    }


    @Test
    public void shouldBuildFlexibleQueryToCheckWhetherCatalogExist()
    {
        Map<String, String> params = new HashMap<>();
        params.put("Catalog.id", "Default");
        Optional<FlexibleSearchQuery> flexibleSearchQuery = this.excelGenericReferenceValidator.buildFlexibleSearchQuery(RequiredAttributeTestFactory.prepareStructureForCatalog(), params, new HashMap<>());
        Assertions.assertThat(flexibleSearchQuery).isPresent();
        Assertions.assertThat(((FlexibleSearchQuery)flexibleSearchQuery.get()).getQuery()).isEqualToIgnoringCase("SELECT {pk} FROM {Catalog} WHERE {id} = ?id");
        Assertions.assertThat(((FlexibleSearchQuery)flexibleSearchQuery.get()).getQueryParameters()).containsKeys((Object[])new String[] {"id"});
        Assertions.assertThat(((FlexibleSearchQuery)flexibleSearchQuery.get()).getQueryParameters()).containsValues(new Object[] {"Default"});
    }


    @Test
    public void shouldBuildFlexibleQueryToCheckWhetherCatalogVersionExist()
    {
        long catalogPk = 123L;
        Map<String, String> params = new HashMap<>();
        params.put("CatalogVersion.version", "Online");
        params.put("Catalog.id", "Default");
        HashMap<Object, Object> context = new HashMap<>();
        context.put("Catalog_Default", prepareCatalog(123L));
        Optional<FlexibleSearchQuery> flexibleSearchQuery = this.excelGenericReferenceValidator.buildFlexibleSearchQuery(RequiredAttributeTestFactory.prepareStructureForCatalogVersion(), params, context);
        Assertions.assertThat(flexibleSearchQuery).isPresent();
        Assertions.assertThat(((FlexibleSearchQuery)flexibleSearchQuery.get()).getQuery())
                        .isEqualToIgnoringCase("SELECT {pk} FROM {CatalogVersion} WHERE {version} = ?version AND {catalog} = ?catalog");
        Assertions.assertThat(((FlexibleSearchQuery)flexibleSearchQuery.get()).getQueryParameters()).containsKeys((Object[])new String[] {"version", "catalog"});
        Assertions.assertThat(((FlexibleSearchQuery)flexibleSearchQuery.get()).getQueryParameters()).containsValues(new Object[] {"Online", Long.valueOf(123L)});
    }


    @Test
    public void shouldBuildFlexibleQueryToCheckWhetherSupercategoryExist()
    {
        long catalogPk = 123L;
        long catalogVersionPk = 987L;
        Map<String, String> params = new HashMap<>();
        params.put("Category.code", "Hardware");
        params.put("CatalogVersion.version", "Online");
        params.put("Catalog.id", "Default");
        HashMap<String, Object> context = new HashMap<>();
        context.put("Catalog_Default", prepareCatalog(123L));
        context.put("CatalogVersion_Online_Default", prepareCatalogVersion(987L));
        Optional<FlexibleSearchQuery> flexibleSearchQuery = this.excelGenericReferenceValidator.buildFlexibleSearchQuery(RequiredAttributeTestFactory.prepareStructureForSupercategories(), params, context);
        Assertions.assertThat(flexibleSearchQuery).isPresent();
        Assertions.assertThat(((FlexibleSearchQuery)flexibleSearchQuery.get()).getQuery())
                        .isEqualToIgnoringCase("SELECT {pk} FROM {Category} WHERE {code} = ?code AND {catalogVersion} = ?catalogVersion");
        Assertions.assertThat(((FlexibleSearchQuery)flexibleSearchQuery.get()).getQueryParameters()).containsKeys((Object[])new String[] {"code", "catalogVersion"});
        Assertions.assertThat(((FlexibleSearchQuery)flexibleSearchQuery.get()).getQueryParameters()).containsValues(new Object[] {"Hardware", Long.valueOf(987L)});
    }


    private CatalogModel prepareCatalog(long pkValue)
    {
        CatalogModel catalogModel = (CatalogModel)Mockito.mock(CatalogModel.class);
        BDDMockito.given(catalogModel.getPk()).willReturn(PK.fromLong(pkValue));
        return catalogModel;
    }


    private CatalogVersionModel prepareCatalogVersion(long pkValue)
    {
        CatalogVersionModel catalogVersionModel = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
        BDDMockito.given(catalogVersionModel.getPk()).willReturn(PK.fromLong(pkValue));
        return catalogVersionModel;
    }
}
