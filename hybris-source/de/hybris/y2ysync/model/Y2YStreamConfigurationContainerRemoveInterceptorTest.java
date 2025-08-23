package de.hybris.y2ysync.model;

import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.y2ysync.services.SyncConfigService;
import de.hybris.y2ysync.services.SyncExecutionService;
import javax.annotation.Resource;
import junit.framework.TestCase;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class Y2YStreamConfigurationContainerRemoveInterceptorTest extends ServicelayerBaseTest
{
    @Resource
    private SyncConfigService syncConfigService;
    @Resource
    private SyncExecutionService syncExecutionService;
    @Resource
    private ModelService modelService;
    private Y2YStreamConfigurationContainerModel container;


    @Before
    public void setUp() throws Exception
    {
        this.container = this.syncConfigService.createStreamConfigurationContainer("TEST_STREAM");
        this.modelService.save(this.container);
    }


    @Test
    public void shouldPreventRemovingContainerIfItHasConnectedJobs() throws Exception
    {
        Y2YSyncJobModel testJob = this.syncExecutionService.createSyncJobForDataHub("TEST_JOB", this.container);
        this.modelService.save(testJob);
        try
        {
            this.modelService.remove(this.container);
            TestCase.fail("Should throw ModelRemovalException");
        }
        catch(ModelRemovalException e)
        {
            Assertions.assertThat(e.getCause()).isInstanceOf(InterceptorException.class);
            Assertions.assertThat(this.modelService.isRemoved(this.container)).isFalse();
            Assertions.assertThat(this.modelService.isRemoved(testJob)).isFalse();
        }
    }


    @Test
    public void shouldAllowRemoveContainerIfItHasNoJobsConnected() throws Exception
    {
        this.modelService.remove(this.container);
        Assertions.assertThat(this.modelService.isRemoved(this.container)).isTrue();
    }
}
