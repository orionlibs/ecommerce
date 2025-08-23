package com.hybris.backoffice.searchservices.setup;

import com.hybris.backoffice.events.ExternalEventCallback;
import com.hybris.backoffice.search.events.AfterInitializationEndBackofficeSearchListener;
import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import com.hybris.backoffice.searchservices.model.BackofficeIndexedTypeToSearchservicesIndexConfigModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSource;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSourceFactory;
import de.hybris.platform.searchservices.indexer.service.SnIndexerRequest;
import de.hybris.platform.searchservices.indexer.service.SnIndexerResponse;
import de.hybris.platform.searchservices.indexer.service.SnIndexerService;
import de.hybris.platform.searchservices.model.AbstractSnIndexerCronJobModel;
import de.hybris.platform.searchservices.model.AbstractSnIndexerItemSourceModel;
import de.hybris.platform.searchservices.model.FullSnIndexerCronJobModel;
import de.hybris.platform.searchservices.model.SnIndexConfigurationModel;
import de.hybris.platform.searchservices.model.SnIndexTypeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BackofficeSearchservicesIndexInitializerTest
{
    @Mock
    BackofficeFacetSearchConfigService backofficeFacetSearchConfigService;
    @Mock
    private SnIndexerService snIndexerService;
    @Mock
    private SnIndexerItemSourceFactory snIndexerItemSourceFactory;
    @Mock
    private ModelService modelService;
    @InjectMocks
    private BackofficeSearchservicesIndexInitializer initializer = (BackofficeSearchservicesIndexInitializer)new Object(this);
    private static final String TYPE_CONFIG_ID = "typeId";
    private final BackofficeIndexedTypeToSearchservicesIndexConfigModel configModel = (BackofficeIndexedTypeToSearchservicesIndexConfigModel)Mockito.mock(BackofficeIndexedTypeToSearchservicesIndexConfigModel.class);
    private final SnIndexConfigurationModel indexConfig = (SnIndexConfigurationModel)Mockito.mock(SnIndexConfigurationModel.class);
    private final SnIndexTypeModel typeConfig = (SnIndexTypeModel)Mockito.mock(SnIndexTypeModel.class);
    private final AfterInitializationEndBackofficeSearchListener searchListener = (AfterInitializationEndBackofficeSearchListener)Mockito.mock(AfterInitializationEndBackofficeSearchListener.class);
    private boolean indexAutoinitEnabled;


    @Before
    public void setup() throws SnIndexerException
    {
        this.initializer.setAfterInitializationEndBackofficeListener(this.searchListener);
        Mockito.when(Boolean.valueOf(this.searchListener.isCallbackRegistered((ExternalEventCallback)Matchers.any()))).thenReturn(Boolean.valueOf(false));
        Mockito.when(this.backofficeFacetSearchConfigService.getAllMappedFacetSearchConfigs()).thenReturn(Arrays.asList(new BackofficeIndexedTypeToSearchservicesIndexConfigModel[] {this.configModel}));
        Mockito.when(this.configModel.getSnIndexConfiguration()).thenReturn(this.indexConfig);
        Mockito.when(this.configModel.getSnIndexType()).thenReturn(this.typeConfig);
    }


    @Test
    public void shouldDoNotingWhenDoNotNeedInitialize()
    {
        this.indexAutoinitEnabled = false;
        this.initializer.initialize();
        Mockito.verifyZeroInteractions(new Object[] {this.backofficeFacetSearchConfigService});
    }


    @Test
    public void shouldDoNothingWhenIndexHasBeenInitialized()
    {
        this.indexAutoinitEnabled = true;
        Mockito.when(Boolean.valueOf(this.configModel.isActive())).thenReturn(Boolean.valueOf(true));
        this.initializer.initialize();
        Mockito.verifyZeroInteractions(new Object[] {this.snIndexerService});
    }


    @Test
    public void shouldNotSaveAllCronJobsWhenCronJobAlreadySucceed()
    {
        this.indexAutoinitEnabled = true;
        Mockito.when(Boolean.valueOf(this.configModel.isActive())).thenReturn(Boolean.valueOf(true));
        this.initializer.initialize();
        ((ModelService)Mockito.verify(this.modelService, Mockito.never())).saveAll(new Object[] {Matchers.any()});
    }


    @Test
    public void shouldSaveAllCronJobsAfterPerformFullIndex() throws SnIndexerException
    {
        this.indexAutoinitEnabled = true;
        Mockito.when(Boolean.valueOf(this.configModel.isActive())).thenReturn(Boolean.valueOf(false));
        FullSnIndexerCronJobModel cronJobModelUnknown = (FullSnIndexerCronJobModel)Mockito.mock(FullSnIndexerCronJobModel.class);
        Mockito.lenient().when(cronJobModelUnknown.getResult()).thenReturn(CronJobResult.UNKNOWN);
        AbstractSnIndexerItemSourceModel absItemSource = (AbstractSnIndexerItemSourceModel)Mockito.mock(AbstractSnIndexerItemSourceModel.class);
        Mockito.when(cronJobModelUnknown.getIndexerItemSource()).thenReturn(absItemSource);
        List<AbstractSnIndexerCronJobModel> cronJobs = Arrays.asList(new AbstractSnIndexerCronJobModel[] {(AbstractSnIndexerCronJobModel)cronJobModelUnknown});
        Mockito.when(this.typeConfig.getIndexerCronJobs()).thenReturn(cronJobs);
        Mockito.when(this.typeConfig.getId()).thenReturn("typeId");
        SnIndexerItemSource itemSource = (SnIndexerItemSource)Mockito.mock(SnIndexerItemSource.class);
        Mockito.when(this.snIndexerItemSourceFactory.createItemSource(absItemSource, Collections.emptyMap())).thenReturn(itemSource);
        SnIndexerRequest snIndexerRequest = (SnIndexerRequest)Mockito.mock(SnIndexerRequest.class);
        Mockito.when(this.snIndexerService.createFullIndexerRequest("typeId", itemSource)).thenReturn(snIndexerRequest);
        SnIndexerResponse snIndexerResponse = (SnIndexerResponse)Mockito.mock(SnIndexerResponse.class);
        Mockito.when(this.snIndexerService.index(snIndexerRequest)).thenReturn(snIndexerResponse);
        this.initializer.initialize();
        ((ModelService)Mockito.verify(this.modelService)).saveAll(Arrays.asList(new ItemModel[] {(ItemModel)cronJobModelUnknown, (ItemModel)this.configModel}));
    }


    private boolean isIndexAutoinitEnabled()
    {
        return this.indexAutoinitEnabled;
    }
}
