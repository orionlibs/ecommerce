package de.hybris.y2ysync.job;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.hybris.bootstrap.annotations.ManualTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.systemsetup.datacreator.impl.EncodingsDataCreator;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.LogFileModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.model.cronjob.ImpExExportJobModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.testframework.HybrisJUnit4Test;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.y2ysync.model.Y2YColumnDefinitionModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.services.SyncConfigService;
import de.hybris.y2ysync.services.SyncExecutionService;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreator;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

@ManualTest
public class Y2YSyncMasterJobPerformableTest extends ServicelayerBaseTest
{
    private static final Logger LOG = Logger.getLogger(Y2YSyncMasterJobPerformableTest.class);
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private ModelService modelService;
    @Resource
    private CatalogVersionService catalogVersionService;
    @Resource
    private CronJobService cronJobService;
    @Resource
    private ImportService importService;
    @Resource
    private SyncConfigService syncConfigService;
    @Resource
    SyncExecutionService syncExecutionService;
    @Resource
    private TypeService typeService;
    @Resource
    private FlexibleSearchService flexibleSearchService;
    @Resource
    private MediaService mediaService;
    private final PropertyConfigSwitcher dataHubUrlProperty = new PropertyConfigSwitcher("y2ysync.datahub.url");
    private Y2YStreamConfigurationContainerModel dataHubSyncContainer;
    private Y2YStreamConfigurationContainerModel zipSyncContainer;


    @Before
    public void setUp() throws Exception
    {
        (new EncodingsDataCreator()).populateDatabase();
        importCsv("/test/source_test_catalog.csv", "utf-8");
        ImpExExportJobModel exportJob = (ImpExExportJobModel)this.modelService.create(ImpExExportJobModel.class);
        exportJob.setCode("ImpEx-Export");
        this.modelService.save(exportJob);
        this.dataHubUrlProperty.switchToValue("/datahub-webapp/v1/data-feeds/y2ysync");
        List<CatalogVersionModel> vers = getTestCatalogVersions();
        this.dataHubSyncContainer = this.syncConfigService.createStreamConfigurationContainer("dataHubSyncContainer", this.catalogVersionService
                        .getCatalogVersion("CatalogA", "CatalogVersionA1"));
        this.modelService.save(this.dataHubSyncContainer);
        for(CatalogVersionModel ctgVer : vers)
        {
            Y2YStreamConfigurationModel ctgVerConf = this.syncConfigService.createStreamConfiguration(this.dataHubSyncContainer, "CatalogVersion", "{version} = ?catalogVersionCode", ctgVer,
                            prepareCatalogVersionAttributeDescriptors(),
                            Collections.emptySet());
            Y2YStreamConfigurationModel categConf = this.syncConfigService.createStreamConfiguration(this.dataHubSyncContainer, "Category", ctgVer,
                            prepareCategoryAttributeDescriptors(),
                            Collections.emptySet());
            this.modelService.saveAll(new Object[] {ctgVerConf, categConf});
        }
        this.modelService.save(this.syncConfigService.createStreamConfiguration(this.dataHubSyncContainer, "Product",
                        prepareProductAttributeDescriptors(),
                        Collections.emptySet()));
        this.zipSyncContainer = this.syncConfigService.createStreamConfigurationContainer("zipSyncContainer", this.catalogVersionService
                        .getCatalogVersion("CatalogA", "CatalogVersionA1"));
        this.modelService.save(this.dataHubSyncContainer);
        for(CatalogVersionModel ctgVer : vers)
        {
            Y2YStreamConfigurationModel ctgVerConf = this.syncConfigService.createStreamConfiguration(this.zipSyncContainer, "CatalogVersion", "{version} = ?catalogVersionCode", ctgVer,
                            prepareCatalogVersionAttributeDescriptors(),
                            Collections.emptySet());
            Y2YStreamConfigurationModel categConf = this.syncConfigService.createStreamConfiguration(this.zipSyncContainer, "Category", ctgVer,
                            prepareCategoryAttributeDescriptors(),
                            Collections.emptySet());
            Y2YStreamConfigurationModel mediaConf = this.syncConfigService.createStreamConfiguration(this.zipSyncContainer, "Media", ctgVer,
                            prepareMediaAttributeDescriptors(),
                            Sets.newHashSet((Object[])new Y2YColumnDefinitionModel[] {this.syncConfigService.createUntypedColumnDefinition("@media[translator=de.hybris.platform.impex.jalo.media.MediaDataTranslator]", "media")}));
            this.modelService.saveAll(new Object[] {ctgVerConf, categConf, mediaConf});
        }
        this.modelService.save(this.syncConfigService.createStreamConfiguration(this.zipSyncContainer, "Product",
                        prepareProductAttributeDescriptors(),
                        Collections.emptySet()));
    }


