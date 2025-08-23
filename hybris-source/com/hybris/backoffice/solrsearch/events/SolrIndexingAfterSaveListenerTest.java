package com.hybris.backoffice.solrsearch.events;

import com.hybris.backoffice.solrsearch.services.BackofficeFacetSearchConfigService;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.tx.AfterSaveEvent;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@IntegrationTest
public class SolrIndexingAfterSaveListenerTest extends ServicelayerTransactionalTest
{
    public static final PK PK_ = PK.fromLong(1L);
    public static final String TYPE_1 = "Type1";
    @InjectMocks
    private final SolrIndexingAfterSaveListener listener = new SolrIndexingAfterSaveListener();
    private final AfterSaveEvent UPDATE_EVENT = new AfterSaveEvent(PK_, 1);
    private final AfterSaveEvent REMOVE_EVENT = new AfterSaveEvent(PK_, 2);
    @Mock
    private BackofficeFacetSearchConfigService backofficeFacetSearchConfigService;
    @Mock
    private ModelService modelService;
    @Mock
    private TypeService typeService;
    @Mock
    private ItemModel itemModel;
    @Mock
    private SolrIndexSynchronizationStrategy solrIndexSynchronizationStrategy;


    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        this.listener.setIgnoredTypeCodes(new TreeSet());
        Mockito.when(this.itemModel.getItemtype()).thenReturn("Type1");
        Mockito.when(this.modelService.get(PK_)).thenReturn(this.itemModel);
        Mockito.when(Boolean.valueOf(this.typeService.isAssignableFrom(Matchers.anyString(), Matchers.anyString())))
                        .thenReturn(Boolean.FALSE);
    }


    @Test
    public void testAfterSaveUpdateEvent()
    {
        Mockito.when(Boolean.valueOf(this.backofficeFacetSearchConfigService.isSolrSearchConfiguredForType("Type1")))
                        .thenReturn(Boolean.TRUE);
        Mockito.when(this.listener.findTypeCode(SolrIndexingAfterSaveListener.SolrIndexOperation.CHANGE, PK_)).thenReturn("Type1");
        this.listener.afterSave(Collections.singletonList(this.UPDATE_EVENT));
        ArgumentCaptor<List> pkList = ArgumentCaptor.forClass(List.class);
        ((SolrIndexSynchronizationStrategy)Mockito.verify(this.solrIndexSynchronizationStrategy)).updateItems((String)Matchers.eq("Type1"), (List)pkList.capture());
        Assertions.assertThat((List)pkList.getValue()).hasSize(1);
        Assertions.assertThat((List)pkList.getValue()).containsExactly(new Object[] {PK_});
    }


    @Test
    public void testAfterSaveUpdateEventNonIndexedType()
    {
        Mockito.when(Boolean.valueOf(this.backofficeFacetSearchConfigService.isSolrSearchConfiguredForType("Type1")))
                        .thenReturn(Boolean.FALSE);
        this.listener.afterSave(Collections.singletonList(this.UPDATE_EVENT));
        Mockito.verifyNoMoreInteractions(new Object[] {this.solrIndexSynchronizationStrategy});
    }


    @Test
    public void testAfterSaveCreateRemoveEvents()
    {
        Mockito.when(Boolean.valueOf(this.backofficeFacetSearchConfigService.isSolrSearchConfiguredForType("Item")))
                        .thenReturn(Boolean.TRUE);
        this.listener.afterSave(Collections.singletonList(this.REMOVE_EVENT));
        ArgumentCaptor<List> pkList = ArgumentCaptor.forClass(List.class);
        ((SolrIndexSynchronizationStrategy)Mockito.verify(this.solrIndexSynchronizationStrategy)).removeItems((String)Matchers.eq("Item"), (List)pkList.capture());
        Assertions.assertThat((List)pkList.getValue()).hasSize(1);
        Assertions.assertThat((List)pkList.getValue()).containsExactly(new Object[] {PK_});
    }
}
