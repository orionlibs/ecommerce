package com.hybris.backoffice.searchservices.aspects;

import com.hybris.backoffice.cockpitng.dataaccess.facades.object.savedvalues.ItemModificationHistoryService;
import com.hybris.backoffice.searchservices.events.SearchservicesIndexSynchronizationStrategy;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacadeOperationResult;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectAccessException;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collections;
import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.aspectj.lang.JoinPoint;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ObjectFacadeSearchservicesIndexingAspectTest
{
    private static final String PRODUCT_TYPE = "Product";
    private static final PK CHANGED_PRODUCT_PK = PK.fromLong(1L);
    private static final PK FAILED_PRODUCT_PK = PK.fromLong(2L);
    @Spy
    @InjectMocks
    private ObjectFacadeSearchservicesIndexingAspect searchservicesIndexingAspect;
    @Mock
    private SearchservicesIndexSynchronizationStrategy synchronizationStrategy;
    @Mock
    private ItemModificationHistoryService itemModificationHistoryService;
    @Mock
    private ModelService modelService;
    @Mock
    private ProductModel changedProduct;
    @Mock
    private ProductModel failedProduct;
    @Mock
    private ConfigurationService configurationService;


    @Before
    public void setUp()
    {
        ((ProductModel)Mockito.doReturn(CHANGED_PRODUCT_PK).when(this.changedProduct)).getPk();
        ((ModelService)Mockito.doReturn("Product").when(this.modelService)).getModelType(this.changedProduct);
        Mockito.lenient().when(this.failedProduct.getPk()).thenReturn(FAILED_PRODUCT_PK);
        Mockito.lenient().when(this.modelService.getModelType(this.failedProduct)).thenReturn("Product");
        ((ConfigurationService)Mockito.doReturn(Mockito.mock(Configuration.class)).when(this.configurationService)).getConfiguration();
    }


    @Test
    public void shouldCleanIndexForRemovedModel()
    {
        this.searchservicesIndexingAspect.updateRemoved((JoinPoint)new JoinPointStub(new Object[] {this.changedProduct}, ), null);
        ArgumentCaptor<String> typeCodeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List> pkCaptor = ArgumentCaptor.forClass(List.class);
        ((SearchservicesIndexSynchronizationStrategy)Mockito.verify(this.synchronizationStrategy)).removeItems((String)typeCodeCaptor.capture(), (List)pkCaptor.capture());
        Assertions.assertThat((String)typeCodeCaptor.getValue()).isEqualToIgnoringCase("Product");
        Assertions.assertThat((List)pkCaptor.getValue()).containsExactly(new Object[] {CHANGED_PRODUCT_PK});
    }


    @Test
    public void shouldCleanIndexForRemovedModels()
    {
        ObjectFacadeOperationResult result = new ObjectFacadeOperationResult();
        result.addFailedObject(this.failedProduct, new ObjectAccessException("", new RuntimeException()));
        this.searchservicesIndexingAspect.updateRemoved((JoinPoint)new JoinPointStub(new Object[] {Collections.singletonList(this.changedProduct)}, ), result);
        ArgumentCaptor<String> typeCodeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List> pkCaptor = ArgumentCaptor.forClass(List.class);
        ((SearchservicesIndexSynchronizationStrategy)Mockito.verify(this.synchronizationStrategy)).removeItems((String)typeCodeCaptor.capture(), (List)pkCaptor.capture());
        Assertions.assertThat((String)typeCodeCaptor.getValue()).isEqualToIgnoringCase("Product");
        Assertions.assertThat((List)pkCaptor.getValue()).containsExactly(new Object[] {CHANGED_PRODUCT_PK});
    }


    @Test
    public void shouldUpdateIndexForUpdatedModel()
    {
        this.searchservicesIndexingAspect.updateChanged((JoinPoint)new JoinPointStub(new Object[] {this.changedProduct}, ), null);
        ArgumentCaptor<String> typeCodeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List> pkCaptor = ArgumentCaptor.forClass(List.class);
        ((SearchservicesIndexSynchronizationStrategy)Mockito.verify(this.synchronizationStrategy)).updateItems((String)typeCodeCaptor.capture(), (List)pkCaptor.capture());
        Assertions.assertThat((String)typeCodeCaptor.getValue()).isEqualToIgnoringCase("Product");
        Assertions.assertThat((List)pkCaptor.getValue()).containsExactly(new Object[] {CHANGED_PRODUCT_PK});
    }


    @Test
    public void shouldUpdateIndexForUpdatedModels()
    {
        ObjectFacadeOperationResult result = new ObjectFacadeOperationResult();
        result.addFailedObject(this.failedProduct, new ObjectAccessException("", new RuntimeException()));
        this.searchservicesIndexingAspect.updateChanged((JoinPoint)new JoinPointStub(new Object[] {Collections.singletonList(this.changedProduct)}, ), result);
        ArgumentCaptor<String> typeCodeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List> pkCaptor = ArgumentCaptor.forClass(List.class);
        ((SearchservicesIndexSynchronizationStrategy)Mockito.verify(this.synchronizationStrategy)).updateItems((String)typeCodeCaptor.capture(), (List)pkCaptor.capture());
        Assertions.assertThat((String)typeCodeCaptor.getValue()).isEqualToIgnoringCase("Product");
        Assertions.assertThat((List)pkCaptor.getValue()).containsExactly(new Object[] {CHANGED_PRODUCT_PK});
    }
}
