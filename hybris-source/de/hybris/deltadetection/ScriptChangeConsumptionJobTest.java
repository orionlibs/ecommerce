package de.hybris.deltadetection;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.enums.ChangeType;
import de.hybris.deltadetection.impl.CsvReportChangesCollector;
import de.hybris.deltadetection.impl.InMemoryChangesCollector;
import de.hybris.deltadetection.model.ScriptChangeConsumptionJobModel;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class ScriptChangeConsumptionJobTest extends ServicelayerBaseTest
{
    @Resource
    private ModelService modelService;
    @Resource
    private TypeService typeService;
    @Resource
    private CronJobService cronJobService;
    @Resource
    private ChangeDetectionService changeDetectionService;
    @Resource
    private MediaService mediaService;
    private ScriptChangeConsumptionJobModel testJobForTitleWithConsumeAllScript;
    private ScriptChangeConsumptionJobModel testJobForDeliveryModeWithConsumeDeletedScript;
    private ScriptChangeConsumptionJobModel testJobForTitleConsumeNothing;
    private TitleModel testTitleFoo;
    private TitleModel testTitleBar;
    private DeliveryModeModel testDeliveryModeX;
    private DeliveryModeModel testDeliveryModeY;
    private DeliveryModeModel testDeliveryModeZ;
    private static final String STREAM_ID_DEFAULT = "FeedDefault";
    private static final String URI_FOR_SCRIPT_CONSUME_ALL = "classpath://test/script-change-consumer-all.groovy";
    private static final String URI_FOR_SCRIPT_CONSUME_DELETED = "classpath://test/script-change-consumer-deleted.groovy";
    private static final String URI_FOR_SCRIPT_CONSUME_NOTHING = "classpath://test/script-change-consumer-nothing.groovy";


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
        this.testJobForTitleWithConsumeAllScript = (ScriptChangeConsumptionJobModel)this.modelService.create(ScriptChangeConsumptionJobModel.class);
        this.testJobForTitleWithConsumeAllScript.setCode("testScriptChangeConsumptionJobForTitle");
        this.testJobForTitleWithConsumeAllScript.setInput(prepareMediaCSVReport("FeedDefault", this.typeService
                        .getComposedTypeForClass(TitleModel.class), "testCronjob", "testJob"));
        this.testJobForTitleConsumeNothing = (ScriptChangeConsumptionJobModel)this.modelService.create(ScriptChangeConsumptionJobModel.class);
        this.testJobForTitleConsumeNothing.setCode("testScriptChangeConsumptionJobForTitleNothing");
        this.testJobForTitleConsumeNothing.setInput(prepareMediaCSVReport("FeedDefault", this.typeService
                        .getComposedTypeForClass(TitleModel.class), "testCronjobNothing", "testJobNothing"));
        this.testJobForDeliveryModeWithConsumeDeletedScript = (ScriptChangeConsumptionJobModel)this.modelService.create(ScriptChangeConsumptionJobModel.class);
        this.testJobForDeliveryModeWithConsumeDeletedScript.setCode("testScriptChangeConsumptionJobForDeliveryMode");
        this.testJobForDeliveryModeWithConsumeDeletedScript.setInput(prepareMediaCSVReport("FeedDefault", this.typeService
                        .getComposedTypeForClass(DeliveryModeModel.class), "testCronjob", "testJob"));
        this.testJobForDeliveryModeWithConsumeDeletedScript.setScriptURI("classpath://test/script-change-consumer-deleted.groovy");
        this.testJobForTitleWithConsumeAllScript.setScriptURI("classpath://test/script-change-consumer-all.groovy");
        this.testJobForTitleConsumeNothing.setScriptURI("classpath://test/script-change-consumer-nothing.groovy");
        this.modelService.saveAll(new Object[] {this.testJobForTitleWithConsumeAllScript, this.testJobForDeliveryModeWithConsumeDeletedScript, this.testJobForTitleConsumeNothing});
    }


    @Test
    public void testConsumeAllChangesWithScript() throws Exception
    {
        CronJobModel myCronjobForTitle = prepareCronjob("cronjobForTitle", (JobModel)this.testJobForTitleWithConsumeAllScript);
        InMemoryChangesCollector inMemoryCollector = new InMemoryChangesCollector();
        ComposedTypeModel composedTypeForTitle = this.typeService.getComposedTypeForClass(TitleModel.class);
        this.changeDetectionService.collectChangesForType(composedTypeForTitle, "FeedDefault", (ChangesCollector)inMemoryCollector);
        Assertions.assertThat(inMemoryCollector.getChanges()).hasSize(2);
        Thread.sleep(3000L);
        this.cronJobService.performCronJob(myCronjobForTitle, true);
        Thread.sleep(3000L);
        inMemoryCollector.clearChanges();
        this.changeDetectionService.collectChangesForType(composedTypeForTitle, "FeedDefault", (ChangesCollector)inMemoryCollector);
        Assertions.assertThat(inMemoryCollector.getChanges()).isEmpty();
    }


    @Test
    public void testConsumeNothingWithScript() throws Exception
    {
        CronJobModel myCronjobForTitle = prepareCronjob("cronjobForTitleNothing", (JobModel)this.testJobForTitleConsumeNothing);
        InMemoryChangesCollector inMemoryCollector = new InMemoryChangesCollector();
        ComposedTypeModel composedTypeForTitle = this.typeService.getComposedTypeForClass(TitleModel.class);
        checkCurrentAmountOfChanges(composedTypeForTitle, inMemoryCollector, 2);
        Thread.sleep(3000L);
        this.cronJobService.performCronJob(myCronjobForTitle, true);
        Thread.sleep(3000L);
        checkCurrentAmountOfChanges(composedTypeForTitle, inMemoryCollector, 2);
    }


    @Test
    public void testConsumeDeletedChangesWithScript() throws Exception
    {
        ComposedTypeModel composedTypeForDeliveryMode = this.typeService.getComposedTypeForClass(DeliveryModeModel.class);
        InMemoryChangesCollector inMemoryCollector = new InMemoryChangesCollector();
        checkCurrentAmountOfChanges(composedTypeForDeliveryMode, inMemoryCollector, 3);
        CronJobModel myCronjobForDeliveryModeFoo = prepareCronjob("cronjobForDeliveryModeFoo", (JobModel)this.testJobForDeliveryModeWithConsumeDeletedScript);
        Thread.sleep(3000L);
        this.cronJobService.performCronJob(myCronjobForDeliveryModeFoo, true);
        Thread.sleep(3000L);
        checkCurrentAmountOfChanges(composedTypeForDeliveryMode, inMemoryCollector, 3);
        this.changeDetectionService.consumeChanges(inMemoryCollector.getChanges());
        this.modelService.remove(this.testDeliveryModeX);
        this.modelService.remove(this.testDeliveryModeY);
        this.testDeliveryModeZ.setName("blabla");
        this.modelService.save(this.testDeliveryModeZ);
        checkCurrentAmountOfChanges(composedTypeForDeliveryMode, inMemoryCollector, 3);
        this.testJobForDeliveryModeWithConsumeDeletedScript.setInput(prepareMediaCSVReport("FeedDefault", this.typeService
                        .getComposedTypeForClass(DeliveryModeModel.class), "testCronjobXYZ", "testJobXYZ"));
        this.modelService.save(this.testJobForDeliveryModeWithConsumeDeletedScript);
        CronJobModel myCronjobForDeliveryModeBar = prepareCronjob("cronjobForDeliveryModeBar", (JobModel)this.testJobForDeliveryModeWithConsumeDeletedScript);
        Thread.sleep(3000L);
        this.cronJobService.performCronJob(myCronjobForDeliveryModeBar, true);
        Thread.sleep(3000L);
        checkCurrentAmountOfChanges(composedTypeForDeliveryMode, inMemoryCollector, 1);
        Assertions.assertThat(inMemoryCollector.getChanges()).extracting("changeType").containsOnly(new Object[] {ChangeType.MODIFIED});
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


    private MediaModel prepareMediaCSVReport(String streamId, ComposedTypeModel type, String cronjobCode, String jobCode)
    {
        String mediaCode = "report_" + streamId + "_" + type.getCode() + "_" + cronjobCode + "_" + jobCode;
        CatalogUnawareMediaModel mediaInput = prepareMedia(mediaCode, mediaCode + ".csv", "text/csv");
        String reportAbsolutePath = null;
        FileWriter csvWriter = null;
        try
        {
            Path path = Files.createTempFile(mediaCode, ".csv", (FileAttribute<?>[])new FileAttribute[0]);
            csvWriter = new FileWriter(path.toFile());
            this.changeDetectionService.collectChangesForType(type, streamId, (ChangesCollector)new CsvReportChangesCollector(csvWriter));
            reportAbsolutePath = path.toAbsolutePath().toString();
        }
        catch(Exception e)
        {
            throw new RuntimeException("Could Not prepare report csv file", e);
        }
        finally
        {
            if(csvWriter != null)
            {
                try
                {
                    csvWriter.close();
                    this.modelService.save(mediaInput);
                    this.mediaService.setStreamForMedia((MediaModel)mediaInput, new FileInputStream(reportAbsolutePath));
                }
                catch(IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }
        return (MediaModel)mediaInput;
    }


    private CatalogUnawareMediaModel prepareMedia(String code, String reportFileName, String mimeType)
    {
        CatalogUnawareMediaModel media = (CatalogUnawareMediaModel)this.modelService.create(CatalogUnawareMediaModel.class);
        media.setCode(code);
        media.setMime(mimeType);
        media.setRealFileName(reportFileName);
        return media;
    }
}
