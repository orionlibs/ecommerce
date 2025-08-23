package de.hybris.y2ysync.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.y2ysync.enums.Y2YSyncType;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.services.SyncExecutionService;
import java.util.Collection;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Test;

@IntegrationTest
public class DefaultSyncExecutionServiceIntegrationTest extends ServicelayerBaseTest
{
    @Resource(name = "syncExecutionService")
    private SyncExecutionService service;
    @Resource
    private ModelService modelService;


    @Test
    public void shouldCreateSyncJobForDataHub() throws Exception
    {
        String syncJobCode = "testJob";
        Y2YStreamConfigurationContainerModel containter = createStreamConfigurationContainer();
        Y2YSyncJobModel job = this.service.createSyncJobForDataHub("testJob", containter);
        Assertions.assertThat(job).isNotNull();
        Assertions.assertThat(this.modelService.isNew(job)).isTrue();
        Assertions.assertThat((Comparable)job.getSyncType()).isEqualTo(Y2YSyncType.DATAHUB);
        Assertions.assertThat(job.getDataHubUrl()).isNull();
        Assertions.assertThat(job.getStreamConfigurationContainer()).isEqualTo(containter);
    }


    @Test
    public void shouldCreateSyncJobForDataHubWithCustomURL() throws Exception
    {
        String syncJobCode = "testJob";
        String customURL = "http://foo.bar.baz";
        Y2YStreamConfigurationContainerModel containter = createStreamConfigurationContainer();
        Y2YSyncJobModel job = this.service.createSyncJobForDataHub("testJob", containter, "http://foo.bar.baz");
        Assertions.assertThat(job).isNotNull();
        Assertions.assertThat(this.modelService.isNew(job)).isTrue();
        Assertions.assertThat((Comparable)job.getSyncType()).isEqualTo(Y2YSyncType.DATAHUB);
        Assertions.assertThat(job.getDataHubUrl()).isEqualTo("http://foo.bar.baz");
        Assertions.assertThat(job.getStreamConfigurationContainer()).isEqualTo(containter);
    }


    @Test
    public void shouldCreateSyncJobForZip() throws Exception
    {
        String syncJobCode = "testJob";
        Y2YStreamConfigurationContainerModel containter = createStreamConfigurationContainer();
        Y2YSyncJobModel job = this.service.createSyncJobForZip("testJob", containter);
        Assertions.assertThat(job).isNotNull();
        Assertions.assertThat(this.modelService.isNew(job)).isTrue();
        Assertions.assertThat((Comparable)job.getSyncType()).isEqualTo(Y2YSyncType.ZIP);
        Assertions.assertThat(job.getDataHubUrl()).isNull();
        Assertions.assertThat(job.getStreamConfigurationContainer()).isEqualTo(containter);
    }


    @Test
    public void shouldReturnAllSyncJobsPresentInTheSystem() throws Exception
    {
        Y2YStreamConfigurationContainerModel containter = createStreamConfigurationContainer();
        Y2YSyncJobModel job1 = this.service.createSyncJobForZip("testJob1", containter);
        Y2YSyncJobModel job2 = this.service.createSyncJobForDataHub("testJob2", containter);
        this.modelService.saveAll(new Object[] {job1, job2});
        Collection<Y2YSyncJobModel> syncJobs = this.service.getAllSyncJobs();
        Assertions.assertThat(syncJobs).isNotNull().hasSize(2);
        Assertions.assertThat(syncJobs).containsOnly((Object[])new Y2YSyncJobModel[] {job1, job2});
    }


    private Y2YStreamConfigurationContainerModel createStreamConfigurationContainer()
    {
        Y2YStreamConfigurationContainerModel container = (Y2YStreamConfigurationContainerModel)this.modelService.create(Y2YStreamConfigurationContainerModel.class);
        container.setId("testContainer");
        this.modelService.save(container);
        return container;
    }
}
