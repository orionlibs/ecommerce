package de.hybris.y2ysync.task.runner;

import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ChangesCollector;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.model.StreamConfigurationContainerModel;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.task.TaskConditionModel;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.task.utils.NeedsTaskEngine;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.util.Config;
import de.hybris.y2ysync.deltadetection.collector.MediaBatchingCollector;
import de.hybris.y2ysync.enums.Y2YSyncType;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.model.media.SyncImpExMediaModel;
import de.hybris.y2ysync.task.dao.Y2YSyncDAO;
import de.hybris.y2ysync.task.internal.MediasForType;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.util.SerializationUtils;

@IntegrationTest
@NeedsTaskEngine
public class ItemChangesProcessorIntegrationTest extends ServicelayerBaseTest
{
    @Resource
    private TaskService taskService;
    @Resource
    private ModelService modelService;
    @Resource
    private MediaService mediaService;
    @Resource
    private TypeService typeService;
    @Resource
    private ChangeDetectionService changeDetectionService;
    @Resource
    private Y2YSyncDAO y2ySyncDAO;
    private TitleModel testTitleFoo;
    private TitleModel testTitleBar;
    private TaskConditionModel condition;
    private Y2YStreamConfigurationContainerModel testContainer;
    private Y2YSyncCronJobModel testCronJob;
    private static final String CHANGES_PROCESSOR_RUNNER_BEAN_ID = "itemChangesProcessor";
    private static final String MEDIA_PK_KEY = "mediaPK";
    private static final String CONDITION_NAME_KEY = "conditionName";
    private static final String IMPEX_HEADER_KEY = "impexHeader";
    private static final String TYPE_CODE_KEY = "typeCode";
    private static final String SYNC_EXECUTION_ID_KEY = "syncExecutionID";
    private static final String SYNC_TYPE_KEY = "syncType";
    private static final String TEST_SYNC_EXECUTION_ID = "TEST_SYNC_EXECUTION_ID";
    private static final String TEST_CONDITION_ID = "TEST_CONDITION_ID";
    private static final String IMPEX_HEADER_CORRECT = "code[unique=true];";
    private static final String IMPEX_HEADER_ERROR = "code[unique=true];ERROR_IN_HEADER";
    final double timeFactor = Math.max(15.0D, Config.getDouble("platform.test.timefactor", 15.0D));


    @Before
    public void setUp() throws Exception
    {
        Assertions.assertThat(this.taskService.getEngine().isRunning()).isTrue();
        this.testTitleFoo = (TitleModel)this.modelService.create(TitleModel.class);
        this.testTitleFoo.setCode("Foo");
        this.testTitleBar = (TitleModel)this.modelService.create(TitleModel.class);
        this.testTitleBar.setCode("Bar");
        this.testContainer = (Y2YStreamConfigurationContainerModel)this.modelService.create(Y2YStreamConfigurationContainerModel.class);
        this.testContainer.setId("testContainer");
        this.modelService.save(this.testContainer);
        this.testCronJob = createExportCronJob();
        this.condition = (TaskConditionModel)this.modelService.create(TaskConditionModel.class);
        this.condition.setUniqueID("TEST_CONDITION_ID");
        this.modelService.save(this.condition);
    }


    @Test
    public void testRunSuccessfully() throws Exception
    {
        String streamId = UUID.randomUUID().toString();
        this.modelService.saveAll(new Object[] {this.testTitleFoo, this.testTitleBar});
        ComposedTypeModel titleComposedType = this.typeService.getComposedTypeForClass(TitleModel.class);
        MediaBatchingCollector collector = findChangesWithBatchingCollector(titleComposedType, streamId);
        List<PK> mediaPks = collector.getPksOfBatches();
        Assertions.assertThat(mediaPks).hasSize(1);
        List<ItemChangeDTO> deserializedChanges = getDeserializedChanges("testDeltaMedia-1-0");
        Assertions.assertThat(deserializedChanges).hasSize(2);
        MediasForType mediasForType1 = MediasForType.builder().withComposedTypeCode(titleComposedType.getCode()).withImpExHeader("code[unique=true];").withDataHubColumns("").withMediaPks(mediaPks).build();
        TaskModel task = prepareTask(getTaskContext("TEST_SYNC_EXECUTION_ID", mediaPks.get(0), mediasForType1));
        task.setExecutionDate(new Date());
        this.taskService.scheduleTask(task);
        Thread.sleep((long)(1000.0D * this.timeFactor));
        String content = this.testTitleFoo.getCode() + "\n" + this.testTitleFoo.getCode();
        verifyScriptMediaCreated("INSERT_UPDATE Title;code[unique=true];", content);
        verifyChangeConsumption((ItemModel)this.testTitleFoo, true, streamId);
        verifyChangeConsumption((ItemModel)this.testTitleBar, true, streamId);
        this.modelService.refresh(this.condition);
        Assertions.assertThat(this.condition.getFulfilled()).isTrue();
    }


