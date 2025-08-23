package de.hybris.y2ysync.task.runner.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.platform.core.PK;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.task.TaskService;
import de.hybris.y2ysync.enums.Y2YSyncType;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.rest.resources.DataStream;
import de.hybris.y2ysync.rest.resources.Y2YSyncRequest;
import de.hybris.y2ysync.task.dao.Y2YSyncDAO;
import de.hybris.y2ysync.task.runner.TaskContext;
import de.hybris.y2ysync.task.runner.Y2YSyncContext;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Resource;
import org.apache.commons.validator.routines.UrlValidator;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

@IntegrationTest
public class DataHubRequestCreatorValidURLIntegrationTest extends ServicelayerBaseTest
{
    private static final String SYNC_EXECUTION_ID = "testExecutionId";
    private static final String HOME_URL = "http://localhost:9001";
    private static final String CONSUME_CHANGES_WEBROOT = "/y2ysync";
    private static final String DATAHUB_URI = "/datahub-webapp/v1/data-feeds/y2ysync";
    private static final String TEST_CONDITION_ID = "TEST_CONDITION_ID";
    private static final String TEST_INSERT_UPDATE_HEADER = "INSERT_UPDATE Product;code[unique=true];name[lang=pl]";
    private static final String TEST_TYPE_CODE = "Product";
    private static final String FEED_NAME = "Y2YSYNC_FEED";
    private static final String POOL_NAME = "Y2YSYNC_POOL";
    private static final String TARGET_SYSTEMS = "";
    private DataHubRequestCreator requestCreator;
    private final RestTemplate restTemplate = getRestTemplate();
    private Y2YSyncContext ctx;
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


    @Before
    public void setUp()
    {
        this.requestCreator = (DataHubRequestCreator)new Object(this);
        this.requestCreator.setY2YSyncDAO(this.y2ySyncDAO);
        this.requestCreator.setRestTemplate(this.restTemplate);
        this
                        .ctx = Y2YSyncContext.builder().withSyncExecutionId("testExecutionId").withUri("/datahub-webapp/v1/data-feeds/y2ysync").withFeed("Y2YSYNC_FEED").withPool("Y2YSYNC_POOL").withAutoPublishTargetSystems("").build();
        createExportCronJob();
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
        cronJob.setCode("testExecutionId");
        cronJob.setJob((JobModel)syncJob);
        this.modelService.save(cronJob);
        return cronJob;
    }


    @Test
    public void shouldGenerateValidMediaULRsForY2YSyncDataStreams() throws Exception
    {
        String content = ";KEB;Kebab\n;KIE;Kielbasa";
        ImportScript script = givenInsertUpdateScript(";KEB;Kebab\n;KIE;Kielbasa");
        ProcessChangesTask task = givenProcessChangesTaskWith(new ImportScript[] {script});
        task.execute();
        this.requestCreator.sendRequest(this.ctx);
        ArgumentCaptor<Y2YSyncRequest> argumentCaptor = ArgumentCaptor.forClass(Y2YSyncRequest.class);
        ((RestTemplate)Mockito.verify(this.restTemplate, Mockito.times(1))).postForEntity(Mockito.anyString(), argumentCaptor.capture(),
                        (Class)Mockito.eq(Void.class), new Object[0]);
        Assertions.assertThat(argumentCaptor.getValue()).isNotNull();
        UrlValidator urlValidator = new UrlValidator(8L);
        Objects.requireNonNull(urlValidator);
        Assertions.assertThat(((Y2YSyncRequest)argumentCaptor.getValue()).getDataStreams()).flatExtracting(DataStream::getUrls).allMatch(urlValidator::isValid);
    }


    private ImportScript givenInsertUpdateScript(String rows)
    {
        return new ImportScript("Product", "INSERT_UPDATE Product;code[unique=true];name[lang=pl]", rows, null);
    }


    private ProcessChangesTask givenProcessChangesTaskWith(ImportScript... scripts)
    {
        ImmutableMap immutableMap = ImmutableMap.builder().put("conditionName", "TEST_CONDITION_ID").put("syncExecutionID", "testExecutionId").put("syncType", Y2YSyncType.DATAHUB).put("mediaPK", PK.createFixedUUIDPK(1, 1L)).build();
        Object object = new Object(this, this.modelService, this.mediaService, (Map)immutableMap);
        return new ProcessChangesTask(this.modelService, this.mediaService, this.changeDetectionService, this.taskService, this.typeService, this.y2ySyncDAO, (TaskContext)object,
                        (Collection)ImmutableList.copyOf((Object[])scripts));
    }


    private RestTemplate getRestTemplate()
    {
        return (RestTemplate)Mockito.mock(RestTemplate.class);
    }
}
