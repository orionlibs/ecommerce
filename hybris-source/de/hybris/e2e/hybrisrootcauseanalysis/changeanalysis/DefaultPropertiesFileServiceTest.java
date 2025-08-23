package de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.E2EPropertiesFileService;
import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.impl.DefaultPropertiesFileService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;
import de.hybris.platform.util.Utilities;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DefaultPropertiesFileServiceTest extends HybrisJUnit4TransactionalTest
{
    private static final Logger LOG = Logger.getLogger(DefaultPropertiesFileServiceTest.class.getName());
    private E2EPropertiesFileService propertiesFileService;
    private static final String NAME_FILE = "test.properties";
    private Properties prop;


    @Before
    public void setUp()
    {
        this.propertiesFileService = (E2EPropertiesFileService)Registry.getApplicationContext().getBean("defaultE2EPropertiesFileService", DefaultPropertiesFileService.class);
        this.prop = new Properties();
        this.prop.put("mykey1", "myvalue1");
        this.prop.put("mykey2", "myvalue2");
        this.propertiesFileService.writeFile(this.prop, "test.properties");
    }


    @After
    public void tearDown()
    {
    }


    @Test
    public void isFileExist()
    {
        File dataDir = ConfigUtil.getPlatformConfig(Utilities.class).getSystemConfig().getDataDir();
        File file = new File("" + dataDir.getAbsoluteFile() + dataDir.getAbsoluteFile() + "e2erootcauseanalysis" + File.separator + "test.properties");
        Assert.assertTrue("File creation", file.exists());
    }


    @Test
    public void testPropertiesInFile()
    {
        File dataDir = ConfigUtil.getPlatformConfig(Utilities.class).getSystemConfig().getDataDir();
        File file = new File("" + dataDir.getAbsoluteFile() + dataDir.getAbsoluteFile() + "e2erootcauseanalysis" + File.separator + "test.properties");
        Properties properties = new Properties();
        InputStream input = null;
        try
        {
            input = new FileInputStream(file);
            properties.load(input);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        Assert.assertEquals("check the properties is equal", properties, this.prop);
        Assert.assertEquals("check if the value is equals", properties.getProperty("mykey1"), this.prop.getProperty("mykey1"));
    }
}
