package de.hybris.platform.util.config;

import de.hybris.platform.util.Config;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Optional;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileBasedConfigLoader implements RuntimeConfigLoader
{
    private static final Logger LOG = LoggerFactory.getLogger(FileBasedConfigLoader.class);
    private static final String RUNTIME_CONFIG_FILE_PATH = "runtime.config.file.path";
    private long lastLoaded;


    public Optional<Properties> load()
    {
        Path propertiesToRead = getConfigFilePath();
        if(shouldLoad(propertiesToRead))
        {
            Properties properties = new Properties();
            try
            {
                InputStream is = Files.newInputStream(propertiesToRead, new java.nio.file.OpenOption[0]);
                try
                {
                    properties.load(is);
                    this.lastLoaded = System.currentTimeMillis();
                    LOG.debug("Loading new set of properties [timestamp: {}]", Long.valueOf(this.lastLoaded));
                    Optional<Properties> optional = Optional.of(properties);
                    if(is != null)
                    {
                        is.close();
                    }
                    return optional;
                }
                catch(Throwable throwable)
                {
                    if(is != null)
                    {
                        try
                        {
                            is.close();
                        }
                        catch(Throwable throwable1)
                        {
                            throwable.addSuppressed(throwable1);
                        }
                    }
                    throw throwable;
                }
            }
            catch(IOException e)
            {
                LOG.error("Problem reading properties file [reason: {}]", e.getMessage());
                LOG.debug("Problem reading properties file", e);
            }
        }
        return Optional.empty();
    }


    Path getConfigFilePath()
    {
        String filePath = Config.getString("runtime.config.file.path", null);
        LOG.debug("Runtime config watched file: {}", filePath);
        return (filePath == null) ? null : Paths.get(filePath, new String[0]);
    }


    private boolean shouldLoad(Path propertiesToRead)
    {
        if(propertiesToRead == null)
        {
            return false;
        }
        try
        {
            File configFile = propertiesToRead.toFile();
            if(configFile.exists())
            {
                BasicFileAttributes attributes = Files.readAttributes(propertiesToRead, BasicFileAttributes.class, new java.nio.file.LinkOption[0]);
                FileTime fileTime = attributes.lastModifiedTime();
                boolean fileHasChanged = (fileTime.toMillis() > this.lastLoaded);
                LOG.debug("dynamic properties file changed: {} [file modifiedTime: {}, lasLoaded: {}]", new Object[] {Boolean.valueOf(fileHasChanged),
                                Long.valueOf(fileTime.toMillis()), Long.valueOf(this.lastLoaded)});
                return fileHasChanged;
            }
            LOG.debug("dynamic properties file not found... skip loading");
        }
        catch(IOException e)
        {
            LOG.error("Problem reading properties file [reason: {}]", e.getMessage());
            LOG.debug("Problem reading properties file", e);
        }
        return false;
    }
}
