package de.hybris.y2ysync.task.runner;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ChangesCollector;
import de.hybris.deltadetection.Y2YTestDataGenerator;
import de.hybris.deltadetection.impl.InMemoryChangesCollector;
import de.hybris.deltadetection.model.ItemVersionMarkerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.task.utils.NeedsTaskEngine;
import de.hybris.y2ysync.model.media.ConsumeMarkerMediaModel;
import de.hybris.y2ysync.task.internal.SyncTaskFactory;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.SerializationUtils;

@IntegrationTest
@NeedsTaskEngine
public class ConsumeY2YChangesTaskRunnerTest extends ServicelayerBaseTest
{
    private static final int TITLES_NUMBER = 5;
    private Y2YTestDataGenerator.TitlesFixture titlesFixture;
    @Resource
    private SyncTaskFactory syncTaskFactory;
    @Resource
    private ModelService modelService;
    @Resource
    private TypeService typeService;
    @Resource
    private ChangeDetectionService changeDetectionService;
    @Resource
    private MediaService mediaService;
    @Resource
    private FlexibleSearchService flexibleSearchService;
    private InMemoryChangesCollector changesCollector;


    @Before
    public void prepareTitles()
    {
        Y2YTestDataGenerator y2YTestDataGenerator = new Y2YTestDataGenerator(this.modelService, this.typeService);
        this.titlesFixture = y2YTestDataGenerator.generateTitles(5);
        this.changesCollector = new InMemoryChangesCollector();
    }


    private void createConsumeMarkerMedia(String syncExecutionId, InMemoryChangesCollector changesCollector)
    {
        ConsumeMarkerMediaModel media = (ConsumeMarkerMediaModel)this.modelService.create(ConsumeMarkerMediaModel.class);
        media.setCode(UUID.randomUUID().toString());
        media.setSyncExecutionID(syncExecutionId);
        this.modelService.save(media);
        this.mediaService.setDataForMedia((MediaModel)media, SerializationUtils.serialize(changesCollector.getChanges()));
        this.modelService.saveAll();
    }


    @Test
    public void taskShouldConsumeAllChanges() throws InterruptedException
    {
        assertItemVersionMarkerNumber(0);
        String syncExecutionId = UUID.randomUUID().toString();
        this.changeDetectionService.collectChangesForType(this.titlesFixture.getComposedType(), this.titlesFixture.getStreamId(), (ChangesCollector)this.changesCollector);
        createConsumeMarkerMedia(syncExecutionId, this.changesCollector);
        assertItemVersionMarkerNumber(0);
        this.syncTaskFactory.runConsumeSyncChangesTask(syncExecutionId);
        Thread.sleep(10000L);
        this.modelService.detachAll();
        assertItemVersionMarkerNumber(5);
    }


    private void assertItemVersionMarkerNumber(int number)
    {
        List<ItemVersionMarkerModel> afterResult = this.flexibleSearchService.search("SELECT {PK} FROM {ItemVersionMarker} WHERE {streamId}=?streamId", Map.of("streamId", this.titlesFixture.getStreamId())).getResult();
        Assertions.assertThat(afterResult).hasSize(number);
    }
}
