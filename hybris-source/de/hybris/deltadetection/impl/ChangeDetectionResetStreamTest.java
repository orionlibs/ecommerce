package de.hybris.deltadetection.impl;

import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ChangesCollector;
import de.hybris.deltadetection.enums.ChangeType;
import de.hybris.deltadetection.enums.ItemVersionMarkerStatus;
import de.hybris.deltadetection.model.ItemVersionMarkerModel;
import de.hybris.deltadetection.model.StreamConfigurationContainerModel;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class ChangeDetectionResetStreamTest extends ServicelayerBaseTest
{
    @Resource
    private ChangeDetectionService changeDetectionService;
    @Resource
    private ModelService modelService;
    @Resource
    private TypeService typeService;
    @Resource
    private FlexibleSearchService flexibleSearchService;
    private static final String TEST_STREAM = "testStream";
    private ComposedTypeModel titleComposedType;
    private TitleModel fooTitle;
    private TitleModel barTitle;
    private TitleModel bazTitle;
    private StreamConfigurationContainerModel streamContainer;
    private StreamConfigurationModel streamConfig;


    private ItemVersionMarkerModel createVersionMarker(TitleModel title, VersionMarkerTS currentTime, ItemVersionMarkerStatus status)
    {
        ItemVersionMarkerModel marker = (ItemVersionMarkerModel)this.modelService.create(ItemVersionMarkerModel.class);
        marker.setItemPK(title.getPk().getLong());
        if(currentTime.equals(VersionMarkerTS.PRESENT))
        {
            marker.setVersionTS(title.getModifiedtime());
        }
        else
        {
            marker.setVersionTS(Date.from(title.getModifiedtime().toInstant().minus(10L, ChronoUnit.MINUTES)));
        }
        marker.setItemComposedType(this.titleComposedType);
        marker.setStreamId("testStream");
        marker.setStatus(status);
        this.modelService.save(marker);
        return marker;
    }


    @Before
    public void prepare()
    {
        this.fooTitle = (TitleModel)this.modelService.create(TitleModel.class);
        this.fooTitle.setCode("foo");
        this.barTitle = (TitleModel)this.modelService.create(TitleModel.class);
        this.barTitle.setCode("bar");
        this.bazTitle = (TitleModel)this.modelService.create(TitleModel.class);
        this.bazTitle.setCode("baz");
        this.titleComposedType = this.typeService.getComposedTypeForClass(TitleModel.class);
        StreamConfigurationContainerModel streamContainer = (StreamConfigurationContainerModel)this.modelService.create(StreamConfigurationContainerModel.class);
        streamContainer.setId("test-container");
        this.modelService.save(streamContainer);
        this.streamConfig = (StreamConfigurationModel)this.modelService.create(StreamConfigurationModel.class);
        this.streamConfig.setItemTypeForStream(this.titleComposedType);
        this.streamConfig.setStreamId("testStream");
        this.streamConfig.setWhereClause("1=1");
        this.streamConfig.setContainer(streamContainer);
        this.modelService.save(this.fooTitle);
        this.modelService.save(this.streamConfig);
    }


    @Test
    public void deletedItemChangeConsumptionShouldChangeStatusToDeleted()
    {
        ItemVersionMarkerModel activeIvm = createVersionMarker(this.fooTitle, VersionMarkerTS.PAST, ItemVersionMarkerStatus.ACTIVE);
        PK versionMarkerPK = activeIvm.getPk();
        this.modelService.remove(this.fooTitle);
        InMemoryChangesCollector collector = new InMemoryChangesCollector();
        this.changeDetectionService.collectChangesForType(this.titleComposedType, "testStream", (ChangesCollector)collector);
        this.changeDetectionService.consumeChanges(collector.getChanges());
        ItemVersionMarkerModel deletedIvm = (ItemVersionMarkerModel)this.modelService.get(versionMarkerPK);
        Assertions.assertThat((Comparable)deletedIvm.getStatus()).isEqualTo(ItemVersionMarkerStatus.DELETED);
    }


    @Test
    public void ivmWithDeletedStatusShouldBeRestoredInCaseOfSameNewItem()
    {
        createVersionMarker(this.fooTitle, VersionMarkerTS.PRESENT, ItemVersionMarkerStatus.DELETED);
        InMemoryChangesCollector collector = new InMemoryChangesCollector();
        this.changeDetectionService.collectChangesForType(this.titleComposedType, "testStream", (ChangesCollector)collector);
        this.changeDetectionService.consumeChanges(collector.getChanges());
        Assertions.assertThat(countVersionMarkerByPK("testStream", this.fooTitle.getPk())).isEqualTo(1);
    }


    @Test
    public void resetStreamShouldChangeIvmTimestamp()
    {
        ItemVersionMarkerModel ivm = createVersionMarker(this.fooTitle, VersionMarkerTS.PRESENT, ItemVersionMarkerStatus.ACTIVE);
        assertChangesNumberForStream("testStream", 0);
        this.changeDetectionService.resetStream("testStream");
        assertChangesNumberForStream("testStream", 1);
        this.modelService.refresh(ivm);
        Date date = Date.from(LocalDate.parse("1971-01-01").atStartOfDay().toInstant(ZoneOffset.MIN));
        Assertions.assertThat(ivm.getVersionTS().before(date)).isTrue();
    }


    @Test
    public void shouldRevertVersionToNull()
    {
        ItemVersionMarkerModel ivm = createVersionMarker(this.fooTitle, VersionMarkerTS.PAST, ItemVersionMarkerStatus.ACTIVE);
        ivm.setVersionValue("foo");
        this.modelService.save(ivm);
        this.changeDetectionService.resetStream("testStream");
        this.modelService.refresh(ivm);
        Assertions.assertThat(ivm.getVersionValue()).isNull();
    }


    @Test
    public void shouldRevertVersionToPreviousValue()
    {
        ItemVersionMarkerModel ivm = createVersionMarker(this.fooTitle, VersionMarkerTS.PAST, ItemVersionMarkerStatus.ACTIVE);
        ivm.setVersionValue("foo");
        ivm.setLastVersionValue("bar");
        this.modelService.save(ivm);
        this.changeDetectionService.resetStream("testStream");
        this.modelService.refresh(ivm);
        Assertions.assertThat(ivm.getVersionValue()).isEqualTo("bar");
    }


    @Test
    public void shouldNotRevertValueInCaseOfDeletion()
    {
        ItemVersionMarkerModel ivm = createVersionMarker(this.fooTitle, VersionMarkerTS.PAST, ItemVersionMarkerStatus.DELETED);
        ivm.setVersionValue("foo");
        ivm.setLastVersionValue("bar");
        ivm.setStatus(ItemVersionMarkerStatus.DELETED);
        this.modelService.save(ivm);
        this.changeDetectionService.resetStream("testStream");
        this.modelService.refresh(ivm);
        Assertions.assertThat(ivm.getVersionValue()).isEqualTo("foo");
    }


    @Test
    public void resetShouldReactiveIvmWithDeletedStatus()
    {
        ItemVersionMarkerModel ivm = createVersionMarker(this.fooTitle, VersionMarkerTS.PAST, ItemVersionMarkerStatus.DELETED);
        this.modelService.remove(this.fooTitle);
        assertChangesNumberForStream("testStream", 0);
        this.changeDetectionService.resetStream("testStream");
        InMemoryChangesCollector collector = new InMemoryChangesCollector();
        this.changeDetectionService.collectChangesForType(this.titleComposedType, "testStream", (ChangesCollector)collector);
        assertChangesNumberForStream("testStream", 1);
        this.modelService.refresh(ivm);
        Assertions.assertThat((Comparable)ivm.getStatus()).isEqualTo(ItemVersionMarkerStatus.ACTIVE);
    }


    @Test
    public void resetStreamEndToEndTest()
    {
        this.modelService.saveAll(new Object[] {this.barTitle, this.bazTitle});
        InMemoryChangesCollector beforeResetCollector = new InMemoryChangesCollector();
        InMemoryChangesCollector afterResetCollector = new InMemoryChangesCollector();
        createVersionMarker(this.fooTitle, VersionMarkerTS.PRESENT, ItemVersionMarkerStatus.ACTIVE);
        createVersionMarker(this.barTitle, VersionMarkerTS.PAST, ItemVersionMarkerStatus.ACTIVE);
        createVersionMarker(this.bazTitle, VersionMarkerTS.PAST, ItemVersionMarkerStatus.DELETED);
        this.changeDetectionService.collectChangesForType(this.titleComposedType, "testStream", (ChangesCollector)beforeResetCollector);
        this.changeDetectionService.consumeChanges(beforeResetCollector.getChanges());
        Assertions.assertThat(beforeResetCollector.getChanges()).hasSize(2);
        Assertions.assertThat(beforeResetCollector.getChanges()).extracting("changeType").containsOnly(new Object[] {ChangeType.NEW, ChangeType.MODIFIED});
        this.changeDetectionService.resetStream("testStream");
        this.changeDetectionService.collectChangesForType(this.titleComposedType, "testStream", (ChangesCollector)afterResetCollector);
        Assertions.assertThat(afterResetCollector.getChanges()).hasSize(3);
        Assertions.assertThat(afterResetCollector.getChanges()).extracting("changeType").containsOnly(new Object[] {ChangeType.MODIFIED});
    }


    @Test
    public void deleteStreamShouldDeleteAllVersionMarkers()
    {
        this.modelService.saveAll(new Object[] {this.barTitle, this.bazTitle});
        createVersionMarker(this.fooTitle, VersionMarkerTS.PRESENT, ItemVersionMarkerStatus.ACTIVE);
        createVersionMarker(this.barTitle, VersionMarkerTS.PAST, ItemVersionMarkerStatus.ACTIVE);
        createVersionMarker(this.bazTitle, VersionMarkerTS.PAST, ItemVersionMarkerStatus.DELETED);
        this.changeDetectionService.deleteItemVersionMarkersForStream("testStream");
        Assertions.assertThat(countStreamVersionMarkers("testStream")).isEqualTo(0);
    }


    private int countStreamVersionMarkers(String streamId)
    {
        FlexibleSearchQuery fsq = new FlexibleSearchQuery("SELECT {PK} FROM {ItemVersionMarker AS ivm} WHERE {ivm.streamId}=?streamId");
        fsq.addQueryParameter("streamId", streamId);
        return this.flexibleSearchService.search(fsq).getCount();
    }


    private int countVersionMarkerByPK(String streamId, PK pk)
    {
        FlexibleSearchQuery fsq = new FlexibleSearchQuery("SELECT {PK} FROM {ItemVersionMarker as ivm} WHERE {ivm.streamId}=?streamId AND {ivm.itemPK}=?itemPK");
        fsq.addQueryParameters((Map)ImmutableMap.of("streamId", streamId, "itemPK", pk));
        return this.flexibleSearchService.search(fsq).getCount();
    }


    private void assertChangesNumberForStream(String streamId, int changesNumber)
    {
        InMemoryChangesCollector collector = new InMemoryChangesCollector();
        this.changeDetectionService.collectChangesForType(this.titleComposedType, streamId, (ChangesCollector)collector);
        Assertions.assertThat(collector.getChanges()).hasSize(changesNumber);
    }
}