    private Set<AttributeDescriptorModel> prepareCatalogVersionAttributeDescriptors()
    {
        Set<AttributeDescriptorModel> res = new HashSet<>();
        res.add(this.typeService.getAttributeDescriptor("CatalogVersion", "catalog"));
        res.add(this.typeService.getAttributeDescriptor("CatalogVersion", "version"));
        res.add(this.typeService.getAttributeDescriptor("CatalogVersion", "languages"));
        res.add(this.typeService.getAttributeDescriptor("CatalogVersion", "inclAssurance"));
        res.add(this.typeService.getAttributeDescriptor("CatalogVersion", "inclDuty"));
        res.add(this.typeService.getAttributeDescriptor("CatalogVersion", "inclFreight"));
        res.add(this.typeService.getAttributeDescriptor("CatalogVersion", "inclPacking"));
        res.add(this.typeService.getAttributeDescriptor("CatalogVersion", "active"));
        return res;
    }


    private Set<AttributeDescriptorModel> prepareCategoryAttributeDescriptors()
    {
        Set<AttributeDescriptorModel> res = new HashSet<>();
        res.add(this.typeService.getAttributeDescriptor("Category", "code"));
        res.add(this.typeService.getAttributeDescriptor("Category", "name"));
        res.add(this.typeService.getAttributeDescriptor("Category", "catalogVersion"));
        return res;
    }


    private Set<AttributeDescriptorModel> prepareProductAttributeDescriptors()
    {
        Set<AttributeDescriptorModel> res = new HashSet<>();
        res.add(this.typeService.getAttributeDescriptor("Product", "code"));
        res.add(this.typeService.getAttributeDescriptor("Product", "catalogVersion"));
        res.add(this.typeService.getAttributeDescriptor("Product", "name"));
        res.add(this.typeService.getAttributeDescriptor("Product", "unit"));
        res.add(this.typeService.getAttributeDescriptor("Product", "supercategories"));
        res.add(this.typeService.getAttributeDescriptor("Product", "approvalStatus"));
        return res;
    }


    private Set<AttributeDescriptorModel> prepareMediaAttributeDescriptors()
    {
        Set<AttributeDescriptorModel> res = new HashSet<>();
        res.add(this.typeService.getAttributeDescriptor("Media", "code"));
        res.add(this.typeService.getAttributeDescriptor("Media", "catalogVersion"));
        res.add(this.typeService.getAttributeDescriptor("Media", "mediaFormat"));
        res.add(this.typeService.getAttributeDescriptor("Media", "mime"));
        res.add(this.typeService.getAttributeDescriptor("Media", "folder"));
        return res;
    }


    @After
    public void tearDown() throws Exception
    {
        this.dataHubUrlProperty.switchBackToDefault();
    }


    @Test
    public void shouldRunSyncProcessToDataHub() throws Exception
    {
        Y2YSyncJobModel job = this.syncExecutionService.createSyncJobForDataHub("dataHubTestSyncJob", this.dataHubSyncContainer);
        this.modelService.save(job);
        MockRestServiceServer server = getMockRestServer();
        server.expect(MockRestRequestMatchers.requestTo("/datahub-webapp/v1/data-feeds/y2ysync"))
                        .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                        .andExpect(MockRestRequestMatchers.content().contentType("application/json;charset=UTF-8"))
                        .andRespond((ResponseCreator)MockRestResponseCreators.withSuccess());
        this.syncExecutionService.startSync(job, SyncExecutionService.ExecutionMode.SYNC);
        boolean cronJobFinished = pollForFinishedCronJob(job);
        Assertions.assertThat(cronJobFinished).isTrue();
        server.verify();
    }


    private boolean pollForFinishedCronJob(Y2YSyncJobModel job) throws InterruptedException
    {
        Optional<CronJobModel> cronJobOpt = job.getCronJobs().stream().findFirst();
        if(cronJobOpt.isPresent())
        {
            CronJobModel cronJob = cronJobOpt.get();
            LocalDateTime start = LocalDateTime.now();
            do
            {
                this.modelService.refresh(cronJob);
                if(this.cronJobService.isFinished(cronJob))
                {
                    return true;
                }
                Thread.sleep(1000L);
            }
            while(LocalDateTime.now().isBefore(start.plusSeconds(20L)));
        }
        return false;
    }


    @Test
    public void shouldRunSyncProcessToZip() throws Exception
    {
        Y2YSyncJobModel job = this.syncExecutionService.createSyncJobForZip("zipTestSyncJob", this.zipSyncContainer);
        this.modelService.save(job);
        this.syncExecutionService.startSync(job, SyncExecutionService.ExecutionMode.SYNC);
        Optional<CronJobModel> cronJobOpt = job.getCronJobs().stream().findFirst();
        if(!cronJobOpt.isPresent())
        {
            Assert.fail("The job " + job + " has not CronJob attached");
        }
        Y2YSyncCronJobModel cronJob = (Y2YSyncCronJobModel)cronJobOpt.get();
        CatalogUnawareMediaModel foundMediaData = getImportMedia(cronJob, Y2YSyncCronJobModel::getImpexZip);
        CatalogUnawareMediaModel foundMediaBinaries = getImportMedia(cronJob, Y2YSyncCronJobModel::getMediasZip);
        ((AbstractObjectAssert)Assertions.assertThat(foundMediaData).isNotNull()).extracting(new Function[] {MediaModel::getCode}).isEqualTo("data-" + cronJob.getCode());
        ((AbstractObjectAssert)Assertions.assertThat(foundMediaBinaries).isNotNull()).extracting(new Function[] {MediaModel::getCode}).isEqualTo("media-" + cronJob.getCode());
        assertLogFilesCreated(cronJob);
    }