    @Test
    public void testWaitWhenNoMatchingNodeGroupGiven() throws Exception
    {
        String streamId = UUID.randomUUID().toString();
        this.modelService.saveAll(new Object[] {this.testTitleFoo, this.testTitleBar});
        ComposedTypeModel titleComposedType = this.typeService.getComposedTypeForClass(TitleModel.class);
        MediaBatchingCollector collector = findChangesWithBatchingCollector(titleComposedType, streamId);
        List<PK> mediaPks = collector.getPksOfBatches();
        Assertions.assertThat(mediaPks).hasSize(1);
        List<ItemChangeDTO> deserializedChanges = getDeserializedChanges("testDeltaMedia-1-0");
        Assertions.assertThat(deserializedChanges).hasSize(2);
        MediasForType mediasForType1 = MediasForType.builder().withComposedTypeCode(titleComposedType.getCode()).withImpExHeader("code[unique=true];").withDataHubColumns("").withMediaPks(mediaPks).build();
        TaskModel task = prepareTask(getTaskContext("TEST_SYNC_EXECUTION_ID", mediaPks.get(0), mediasForType1));
        task.setExecutionDate(new Date());
        task.setNodeGroup("badGroup");
        Assertions.assertThat((Comparable)task.getPk()).isNull();
        try
        {
            this.taskService.scheduleTask(task);
            Thread.sleep((long)(1000.0D * this.timeFactor));
        }
        catch(Exception e)
        {
            Assert.fail("No Exception was expected here but thrown: " + e);
        }
        finally
        {
            List<SyncImpExMediaModel> syncImpexMedias = this.y2ySyncDAO.findSyncMediasBySyncCronJob(this.testCronJob.getCode());
            Assertions.assertThat(syncImpexMedias).isEmpty();
            verifyChangeConsumption((ItemModel)this.testTitleFoo, false, streamId);
            this.modelService.refresh(this.condition);
            Assertions.assertThat(this.condition.getFulfilled()).isFalse();
            Assertions.assertThat((Comparable)task.getPk()).isNotNull();
            this.modelService.refresh(task);
            Assertions.assertThat(task).isNotNull();
            Assertions.assertThat(task.getNodeGroup()).isEqualTo("badGroup");
        }
    }


    private void verifyScriptMediaCreated(String expectedImpexHeader, String expectedContent)
    {
        List<SyncImpExMediaModel> syncImpexMedias = this.y2ySyncDAO.findSyncMediasBySyncCronJob(this.testCronJob.getCode());
        Assertions.assertThat(syncImpexMedias).hasSize(1);
        SyncImpExMediaModel media = syncImpexMedias.get(0);
        Assertions.assertThat(media.getImpexHeader()).isEqualTo(expectedImpexHeader);
        Assertions.assertThat(new String(this.mediaService.getDataFromMedia((MediaModel)media))).contains(new CharSequence[] {expectedContent});
    }


    @Test
    public void testRunWithErrorsInImpexHeader() throws Exception
    {
        String streamId = UUID.randomUUID().toString();
        this.modelService.save(this.testTitleFoo);
        ComposedTypeModel titleComposedType = this.typeService.getComposedTypeForClass(TitleModel.class);
        MediaBatchingCollector collector = findChangesWithBatchingCollector(titleComposedType, streamId);
        List<PK> mediaPks = collector.getPksOfBatches();
        Assertions.assertThat(mediaPks).hasSize(1);
        List<ItemChangeDTO> deserializedChanges = getDeserializedChanges("testDeltaMedia-1-0");
        Assertions.assertThat(deserializedChanges).hasSize(1);
        MediasForType mediasForType1 = MediasForType.builder().withComposedTypeCode(titleComposedType.getCode()).withImpExHeader("code[unique=true];ERROR_IN_HEADER").withDataHubColumns("").withMediaPks(mediaPks).build();
        TaskModel task = prepareTask(getTaskContext("TEST_SYNC_EXECUTION_ID", mediaPks.get(0), mediasForType1));
        task.setExecutionDate(new Date());
        try
        {
            TestUtils.disableFileAnalyzer("de.hybris.y2ysync.task.runner.ItemChangesProcessorIntegrationTest.testRunWithErrorsInImpexHeader()");
            this.taskService.scheduleTask(task);
            Thread.sleep((long)(1000.0D * this.timeFactor));
        }
        catch(Exception e)
        {
            Assertions.assertThat(e).isInstanceOf(ImpExException.class);
        }
        finally
        {
            TestUtils.enableFileAnalyzer();
            List<SyncImpExMediaModel> syncImpexMedias = this.y2ySyncDAO.findSyncMediasBySyncCronJob(this.testCronJob.getCode());
            Assertions.assertThat(syncImpexMedias).isEmpty();
            verifyChangeConsumption((ItemModel)this.testTitleFoo, false, streamId);
            this.modelService.refresh(this.condition);
            Assertions.assertThat(this.condition.getFulfilled()).isFalse();
        }
    }


