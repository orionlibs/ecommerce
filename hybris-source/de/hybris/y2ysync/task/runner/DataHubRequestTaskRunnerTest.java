package de.hybris.y2ysync.task.runner;

import com.google.common.collect.ImmutableMap;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.task.dao.Y2YSyncDAO;
import de.hybris.y2ysync.task.runner.internal.DataHubRequestCreator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class DataHubRequestTaskRunnerTest
{
    public static final String TEST_EXECUTION_ID = "testExecutionID";
    public static final String TEST_DATAHUB_URL = "http://localhost:8080/some/feed";
    public static final String DEFAULT_DATAHUB_URL = "http://localhost:5050/default/feed";
    private static final String FEED_NAME = "Y2YSYNC_FEED";
    private static final String POOL_NAME = "Y2YSYNC_POOL";
    private static final String TARGET_SYSTEM = "Y2YSYNC_POOL";
    @InjectMocks
    private final DataHubRequestTaskRunner taskRunner = (DataHubRequestTaskRunner)new Object(this);
    @InjectMocks
    private final DataHubRequestTaskRunner wronglyConfiguredRunner = (DataHubRequestTaskRunner)new Object(this);
    @Mock
    private Y2YSyncDAO syncDAO;
    @Mock
    private DataHubRequestCreator requestCreator;
    @Mock
    private ModelService modelService;
    @Mock
    private TaskService taskService;
    @Mock
    private TaskModel task;
    @Mock
    private Y2YSyncCronJobModel cronJob;
    @Mock
    private Y2YSyncJobModel job;
    @Mock
    private Y2YStreamConfigurationContainerModel configurationContainer;
    private Y2YSyncContext ctx;


    @Before
    public void setUp() throws Exception
    {
        BDDMockito.given(this.task.getContext())
                        .willReturn(ImmutableMap.builder().put("syncExecutionID", "testExecutionID").build());
        BDDMockito.given(this.syncDAO.findSyncCronJobByCode("testExecutionID")).willReturn(this.cronJob);
        BDDMockito.given(this.cronJob.getJob()).willReturn(this.job);
        BDDMockito.given(this.job.getStreamConfigurationContainer()).willReturn(this.configurationContainer);
        BDDMockito.given(this.configurationContainer.getFeed()).willReturn("Y2YSYNC_FEED");
        BDDMockito.given(this.configurationContainer.getPool()).willReturn("Y2YSYNC_POOL");
        BDDMockito.given(this.configurationContainer.getTargetSystem()).willReturn("Y2YSYNC_POOL");
        this
                        .ctx = Y2YSyncContext.builder().withSyncExecutionId("testExecutionID").withUri("http://localhost:8080/some/feed").withFeed("Y2YSYNC_FEED").withPool("Y2YSYNC_POOL").withAutoPublishTargetSystems("Y2YSYNC_POOL").build();
    }


    @Test
    public void shouldUseDataHubUrlFromY2YSyncJobIfPresent() throws Exception
    {
        BDDMockito.given(this.job.getDataHubUrl()).willReturn("http://localhost:8080/some/feed");
        this.taskRunner.run(this.taskService, this.task);
        ((DataHubRequestCreator)Mockito.verify(this.requestCreator)).sendRequest((Y2YSyncContext)Matchers.refEq(this.ctx, new String[0]));
    }


    @Test
    public void shouldUseDataHubUrlFromPropertiesIfY2YSyncJobHasntConfiguredItDirectly() throws Exception
    {
        BDDMockito.given(this.job.getDataHubUrl()).willReturn(null);
        this.taskRunner.run(this.taskService, this.task);
        Y2YSyncContext ctx = Y2YSyncContext.builder().withSyncExecutionId("testExecutionID").withUri("http://localhost:5050/default/feed").withFeed("Y2YSYNC_FEED").withPool("Y2YSYNC_POOL").withAutoPublishTargetSystems("Y2YSYNC_POOL").build();
        ((DataHubRequestCreator)Mockito.verify(this.requestCreator)).sendRequest((Y2YSyncContext)Matchers.refEq(ctx, new String[0]));
    }


    @Test
    public void shouldSaveRelatedCronJobWithResultErrorWhenDataHubUrlCannotBeDetermined() throws Exception
    {
        this.wronglyConfiguredRunner.run(this.taskService, this.task);
        ((Y2YSyncCronJobModel)Mockito.verify(this.cronJob)).setStatus(CronJobStatus.FINISHED);
        ((Y2YSyncCronJobModel)Mockito.verify(this.cronJob)).setResult(CronJobResult.ERROR);
        ((ModelService)Mockito.verify(this.modelService)).save(this.cronJob);
    }
}