    @Test
    public void shouldWaitWithSyncProcessWithNotMatchingNodeGroup() throws Exception
    {
        Y2YSyncJobModel job = this.syncExecutionService.createSyncJobForZip("zipTestSyncJob", this.zipSyncContainer);
        String notMatchingNodeGroup = "badGroup";
        job.setNodeGroup("badGroup");
        this.modelService.save(job);
        Assertions.assertThat(findCurrentTasksForNodeGroup(job.getNodeGroup())).isEmpty();
        this.syncExecutionService.startSync(job, SyncExecutionService.ExecutionMode.SYNC);
        Optional<CronJobModel> cronJobOpt = job.getCronJobs().stream().findFirst();
        if(!cronJobOpt.isPresent())
        {
            Assert.fail("The job " + job + " has not CronJob attached");
        }
        Y2YSyncCronJobModel cronJob = (Y2YSyncCronJobModel)cronJobOpt.get();
        CatalogUnawareMediaModel foundMediaData = getImportMedia(cronJob, Y2YSyncCronJobModel::getImpexZip);
        CatalogUnawareMediaModel foundMediaBinaries = getImportMedia(cronJob, Y2YSyncCronJobModel::getMediasZip);
        Assertions.assertThat(foundMediaData).isNull();
        Assertions.assertThat(foundMediaBinaries).isNull();
        Assertions.assertThat(findCurrentTasksForNodeGroup(job.getNodeGroup())).isNotEmpty();
        assertLogFilesCreated(cronJob);
    }


    private void assertLogFilesCreated(Y2YSyncCronJobModel cronJob)
    {
        Assertions.assertThat(cronJob.getLogFiles()).isNotEmpty();
        for(LogFileModel logFileModel : cronJob.getLogFiles())
        {
            Assertions.assertThat(this.mediaService.getDataFromMedia((MediaModel)logFileModel)).isNotEmpty();
        }
    }


    private MockRestServiceServer getMockRestServer()
    {
        return MockRestServiceServer.createServer(this.restTemplate);
    }


    private List<CatalogVersionModel> getTestCatalogVersions()
    {
        return Lists.newArrayList((Object[])new CatalogVersionModel[] {this.catalogVersionService
                        .getCatalogVersion("CatalogA", "CatalogVersionA1"), this.catalogVersionService
                        .getCatalogVersion("CatalogA", "CatalogVersionA2"), this.catalogVersionService
                        .getCatalogVersion("CatalogA", "CatalogVersionA3"), this.catalogVersionService
                        .getCatalogVersion("CatalogB", "CatalogVersionB1")});
    }


    private CatalogUnawareMediaModel getImportMedia(Y2YSyncCronJobModel cronJob, Function<Y2YSyncCronJobModel, CatalogUnawareMediaModel> cronJobCallback) throws InterruptedException
    {
        LocalDateTime start = LocalDateTime.now();
        do
        {
            this.modelService.refresh(cronJob);
            if(cronJobCallback.apply(cronJob) != null)
            {
                return cronJobCallback.apply(cronJob);
            }
            Thread.sleep(1000L);
        }
        while(LocalDateTime.now().isBefore(start.plusSeconds(20L)));
        return null;
    }


    private void importCsv(String csvFile, String encoding) throws ImpExException
    {
        LOG.info("importing resource " + csvFile);
        InputStream inputstream = HybrisJUnit4Test.class.getResourceAsStream(csvFile);
        ImportConfig config = new ImportConfig();
        config.setScript((ImpExResource)new StreamBasedImpExResource(inputstream, encoding));
        ImportResult importResult = this.importService.importData(config);
        if(importResult.hasUnresolvedLines())
        {
            Assert.fail("Import has unresolved lines:\n" + importResult.getUnresolvedLines());
        }
        Assertions.assertThat(importResult.isError()).isFalse();
    }


    private List<TaskModel> findCurrentTasksForNodeGroup(String nodeGroup)
    {
        String query = "SELECT {PK} FROM {Task} WHERE {nodeGroup}=?nodeGroup";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {Task} WHERE {nodeGroup}=?nodeGroup", (Map)ImmutableMap.of("nodeGroup", nodeGroup));
        List<TaskModel> tasks = this.flexibleSearchService.search(fQuery).getResult();
        return tasks;
    }
}
