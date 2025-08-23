package de.hybris.y2ysync.task.runner.internal;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.y2ysync.enums.Y2YSyncType;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.model.media.SyncImpExMediaModel;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;
import java.util.zip.ZipInputStream;
import javax.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class ImportZipCreatorTest extends ServicelayerTransactionalBaseTest
{
    private static final String SYNC_EXECUTION_ID = "testExecutionId";
    @Resource
    private ImportZipCreator importZipCreator;
    @Resource
    private ModelService modelService;
    @Resource
    private MediaService mediaService;
    @Resource
    private TypeService typeService;
    private Y2YSyncCronJobModel cronJob;


    @Before
    public void setUp() throws Exception
    {
        this.cronJob = createExportCronJob();
        createSyncMedia("Product", ";productCode1;some description");
        createSyncMedia("Product", ";productCode2;other description");
        createSyncMedia("Title", ";titleCode1;some description");
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


    private void createSyncMedia(String type, String impexLine)
    {
        SyncImpExMediaModel media = (SyncImpExMediaModel)this.modelService.create(SyncImpExMediaModel.class);
        media.setCode("test-code-" + UUID.randomUUID());
        media.setSyncType(this.typeService.getComposedTypeForCode(type));
        media.setImpexHeader("INSERT_UPDATE " + type + ";code[unique=true];description;");
        media.setExportCronJob(this.cronJob);
        this.modelService.save(media);
        this.mediaService.setStreamForMedia((MediaModel)media, getStreamForString(impexLine));
    }


    private InputStream getStreamForString(String line)
    {
        return new ByteArrayInputStream(line.getBytes());
    }


    @Test
    public void shouldSaveSyncImpExMediaWithLongAttributes()
    {
        SyncImpExMediaModel media = (SyncImpExMediaModel)this.modelService.create(SyncImpExMediaModel.class);
        media.setCode(UUID.randomUUID().toString());
        media.setImpexHeader(RandomStringUtils.randomAlphanumeric(10000));
        media.setDataHubColumns(RandomStringUtils.randomAlphanumeric(10000));
        media.setSyncType(this.typeService.getComposedTypeForCode("Product"));
        this.modelService.save(media);
    }


    @Test
    public void shouldGenerateMainZipMedia() throws Exception
    {
        ImportPackage importPackage = this.importZipCreator.createImportMedias("testExecutionId");
        CatalogUnawareMediaModel catalogUnawareMediaModel = importPackage.getMediaData();
        Assertions.assertThat(this.modelService.isNew(catalogUnawareMediaModel)).isFalse();
        Assertions.assertThat(catalogUnawareMediaModel.getCode()).isEqualTo("data-testExecutionId");
        Assertions.assertThat(this.mediaService.hasData((MediaModel)catalogUnawareMediaModel)).isTrue();
        ZipInputStreamAssert.assertThat(getZipInputStreamFromMedia((MediaModel)catalogUnawareMediaModel)).hasNumEntries(4);
        ZipInputStreamAssert.assertThat(getZipInputStreamFromMedia((MediaModel)catalogUnawareMediaModel))
                        .containsEntryWithText("Product-339d26e5fab4676cfc6e25c6a211b6b9-0.csv", ";productCode1;some description");
        ZipInputStreamAssert.assertThat(getZipInputStreamFromMedia((MediaModel)catalogUnawareMediaModel))
                        .containsEntryWithText("Product-339d26e5fab4676cfc6e25c6a211b6b9-1.csv", ";productCode2;other description");
        ZipInputStreamAssert.assertThat(getZipInputStreamFromMedia((MediaModel)catalogUnawareMediaModel))
                        .containsEntryWithText("Title-d509069a33d3922e3455d2e5403885ed-0.csv", ";titleCode1;some description");
        ZipInputStreamAssert.assertThat(getZipInputStreamFromMedia((MediaModel)catalogUnawareMediaModel)).containsEntryWithText("importscript.impex", "INSERT_UPDATE Title;code[unique=true];description;");
        ZipInputStreamAssert.assertThat(getZipInputStreamFromMedia((MediaModel)catalogUnawareMediaModel)).containsEntryWithText("importscript.impex", "INSERT_UPDATE Product;code[unique=true];description;");
        ZipInputStreamAssert.assertThat(getZipInputStreamFromMedia((MediaModel)catalogUnawareMediaModel)).containsEntryWithText("importscript.impex", "\"#% impex.includeExternalDataMedia(\"\"Title-d509069a33d3922e3455d2e5403885ed-0.csv\"\", \"\"UTF-8\"\", ';', 1, -1);\"");
        ZipInputStreamAssert.assertThat(getZipInputStreamFromMedia((MediaModel)catalogUnawareMediaModel)).containsEntryWithText("importscript.impex", "\"#% impex.includeExternalDataMedia(\"\"Product-339d26e5fab4676cfc6e25c6a211b6b9-0.csv\"\", \"\"UTF-8\"\", ';', 1, -1);\"");
        ZipInputStreamAssert.assertThat(getZipInputStreamFromMedia((MediaModel)catalogUnawareMediaModel)).containsEntryWithText("importscript.impex", "\"#% impex.includeExternalDataMedia(\"\"Product-339d26e5fab4676cfc6e25c6a211b6b9-1.csv\"\", \"\"UTF-8\"\", ';', 1, -1);\"");
    }


    private ZipInputStream getZipInputStreamFromMedia(MediaModel mediaModel)
    {
        return new ZipInputStream(this.mediaService.getStreamFromMedia(mediaModel));
    }
}
