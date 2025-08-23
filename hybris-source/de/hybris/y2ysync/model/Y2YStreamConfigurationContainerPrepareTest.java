package de.hybris.y2ysync.model;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.y2ysync.services.SyncConfigService;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class Y2YStreamConfigurationContainerPrepareTest extends ServicelayerTransactionalTest
{
    @Resource
    private ModelService modelService;
    @Resource
    private SyncConfigService syncConfigService;
    private Y2YStreamConfigurationContainerModel testContainer;


    @Before
    public void setUp() throws Exception
    {
        this.testContainer = this.syncConfigService.createStreamConfigurationContainer("testContainer");
    }


    @Test
    public void shouldGenerateFeedAndPool()
    {
        this.testContainer = this.syncConfigService.createStreamConfigurationContainer("testContainer");
        this.modelService.save(this.testContainer);
        Assertions.assertThat(this.testContainer.getFeed()).isEqualTo("testContainer_feed");
        Assertions.assertThat(this.testContainer.getPool()).isEqualTo("testContainer_pool");
    }


    @Test
    public void shouldNotGenerateFeedAndPoolIfExplicitlySet()
    {
        this.testContainer = this.syncConfigService.createStreamConfigurationContainer("testContainer");
        this.testContainer.setFeed("customFeed");
        this.testContainer.setPool("customPool");
        this.modelService.save(this.testContainer);
        Assertions.assertThat(this.testContainer.getFeed()).isEqualTo("customFeed");
        Assertions.assertThat(this.testContainer.getPool()).isEqualTo("customPool");
    }
}