    @Test
    public void testRunWithErrorsDuringChangeConsumption() throws Exception
    {
        String streamId = UUID.randomUUID().toString();
        this.modelService.save(this.testTitleFoo);
        ComposedTypeModel titleComposedType = this.typeService.getComposedTypeForClass(TitleModel.class);
        MediaBatchingCollector collector = findChangesWithBatchingCollector(titleComposedType, streamId);
        Y2YStreamConfigurationModel badStream = (Y2YStreamConfigurationModel)this.modelService.create(Y2YStreamConfigurationModel.class);
        badStream.setContainer((StreamConfigurationContainerModel)this.testContainer);
        badStream.setItemTypeForStream(titleComposedType);
        badStream.setStreamId(streamId);
        badStream.setInfoExpression("#{getBADMETHOD()}");
        this.modelService.save(badStream);
        List<PK> mediaPks = collector.getPksOfBatches();
        Assertions.assertThat(mediaPks).hasSize(1);
        List<ItemChangeDTO> deserializedChanges = getDeserializedChanges("testDeltaMedia-1-0");
        Assertions.assertThat(deserializedChanges).hasSize(1);
        MediasForType mediasForType1 = MediasForType.builder().withComposedTypeCode(titleComposedType.getCode()).withImpExHeader("code[unique=true];").withDataHubColumns("").withMediaPks(mediaPks).build();
        TaskModel task = prepareTask(getTaskContext("TEST_SYNC_EXECUTION_ID", mediaPks.get(0), mediasForType1));
        task.setExecutionDate(new Date());
        try
        {
            TestUtils.disableFileAnalyzer("de.hybris.y2ysync.task.runner.ItemChangesProcessorIntegrationTest.testRunWithErrorsDuringChangeConsumption()");
            this.taskService.scheduleTask(task);
            Thread.sleep((long)(1000.0D * this.timeFactor));
        }
        catch(Exception e)
        {
            Assertions.assertThat(e).isInstanceOf(SpelEvaluationException.class);
        }
        finally
        {
            TestUtils.enableFileAnalyzer();
            this.modelService.detachAll();
            List<SyncImpExMediaModel> syncImpexMedias = this.y2ySyncDAO.findSyncMediasBySyncCronJob(this.testCronJob.getCode());
            Assertions.assertThat(syncImpexMedias).isEmpty();
            verifyChangeConsumption((ItemModel)this.testTitleFoo, false, streamId);
            this.modelService.refresh(this.condition);
            Assertions.assertThat(this.condition.getFulfilled()).isFalse();
        }
    }


    private TaskModel prepareTask(Map<String, Object> ctx)
    {
        TaskModel task = (TaskModel)this.modelService.create(TaskModel.class);
        task.setContext(ctx);
        task.setRunnerBean("itemChangesProcessor");
        return task;
    }


    private Map<String, Object> getTaskContext(String syncExecutionId, PK mediaPK, MediasForType mediasForType)
    {
        ImmutableMap.Builder<String, Object> result = ImmutableMap.builder().put("syncExecutionID", syncExecutionId).put("mediaPK", mediaPK).put("conditionName", "TEST_CONDITION_ID").put("impexHeader", mediasForType.getImpexHeader()).put("syncType", Y2YSyncType.ZIP)
                        .put("typeCode", mediasForType.getComposedTypeCode());
        return (Map<String, Object>)result.build();
    }


    private Y2YSyncCronJobModel createExportCronJob()
    {
        Y2YSyncJobModel syncJob = (Y2YSyncJobModel)this.modelService.create(Y2YSyncJobModel.class);
        syncJob.setCode("testJob");
        syncJob.setSyncType(Y2YSyncType.ZIP);
        syncJob.setStreamConfigurationContainer(this.testContainer);
        this.modelService.save(syncJob);
        Y2YSyncCronJobModel cronJob = (Y2YSyncCronJobModel)this.modelService.create(Y2YSyncCronJobModel.class);
        cronJob.setCode("TEST_SYNC_EXECUTION_ID");
        cronJob.setJob((JobModel)syncJob);
        this.modelService.save(cronJob);
        return cronJob;
    }


    private MediaBatchingCollector findChangesWithBatchingCollector(ComposedTypeModel composedType, String streamId)
    {
        MediaBatchingCollector collector = new MediaBatchingCollector("testDeltaMedia", 3, this.modelService, this.mediaService);
        collector.setId("1");
        this.changeDetectionService.collectChangesForType(composedType, streamId, (ChangesCollector)collector);
        return collector;
    }


    private void verifyChangeConsumption(ItemModel item, boolean consumptionExpected, String streamId)
    {
        ItemChangeDTO change = this.changeDetectionService.getChangeForExistingItem(item, streamId);
        if(consumptionExpected)
        {
            Assertions.assertThat(change).isNull();
        }
        else
        {
            Assertions.assertThat(change).isNotNull();
        }
    }


    private List<ItemChangeDTO> getDeserializedChanges(String mediaCode)
    {
        CatalogUnawareMediaModel media0 = (CatalogUnawareMediaModel)this.mediaService.getMedia(mediaCode);
        byte[] data0 = this.mediaService.getDataFromMedia((MediaModel)media0);
        return (List<ItemChangeDTO>)SerializationUtils.deserialize(data0);
    }
}
