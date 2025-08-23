package de.hybris.deltadetection;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.impl.CsvReportChangesCollector;
import de.hybris.deltadetection.impl.InMemoryChangesCollector;
import de.hybris.deltadetection.model.ConsumeAllChangesJobModel;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.model.media.MediaModel;
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
public class ConsumeAllChangesJobTest extends ServicelayerBaseTest
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
    private ConsumeAllChangesJobModel testJobForTitle;
    private TitleModel testTitleFoo;
    private TitleModel testTitleBar;
    private static final String STREAM_ID_DEFAULT = "FeedDefault";


    @Before
    public void setUp() throws Exception
    {
        this.testTitleFoo = (TitleModel)this.modelService.create(TitleModel.class);
        this.testTitleFoo.setCode("Foo");
        this.testTitleBar = (TitleModel)this.modelService.create(TitleModel.class);
        this.testTitleBar.setCode("Bar");
        this.modelService.saveAll(new Object[] {this.testTitleFoo, this.testTitleBar});
        this.testJobForTitle = (ConsumeAllChangesJobModel)this.modelService.create(ConsumeAllChangesJobModel.class);
        this.testJobForTitle.setCode("testChangeConsumptionJobForTitle");
        this.testJobForTitle.setInput(prepareMediaCSVReport("FeedDefault", this.typeService.getComposedTypeForClass(TitleModel.class), "testCronjob", "testJob"));
        this.modelService.save(this.testJobForTitle);
    }


    @Test
    public void testConsumeAllChanges() throws Exception
    {
        CronJobModel myCronjobForTitle = prepareCronjob("cronjobForTitle", (JobModel)this.testJobForTitle);
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
    public void testConsumeSomeChanges() throws InterruptedException
    {
        CronJobModel myCronjobForTitle = prepareCronjob("cronjobForTitle", (JobModel)this.testJobForTitle);
        Object object = new Object(this);
        ComposedTypeModel composedTypeForTitle = this.typeService.getComposedTypeForClass(TitleModel.class);
        this.changeDetectionService.collectChangesForType(composedTypeForTitle, "FeedDefault", (ChangesCollector)object);
        Assertions.assertThat(object.getChanges()).hasSize(1);
        Thread.sleep(3000L);
        this.cronJobService.performCronJob(myCronjobForTitle, true);
        Thread.sleep(3000L);
        object.clearChanges();
        this.changeDetectionService.collectChangesForType(composedTypeForTitle, "FeedDefault", (ChangesCollector)object);
        Assertions.assertThat(object.getChanges()).isEmpty();
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
            reportAbsolutePath = path.toAbsolutePath().toString();
            csvWriter = new FileWriter(path.toFile());
            this.changeDetectionService.collectChangesForType(type, streamId, (ChangesCollector)new CsvReportChangesCollector(csvWriter));
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
