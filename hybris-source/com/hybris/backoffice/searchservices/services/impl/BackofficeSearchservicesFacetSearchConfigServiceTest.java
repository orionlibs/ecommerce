package com.hybris.backoffice.searchservices.services.impl;

import com.hybris.backoffice.search.cache.BackofficeFacetSearchConfigCache;
import com.hybris.backoffice.search.daos.FacetSearchConfigDAO;
import com.hybris.backoffice.searchservices.model.BackofficeIndexedTypeToSearchservicesIndexConfigModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class BackofficeSearchservicesFacetSearchConfigServiceTest
{
    private static final String PRODUCT_TYPECODE = "Product";
    private static final String INDEX_TYPE_ID = "Backoffice-Product";
    private static final String INDEX_TYPE_ID1 = "Other-Product";
    @Mock
    private TypeService typeService;
    @Mock
    private FacetSearchConfigDAO facetSearchConfigDAO;
    @Mock
    private BackofficeFacetSearchConfigCache backofficeFacetSearchConfigCache;
    @Mock
    private ComposedTypeModel composedType;
    @Mock
    private BackofficeIndexedTypeToSearchservicesIndexConfigModel searchConfig;
    @Mock
    private SnIndexTypeModel snIndexTypeModel;
    @InjectMocks
    private final BackofficeSearchservicesFacetSearchConfigService backofficeSearchservicesFacetSearchConfigService = new BackofficeSearchservicesFacetSearchConfigService();


    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);
        Mockito.when(this.snIndexTypeModel.getId()).thenReturn("Backoffice-Product");
        Mockito.when(this.searchConfig.getSnIndexType()).thenReturn(this.snIndexTypeModel);
        Mockito.when(this.searchConfig.getIndexedType()).thenReturn(this.composedType);
        Mockito.when(this.typeService.getComposedTypeForCode("Product")).thenReturn(this.composedType);
        Mockito.when(this.facetSearchConfigDAO.findSearchConfigurationsForTypes((List)ArgumentMatchers.any())).thenReturn(Arrays.asList(new BackofficeIndexedTypeToSearchservicesIndexConfigModel[] {this.searchConfig}));
    }


    @Test
    public void shouldReturnSearchConfig()
    {
        Optional<BackofficeIndexedTypeToSearchservicesIndexConfigModel> searchConfigOptional = this.backofficeSearchservicesFacetSearchConfigService.findSearchConfigForTypeCodeAndIndexTypeId("Product", "Backoffice-Product");
        Assert.assertEquals(searchConfigOptional, Optional.ofNullable(this.searchConfig));
    }


    @Test
    public void shouldReturnNullWhenIndexTypeIdNotMatch()
    {
        Optional<BackofficeIndexedTypeToSearchservicesIndexConfigModel> searchConfigOptional = this.backofficeSearchservicesFacetSearchConfigService.findSearchConfigForTypeCodeAndIndexTypeId("Product", "Other-Product");
        Assert.assertEquals(searchConfigOptional, Optional.empty());
    }


    @Test
    public void shouldReturnNullWhenSearchConfigIsNull()
    {
        Mockito.when(this.facetSearchConfigDAO.findSearchConfigurationsForTypes((List)ArgumentMatchers.any())).thenReturn(null);
        Optional<BackofficeIndexedTypeToSearchservicesIndexConfigModel> searchConfigOptional = this.backofficeSearchservicesFacetSearchConfigService.findSearchConfigForTypeCodeAndIndexTypeId("Product", "Other-Product");
        Assert.assertEquals(searchConfigOptional, Optional.empty());
    }
}
