package de.hybris.bootstrap.config;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.util.Utilities;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@UnitTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:test/dummy-test-spring.xml"})
public class TenantsInfoLoaderTest
{
    private PlatformConfig localPlatformConfig;
    private File platformHome;
    private File configDir;


    @Before
    public void setUp()
    {
        this.localPlatformConfig = Utilities.getPlatformConfig();
        String testDir = "ext/core/resources/test";
        this.platformHome = new File(this.localPlatformConfig.getPlatformHome(), "ext/core/resources/test");
        this.configDir = this.platformHome;
    }


    @Test
    public void testAssureTenantsInfoLoaderWhenNoLocalNoGlobalFileAvaialble()
    {
        try
        {
            TestTenantsInforLoader loader = new TestTenantsInforLoader(this.localPlatformConfig, Collections.singleton("noProp"), this.platformHome, this.configDir);
            loader.getSlaveTenants();
            Assert.fail("BootstrapConfigException expected");
        }
        catch(BootstrapConfigException bootstrapConfigException)
        {
        }
    }


    @Test
    public void testLoadTwoTenants()
    {
        TestTenantsInforLoader loader = new TestTenantsInforLoader(this.localPlatformConfig, Arrays.asList(new String[] {"foo", "bar"}, ), this.platformHome, this.configDir);
        Map<String, TenantInfo> slaveTenants = loader.getSlaveTenants();
        Assert.assertEquals(2, slaveTenants.size());
        Assert.assertNotNull(slaveTenants.get("bar"));
        Assert.assertNotNull(slaveTenants.get("foo"));
    }


    @Test
    public void testAssureTenantsInfoLoaderWhenGlobalFileAvailable()
    {
        TestTenantsInforLoader loader = new TestTenantsInforLoader(this.localPlatformConfig, Collections.singleton("bar"), this.platformHome, this.configDir);
        Map<String, TenantInfo> slaveTenants = loader.getSlaveTenants();
        Assert.assertEquals(1, slaveTenants.size());
        for(Map.Entry<String, TenantInfo> entry : slaveTenants.entrySet())
        {
            Assert.assertEquals("bar", entry.getKey());
            TenantInfo tenantBarInfo = entry.getValue();
            Properties tenantBarProperties = tenantBarInfo.getTenantProperties();
            Assert.assertEquals(3, tenantBarProperties.size());
            Assert.assertEquals("value2", tenantBarProperties.get("foo"));
            Assert.assertEquals("${db.url}", tenantBarProperties.get("faa"));
            String allowedExtensions = (String)tenantBarProperties.get("allowed.extensions");
            Assert.assertTrue((allowedExtensions.indexOf("core") > -1));
        }
    }


    @Test
    public void testAssureTenantsInfoLoaderWhenLocalFileAvailable()
    {
        TestTenantsInforLoader loader = new TestTenantsInforLoader(this.localPlatformConfig, Collections.singleton("foo"), this.platformHome, this.configDir);
        Map<String, TenantInfo> slaveTenants = loader.getSlaveTenants();
        Assert.assertEquals(1, slaveTenants.size());
        for(Map.Entry<String, TenantInfo> entry : slaveTenants.entrySet())
        {
            Assert.assertEquals("foo", entry.getKey());
            TenantInfo tenantBarInfo = entry.getValue();
            Properties tenantBarProperties = tenantBarInfo.getTenantProperties();
            Assert.assertEquals(5, tenantBarProperties.size());
            Assert.assertEquals("value1", tenantBarProperties.get("boo"));
            String replacedProp = this.localPlatformConfig.getSystemConfig().replaceProperties("${HYBRIS_BIN_DIR}");
            Assert.assertEquals(replacedProp, tenantBarProperties.get("baa"));
            String allowedExtensions = (String)tenantBarProperties.get("allowed.extensions");
            Assert.assertTrue((allowedExtensions.indexOf("core") > -1));
            Assert.assertEquals("/baar", tenantBarInfo.getWebMapping("baar"));
            Assert.assertTrue(tenantBarInfo.isWebrootOwner("/baar"));
            Assert.assertEquals("boor", tenantBarInfo.getWebMapping("boor"));
            Assert.assertTrue(tenantBarInfo.isWebrootOwner("boor"));
            Assert.assertFalse(tenantBarInfo.isWebrootOwner("/foo"));
        }
    }


    @Test
    public void testAssureTenantsInfoLoaderWhenBothFilesAvailable()
    {
        TestTenantsInforLoader loader = new TestTenantsInforLoader(this.localPlatformConfig, Collections.singleton("both"), this.platformHome, this.configDir);
        Map<String, TenantInfo> slaveTenants = loader.getSlaveTenants();
        Assert.assertEquals(1, slaveTenants.size());
        for(Map.Entry<String, TenantInfo> entry : slaveTenants.entrySet())
        {
            Assert.assertEquals("both", entry.getKey());
            TenantInfo tenantBarInfo = entry.getValue();
            Properties tenantBarProperties = tenantBarInfo.getTenantProperties();
            Assert.assertEquals(2, tenantBarProperties.size());
            Assert.assertEquals("valueLocal", tenantBarProperties.get("brr"));
            String allowedExtensions = (String)tenantBarProperties.get("allowed.extensions");
            Assert.assertTrue((allowedExtensions.indexOf("core") > -1));
        }
    }
}
