package com.hybris.backoffice.searchservices.events;

import com.hybris.backoffice.searchservices.model.BackofficeIndexedTypeToSearchservicesIndexConfigModel;
import com.hybris.backoffice.searchservices.services.impl.BackofficeSearchservicesFacetSearchConfigService;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.searchservices.enums.SnDocumentOperationType;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSourceOperation;
import de.hybris.platform.searchservices.indexer.service.SnIndexerRequest;
import de.hybris.platform.searchservices.indexer.service.SnIndexerResponse;
import de.hybris.platform.searchservices.indexer.service.SnIndexerService;
import de.hybris.platform.searchservices.indexer.service.impl.DefaultSnIndexerContext;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class DirectSearchservicesIndexSynchronizationStrategyTest
{
    public static final String PRODUCT_TYPECODE = "Product";
    public static final long PK_1 = 1L;
    public static final String INDEX_TYPE_ID = "test_backoffice_product";
    @Mock
    private ComposedTypeModel typeModel;
    @Mock
    private SnIndexTypeModel snIndexTypeModel;
    @Mock
    private BackofficeIndexedTypeToSearchservicesIndexConfigModel backofficeIndexedTypeToSearchservicesIndexConfigModel;
    @Mock
    private SnIndexerService snIndexerService;
    @Mock
    private BackofficeSearchservicesFacetSearchConfigService backofficeFacetSearchConfigService;
    @Mock
    private TypeService typeService;
    @InjectMocks
    private final DirectSearchservicesIndexSynchronizationStrategy strategy = new DirectSearchservicesIndexSynchronizationStrategy();


    @Before
    public void init()
    {
        MockitoAnnotations.initMocks(this);
        Mockito.when(this.typeService.getTypeForCode("Product")).thenReturn(this.typeModel);
        Mockito.when(this.snIndexTypeModel.getIndexerCronJobs()).thenReturn(new ArrayList());
        Mockito.when(this.backofficeIndexedTypeToSearchservicesIndexConfigModel.getSnIndexType()).thenReturn(this.snIndexTypeModel);
        Mockito.when(this.backofficeFacetSearchConfigService.getIndexedTypeModel("Product")).thenReturn(this.snIndexTypeModel);
        Mockito.when(this.backofficeFacetSearchConfigService.findSearchConfigForTypeCode("Product")).thenReturn(this.backofficeIndexedTypeToSearchservicesIndexConfigModel);
        Mockito.when(this.snIndexTypeModel.getId()).thenReturn("test_backoffice_product");
    }


    @Test
    public void testRemoveItemWhenIndexInitialized() throws SnIndexerException
    {
        SnIndexerRequest indexerRequest = (SnIndexerRequest)Mockito.mock(SnIndexerRequest.class);
        SnIndexerResponse snIndexerResponse = (SnIndexerResponse)Mockito.mock(SnIndexerResponse.class);
        Mockito.when(this.snIndexerService.createIncrementalIndexerRequest((String)Matchers.eq("test_backoffice_product"), (List)Mockito.any())).thenReturn(indexerRequest);
        Mockito.when(this.snIndexerService.index((SnIndexerRequest)Mockito.any())).thenReturn(snIndexerResponse);
        Mockito.when(Boolean.valueOf(this.backofficeIndexedTypeToSearchservicesIndexConfigModel.isActive())).thenReturn(Boolean.valueOf(true));
        this.strategy.removeItem("Product", 1L);
        ArgumentCaptor<List> indexerItemSourceOperationsList = ArgumentCaptor.forClass(List.class);
        ((SnIndexerService)Mockito.verify(this.snIndexerService)).createIncrementalIndexerRequest((String)Matchers.eq("test_backoffice_product"), (List)indexerItemSourceOperationsList.capture());
        List<SnIndexerItemSourceOperation> indexerItemSourceOperations = (List<SnIndexerItemSourceOperation>)indexerItemSourceOperationsList.getValue();
        Assertions.assertThat(indexerItemSourceOperations).hasSize(1);
        SnIndexerItemSourceOperation indexerItemSourceOperation = indexerItemSourceOperations.get(0);
        Assertions.assertThat((Comparable)indexerItemSourceOperation.getDocumentOperationType()).isEqualTo(SnDocumentOperationType.DELETE);
        Assertions.assertThat(indexerItemSourceOperation.getIndexerItemSource().getPks((SnIndexerContext)new DefaultSnIndexerContext())).hasSize(1);
        Assertions.assertThat(indexerItemSourceOperation.getIndexerItemSource().getPks((SnIndexerContext)new DefaultSnIndexerContext()))
                        .containsExactly((Object[])new PK[] {PK.fromLong(1L)});
        ((SnIndexerService)Mockito.verify(this.snIndexerService)).index(indexerRequest);
    }


    @Test
    public void testNotRemoveItemWhenIndexNotInitialized() throws SnIndexerException
    {
        SnIndexerRequest indexerRequest = (SnIndexerRequest)Mockito.mock(SnIndexerRequest.class);
        SnIndexerResponse snIndexerResponse = (SnIndexerResponse)Mockito.mock(SnIndexerResponse.class);
        Mockito.when(this.snIndexerService.createIncrementalIndexerRequest((String)Matchers.eq("test_backoffice_product"), (List)Mockito.any())).thenReturn(indexerRequest);
        Mockito.when(this.snIndexerService.index((SnIndexerRequest)Mockito.any())).thenReturn(snIndexerResponse);
        Mockito.when(Boolean.valueOf(this.backofficeIndexedTypeToSearchservicesIndexConfigModel.isActive())).thenReturn(Boolean.valueOf(false));
        this.strategy.removeItem("Product", 1L);
        ArgumentCaptor<List> indexerItemSourceOperationsList = ArgumentCaptor.forClass(List.class);
        ((SnIndexerService)Mockito.verify(this.snIndexerService, Mockito.never())).createIncrementalIndexerRequest((String)Matchers.eq("test_backoffice_product"), (List)indexerItemSourceOperationsList.capture());
    }


    @Test
    public void testUpdateItemWhenIndexInitialized() throws SnIndexerException
    {
        SnIndexerRequest indexerRequest = (SnIndexerRequest)Mockito.mock(SnIndexerRequest.class);
        SnIndexerResponse snIndexerResponse = (SnIndexerResponse)Mockito.mock(SnIndexerResponse.class);
        Mockito.when(this.snIndexerService.createIncrementalIndexerRequest((String)Matchers.eq("test_backoffice_product"), (List)Mockito.any())).thenReturn(indexerRequest);
        Mockito.when(this.snIndexerService.index((SnIndexerRequest)Mockito.any())).thenReturn(snIndexerResponse);
        Mockito.when(Boolean.valueOf(this.backofficeIndexedTypeToSearchservicesIndexConfigModel.isActive())).thenReturn(Boolean.valueOf(true));
        this.strategy.updateItem("Product", 1L);
        ArgumentCaptor<List> indexerItemSourceOperationsList = ArgumentCaptor.forClass(List.class);
        ((SnIndexerService)Mockito.verify(this.snIndexerService)).createIncrementalIndexerRequest((String)Matchers.eq("test_backoffice_product"), (List)indexerItemSourceOperationsList.capture());
        List<SnIndexerItemSourceOperation> indexerItemSourceOperations = (List<SnIndexerItemSourceOperation>)indexerItemSourceOperationsList.getValue();
        Assertions.assertThat(indexerItemSourceOperations).hasSize(1);
        SnIndexerItemSourceOperation indexerItemSourceOperation = indexerItemSourceOperations.get(0);
        Assertions.assertThat((Comparable)indexerItemSourceOperation.getDocumentOperationType()).isEqualTo(SnDocumentOperationType.CREATE_UPDATE);
        Assertions.assertThat(indexerItemSourceOperation.getIndexerItemSource().getPks((SnIndexerContext)new DefaultSnIndexerContext())).hasSize(1);
        Assertions.assertThat(indexerItemSourceOperation.getIndexerItemSource().getPks((SnIndexerContext)new DefaultSnIndexerContext()))
                        .containsExactly((Object[])new PK[] {PK.fromLong(1L)});
        ((SnIndexerService)Mockito.verify(this.snIndexerService)).index(indexerRequest);
    }


    @Test
    public void testNotUpdateItemWhenIndexNotInitialized() throws SnIndexerException
    {
        SnIndexerRequest indexerRequest = (SnIndexerRequest)Mockito.mock(SnIndexerRequest.class);
        SnIndexerResponse snIndexerResponse = (SnIndexerResponse)Mockito.mock(SnIndexerResponse.class);
        Mockito.when(this.snIndexerService.createIncrementalIndexerRequest((String)Matchers.eq("test_backoffice_product"), (List)Mockito.any())).thenReturn(indexerRequest);
        Mockito.when(this.snIndexerService.index((SnIndexerRequest)Mockito.any())).thenReturn(snIndexerResponse);
        Mockito.when(Boolean.valueOf(this.backofficeIndexedTypeToSearchservicesIndexConfigModel.isActive())).thenReturn(Boolean.valueOf(false));
        this.strategy.updateItem("Product", 1L);
        ArgumentCaptor<List> indexerItemSourceOperationsList = ArgumentCaptor.forClass(List.class);
        ((SnIndexerService)Mockito.verify(this.snIndexerService, Mockito.never())).createIncrementalIndexerRequest((String)Matchers.eq("test_backoffice_product"), (List)indexerItemSourceOperationsList.capture());
    }
}
