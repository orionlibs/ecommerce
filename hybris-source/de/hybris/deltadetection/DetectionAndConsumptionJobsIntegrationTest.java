package de.hybris.deltadetection;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.impl.InMemoryChangesCollector;
import de.hybris.deltadetection.model.ChangeDetectionJobModel;
import de.hybris.deltadetection.model.ScriptChangeConsumptionJobModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class DetectionAndConsumptionJobsIntegrationTest extends ServicelayerBaseTest
{
    @Resource
    private ModelService modelService;
    @Resource
    private TypeService typeService;
    @Resource
    private CronJobService cronJobService;
    @Resource
    private ChangeDetectionService changeDetectionService;
    private ScriptChangeConsumptionJobModel testConsumeJobForTitle;
    private ScriptChangeConsumptionJobModel testConsumeJobForDeliveryMode;
    private ChangeDetectionJobModel testDetectionJobForTitle;
    private ChangeDetectionJobModel testDetectionJobForDeliveryMode;
    private TitleModel testTitleFoo;
    private TitleModel testTitleBar;
    private DeliveryModeModel testDeliveryModeX;
    private DeliveryModeModel testDeliveryModeY;
    private DeliveryModeModel testDeliveryModeZ;
    private static final String STREAM_ID_DEFAULT = "FeedDefault";
    private static final String URI_FOR_SCRIPT_CONSUME_ALL = "classpath://test/script-change-consumer-all.groovy";
    private static final String URI_FOR_SCRIPT_CONSUME_DELETED = "classpath://test/script-change-consumer-deleted.groovy";


    @Before
    public void setUp() throws Exception
    {
        this.testTitleFoo = (TitleModel)this.modelService.create(TitleModel.class);
        this.testTitleFoo.setCode("Foo");
        this.testTitleBar = (TitleModel)this.modelService.create(TitleModel.class);
        this.testTitleBar.setCode("Bar");
        this.testDeliveryModeX = (DeliveryModeModel)this.modelService.create(DeliveryModeModel.class);
        this.testDeliveryModeX.setCode("testDeliveryModeX");
        this.testDeliveryModeY = (DeliveryModeModel)this.modelService.create(DeliveryModeModel.class);
        this.testDeliveryModeY.setCode("testDeliveryModeY");
        this.testDeliveryModeZ = (DeliveryModeModel)this.modelService.create(DeliveryModeModel.class);
        this.testDeliveryModeZ.setCode("testDeliveryModeZ");
        this.modelService.saveAll(new Object[] {this.testTitleFoo, this.testTitleBar, this.testDeliveryModeX, this.testDeliveryModeY, this.testDeliveryModeZ});
        this.testDetectionJobForTitle = (ChangeDetectionJobModel)this.modelService.create(ChangeDetectionJobModel.class);
        this.testDetectionJobForTitle.setCode("testChangeDetectionJobForTitle");
        this.testDetectionJobForTitle.setStreamId("FeedDefault");
        this.testDetectionJobForTitle.setTypePK(this.typeService.getComposedTypeForClass(TitleModel.class));
        this.testDetectionJobForDeliveryMode = (ChangeDetectionJobModel)this.modelService.create(ChangeDetectionJobModel.class);
        this.testDetectionJobForDeliveryMode.setCode("testChangeDetectionJobForDeliveryMode");
        this.testDetectionJobForDeliveryMode.setStreamId("FeedDefault");
        this.testDetectionJobForDeliveryMode.setTypePK(this.typeService.getComposedTypeForClass(DeliveryModeModel.class));
        this.modelService.saveAll(new Object[] {this.testDetectionJobForTitle, this.testDetectionJobForDeliveryMode});
        this.testConsumeJobForTitle = (ScriptChangeConsumptionJobModel)this.modelService.create(ScriptChangeConsumptionJobModel.class);
        this.testConsumeJobForTitle.setCode("testScriptChangeConsumptionJobForTitle");
        this.testConsumeJobForTitle.setScriptURI("classpath://test/script-change-consumer-all.groovy");
        this.testConsumeJobForDeliveryMode = (ScriptChangeConsumptionJobModel)this.modelService.create(ScriptChangeConsumptionJobModel.class);
        this.testConsumeJobForDeliveryMode.setCode("testScriptChangeConsumptionJobForDeliveryMode");
        this.testConsumeJobForDeliveryMode.setScriptURI("classpath://test/script-change-consumer-deleted.groovy");
    }


    @Test
    public void testDetectAndConsumeAllChangesForGivenType() throws Exception
    {
        ComposedTypeModel composedTypeForTitle = this.typeService.getComposedTypeForClass(TitleModel.class);
        InMemoryChangesCollector inMemoryCollector = new InMemoryChangesCollector();
        CronJobModel myDetectionCronjobForTitle = prepareCronjob("cronjobForTitleChangesDetection", (JobModel)this.testDetectionJobForTitle);
        Thread.sleep(3000L);
        this.cronJobService.performCronJob(myDetectionCronjobForTitle, true);
        Thread.sleep(3000L);
        checkCurrentAmountOfChanges(composedTypeForTitle, inMemoryCollector, 2);
        MediaModel detectionOutput = this.testDetectionJobForTitle.getOutput();
        Assertions.assertThat(detectionOutput).isNotNull();
        this.testConsumeJobForTitle.setInput(detectionOutput);
        this.modelService.save(this.testConsumeJobForTitle);
        CronJobModel myConsumptionCronjobForTitle = prepareCronjob("cronjobForTitleChangesConsumption", (JobModel)this.testConsumeJobForTitle);
        Thread.sleep(3000L);
        this.cronJobService.performCronJob(myConsumptionCronjobForTitle, true);
        Thread.sleep(3000L);
        checkCurrentAmountOfChanges(composedTypeForTitle, inMemoryCollector, 0);
    }


    @Test
    public void testDetectAndConsumeOnlyDeletedChangesForGivenType() throws Exception
    {
        ComposedTypeModel composedTypeForDeliveryMode = this.typeService.getComposedTypeForClass(DeliveryModeModel.class);
        InMemoryChangesCollector inMemoryCollector = new InMemoryChangesCollector();
        CronJobModel myDetectionCronjobForDeliveryMode = prepareCronjob("cronjobForDeliveryModeChangesDetection", (JobModel)this.testDetectionJobForDeliveryMode);
        Thread.sleep(3000L);
        this.cronJobService.performCronJob(myDetectionCronjobForDeliveryMode, true);
        Thread.sleep(3000L);
        checkCurrentAmountOfChanges(composedTypeForDeliveryMode, inMemoryCollector, 3);
        MediaModel detectionOutput = this.testDetectionJobForDeliveryMode.getOutput();
        Assertions.assertThat(detectionOutput).isNotNull();
        this.testConsumeJobForDeliveryMode.setInput(detectionOutput);
        this.modelService.save(this.testConsumeJobForDeliveryMode);
        CronJobModel myConsumptionCronjobForDeliveryModeFoo = prepareCronjob("cronjobForDeliveryModeChangesConsumptionFoo", (JobModel)this.testConsumeJobForDeliveryMode);
        Thread.sleep(3000L);
        this.cronJobService.performCronJob(myConsumptionCronjobForDeliveryModeFoo, true);
        Thread.sleep(3000L);
        checkCurrentAmountOfChanges(composedTypeForDeliveryMode, inMemoryCollector, 3);
        this.changeDetectionService.consumeChanges(inMemoryCollector.getChanges());
        this.modelService.remove(this.testDeliveryModeX);
        this.modelService.remove(this.testDeliveryModeY);
        this.testDeliveryModeZ.setName("blabla");
        this.modelService.save(this.testDeliveryModeZ);
        CronJobModel myDetectionCronjobForDeliveryModeBar = prepareCronjob("cronjobForDeliveryModeChangesDetectionBar", (JobModel)this.testDetectionJobForDeliveryMode);
        Thread.sleep(3000L);
        this.cronJobService.performCronJob(myDetectionCronjobForDeliveryModeBar, true);
        Thread.sleep(3000L);
        checkCurrentAmountOfChanges(composedTypeForDeliveryMode, inMemoryCollector, 3);
        this.modelService.refresh(this.testDetectionJobForDeliveryMode);
        detectionOutput = this.testDetectionJobForDeliveryMode.getOutput();
        Assertions.assertThat(detectionOutput).isNotNull();
        this.testConsumeJobForDeliveryMode.setInput(detectionOutput);
        this.modelService.save(this.testConsumeJobForDeliveryMode);
        CronJobModel myConsumptionCronjobForDeliveryModeBar = prepareCronjob("cronjobForDeliveryModeChangesConsumptionBar", (JobModel)this.testConsumeJobForDeliveryMode);
        Thread.sleep(3000L);
        this.cronJobService.performCronJob(myConsumptionCronjobForDeliveryModeBar, true);
        Thread.sleep(3000L);
        checkCurrentAmountOfChanges(composedTypeForDeliveryMode, inMemoryCollector, 1);
    }


    private void checkCurrentAmountOfChanges(ComposedTypeModel composedTypeForDeliveryMode, InMemoryChangesCollector inMemoryCollector, int expectedSize)
    {
        inMemoryCollector.clearChanges();
        this.changeDetectionService.collectChangesForType(composedTypeForDeliveryMode, "FeedDefault", (ChangesCollector)inMemoryCollector);
        Assertions.assertThat(inMemoryCollector.getChanges()).hasSize(expectedSize);
    }


    private CronJobModel prepareCronjob(String code, JobModel job)
    {
        CronJobModel cronjob = (CronJobModel)this.modelService.create(CronJobModel.class);
        cronjob.setCode(code);
        cronjob.setSingleExecutable(Boolean.TRUE);
        cronjob.setJob(job);
        this.modelService.save(cronjob);
        return cronjob;
    }
}
