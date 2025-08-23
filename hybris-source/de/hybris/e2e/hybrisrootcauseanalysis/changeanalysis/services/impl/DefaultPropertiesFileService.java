package de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.impl;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.E2EPropertiesFileService;
import de.hybris.e2e.hybrisrootcauseanalysis.utils.E2EUtils;
import de.hybris.platform.util.Utilities;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import org.apache.log4j.Logger;

public class DefaultPropertiesFileService implements E2EPropertiesFileService
{
    private static final Logger LOG = Logger.getLogger(DefaultPropertiesFileService.class.getName());
    private String nameFolder;


    public void setNameFolder(String nameFolder)
    {
        this.nameFolder = nameFolder;
    }


    public void writeFile(Properties properties, String nameFile)
    {
        if(properties == null || properties.size() == 0)
        {
            LOG.info("Properties empty, the file " + nameFile + " will not be generated");
            return;
        }
        File dataDir = ConfigUtil.getPlatformConfig(Utilities.class).getSystemConfig().getDataDir();
        File file = E2EUtils.checkDirectory(dataDir.getAbsolutePath(), this.nameFolder);
        if(file == null)
        {
            return;
        }
        try
        {
            FileOutputStream fos = new FileOutputStream(file.getAbsolutePath() + file.getAbsolutePath() + File.separator);
            try
            {
                OutputStreamWriter output = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                try
                {
                    properties.store(output, "**** E2E CHANGE ANALYSIS FILE AUTOMATICALLY GENERATED *****");
                    output.close();
                }
                catch(Throwable throwable)
                {
                    try
                    {
                        output.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                    throw throwable;
                }
                fos.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    fos.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        catch(IOException io)
        {
            LOG.error("Error saving file: " + nameFile);
            LOG.error(io);
        }
    }
}
