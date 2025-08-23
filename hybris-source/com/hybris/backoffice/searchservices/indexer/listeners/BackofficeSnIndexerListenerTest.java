package com.hybris.backoffice.searchservices.indexer.listeners;

import com.hybris.backoffice.searchservices.model.BackofficeIndexedTypeToSearchservicesIndexConfigModel;
import com.hybris.backoffice.searchservices.services.impl.BackofficeSearchservicesFacetSearchConfigService;
import de.hybris.platform.searchservices.admin.data.SnIndexType;
import de.hybris.platform.searchservices.enums.SnIndexerOperationType;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class BackofficeSnIndexerListenerTest
{
    private static final String PRODUCT_TYPECODE = "Product";
    private static final String INDEX_TYPE_ID = "Backoffice-Product";
    private static final String INDEX_TYPE_ID1 = "Other-Product";
    @Mock
    private BackofficeSearchservicesFacetSearchConfigService backofficeFacetSearchConfigService;
    @Mock
    private SnIndexerContext indexerContext;
    @Mock
    private BackofficeIndexedTypeToSearchservicesIndexConfigModel backofficeIndexedTypeToSearchservicesIndexConfigModel;
    @Mock
    private ModelService modelService;
    @Mock
    private SnIndexTypeModel snIndexTypeModel;
    @Mock
    private SnIndexType snIndexType;
    @InjectMocks
    private final BackofficeSnIndexerListener backofficeSnIndexerListener = new BackofficeSnIndexerListener();


    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);
        Mockito.when(this.backofficeFacetSearchConfigService.findSearchConfigForTypeCodeAndIndexTypeId("Product", "Backoffice-Product")).thenReturn(Optional.of(this.backofficeIndexedTypeToSearchservicesIndexConfigModel));
        Mockito.when(this.indexerContext.getIndexType()).thenReturn(this.snIndexType);
        Mockito.when(this.backofficeIndexedTypeToSearchservicesIndexConfigModel.getSnIndexType()).thenReturn(this.snIndexTypeModel);
        Mockito.when(this.snIndexType.getItemComposedType()).thenReturn("Product");
        Mockito.when(this.snIndexTypeModel.getId()).thenReturn("Backoffice-Product");
    }


    @Test
    public void shouldSetIndexToActiveWhenFullIndexIsFull()
    {
        Mockito.when(this.indexerContext.getIndexerOperationType()).thenReturn(SnIndexerOperationType.FULL);
        Mockito.when(this.snIndexType.getId()).thenReturn("Backoffice-Product");
        Mockito.when(Boolean.valueOf(this.backofficeIndexedTypeToSearchservicesIndexConfigModel.isActive())).thenReturn(Boolean.valueOf(false));
        this.backofficeSnIndexerListener.afterIndex(this.indexerContext);
        ((ModelService)Mockito.verify(this.modelService)).save(Mockito.any());
    }


    @Test
    public void shouldNotSetIndexToActiveWhenFullIndexIsActive()
    {
        Mockito.when(this.indexerContext.getIndexerOperationType()).thenReturn(SnIndexerOperationType.FULL);
        Mockito.when(this.snIndexType.getId()).thenReturn("Backoffice-Product");
        Mockito.when(Boolean.valueOf(this.backofficeIndexedTypeToSearchservicesIndexConfigModel.isActive())).thenReturn(Boolean.valueOf(true));
        this.backofficeSnIndexerListener.afterIndex(this.indexerContext);
        ((ModelService)Mockito.verify(this.modelService, Mockito.never())).save(Mockito.any());
    }


    @Test
    public void shouldNotSetIndexToActiveWhenIsNotFullIndex()
    {
        Mockito.when(this.indexerContext.getIndexerOperationType()).thenReturn(SnIndexerOperationType.INCREMENTAL);
        Mockito.when(this.snIndexType.getId()).thenReturn("Backoffice-Product");
        Mockito.when(Boolean.valueOf(this.backofficeIndexedTypeToSearchservicesIndexConfigModel.isActive())).thenReturn(Boolean.valueOf(false));
        this.backofficeSnIndexerListener.afterIndex(this.indexerContext);
        ((ModelService)Mockito.verify(this.modelService, Mockito.never())).save(Mockito.any());
    }


    @Test
    public void shouldNotSetIndexToActiveWhenIndexTypeIdNotMatch()
    {
        Mockito.when(this.indexerContext.getIndexerOperationType()).thenReturn(SnIndexerOperationType.FULL);
        Mockito.when(this.snIndexType.getId()).thenReturn("Other-Product");
        Mockito.when(Boolean.valueOf(this.backofficeIndexedTypeToSearchservicesIndexConfigModel.isActive())).thenReturn(Boolean.valueOf(false));
        this.backofficeSnIndexerListener.afterIndex(this.indexerContext);
        ((ModelService)Mockito.verify(this.modelService, Mockito.never())).save(Mockito.any());
    }
}
