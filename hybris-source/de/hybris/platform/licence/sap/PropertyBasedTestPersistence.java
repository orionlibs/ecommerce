package de.hybris.platform.licence.sap;

import de.hybris.bootstrap.config.ConfigUtil;
import java.io.File;
import org.apache.commons.io.FileUtils;

public class PropertyBasedTestPersistence extends DefaultPersistence
{
    private static final String FILE_NAME = "" + ConfigUtil.getPlatformConfig(DefaultPersistence.class).getSystemConfig().getTempDir() + "/testPersistence.properties";


    String getPropsFileName()
    {
        return FILE_NAME;
    }


    public void removePersistenceFile()
    {
        File file = new File(FILE_NAME);
        FileUtils.deleteQuietly(file);
    }
}
