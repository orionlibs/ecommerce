package com.hybris.backoffice.solrsearch.events;

import com.hybris.backoffice.solrsearch.enums.SolrItemModificationType;
import com.hybris.backoffice.solrsearch.model.SolrModifiedItemModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CronJobSolrIndexSynchronizationStrategyTest
{
    private static final String TEST_TYPE_CODE = "testTypeCode";
    private static final long TEST_PK = 1000000L;
    @Mock
    private ModelService modelService;
    @InjectMocks
    @Spy
    private CronJobSolrIndexSynchronizationStrategy cronJobSolrIndexSynchronizationStrategy;


    @Before
    public void setUp() throws Exception
    {
        SolrModifiedItemModel modifiedItem = (SolrModifiedItemModel)Mockito.mock(SolrModifiedItemModel.class);
        Mockito.when(this.modelService.create(SolrModifiedItemModel.class)).thenReturn(modifiedItem);
    }


    @Test
    public void shouldAddModifiedItemWhenSettingIsSetToTrue() throws Exception
    {
        ((CronJobSolrIndexSynchronizationStrategy)Mockito.doReturn(Boolean.valueOf(true)).when(this.cronJobSolrIndexSynchronizationStrategy)).shouldUpdateModifiedItem();
        this.cronJobSolrIndexSynchronizationStrategy.updateItem("testTypeCode", 1000000L);
        ArgumentCaptor<List> pkList = ArgumentCaptor.forClass(List.class);
        ((CronJobSolrIndexSynchronizationStrategy)Mockito.verify(this.cronJobSolrIndexSynchronizationStrategy, Mockito.times(1))).addModifiedItems((String)Matchers.eq("testTypeCode"), (List)pkList.capture(), (SolrItemModificationType)Matchers.any());
        Assertions.assertThat((List)pkList.getValue()).hasSize(1);
        Assertions.assertThat((List)pkList.getValue()).containsExactly(new Object[] {PK.fromLong(1000000L)});
    }


    @Test
    public void shouldNotAddModifiedItemWhenSettingIsSetToFalse() throws Exception
    {
        ((CronJobSolrIndexSynchronizationStrategy)Mockito.doReturn(Boolean.valueOf(false)).when(this.cronJobSolrIndexSynchronizationStrategy)).shouldUpdateModifiedItem();
        this.cronJobSolrIndexSynchronizationStrategy.updateItem("testTypeCode", 1000000L);
        ((CronJobSolrIndexSynchronizationStrategy)Mockito.verify(this.cronJobSolrIndexSynchronizationStrategy, Mockito.never())).addModifiedItem((String)Matchers.eq("testTypeCode"), Matchers.eq(1000000L), (SolrItemModificationType)Matchers.any());
    }
}
