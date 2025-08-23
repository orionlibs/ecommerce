package de.hybris.platform.mediaconversion.imagemagick;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class BasicImageMagickStrategy
{
    private static final Logger LOG = Logger.getLogger(BasicImageMagickStrategy.class);
    public static final String IMAGEMAGICK_CONFIGURATION_DIRECTORY = "imagemagick.configuration.directory";
    @Deprecated(since = "6.1", forRemoval = true)
    public static final String EMBEDDED_CONFIG_DIR = "/mediaconversion/imagemagick/config/";
    private ConfigurationService configurationService;
    private File configurationDirectory;
    private File tmpDir;


    public File getTmpDir()
    {
        if(this.tmpDir == null)
        {
            this.tmpDir = new File(System.getProperty("HYBRIS_TEMP_DIR", System.getProperty("java.io.tmpdir")), "convert");
            LOG.info("Using tmp dir '" + this.tmpDir.getAbsolutePath() + "'.");
        }
        if(!this.tmpDir.isDirectory() && !this.tmpDir.mkdirs())
        {
            LOG.warn("Failed to create tmp directory '" + this.tmpDir.getAbsolutePath() + "'.");
        }
        return this.tmpDir;
    }


    public void setTmpDir(File tmpDir)
    {
        this.tmpDir = tmpDir;
    }


    public File getConfigurationDirectory() throws IOException
    {
        if(this.configurationDirectory == null)
        {
            this.configurationDirectory = buildConfigurationDirectory();
            LOG.info("Using image magick configuration directory '" + this.configurationDirectory.getAbsolutePath() + "'.");
        }
        return this.configurationDirectory;
    }


    private File buildConfigurationDirectory() throws IOException
    {
        synchronized(this)
        {
            if(this.configurationDirectory != null)
            {
                return this.configurationDirectory;
            }
            String path = getConfigurationService().getConfiguration().getString("imagemagick.configuration.directory",
                            System.getenv(ImageMagickEnvironment.MAGICK_CONFIGURE_PATH.name()));
            if(path != null)
            {
                File ret = new File(path);
                if(ret.exists())
                {
                    return ret;
                }
                LOG.warn("Specified ImageMagick configuration directory '" + ret.getAbsolutePath() + "' does not exist!");
            }
            throw new IOException("Failed to find ImageMagick configuration directory. Please adjust the property: imagemagick.configuration.directory to your local Image Magick installation");
        }
    }


    public void setConfigurationDirectory(File configurationDirectory)
    {
        this.configurationDirectory = configurationDirectory;
    }


    public ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
