package de.hybris.platform.mediaconversion.imagemagick;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.log4j.Logger;

public enum ImageMagickEnvironment
{
    HOME, LD_LIBRARY_PATH, MAGICK_AREA_LIMIT, MAGICK_CODER_FILTER_PATH, MAGICK_CODER_MODULE_PATH, MAGICK_CONFIGURE_PATH, MAGICK_DEBUG, MAGICK_DISK_LIMIT, MAGICK_FILE_LIMIT, MAGICK_FONT_PATH, MAGICK_HOME, MAGICK_MAP_LIMIT, MAGICK_MEMORY_LIMIT, MAGICK_PRECISION, MAGICK_SYNCHRONIZE, MAGICK_TEMPORARY_PATH, MAGICK_THREAD_LIMIT, MAGICK_THROTTLE, MAGICK_TIME_LIMIT;
    private static final Logger LOG;

    static
    {
        LOG = Logger.getLogger(ImageMagickEnvironment.class);
    }

    String retrieveValue(String confDir, String tmpDir)
    {
        return System.getenv(name());
    }


    static String resourcePath(String path)
    {
        return resourceFile(path).getAbsolutePath();
    }


    static File resourceFile(String path)
    {
        try
        {
            URL url = ImageMagickEnvironment.class.getResource(path);
            if(url == null)
            {
                String msg = "Corrupted installation. Failed to retrieve resource '" + path + "'.";
                LOG.fatal(msg);
                throw new IllegalStateException(msg);
            }
            return new File(url.toURI());
        }
        catch(URISyntaxException e)
        {
            String msg = "Failed to assemble absolute path to resource '" + path + "'.";
            LOG.fatal(msg, e);
            throw new IllegalStateException(msg, e);
        }
    }
}
