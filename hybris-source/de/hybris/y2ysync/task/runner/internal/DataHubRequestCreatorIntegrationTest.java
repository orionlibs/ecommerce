package de.hybris.y2ysync.task.runner.internal;

import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.util.Config;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;

public class DataHubRequestCreatorIntegrationTest extends ServicelayerBaseTest
{
    @Resource
    private DataHubRequestCreator dataHubRequestCreator;
    private final PropertyConfigSwitcher y2ySyncWebRootProperty = new PropertyConfigSwitcher("y2ysync.webroot");
    private final PropertyConfigSwitcher y2ySyncHomeUrlProperty = new PropertyConfigSwitcher("y2ysync.home.url");
    final String defaultHomeUrl = Config.getString("y2ysync.home.url", "http://localhost:9001");
    final String defaultWebRoot = Config.getString("y2ysync.webroot", "/y2ysync");


    @After
    public void tearDown() throws Exception
    {
        this.y2ySyncWebRootProperty.switchBackToDefault();
        this.y2ySyncHomeUrlProperty.switchBackToDefault();
    }


    @Test
    public void shouldReturnDefaultJunitTenantY2YSyncWebRoot() throws Exception
    {
        String webRoot = this.dataHubRequestCreator.getY2YSyncWebRoot();
        Assertions.assertThat(webRoot).isEqualTo(this.defaultHomeUrl + this.defaultHomeUrl);
    }


    @Test
    public void shouldReturnCustomY2YSyncWebRoot() throws Exception
    {
        this.y2ySyncWebRootProperty.switchToValue("/y2ysync_custom");
        String webRoot = this.dataHubRequestCreator.getY2YSyncWebRoot();
        Assertions.assertThat(webRoot).isEqualTo(this.defaultHomeUrl + "/y2ysync_custom");
    }


    @Test
    public void shouldReturnDefaultY2YSyncHomeUrl() throws Exception
    {
        String homeUrl = this.dataHubRequestCreator.getHomeUrl();
        Assertions.assertThat(homeUrl).isEqualTo(this.defaultHomeUrl);
    }


    @Test
    public void shouldReturnCustomY2YSyncHomeUrl() throws Exception
    {
        this.y2ySyncHomeUrlProperty.switchToValue("http://192.168.1.5:8080");
        String homeUrl = this.dataHubRequestCreator.getHomeUrl();
        Assertions.assertThat(homeUrl).isEqualTo("http://192.168.1.5:8080");
    }
}
