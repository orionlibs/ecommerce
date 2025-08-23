package de.hybris.y2ysync.task.runner.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.deltadetection.ChangesCollector;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.StreamConfiguration;
import de.hybris.deltadetection.impl.InMemoryChangesCollector;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.task.TaskConditionModel;
import de.hybris.platform.task.TaskService;
import de.hybris.y2ysync.enums.Y2YSyncType;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.model.media.SyncImpExMediaModel;
import de.hybris.y2ysync.task.dao.Y2YSyncDAO;
import de.hybris.y2ysync.task.runner.TaskContext;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class ProcessChangesTaskTest extends ServicelayerBaseTest
{
    private static final String TEST_SYNC_EXECUTION_ID = "TEST_SYNC_EXECUTION_ID";
    private static final String TEST_CONDITION_ID = "TEST_CONDITION_ID";
    private static final String TEST_INSERT_UPDATE_HEADER = "INSERT_UPDATE Product;code[unique=true];name[lang=pl]";
    private static final String TEST_REMOVE_HEADER = "REMOVE Product;code[unique=true]";
    private static final String TEST_TYPE_CODE = "Product";
    @Resource
    private ModelService modelService;
    @Resource
    private MediaService mediaService;
    @Resource
    private ChangeDetectionService changeDetectionService;
    @Resource
    private TaskService taskService;
    @Resource
    private TypeService typeService;
    @Resource
    private Y2YSyncDAO y2ySyncDAO;
    private TaskConditionModel condition;


    @Before
    public void setUp()
    {
        createExportCronJob();
        this.condition = (TaskConditionModel)this.modelService.create(TaskConditionModel.class);
        this.condition.setUniqueID("TEST_CONDITION_ID");
        this.modelService.saveAll();
    }


    private Y2YSyncCronJobModel createExportCronJob()
    {
        Y2YStreamConfigurationContainerModel container = (Y2YStreamConfigurationContainerModel)this.modelService.create(Y2YStreamConfigurationContainerModel.class);
        container.setId("testContainer");
        this.modelService.save(container);
        Y2YSyncJobModel syncJob = (Y2YSyncJobModel)this.modelService.create(Y2YSyncJobModel.class);
        syncJob.setCode("testJob");
        syncJob.setSyncType(Y2YSyncType.ZIP);
        syncJob.setStreamConfigurationContainer(container);
        this.modelService.save(syncJob);
        Y2YSyncCronJobModel cronJob = (Y2YSyncCronJobModel)this.modelService.create(Y2YSyncCronJobModel.class);
        cronJob.setCode("TEST_SYNC_EXECUTION_ID");
        cronJob.setJob((JobModel)syncJob);
        this.modelService.save(cronJob);
        return cronJob;
    }


    @Test
    public void shouldNotCreateMediaWhenThereIsNoscript() throws Exception
    {
        ProcessChangesTask task = givenProcessChangesTaskWith(noScript());
        List<SyncImpExMediaModel> medias = whenExecuted(task);
        Assertions.assertThat(medias).isEmpty();
    }


    @Test
    public void shouldTriggerConditionAfterFinishingTask() throws Exception
    {
        ProcessChangesTask task = givenProcessChangesTaskWith(noScript());
        whenExecuted(task);
        TaskConditionModel conditionToCheck = (TaskConditionModel)this.modelService.get(this.condition.getPk());
        Assertions.assertThat(conditionToCheck.getFulfilled()).isTrue();
    }


    @Test
    public void shouldCreateMediaForGivenInsertUpdateScript() throws Exception
    {
        String content = ";KEB;Kebab\n;KIE;Kielbasa";
        ImportScript script = givenInsertUpdateScript(";KEB;Kebab\n;KIE;Kielbasa");
        ProcessChangesTask task = givenProcessChangesTaskWith(new ImportScript[] {script});
        List<SyncImpExMediaModel> medias = whenExecuted(task);
        Assertions.assertThat(medias).hasSize(1);
        SyncImpExMediaModel media = medias.get(0);
        Assertions.assertThat(media).isNotNull();
        Assertions.assertThat(media.getImpexHeader()).isEqualTo("INSERT_UPDATE Product;code[unique=true];name[lang=pl]");
        Assertions.assertThat(media.getSyncType().getCode()).isEqualTo("Product");
        Assertions.assertThat(media.getExportCronJob().getCode()).isEqualTo("TEST_SYNC_EXECUTION_ID");
        Assertions.assertThat(new String(this.mediaService.getDataFromMedia((MediaModel)media))).isEqualTo(";KEB;Kebab\n;KIE;Kielbasa");
    }


    @Test
    public void shouldCreateMediaForGivenRemoveScript() throws Exception
    {
        String content = ";KEB\n;BUR";
        ImportScript script = givenRemoveScript(";KEB\n;BUR");
        ProcessChangesTask task = givenProcessChangesTaskWith(new ImportScript[] {script});
        List<SyncImpExMediaModel> medias = whenExecuted(task);
        Assertions.assertThat(medias).hasSize(1);
        SyncImpExMediaModel media = medias.get(0);
        Assertions.assertThat(media).isNotNull();
        Assertions.assertThat(media.getImpexHeader()).isEqualTo("REMOVE Product;code[unique=true]");
        Assertions.assertThat(media.getSyncType().getCode()).isEqualTo("Product");
        Assertions.assertThat(media.getExportCronJob().getCode()).isEqualTo("TEST_SYNC_EXECUTION_ID");
        Assertions.assertThat(new String(this.mediaService.getDataFromMedia((MediaModel)media))).isEqualTo(";KEB\n;BUR");
    }


    @Test
    public void shouldCreateMediaForGivenInsertUpdateAndRemoveScript() throws Exception
    {
        String rowsToInsertUpdate = ";KEB;Kebab\n;KIE;Kielbasa";
        ImportScript insertUpdateScript = givenInsertUpdateScript(";KEB;Kebab\n;KIE;Kielbasa");
        String rowsToRemove = ";KEB\n;BUR";
        ImportScript removeScript = givenRemoveScript(";KEB\n;BUR");
        ProcessChangesTask task = givenProcessChangesTaskWith(new ImportScript[] {insertUpdateScript, removeScript});
        List<SyncImpExMediaModel> medias = whenExecuted(task);
        Assertions.assertThat(medias).hasSize(2);
        SyncImpExMediaModel removeMedia = medias.stream().filter(m -> m.getImpexHeader().contains("REMOVE")).findFirst().orElse(null);
        SyncImpExMediaModel insertUpdateMedia = medias.stream().filter(m -> m.getImpexHeader().contains("INSERT_UPDATE")).findFirst().orElse(null);
        Assertions.assertThat(removeMedia).isNotNull();
        Assertions.assertThat(removeMedia.getImpexHeader()).isEqualTo("REMOVE Product;code[unique=true]");
        Assertions.assertThat(removeMedia.getSyncType().getCode()).isEqualTo("Product");
        Assertions.assertThat(removeMedia.getExportCronJob().getCode()).isEqualTo("TEST_SYNC_EXECUTION_ID");
        Assertions.assertThat(new String(this.mediaService.getDataFromMedia((MediaModel)removeMedia))).isEqualTo(";KEB\n;BUR");
        Assertions.assertThat(insertUpdateMedia).isNotNull();
        Assertions.assertThat(insertUpdateMedia.getImpexHeader()).isEqualTo("INSERT_UPDATE Product;code[unique=true];name[lang=pl]");
        Assertions.assertThat(insertUpdateMedia.getSyncType().getCode()).isEqualTo("Product");
        Assertions.assertThat(insertUpdateMedia.getExportCronJob().getCode()).isEqualTo("TEST_SYNC_EXECUTION_ID");
        Assertions.assertThat(new String(this.mediaService.getDataFromMedia((MediaModel)insertUpdateMedia))).isEqualTo(";KEB;Kebab\n;KIE;Kielbasa");
    }


    @Test
    public void shouldConsumeChangesWhenTaskIsFinished() throws Exception
    {
        String titleCode = "TITLE-" + uniqueId();
        TitleModel testTitle = (TitleModel)this.modelService.create(TitleModel.class);
        testTitle.setCode(titleCode);
        this.modelService.save(testTitle);
        InMemoryChangesCollector collector = new InMemoryChangesCollector();
        StreamConfiguration streamConfiguration = StreamConfiguration.buildFor(uniqueId()).withItemSelector("{item.code}=?code").withParameters((Map)ImmutableMap.of("code", titleCode));
        this.changeDetectionService.collectChangesForType(this.typeService.getComposedTypeForClass(TitleModel.class), streamConfiguration, (ChangesCollector)collector);
        Assertions.assertThat(collector.getChanges()).hasSize(1);
        ProcessChangesTask task = givenProcessChangesFor(collector.getChanges());
        task.execute();
        collector.clearChanges();
        this.changeDetectionService.collectChangesForType(this.typeService.getComposedTypeForClass(TitleModel.class), streamConfiguration, (ChangesCollector)collector);
        Assertions.assertThat(collector.getChanges()).isEmpty();
    }


    private ImportScript givenInsertUpdateScript(String rows)
    {
        return new ImportScript("Product", "INSERT_UPDATE Product;code[unique=true];name[lang=pl]", rows, null);
    }


    private ImportScript givenRemoveScript(String rows)
    {
        return new ImportScript("Product", "REMOVE Product;code[unique=true]", rows, null);
    }


    private ProcessChangesTask givenProcessChangesTaskWith(ImportScript... scripts)
    {
        ImmutableMap immutableMap = ImmutableMap.builder().put("conditionName", "TEST_CONDITION_ID").put("syncExecutionID", "TEST_SYNC_EXECUTION_ID").put("syncType", Y2YSyncType.ZIP).put("mediaPK", PK.createFixedUUIDPK(1, 1L)).build();
        Object object = new Object(this, this.modelService, this.mediaService, (Map)immutableMap);
        return new ProcessChangesTask(this.modelService, this.mediaService, this.changeDetectionService, this.taskService, this.typeService, this.y2ySyncDAO, (TaskContext)object,
                        (Collection)ImmutableList.copyOf((Object[])scripts));
    }


    private ProcessChangesTask givenProcessChangesFor(List<ItemChangeDTO> changes)
    {
        ImmutableMap immutableMap = ImmutableMap.builder().put("conditionName", "TEST_CONDITION_ID").put("syncExecutionID", "TEST_SYNC_EXECUTION_ID").put("syncType", Y2YSyncType.ZIP).put("mediaPK", PK.createFixedUUIDPK(1, 1L)).build();
        Object object = new Object(this, this.modelService, this.mediaService, (Map)immutableMap, changes);
        return new ProcessChangesTask(this.modelService, this.mediaService, this.changeDetectionService, this.taskService, this.typeService, this.y2ySyncDAO, (TaskContext)object,
                        Collections.emptyList());
    }


    private List<SyncImpExMediaModel> whenExecuted(ProcessChangesTask task) throws Exception
    {
        return (List<SyncImpExMediaModel>)task.execute()
                        .stream()
                        .map(code -> (SyncImpExMediaModel)this.mediaService.getMedia(code))
                        .collect(Collectors.toList());
    }


    private ImportScript[] noScript()
    {
        return new ImportScript[0];
    }


    private String uniqueId()
    {
        return UUID.randomUUID().toString();
    }
}
